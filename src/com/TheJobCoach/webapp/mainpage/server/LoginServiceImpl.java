package com.TheJobCoach.webapp.mainpage.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.mainpage.client.LoginService;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {
	
	static com.TheJobCoach.userdata.Account account = new com.TheJobCoach.userdata.Account();
	
	private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	public MainPageReturnCode.CreateAccountStatus createAccount(
			UserId id,
			UserInformation info,
			String locale)
			throws IllegalArgumentException, CassandraException {
		logger.info("Create account request for: '" + id.userName + "' with password: '" + info.password+ "' with EMail:'" + info.email + "' with locale '" + locale +"'");		
		return account.createAccount(id, info, locale);
	}

	@Override
	public MainPageReturnLogin connect(String userName, String userPassword) {
		logger.info("Connection from: '" + userName + "' with password: '" + userPassword+ "'");
		return account.loginAccount(userName, userPassword);
	}

	@Override
	public ValidateAccountStatus validateAccount(String userName, String token) throws CassandraException {
		return account.validateAccount(userName, token);
	}

	@Override
	public Boolean lostCredentials(String email, String lang) throws CassandraException {
		return account.lostCredentials(email, lang);
	}

	@Override
	public UserId createTestUser(String lang, UserType type) throws CassandraException, SystemException 
	{
		// purge old accounts.
		account.purgeTestAccount(60 * 60 * 24); // 1 day
		// create account
		return account.createTestAccount(lang, type);
	}

}
