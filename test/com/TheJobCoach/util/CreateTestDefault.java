package com.TheJobCoach.util;

import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.userdata.ContactManager;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTestDefault {

	AccountManager account = new AccountManager();
	private static Logger logger = LoggerFactory.getLogger(CreateTestDefault.class);

	public void deleteAccountNoException(String userName, String email)
	{
		String userMail = account.getUsernameFromEmail(email);
		try { 
			account.deleteAccount(userName);
		account.deleteAccount(userMail);} 
		catch (Exception e) {};
	}
	
	@Test
	public void testCreateAccount() throws CassandraException
	{
		MockMailer mockMail = new MockMailer();
		MailerFactory.setMailer(mockMail);
		
		{
			UserInformation userInfo = new UserInformation("Nuser1", "mathieu.avila@laposte.net", "password", "Nprenomuser1");
			deleteAccountNoException("user1", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					new UserId("user1","mytoken1", UserId.UserType.USER_TYPE_SEEKER),
					userInfo, "en"));
			account.validateAccount("user1", "mytoken1");
			logger.info("logging in user1: " + account.loginAccount("user1", "password").getLoginStatus());
		}
		logger.info("");
		{
			UserInformation userInfo = new UserInformation("Nuser2", "mathieu.avila@free.fr", "password", "Nprenomuser2");
			deleteAccountNoException("user2", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					new UserId("user2","mytoken2", UserId.UserType.USER_TYPE_SEEKER),
					userInfo, "en"));
			account.validateAccount("user2", "mytoken2");
			account.loginAccount("user2", "password");
			logger.info("logging in user2: " + account.loginAccount("user2", "password").getLoginStatus());
		}
		{
			UserInformation userInfo = new UserInformation("nom", "testtoto@toto.com", "password", "prenom");
			deleteAccountNoException("test_user", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					new UserId("test_user","test_token", UserId.UserType.USER_TYPE_SEEKER, true),
					new UserInformation("nom", "testtoto@toto.com", "password", "prenom"), "en"));
			account.markTestAccount("test_user");
			account.validateAccount("test_user", "test_token");
			account.loginAccount("test_user", "password");
			logger.info("logging in test_user: " + account.loginAccount("test_user", "password").getLoginStatus());
		}
		{
			UserInformation userInfo = new UserInformation("nom", "admintoto@toto.com", "password", "admintoto@toto.com");
			deleteAccountNoException("admin", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					new UserId("admin","mytokenadmin", UserId.UserType.USER_TYPE_ADMIN),
					new UserInformation("nom", "admintoto@toto.com", "password", "admintoto@toto.com"), "en"));
			account.validateAccount("admin", "mytokenadmin");
			account.loginAccount("admin", "password");
			logger.info("logging in admin: " + account.loginAccount("admin", "password").getLoginStatus());
		}
		{
			UserInformation userInfo = new UserInformation("nom", "coachtoto@toto.com", "password", "coachtoto@toto.com");
			deleteAccountNoException("coach", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					new UserId("coach","mytokencoach", UserId.UserType.USER_TYPE_COACH),
					new UserInformation("nom", "coachtoto@toto.com", "password", "coachtoto@toto.com"), "en"));
			account.validateAccount("coach", "mytokencoach");
			account.loginAccount("coach", "password");			
			logger.info("logging in coach: " + account.loginAccount("coach", "password").getLoginStatus());
		}
	}
}
