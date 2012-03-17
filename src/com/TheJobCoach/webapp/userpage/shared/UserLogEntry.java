package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class UserLogEntry implements Serializable {


	private static final long serialVersionUID = 1115255124501443730L;

	public enum LogEntryType 
	{
		NEW,
		INFO,
		APPLICATION,
		INTERVIEW,
		PHONE_INFO,
		SUCCESS,
		FAILURE,
		OTHER
	};

	public String opportunityId;
	public String ID;
	public String title;
	public String text;
	public Date update;
	public Date expectedFollowUp;
	public LogEntryType type;
	public Vector<String> contactId;
	public Vector<String> attachedDocumentId;

	static public String entryTypeToString(LogEntryType t)
	{
		switch (t)
		{
		case NEW: return "NEW";
		case INFO: return "INFO";
		case APPLICATION: return "APPLICATION";
		case INTERVIEW: return "INTERVIEW";
		case SUCCESS: return "SUCCESS";
		case FAILURE: return "FAILURE";
		case OTHER: return "OTHER";
		}
		return "NEW";
	}
	
	static public LogEntryType entryTypeToString(String t)
	{
		if ("NEW".equals(t)) return LogEntryType.NEW;
		if ("INFO".equals(t)) return LogEntryType.INFO;
		if ("APPLICATION".equals(t)) return LogEntryType.APPLICATION;
		if ("INTERVIEW".equals(t))return LogEntryType.INTERVIEW;
		if ("SUCCESS".equals(t)) return LogEntryType.SUCCESS;
		if ("FAILURE".equals(t)) return LogEntryType.FAILURE;
		if ("OTHER".equals(t)) return LogEntryType.OTHER;
		return LogEntryType.NEW;
	}

	public UserLogEntry()
	{
	}

	public UserLogEntry(String opportunityId, String iD, String title,
			String text, Date update, Date expectedFollowUp, LogEntryType type,
			Vector<String> contactId, Vector<String> attachedDocumentId) {
		super();
		this.opportunityId = opportunityId;
		ID = iD;
		this.title = title;
		this.text = text;
		this.update = update;
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
