package com.TheJobCoach.userdata;

import java.util.Vector;

import com.TheJobCoach.webapp.adminpage.shared.UserSearchResult;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;

public interface AccountInterface
{
	public boolean existsAccount(String userName);
	
	public CreateAccountStatus createAccount(UserId id, UserInformation info, String langStr) throws CassandraException;
	
	public ValidateAccountStatus validateAccount(String userName, String token) throws CassandraException;
	
	public MainPageReturnLogin loginAccount(String userName, String password) throws CassandraException;
	
	public Vector<UserId> listUser(boolean onlyLive) throws CassandraException;

	public void sendComment(UserId id, String comment) throws CassandraException;
	
	public void purgeAccount() throws CassandraException;

	public void setPassword(UserId id, String newPassword) throws CassandraException;

	public UserSearchResult searchUsers(UserId id, String firstName,
			String lastName, int sizeRange, int startRange) throws CassandraException, SystemException;
	
	public void toggleAccountDeletion(UserId userId, boolean delete) throws CassandraException, SystemException;
	
}
