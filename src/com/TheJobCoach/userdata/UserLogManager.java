package com.TheJobCoach.userdata;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.userdata.TodoList.TodoListSubscriber;
import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.TodoCommon;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.UserId;

public class UserLogManager
{

	final static String COLUMN_FAMILY_NAME_LIST = "loglist";
	final static String COLUMN_FAMILY_NAME_DATA = "logdata";
	final static String COLUMN_FAMILY_NAME_LOG_CHANGE = "logchange";
	
	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;
	static ColumnFamilyDefinition cfDefLogChange = null;
	
	static ITodoList todoList = new TodoList();

	public UserLogManager()
	{
	}
	
	static Set<UserLogEntry.LogEntryType> logWithTodo = new TreeSet<UserLogEntry.LogEntryType>();
	
	static boolean inited = false;
	{
		if (!inited)
		{
			TodoList.registerTodoListSubscriber(new LogTodoListSubscriber(UserLogEntry.LogEntryType.INTERVIEW, this));
			TodoList.registerTodoListSubscriber(new LogTodoListSubscriber(UserLogEntry.LogEntryType.RECALL, this));
			TodoList.registerTodoListSubscriber(new LogTodoListSubscriber(UserLogEntry.LogEntryType.EVENT, this));
			logWithTodo.add(UserLogEntry.LogEntryType.INTERVIEW);
			logWithTodo.add(UserLogEntry.LogEntryType.RECALL);
			logWithTodo.add(UserLogEntry.LogEntryType.EVENT);
			inited = true;
		}
	}

