package com.TheJobCoach.webapp.mainpage.shared;

import java.io.Serializable;

public class MainPageReturnLogin implements Serializable
{
	private static final long serialVersionUID = 100000L;
	
	String token;
	
	public enum LoginStatus 
	{ 
		CONNECT_STATUS_UNKNOWN_USER, 
		CONNECT_STATUS_PASSWORD,		
		CONNECT_STATUS_NOT_VALIDATED,		
		CONNECT_STATUS_OK
	};
	
	LoginStatus loginStatus;

	public MainPageReturnLogin(LoginStatus _loginStatus, String _token)
	{
		loginStatus = _loginStatus;
		token = _token;
	}
	
	public MainPageReturnLogin()
	{
		loginStatus = LoginStatus.CONNECT_STATUS_UNKNOWN_USER;
		token = "";
	}

	public String getToken() 
	{
		return token;
	}

	public LoginStatus getLoginStatus()
	{
		return loginStatus;
	}

}
