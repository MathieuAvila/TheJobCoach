package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
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
}
