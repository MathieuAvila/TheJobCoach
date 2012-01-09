package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;

public class UserDocument implements Serializable {

	
	private static final long serialVersionUID = 1115255124501443731L;
	
	public String ID;
	public String name;
	public String description;
	public Date lastVisit;

	public UserDocument(String _ID, String _name, String _description, Date _lastVisit)
	{
		ID = _ID;
		name = _name;
		description = _description;
		lastVisit = _lastVisit;
	}
	
	public UserDocument()
	{
	}
}
