package com.TheJobCoach.webapp.mainpage.shared;

import java.io.Serializable;
import com.TheJobCoach.webapp.mainpage.shared.UserId;

public class MainPageReturnLogin implements Serializable
{
	private static final long serialVersionUID = 1001001L;
	
	public UserId id = null;
	
	
	public enum LoginStatus 
	{ 
		CONNECT_STATUS_UNKNOWN_USER, 
		CONNECT_STATUS_PASSWORD,		
		CONNECT_STATUS_NOT_VALIDATED,		
		CONNECT_STATUS_OK
	};
	
	LoginStatus loginStatus;

	public MainPageReturnLogin(LoginStatus _loginStatus, UserId _id)
	{
		loginStatus = _loginStatus;
		id = _id;
	}
	
	public MainPageReturnLogin(LoginStatus status)
	{
		loginStatus = status;
	}
	
	public MainPageReturnLogin()
	{
		loginStatus = LoginStatus.CONNECT_STATUS_UNKNOWN_USER;
	}

	public LoginStatus getLoginStatus()
	{
		return loginStatus;
	}

}
