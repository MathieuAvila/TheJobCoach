package com.TheJobCoach.webapp.util.shared;

import java.io.Serializable;

public class UserId implements Serializable {
	
	private static final long serialVersionUID = 5013720505251872867L;
	public String userName;
	public String token;

	/** set to true if this is a test account. This limits some functionalities, like connecting people. */
	public boolean testAccount = false;
	
	public enum UserType
	{
		USER_TYPE_SEEKER,
		USER_TYPE_ADMIN,
		USER_TYPE_COACH;
	};

	public static String userTypeToString(UserId.UserType type)
	{
		switch (type)
		{
		case USER_TYPE_SEEKER: return "seeker";
		case USER_TYPE_COACH:  return "coach";
		case USER_TYPE_ADMIN:  return "admin";
		}
		return "";
	}

	public static UserType stringToUserType(String type)
	{
		if (type == null) return UserType.USER_TYPE_SEEKER;
		if (type.equals("seeker"))
			return UserType.USER_TYPE_SEEKER;
		if (type.equals("coach"))
			return UserType.USER_TYPE_COACH;
		if (type.equals("admin"))
			return UserType.USER_TYPE_ADMIN;
		return UserId.UserType.USER_TYPE_SEEKER;
	}
	
	public UserType type;
	public UserId(String _userName, String _token, UserId.UserType _type)
	{
		userName = _userName;
		token = _token;
		type = _type;
	}
	
	public UserId(String _userName, String _token, UserId.UserType _type, boolean testAccount)
	{
		userName = _userName;
		token = _token;
		type = _type;
		this.testAccount = testAccount;
	}
	
	public UserId() {}
	
	public static String getRegexp()
	{
		return "[a-zA-Z0-9_.-]+";
	}
	
	public static boolean checkUserName(String userName)
	{
		if (userName == null) return false;
		if (userName.equals("")) return false;
		return userName.matches(getRegexp());
	}
	
	public boolean equals(UserId id)
	{
		return token.equals(id.token)
				&& userName.equals(id.userName)
				&& type.equals(type);
	}
};