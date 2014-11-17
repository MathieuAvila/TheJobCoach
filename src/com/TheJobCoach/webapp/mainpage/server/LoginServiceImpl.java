package com.TheJobCoach.webapp.mainpage.server;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.userdata.EvaluationAccountManager;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MailerInterface;
import com.TheJobCoach.webapp.mainpage.client.LoginService;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.server.CoachSecurityCheck;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {
	
	static AccountManager account = new AccountManager();
	static EvaluationAccountManager evaluationAccount = new EvaluationAccountManager();
	
	private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	public MainPageReturnCode.CreateAccountStatus createAccount(
			UserId id,
			UserInformation info,
			String locale)
			throws IllegalArgumentException, CassandraException {
		logger.info("Create account request for: '" + id.userName + "' with EMail:'" + info.email + "' with locale '" + locale +"'");	
		
		Map<String, MailerInterface.Attachment> parts = new HashMap<String, MailerInterface.Attachment>();

		MailerFactory.getMailer().sendEmail("mathieu.avila@laposte.net", 
				"creation de compte de " + info.email, 
				"username:" + id.userName +"<br/>\n" + "mail: " + info.email,
				"noreply@www.thejobcoach.fr", parts);

		return account.createAccount(id, info, locale);
	}

	@Override
	public MainPageReturnLogin connect(String userName, String userPassword) throws CassandraException 
	{
		logger.info("Connection from: " + userName);
		MainPageReturnLogin result = account.loginAccount(userName, userPassword);
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		if (result.getLoginStatus() == MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK)
			CoachSecurityCheck.loginUser(result.id, session);
		session.setAttribute("accountmanager", account);
		session.setAttribute("userid", result.id);
		logger.info("Logged in with: " + result.id);
		return result;
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
	public UserId createTestUser(String lang, UserId.UserType type) throws CassandraException, SystemException 
	{
		// purge old accounts.
		evaluationAccount.purgeTestAccount(60 * 60 * 24); // 1 day
		// create account
		UserId testAccount = evaluationAccount.createTestAccount(lang, type);
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		CoachSecurityCheck.disconnectUser(session);
		CoachSecurityCheck.loginUser(testAccount, session);
		session.setAttribute("accountmanager", account);
		session.setAttribute("userid", testAccount);
		logger.info("Logged in with: " + testAccount);
		return testAccount;
	}

	@Override
	public String disconnect()
			throws CassandraException, SystemException
	{
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		CoachSecurityCheck.disconnectUser(session);
		session.invalidate();
		return "";
	}

}
