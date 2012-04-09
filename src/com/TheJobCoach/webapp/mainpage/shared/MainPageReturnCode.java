package com.TheJobCoach.webapp.mainpage.shared;

import java.io.Serializable;

public class MainPageReturnCode implements Serializable {

	private static final long serialVersionUID = 7528193994912150674L;
	
	public static enum ConnectStatus 
	{ 
		CONNECT_STATUS_ERROR, 
		CONNECT_STATUS_UNKNOWN, 
		CONNECT_STATUS_OK
	}

	public static enum CreateAccountStatus 
	{ 
		CREATE_STATUS_ALREADY_EXISTS, 
		CREATE_STATUS_ERROR, 
		CREATE_STATUS_INVALID, 
		CREATE_STATUS_OK
	}
	
	public static enum ValidateAccountStatus 
	{ 
		VALIDATE_STATUS_ERROR,
		VALIDATE_STATUS_UNKNOWN, 
		VALIDATE_STATUS_OK	
	}
	
	public MainPageReturnCode() {}
	
}
