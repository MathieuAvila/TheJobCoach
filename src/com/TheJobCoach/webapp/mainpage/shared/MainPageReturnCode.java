package com.TheJobCoach.webapp.mainpage.shared;

public class MainPageReturnCode {

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

}
