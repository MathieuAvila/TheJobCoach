package com.TheJobCoach.webapp.adminpage.shared;

import java.io.Serializable;
import java.util.Date;

import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;

public class UserReport implements Serializable {
	
	private static final long serialVersionUID = 5023720505251872868L;
	
	public String userName;
	public String token;
	public UserType type;
	public Date creationDate;
	public Date lastLogin;
	public boolean validated;
	
	public UserReport(String _userName, String _token, UserType _type, Date _creationDate, Date _lastLogin, boolean _validated)
	{
		userName = _userName;
		token = _token;
		type = _type;
		creationDate = _creationDate;
		lastLogin = _lastLogin;
		validated = _validated;
	}
	public UserReport() {}
};