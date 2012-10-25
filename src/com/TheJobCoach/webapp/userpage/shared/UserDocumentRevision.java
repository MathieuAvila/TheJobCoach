package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class UserDocumentRevision implements Serializable 
{		
	private static final long serialVersionUID = 1115255124501443732L;
	public Date date;
	public String ID;
	public String fileName;
	public UserDocumentRevision(Date date, String ID, String fileName) {
		this.date = date;
		this.ID = ID;
		this.fileName = fileName;
	}
	public UserDocumentRevision() {			
	}
}
