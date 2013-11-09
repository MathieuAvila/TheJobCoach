package com.TheJobCoach.userdata;

import java.util.Map;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;

public class UserOpportunityManager {

	final static String COLUMN_FAMILY_NAME_LIST = "opportunitieslist";
	final static String COLUMN_FAMILY_NAME_DATA = "opportunitiesdata";

	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;
	
	final public static String MANAGED_LIST = "managed";
	
	final static UserLogManager log = new UserLogManager();

	public UserOpportunityManager()
	{
		cfDefList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST, cfDefList);
		cfDefData = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_DATA, cfDefData);
	}

	public Vector<UserOpportunity> getOpportunitiesList(UserId id, String listName) throws CassandraException 
	{	
		String key = id.userName + "#" + listName;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_LIST, key);
		Vector<UserOpportunity> result = new Vector<UserOpportunity>();		
		if (resultReq == null)
			return result;
		for (String oppId: resultReq.keySet())
		{
			UserOpportunity opp = getOpportunityLong(id, oppId);
			if (opp == null)
			{
				deleteUserOpportunityFromList(id, oppId, listName);
			}
			else 
				result.add(opp);
		}
		return result;
	}

	public UserOpportunity getOpportunityShort(UserId id, String ID) throws CassandraException 
	{
		String reqId = ID;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (resultReq == null)
		{
			return null;  // this means it was deleted.
		}
		return new UserOpportunity(
				ID,
				Convertor.toDate(resultReq.get("firstseen")),
				Convertor.toDate(resultReq.get("lastupdate")),
				Convertor.toString(resultReq.get("title")),
				"",
				Convertor.toString(resultReq.get("companyid")),
				Convertor.toString(resultReq.get("contracttype")),
				Convertor.toDouble(resultReq.get("salary")),
				Convertor.toDate(resultReq.get("startdate")),
				Convertor.toDate(resultReq.get("enddate")),
				true,
				"",
				Convertor.toString(resultReq.get("url")),
				Convertor.toString(resultReq.get("location")),
				UserOpportunity.applicationStatusToString(resultReq.get("status")),
				Convertor.toString(resultReq.get("note"))
				);
	}

	public UserOpportunity getOpportunityLong(UserId id, String ID) throws CassandraException 
	{
		String reqId = ID;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (resultReq == null)
		{
			return null;  // this means it was deleted.
		}
		return new UserOpportunity(
				ID,
				Convertor.toDate(resultReq.get("firstseen")),
				Convertor.toDate(resultReq.get("lastupdate")),
				Convertor.toString(resultReq.get("title")),
				Convertor.toString(resultReq.get("description")),
				Convertor.toString(resultReq.get("companyid")),
				Convertor.toString(resultReq.get("contracttype")),
				Convertor.toDouble(resultReq.get("salary")),
				Convertor.toDate(resultReq.get("startdate")),
				Convertor.toDate(resultReq.get("enddate")),
				Convertor.toBoolean(resultReq.get("systemsource")),
				Convertor.toString(resultReq.get("source")),
				Convertor.toString(resultReq.get("url")),
				Convertor.toString(resultReq.get("location")),
				UserOpportunity.applicationStatusToString(resultReq.get("status")),
				Convertor.toString(resultReq.get("note"))				
				);
	}

	public void setUserOpportunity(UserId id, UserOpportunity result, String listName) throws CassandraException 
	{
		String key = id.userName + "#" + listName;		
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_LIST, 
				key, 
				(new ShortMap())
				.add(result.ID, result.ID)
				.get());
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				result.ID, 
				(new ShortMap())
				.add("firstseen", result.firstSeen)
				.add("lastupdate", result.lastUpdate)
				.add("title", result.title)
				.add("description", result.description)
				.add("companyid", result.companyId)
				.add("contracttype", result.contractType)
				.add("salary", result.salary)
				.add("startdate", result.startDate)
				.add("enddate", result.endDate)
				.add("systemsource", result.systemSource)
				.add("source", result.source)
				.add("location", result.location)
				.add("url", result.url)
				.add("note", result.note)
				.add("status", UserOpportunity.applicationStatusToString(result.status))
				.get());	
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_LIST, 
				id.userName,
				(new ShortMap()).add(result.ID, result.ID).get());
	}

	public void deleteUserOpportunityFromList(UserId id, String ID, String listName) throws CassandraException
	{
		String key = id.userName + "#" + listName;		
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST, key, ID);	
	}

	public void deleteUserOpportunity(UserId id, String ID) throws CassandraException 
	{
		log.deleteOpportunityLogList(id, ID);
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, ID);
	}

	public void deleteUserList(UserId id, String listName) throws CassandraException
	{
		String key = id.userName + "#" + listName;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_LIST, key);
		if (resultReq == null)
			return;
		for (String oppId: resultReq.keySet())
		{			
			deleteUserOpportunity(id, oppId);
		}
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST, key);
	}

	public void deleteUser(UserId id) throws CassandraException
	{
		deleteUserList(id, MANAGED_LIST);
		log.deleteUser(id);
	}
	
}
