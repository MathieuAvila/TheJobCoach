package com.TheJobCoach.webapp.mainpage.shared;

import java.io.Serializable;

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
	
	public static String getMailRegexp()
	{
		return "[a-zA-Z0-9_.-]+@[a-zA-Z0-9_.-]+";
	}
	
	public static boolean checkEmail(String email)
	{
		if (email == null) return false;
		return email.matches(getMailRegexp());
	}
}
