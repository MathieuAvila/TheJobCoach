package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class UserLogEntry implements Serializable {


	private static final long serialVersionUID = 1115255124501443735L;

	public enum LogEntryType 
	{
		INFO,
		APPLICATION,
		EVENT,
		RECALL,
		INTERVIEW,
		PROPOSAL,
		CLOSED
	};

	public String opportunityId;
	public String ID;
	public String title;
	public String description;
	
	/** User private notes */
	public String note;
	
	/** Date specified by user */
	public Date eventDate;
	
	public LogEntryType type;
	
	/** In case of Interview or Recall, set to TRUE if action was performed */
	public boolean done;
	
	public Vector<String> contactId;
	public Vector<UserDocumentId> attachedDocumentId;
	
	static public String entryTypeToString(LogEntryType t)
	{
		switch (t)
		{
		case INFO: return "INFO";
		case APPLICATION: return "APPLICATION";
		case INTERVIEW: return "INTERVIEW";
		case EVENT: return "EVENT";
		case RECALL: return "RECALL";
		case PROPOSAL: return "PROPOSAL";
		case CLOSED: return "CLOSED";
		}
		return "INFO";
	}
	
	static public LogEntryType entryTypeToString(String t)
	{
		if ("INFO".equals(t)) return LogEntryType.INFO;
		if ("APPLICATION".equals(t)) return LogEntryType.APPLICATION;
		if ("INTERVIEW".equals(t))return LogEntryType.INTERVIEW;
		if ("EVENT".equals(t))return LogEntryType.EVENT;
		if ("RECALL".equals(t))return LogEntryType.RECALL;
		if ("PROPOSAL".equals(t))return LogEntryType.PROPOSAL;
		if ("CLOSED".equals(t))return LogEntryType.CLOSED;
		return LogEntryType.INFO;
	}

	public UserLogEntry()
	{
	}

	public UserLogEntry(String opportunityId, String iD, String title,
			String description, Date eventDate, LogEntryType type,
			Vector<String> contactId, Vector<UserDocumentId> attachedDocumentId,
			String note,
			boolean done) {
		super();
		this.opportunityId = opportunityId;
		ID = iD;
		this.title = title;
		this.description = description;
		this.eventDate = eventDate;
		this.type = type;
		this.contactId = contactId;
		this.attachedDocumentId = attachedDocumentId;
		this.note = note;
		this.done = done;
	}
	
	static Vector<LogEntryType> logTypeTable = null;
	
	public static final Vector<LogEntryType> getLogTypeTable()
	{
		if (logTypeTable == null)
		{
			logTypeTable = new Vector<LogEntryType>();
			LogEntryType[] table = LogEntryType.values();
			for (LogEntryType e : table)
			{
				logTypeTable.add(e);
			}
		}
		return logTypeTable;
	}
}
