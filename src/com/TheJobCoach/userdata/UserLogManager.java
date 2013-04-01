package com.TheJobCoach.userdata;

import java.util.Map;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.util.shared.CassandraException;

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

	public Vector<UserLogEntry> getLogList(UserId id, String oppId) throws CassandraException 
	{	
		String key = id.userName + "#" + oppId;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_LIST, key);
		Vector<UserLogEntry> result = new Vector<UserLogEntry>();		
		if (resultReq == null)
			return result;
		for (String logId: resultReq.keySet())
		{
			UserLogEntry opp = getLogEntryLong(id, logId);
			if (opp == null)
			{
				deleteUserLogEntryFromList(id, logId, oppId);
			}
			else 
				result.add(opp);
		}
		return result;
	}

	public UserLogEntry getLogEntryLong(UserId id, String ID) throws CassandraException 
	{
		String reqId = ID;
		Map<String, String> resultMap = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (resultMap == null)
		{
			return null;  // this means it was deleted.
		}		
		ShortMap resultReq = new ShortMap(resultMap);

		Vector<UserDocumentId> docIdList = new Vector<UserDocumentId>();
		Vector<String> docIdVector = resultReq.getVector("vdoc");	
		for (String d: docIdVector)
		{
			UserDocumentId docId = UserDocumentManager.getInstance().getUserDocumentId(id, d);
			if (docId != null) docIdList.add(docId);			
		}
		
		Vector<ExternalContact> finalContactVector = new Vector<ExternalContact>();
		Vector<String> contactVector = resultReq.getVector("vcontact");
			
		for (String contactId: contactVector)
		{
			ExternalContact contact = UserExternalContactManager.getInstance().getExternalContact(id, contactId);
			if (contact != null) 
			{
				finalContactVector.add(contact);
			}
		}
		
		return new UserLogEntry(
				resultReq.getString("opportunityid"),
				ID,
				resultReq.getString("title"),
				resultReq.getString("description"),
				resultReq.getDate("creation"),
				UserLogEntry.entryTypeToString(resultReq.getString("status")),
				finalContactVector, docIdList,
				resultReq.getString("note"),
				resultReq.getBoolean("done"));
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
		ShortMap update = new ShortMap()				
				.add("opportunityid", result.opportunityId)
				.add("creation", result.eventDate)
				.add("title", result.title)
				.add("description", result.description)				
				.add("status", UserLogEntry.entryTypeToString(result.type))
				.add("note", result.note).add("done", result.done);
		
		Vector<String> docIdVector = new Vector<String>();		
		if (result.attachedDocumentId != null)
			for (UserDocumentId d: result.attachedDocumentId)
			{
				docIdVector.add(d.updateId);				
			}		
		update.addVector("vdoc", docIdVector);
		
		Vector<String> contactVector = new Vector<String>();		
		if (result.linkedExternalContact != null)
			for (ExternalContact d: result.linkedExternalContact)
			{
				contactVector.add(d.ID);				
			}
		update.addVector("vcontact", contactVector);
		
		boolean resultReq = CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				result.ID, 
				update.get());	
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
		String key = id.userName + "#" + oppId;		
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST, key, ID);	
	}

	public void deleteUserLogEntry(UserId id, String ID) throws CassandraException 
	{
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, ID);
	}
	
	public void deleteOpportunityLogList(UserId id, String oppId) throws CassandraException
	{
		Vector<UserLogEntry> resultReq = getLogList(id, oppId);
		for (UserLogEntry log: resultReq)
		{			
				deleteUserLogEntryFromList(id, log.ID, oppId);			
		}
		String key = id.userName + "#" + oppId;		
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST, key);
	}
}
