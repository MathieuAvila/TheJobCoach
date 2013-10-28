package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	
	MainPageReturnCode.CreateAccountStatus createAccount(UserId id,
			UserInformation info, String locale) throws IllegalArgumentException, CassandraException;
		
	MainPageReturnLogin connect(String userName, String userPassword);

	MainPageReturnCode.ValidateAccountStatus validateAccount(String userName, String token) throws CassandraException;
	
	Boolean lostCredentials(String email, String lang) throws CassandraException;
	
	UserId createTestUser(String lang, UserId.UserType type) throws CassandraException, SystemException ;	

	String disconnect(String lang, UserId.UserType type) throws CassandraException, SystemException ;	
}