	static
	{		
		cfDefList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST, cfDefList);
		cfDefData = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_DATA, cfDefData);
		cfDefLogChange = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LOG_CHANGE, cfDefLogChange);
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
			try {
				UserDocumentId docId = UserDocumentManager.getInstance().getUserDocumentId(id, d);
				if (docId != null) docIdList.add(docId);		
			}
			catch (Exception e) {} // Removed at next write
		}
		
		Vector<ExternalContact> finalContactVector = new Vector<ExternalContact>();
		Vector<String> contactVector = resultReq.getVector("vcontact");
			
		for (String contactId: contactVector)
		{
			try {
				ExternalContact contact = UserExternalContactManager.getInstance().getExternalContact(id, contactId);
				if (contact != null) 
				{
					finalContactVector.add(contact);
				}
			}
			catch (Exception e)
			{
				// contact removed. Remove from list, purged at next write.
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
		UserLogEntry previousLog = getLogEntryLong(id, result.ID);
		if (previousLog != null)
			removeUserOppStatusChange(id, previousLog.eventDate, result.ID);
		
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
		
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				result.ID, 
				update.get());
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_LIST, 
				id.userName,
				(new ShortMap()).add(result.ID, result.ID).get());
		addUserOppStatusChange(id, result.eventDate, result.ID);
		
		// Create a TodoEvent
		if ((!result.done)&&(logWithTodo.contains(result.type)))
		{
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put(TodoCommon.ID, result.ID);
			hash.put(TodoCommon.OPPID, result.opportunityId);
			hash.put(TodoCommon.LAST, FormatUtil.getDateString(result.eventDate));
			hash.put(TodoCommon.NAME, result.title);
			hash.put(TodoCommon.TYPE, UserLogEntry.entryTypeToString(result.type));

			TodoEvent te = new TodoEvent(
					result.ID, 
					"",
					hash,  
					UserLogEntry.entryTypeToString(result.type), 
					TodoEvent.Priority.NORMAL,  
					result.eventDate, 
					TodoEvent.EventColor.GREEN);
			todoList.setTodoEvent(id, te);
		}
	}

	public void deleteUserLogEntryFromList(UserId id, String ID, String oppId) throws CassandraException
	{
		String key = id.userName + "#" + oppId;		
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST, key, ID);	
	}

	public void deleteUserLogEntry(UserId id, String ID) throws CassandraException 
	{
		UserLogEntry previousLog = getLogEntryLong(id, ID);
		removeUserOppStatusChange(id, previousLog.eventDate, ID);
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, ID);
	}
	
	public void deleteOpportunityLogList(UserId id, String oppId) throws CassandraException
	{
		Vector<UserLogEntry> resultReq = getLogList(id, oppId);
		for (UserLogEntry log: resultReq)
		{			
			deleteUserLogEntryFromList(id, log.ID, oppId);
			removeUserOppStatusChange(id, log.eventDate, log.ID);
		}
		String key = id.userName + "#" + oppId;		
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST, key);
	}
	
	public void deleteUser(UserId id) throws CassandraException
	{
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LOG_CHANGE, id.userName);
	}

	public void addUserOppStatusChange(UserId id, Date d, String logId) throws CassandraException
	{
		ShortMap update = new ShortMap().add(Convertor.getDateStringUnique(d), logId);
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_LOG_CHANGE, 
				id.userName, 
				update.get());	
	}

	public void removeUserOppStatusChange(UserId id, Date d, String logId) throws CassandraException
	{
		Date start = FormatUtil.startOfTheDay(d);
		Date end = FormatUtil.endOfTheDay(d);
		String strStart = FormatUtil.getDateString(start);
		String strEnd = FormatUtil.getDateString(end);
		Map<String, String> values = CassandraAccessor.getColumnRange(COLUMN_FAMILY_NAME_LOG_CHANGE, id.userName, strStart, strEnd, 
				1000); // TODO: can be more than that. Do we really care of this use case ?
		for (String key: values.keySet())
		{
			if (values.get(key).equals(logId))
			{
				CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LOG_CHANGE, id.userName, key);
			};
		}
	}

	public Vector<String> getPeriodUserOppStatusChange(UserId id, Date start, Date end) throws CassandraException
	{
		String strStart = FormatUtil.getDateString(start);
		String strEnd = FormatUtil.getDateString(end);
		Map<String, String> values = CassandraAccessor.getColumnRange(COLUMN_FAMILY_NAME_LOG_CHANGE, id.userName, strStart, strEnd, 
				1000); // TODO: can be more than that. Do we really care of this use case ?
		return new Vector<String>(values.values());
	}

	/*
	 * Those are for TodoList interface
	 */
	private class LogTodoListSubscriber implements TodoListSubscriber 
	{
		UserLogEntry.LogEntryType opType;
	    UserLogManager manager;
		
		public LogTodoListSubscriber(UserLogEntry.LogEntryType opType, UserLogManager manager)
		{
			this.opType = opType;
			this.manager = manager;
		}
		
		@Override
		public String getSubscriberId()
		{
			return UserLogEntry.entryTypeToString(opType);
		}

		@Override
		public void event(UserId id, TodoEvent event)
		{
			manager.event(opType, id, event); 
		}

		@Override
		public boolean isEventValid(UserId id, TodoEvent event)
		{
			return manager.isEventValid(opType, id, event);
		}

		@Override
		public void eventDone(UserId id, TodoEvent event)
		{
			manager.eventDone(opType, id, event);
		}
	}
	
	private void event(UserLogEntry.LogEntryType opType, UserId id, TodoEvent event)
	{
		// TODO
	}

	private boolean isEventValid(UserLogEntry.LogEntryType opType, UserId id, TodoEvent event)
	{
		// Does ID exist ? 
		String logId = event.systemText.get(TodoCommon.ID);
		String lastDate = event.systemText.get(TodoCommon.LAST);
		String logType = event.systemText.get(TodoCommon.TYPE);
		if ((logId == null)||(logType == null)||(lastDate == null)) return false;

		UserLogEntry ujs;
		try {
			ujs = getLogEntryLong(id, logId);
		}
		catch (CassandraException e)
		{
			return false;
		}
		UserLogEntry.LogEntryType type = UserLogEntry.entryTypeToString(logType);
		if (type != opType) return false;
		return FormatUtil.getDateString(ujs.eventDate).equals(lastDate) && !ujs.done;
	}

	private void eventDone(UserLogEntry.LogEntryType opType, UserId id, TodoEvent event)
	{
		
	}
}
