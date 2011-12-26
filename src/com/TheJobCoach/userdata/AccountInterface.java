package com.TheJobCoach.userdata;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;

public interface AccountInterface
{
	public boolean existsAccount(String userName);
	
	public CreateAccountStatus createAccount(String userName, String name, String surName, String email, String password, String lang);
	
	public ValidateAccountStatus validateAccount(String userName, String token);
	
	public MainPageReturnLogin loginAccount(String userName, String password);	
}
