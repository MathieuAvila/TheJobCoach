package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;

public class UserDocumentId implements Serializable {

	
	private static final long serialVersionUID = 1115255124501443731L;
	
	public String ID;
	public String updateId;
	
	public String name;
	public String fileName;
	public Date lastUpdate;
	
	public UserDocumentId(String ID, String updateId, String name, String fileName, Date lastUpdate) {
		super();
		 this.ID = ID;
		 this.updateId = updateId;
		 this.name = name;
		 this.fileName = fileName;
		 this.lastUpdate = lastUpdate;
	}

	public UserDocumentId()
	{
	}
}
