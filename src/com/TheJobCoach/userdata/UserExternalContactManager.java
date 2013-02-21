package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class UserExternalContactManager {

	final static String COLUMN_FAMILY_NAME_LIST = "userexternalcontactlist";
	final static String COLUMN_FAMILY_NAME_DATA = "userexternalcontactdata";
	
	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;
	
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
			return null;
		}	
		/*public ExternalContact(String iD, String firstName, String lastName,
				String email, String personalNote, String organization,
				UpdatePeriod update)*/
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
		boolean resultReq = CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				reqId, 
				sm.get());	
		if (resultReq == false)
		{
			throw new CassandraException(); 
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

}
