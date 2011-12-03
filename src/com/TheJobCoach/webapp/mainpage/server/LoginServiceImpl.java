package com.TheJobCoach.webapp.mainpage.server;

import com.TheJobCoach.webapp.mainpage.client.LoginService;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {
	
	static com.TheJobCoach.userdata.Account account = new com.TheJobCoach.userdata.Account();
	
	public MainPageReturnCode.CreateAccountStatus createAccount(String userName, String name, String firstName, String userPassword, String email) throws IllegalArgumentException {
		System.out.println("Create account request for: '" + userName + "' with password: '" + userPassword+ "' with EMail:'" + email + "'");
		return account.createAccount(userName, name, firstName, email, "FR");
	}

	@Override
	public MainPageReturnCode.ConnectStatus connect(String userName, String userPassword) {
		System.out.println("Connection from: '" + userName + "' with password: '" + userPassword+ "'");
		return MainPageReturnCode.ConnectStatus.CONNECT_STATUS_OK;
	}

	@Override
	public ValidateAccountStatus validateAccount(String userName) {
		// TODO Auto-generated method stub
		return account.validateAccount(userName);
	}

}
