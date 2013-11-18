package com.TheJobCoach.userdata;

import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;

public interface AccountInterface
{
	public boolean existsAccount(String userName);
	
	public CreateAccountStatus createAccount(UserId id, UserInformation info, String langStr) throws CassandraException;
	
	public ValidateAccountStatus validateAccount(String userName, String token) throws CassandraException;
	
	public MainPageReturnLogin loginAccount(String userName, String password) throws CassandraException;
	
	public Vector<UserId> listUser() throws CassandraException;

	public void sendComment(UserId id, String comment) throws CassandraException;
	
	public void purgeAccount() throws CassandraException;
}
