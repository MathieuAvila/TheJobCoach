package com.TheJobCoach.webapp.adminpage.shared;

import java.io.Serializable;
import java.util.Date;

import com.TheJobCoach.webapp.util.shared.UserId.UserType;



public class UserReport implements Serializable {
	
	private static final long serialVersionUID = 5023720505251872868L;
	
	public String userName;
	public String password;
	public String token;
	public UserType type;
	public Date creationDate;
	public Date lastLogin;
	public boolean validated;
	public String mail;
	
	public UserReport(String _userName, String _password, String _mail,  String _token, UserType _type, Date _creationDate, Date _lastLogin, boolean _validated)
	{
		userName = _userName;
		password = _password;
		mail = _mail;
		token = _token;
		type = _type;
		creationDate = _creationDate;
		lastLogin = _lastLogin;
		validated = _validated;
	}
	public UserReport() {}
};