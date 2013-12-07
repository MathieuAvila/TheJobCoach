package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.userdata.TodoList.TodoListSubscriber;
import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.TodoCommon;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.UserId;

public class UserExternalContactManager implements TodoListSubscriber, IUserDataManager  {

	final static String COLUMN_FAMILY_NAME_LIST = "userexternalcontactlist";
	final static String COLUMN_FAMILY_NAME_DATA = "userexternalcontactdata";
	
	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;
	
	static UserExternalContactManager instance = new UserExternalContactManager();
	
	static ITodoList todoList = new TodoList();
	
	public static UserExternalContactManager getInstance() 
	{
		return instance;
	}

	public UserExternalContactManager()
	{
		cfDefList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST, cfDefList);
		cfDefData = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_DATA, cfDefData);
	}

	public List<String> getExternalContactListId(UserId id) throws CassandraException 
	{	
		Map<String,String> resultReq = null;
		resultReq = CassandraAccessor.getRow(
				COLUMN_FAMILY_NAME_LIST, 
				id.userName);
		if (resultReq == null) return new ArrayList<String>();
		return new ArrayList<String>(resultReq.keySet());
	}

	public Vector<ExternalContact> getExternalContactList(UserId id) throws CassandraException 
	{	
		List<String> list = getExternalContactListId(id);
		Vector<ExternalContact> result = new Vector<ExternalContact>();
		for (String ID: list)
		{
			ExternalContact contact = getExternalContact(id, ID);
			if (contact == null)
			{
				deleteExternalContact(id, ID);
			}
			else
			{
				result.add(contact);
			}
		}
		return result;
	}

	public ExternalContact getExternalContact(UserId id, String ID) throws CassandraException 
	{
		String reqId = id.userName + "_" + ID;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (resultReq == null)
		{
			throw new CassandraException(); 
		}		
		return new ExternalContact(
				ID, 
				Convertor.toString(resultReq.get("firstName")),
				Convertor.toString(resultReq.get("lastName")),
				Convertor.toString(resultReq.get("email")),
				Convertor.toString(resultReq.get("phone")),
				Convertor.toString(resultReq.get("personalNote")),
				Convertor.toString(resultReq.get("organization")),
				UpdatePeriodAccessor.fromCassandra(resultReq));
		}

	public void setExternalContact(UserId id, ExternalContact result) throws CassandraException 
	{
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_LIST, id.userName, (new ShortMap()).add(result.ID, result.ID).get());
		String reqId = id.userName + "_" + result.ID;
		ShortMap sm = new ShortMap()
		.add("email", result.email)
		.add("phone", result.phone)
		.add("firstName", result.firstName)
		.add("lastName", result.lastName)
		.add("organization", result.organization)
		.add("personalNote", result.personalNote);
		UpdatePeriodAccessor.toCassandra(sm, result.update);
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				reqId, 
				sm.get());	
		
		if (result.update.needRecall)
		{
			// Create a TodoEvent
			Date nextDate = result.update.getNextCall();
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put(TodoCommon.ID, result.ID);
			hash.put(TodoCommon.LAST, FormatUtil.getDateString(result.update.last));
			hash.put(TodoCommon.FIRSTNAME, result.firstName);
			hash.put(TodoCommon.LASTNAME, result.lastName);
			hash.put(TodoCommon.COMPANY, result.organization);
			
			TodoEvent te = new TodoEvent(
					result.ID, 
					"",
					hash,  
					TodoCommon.EXTERNALCONTACTMANAGER_SUBSCRIBER_ID, 
					TodoEvent.Priority.NORMAL,  
					nextDate, 
					TodoEvent.EventColor.BLUE);
			todoList.setTodoEvent(id, te);
		}
	}
	
	public void deleteExternalContact(UserId id, String ID) throws CassandraException 
	{
		String reqId = id.userName + "_" + ID;
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, reqId);	
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST, id.userName, ID);		
	}

	public void deleteUser(UserId id) throws CassandraException 
	{
		List<String> siteList = getExternalContactListId(id);
		for (String siteId: siteList)
		{
			deleteExternalContact(id, siteId);
		}
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST, id.userName);	
	}

	@Override
	public String getSubscriberId()
	{
		return TodoCommon.EXTERNALCONTACTMANAGER_SUBSCRIBER_ID;
	}

	@Override
	public void event(UserId id, TodoEvent event)
	{
		// TODO : WTF ?
	}

	@Override
	public boolean isEventValid(UserId id, TodoEvent event)
	{
		// Does ID exist ? 
		String contactId = event.systemText.get(TodoCommon.ID);
		String lastDate = event.systemText.get(TodoCommon.LAST);
		if (contactId == null) return false;
		ExternalContact ujs;
		try {
			ujs = getExternalContact(id, contactId);
		}
		catch (CassandraException e)
		{
			return false;
		}
		return FormatUtil.getDateString(ujs.update.last).equals(lastDate);
	}

	@Override
	public void eventDone(UserId id, TodoEvent event)
	{
		// TODO Auto-generated method stub
		
	}

	/*
	 * Those are for CentralManager
	 */

	@Override
	public void createUser(UserId user)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createTestUser(UserId user, String lang)
	{
		// TODO Auto-generated method stub
		
	}

}
