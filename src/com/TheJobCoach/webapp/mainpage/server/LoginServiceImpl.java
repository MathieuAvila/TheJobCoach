package com.TheJobCoach.webapp.mainpage.server;

import com.TheJobCoach.webapp.mainpage.client.LoginService;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {
	
	static com.TheJobCoach.userdata.Account account = new com.TheJobCoach.userdata.Account();
	
	public MainPageReturnCode.CreateAccountStatus createAccount(
			String userName, String name, 
			String firstName, String email, 
			String password, String locale,
			UserId.UserType type)
			throws IllegalArgumentException {
		System.out.println("Create account request for: '" + userName + "' with password: '" + password+ "' with EMail:'" + email + "' with locale '" + locale +"'");
		return account.createAccount(userName, name, firstName, email, password, locale, type);
	}

	@Override
	public MainPageReturnLogin connect(String userName, String userPassword) {
		System.out.println("Connection from: '" + userName + "' with password: '" + userPassword+ "'");
		return account.loginAccount(userName, userPassword);
	}

	@Override
	public ValidateAccountStatus validateAccount(String userName, String token) {
		// TODO Auto-generated method stub
		return account.validateAccount(userName, token);
	}

}
