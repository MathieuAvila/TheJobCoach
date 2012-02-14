package com.TheJobCoach.webapp.mainpage.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class UserInformation implements Serializable {
	
	private static final long serialVersionUID = 1115255124501443731L;
	
	public String name;
	public String email;
	public String password;
	public String firstName;
	
	public UserInformation(String _name, String _email, String _password, String _firstName)
	{
		name = _name;
		email = _email;
		password = _password;
		firstName = _firstName;
	}

	public UserInformation()
	{		
	}	
}
