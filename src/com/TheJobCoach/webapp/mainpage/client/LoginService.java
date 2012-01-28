package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	
	MainPageReturnCode.CreateAccountStatus createAccount(String userName, String name, String firstName, String email, String password, String locale, UserId.UserType type);
		
	MainPageReturnLogin connect(String userName, String userPassword);

	MainPageReturnCode.ValidateAccountStatus validateAccount(String userName, String token);
}
