package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.userdata.TodoList.TodoListSubscriber;
import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.userpage.shared.TodoCommon;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.UserId;

public class UserJobSiteManager implements TodoListSubscriber, IUserDataManager {

	final static String COLUMN_FAMILY_NAME_LIST = "userjobsitelist";
	final static String COLUMN_FAMILY_NAME_DATA = "userjobsitedata";
	
	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;
	static ITodoList todoList = new TodoList();
	
	static boolean inited = false;
	{
		if (!inited)
		{
			TodoList.registerTodoListSubscriber(this);
			UserDataCentralManager.addManager(this);
			inited = true;
		}
	}
	
	public UserJobSiteManager()
	{
		cfDefList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST, cfDefList);
		cfDefData = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_DATA, cfDefData);
	}

	public List<String> getUserSiteList(UserId id) throws CassandraException 
	{	
		Map<String,String> resultReq = null;
		resultReq = CassandraAccessor.getRow(
				COLUMN_FAMILY_NAME_LIST, 
				id.userName);
		if (resultReq == null) return new ArrayList<String>();
		return new ArrayList<String>(resultReq.keySet());
	}

	public UserJobSite getUserSite(UserId id, String ID) throws CassandraException 
	{
		String reqId = id.userName + "_" + ID;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (resultReq == null)
		{
			throw new CassandraException(); 
		}
		return new UserJobSite(
				ID, 
				Convertor.toString(resultReq.get("name")),
				Convertor.toString(resultReq.get("url")),
				Convertor.toString(resultReq.get("description")),
				Convertor.toString(resultReq.get("login")),
				Convertor.toString(resultReq.get("password")),
				UpdatePeriodAccessor.fromCassandra(resultReq)
				);
	}

	public void setUserSite(UserId id, UserJobSite result) throws CassandraException 
	{
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_LIST, id.userName, (new ShortMap()).add(result.ID, result.ID).get());
		String reqId = id.userName + "_" + result.ID;
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				reqId, 
				UpdatePeriodAccessor.toCassandra(new ShortMap()
				.add("name", result.name)
				.add("url", result.URL)
				.add("description", result.description)
				.add("login", result.login)
				.add("password", result.password), result.update)
				.get());
		if (result.update.needRecall)
		{
			// Create a TodoEvent
			Date nextDate = result.update.getNextCall();
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put(TodoCommon.ID, result.ID);
			hash.put(TodoCommon.LAST, FormatUtil.getDateString(result.update.last));
			hash.put(TodoCommon.NAME, result.name);
			
			TodoEvent te = new TodoEvent(
					result.ID, 
					"",
					hash,  
					TodoCommon.SITEMANAGER_SUBSCRIBER_ID, 
					TodoEvent.Priority.NORMAL,  
					nextDate, 
					TodoEvent.EventColor.BLUE);
			todoList.setTodoEvent(id, te);
		}
	}
	
	public void deleteUserSite(UserId id, String ID) throws CassandraException 
	{
		String reqId = id.userName + "_" + ID;
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, reqId);	
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST, id.userName, ID);		
	}

	public void deleteUser(UserId id) throws CassandraException 
	{
		List<String> siteList = getUserSiteList(id);
		for (String siteId: siteList)
		{
			deleteUserSite(id, siteId);
		}
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST, id.userName);	
	}
	
	/*
	 * Those are for TodoList interface
	 */
	
	@Override
	public String getSubscriberId()
	{
		return TodoCommon.SITEMANAGER_SUBSCRIBER_ID;
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
		String siteId = event.systemText.get(TodoCommon.ID);
		String lastDate = event.systemText.get(TodoCommon.LAST);
		if (siteId == null) return false;
		UserJobSite ujs;
		try {
			ujs = getUserSite(id, siteId);
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
		String siteId = event.systemText.get("siteid");
		if (siteId == null) return;
		UserJobSite ujs;
		try {
			ujs = getUserSite(id, siteId);
		}
		catch (CassandraException e)
		{
			return;
		}
		// Update last update, set new.
		ujs.update.last = new Date();
		try {
			setUserSite(id, ujs);
		}
		catch (CassandraException e) {};
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
