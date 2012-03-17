package com.TheJobCoach.userdata;

import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;

public interface AccountInterface
{
	public boolean existsAccount(String userName);
	
	public CreateAccountStatus createAccount(UserId id, UserInformation info, String langStr) throws CassandraException;
	
	public ValidateAccountStatus validateAccount(String userName, String token) throws CassandraException;
	
	public MainPageReturnLogin loginAccount(String userName, String password);
	
	public Vector<UserId> listUser() throws CassandraException;
	
	public void purgeAccount() throws CassandraException;
}
