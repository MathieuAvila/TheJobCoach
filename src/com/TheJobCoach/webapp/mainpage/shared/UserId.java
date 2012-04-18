package com.TheJobCoach.webapp.mainpage.shared;

import java.io.Serializable;

public class UserId implements Serializable {
	private static final long serialVersionUID = 5023720505251872867L;
	public String userName;
	public String token;
	public enum UserType
	{ 
		USER_TYPE_SEEKER,
		USER_TYPE_ADMIN,
		USER_TYPE_COACH
	};
	public UserType type;
	public UserId(String _userName, String _token, UserType _type)
	{
		userName = _userName;
		token = _token;
		type = _type;
	}
	public UserId() {}
	
	public static boolean checkUserName(String userName)
	{
		if (userName == null) return false;
		if (userName.equals("")) return false;
		return userName.matches("[a-zA-Z0-9_.]+");
	}
};