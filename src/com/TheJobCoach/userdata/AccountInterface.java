package com.TheJobCoach.userdata;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;

public interface AccountInterface
{
	public boolean existsAccount(String userName);
	
	public CreateAccountStatus createAccount(String userName, String name, String surName, String email, String lang);
	
	public ValidateAccountStatus validateAccount(String userName);
}
