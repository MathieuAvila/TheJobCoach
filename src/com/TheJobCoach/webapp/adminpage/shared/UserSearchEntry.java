package com.TheJobCoach.webapp.adminpage.shared;

import java.io.Serializable;

import com.TheJobCoach.webapp.util.shared.UserId.UserType;

public class UserSearchEntry implements Serializable {
	
	private static final long serialVersionUID = 5023720505261872868L;
	
	public String userName;
	public String firstName;
	public String lastName;
	public String job;
	public UserType type;
	
	public UserSearchEntry()
	{
		super();
	}

	public UserSearchEntry(String userName, String firstName, String lastName,
			String job, UserType type)
	{
		super();
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.job = job;
		this.type = type;
	}

};