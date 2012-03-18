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
		INTERVIEW,	
	};

	public String opportunityId;
	public String ID;
	public String title;
	public String description;
	public Date creation;
	public Date expectedFollowUp;
	public LogEntryType type;
	public Vector<String> contactId;
	public Vector<String> attachedDocumentId;
	
	static public String entryTypeToString(LogEntryType t)
	{
		switch (t)
		{
		case INFO: return "INFO";
		case APPLICATION: return "APPLICATION";
		case INTERVIEW: return "INTERVIEW";
		}
		return "INFO";
	}
	
	static public LogEntryType entryTypeToString(String t)
	{
		if ("INFO".equals(t)) return LogEntryType.INFO;
		if ("APPLICATION".equals(t)) return LogEntryType.APPLICATION;
		if ("INTERVIEW".equals(t))return LogEntryType.INTERVIEW;
		return LogEntryType.INFO;
	}

	public UserLogEntry()
	{
	}

	public UserLogEntry(String opportunityId, String iD, String title,
			String description, Date creation, Date expectedFollowUp, LogEntryType type,
			Vector<String> contactId, Vector<String> attachedDocumentId) {
		super();
		this.opportunityId = opportunityId;
		ID = iD;
		this.title = title;
		this.description = description;
		this.creation = creation;
		this.expectedFollowUp = expectedFollowUp;
		this.type = type;
		this.contactId = contactId;
		this.attachedDocumentId = attachedDocumentId;
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
