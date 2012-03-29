package com.TheJobCoach.userdata;

import java.util.Map;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;

public class UserLogManager {

	final static String COLUMN_FAMILY_NAME_LIST = "loglist";
	final static String COLUMN_FAMILY_NAME_DATA = "logdata";

	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;

	public UserLogManager()
	{
		cfDefList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST, cfDefList);
		cfDefData = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_DATA, cfDefData);
	}

	public Vector<UserLogEntry> getLogShortList(UserId id, String oppId) throws CassandraException 
	{	
		String key = id.userName + "#" + oppId;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_LIST, key);
		Vector<UserLogEntry> result = new Vector<UserLogEntry>();		
		if (resultReq == null)
			return result;
		for (String logId: resultReq.keySet())
		{
			UserLogEntry opp = getLogEntryShort(id, logId);
			if (opp == null)
			{
				deleteUserLogEntryFromList(id, logId, oppId);
			}
			else 
				result.add(opp);
		}
		return result;
	}

	public UserLogEntry getLogEntryShort(UserId id, String ID) throws CassandraException 
	{
		String reqId = ID;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (resultReq == null)
		{
			return null;  // this means it was deleted.
		}
		System.out.println("found log entry " + ID + " or opportunity " + resultReq.get("opportunityid"));		
		return new UserLogEntry(
				Convertor.toString(resultReq.get("opportunityid")),
				ID,
				Convertor.toString(resultReq.get("title")),
				"",
				Convertor.toDate(resultReq.get("creation")),
				Convertor.toDate(resultReq.get("expectedfollowup")),
				UserLogEntry.entryTypeToString(resultReq.get("status")),
				null, null);
	}

	public UserLogEntry getLogEntryLong(UserId id, String ID) throws CassandraException 
	{
		String reqId = ID;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (resultReq == null)
		{
			return null;  // this means it was deleted.
		}
		return new UserLogEntry(
				Convertor.toString(resultReq.get("opportunityid")),
				ID,
				Convertor.toString(resultReq.get("title")),
				Convertor.toString(resultReq.get("description")),
				Convertor.toDate(resultReq.get("creation")),
				Convertor.toDate(resultReq.get("expectedfollowup")),
				UserLogEntry.entryTypeToString(resultReq.get("status")),
				null, null);
	}

	public void setUserLogEntry(UserId id, UserLogEntry result) throws CassandraException 
	{
		String key = id.userName + "#" + result.opportunityId;
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_LIST, 
				key, 
				(new ShortMap())
				.add(result.ID, result.ID)
				.get());
		boolean resultReq = CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				result.ID, 
				(new ShortMap())				
				.add("opportunityid", result.opportunityId)
				.add("creation", result.creation)
				.add("expectedfollowup", result.expectedFollowUp)
				.add("title", result.title)
				.add("description", result.description)				
				.add("type", UserLogEntry.entryTypeToString(result.type))
				.get());	
		if (resultReq == false)
		{
			throw new CassandraException(); 
		}
		resultReq = CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_LIST, 
				id.userName,
				(new ShortMap()).add(result.ID, result.ID).get());
		if (resultReq == false)
		{
			throw new CassandraException(); 
		}
	}

	public void deleteUserLogEntryFromList(UserId id, String ID, String oppId) throws CassandraException
	{
		System.out.println("DELETE UserLogEntry " + ID + " from opp " + oppId);
		String key = id.userName + "#" + oppId;		
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST, key, ID);	
	}

	public void deleteUserLogEntry(UserId id, String ID) throws CassandraException 
	{
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, ID);
	}
	
	public void deleteOpportunityLogList(UserId id, String oppId) throws CassandraException
	{
		Vector<UserLogEntry> resultReq = getLogShortList(id, oppId);
		for (UserLogEntry log: resultReq)
		{			
				deleteUserLogEntryFromList(id, log.ID, oppId);			
		}
		String key = id.userName + "#" + oppId;		
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST, key);
	}
}
