package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;

public class UserDocument implements Serializable {

	
	private static final long serialVersionUID = 1115255124501443731L;
	
	public enum DocumentStatus { NEW, OUTDATED, USED, MASTER, SECONDARY };
	
	public String ID;
	public String name;
	public String description;
	public Date lastUpdate;
	public String fileName;	
	public DocumentStatus status;
	
	public UserDocument(String iD, String name, String description,
			Date lastUpdate, String fileName, DocumentStatus status) {
		super();
		ID = iD;
		this.name = name;
		this.description = description;
		this.lastUpdate = lastUpdate;
		this.fileName = fileName;
		this.status = status;
	}

	static public String documentStatusToString(DocumentStatus t)
	{
		switch (t)
		{
		case NEW: return "NEW";
		case OUTDATED: return "OUTDATED";
		case USED: return "USED";
		case MASTER: return "MASTER";
		case SECONDARY: return "SECONDARY";
		}
		return "NEW";
	}
	
	static public DocumentStatus documentStatusToString(String t)
	{
		if ("NEW".equals(t)) return DocumentStatus.NEW;
		if ("OUTDATED".equals(t)) return DocumentStatus.OUTDATED;
		if ("USED".equals(t))return DocumentStatus.USED;
		if ("MASTER".equals(t))return DocumentStatus.MASTER;
		if ("SECONDARY".equals(t))return DocumentStatus.SECONDARY;
		return DocumentStatus.NEW;
	}
	
	public UserDocument()
	{
	}
}
