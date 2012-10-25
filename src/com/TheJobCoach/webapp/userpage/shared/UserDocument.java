package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class UserDocument implements Serializable {
	
	private static final long serialVersionUID = 1115255124501443731L;
	
	public enum DocumentStatus { NEW, OUTDATED, USED, MASTER, SECONDARY };
	public enum DocumentType { RESUME, MOTIVATION, RECOMMANDATION, OTHER };
	
	public String ID;
	public String name;
	public String description;
	public Date lastUpdate;
	public String fileName;	
	public DocumentStatus status;
	public DocumentType type;
	public Vector<UserDocumentRevision> revisions;
	
	public UserDocument(String iD, String name, String description,
			Date lastUpdate, String fileName, DocumentStatus status,
			DocumentType type,
			Vector<UserDocumentRevision> revisions) {
		super();
		ID = iD;
		this.name = name;
		this.description = description;
		this.lastUpdate = lastUpdate;
		this.fileName = fileName;
		this.status = status;
		this.type = type;
		this.revisions = revisions;
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
	
	static public String documentTypeToString(DocumentType t)
	{
		switch (t)
		{
		case RESUME: return "RESUME";
		case MOTIVATION: return "MOTIVATION";
		case RECOMMANDATION: return "RECOMMANDATION";
		case OTHER: return "OTHER";
		}
		return "OTHER";
	}
	
	static public DocumentType documentTypeToString(String t)
	{
		if ("RESUME".equals(t)) return DocumentType.RESUME;
		if ("MOTIVATION".equals(t)) return DocumentType.MOTIVATION;
		if ("RECOMMANDATION".equals(t)) return DocumentType.RECOMMANDATION;
		if ("OTHER".equals(t)) return DocumentType.OTHER;
		return DocumentType.OTHER;
	}
	
	public UserDocument()
	{
	}
}
