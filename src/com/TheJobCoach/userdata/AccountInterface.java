package com.TheJobCoach.userdata;

import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;

public interface AccountInterface
{
	public boolean existsAccount(String userName);
	
	public CreateAccountStatus createAccount(UserId id, UserInformation info, String langStr);
	
	public ValidateAccountStatus validateAccount(String userName, String token);
	
	public MainPageReturnLogin loginAccount(String userName, String password);
	
	public Vector<UserId> listUser();
	
	public void purgeAccount();
}
