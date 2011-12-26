package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;

public class UserJobSite implements Serializable {

	
	private static final long serialVersionUID = 1115255124501443730L;
	
	public String ID;
	public String name;
	public String URL;
	public String description;
	public String login;
	public String password;
	public Date lastVisit;

	public UserJobSite(String _ID, String _name, String _URL, String _description, String _login, String _password, Date _lastVisit)
	{
		ID = _ID;
		name = _name;
		URL = _URL;
		description = _description;
		login = _login;
		password = _password;
		lastVisit = _lastVisit;
	}
	
	public UserJobSite()
	{
	}
}
