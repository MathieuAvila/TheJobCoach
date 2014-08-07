package com.TheJobCoach.util;

import static org.junit.Assert.assertEquals;

import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.userdata.ContactManager;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
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
	public void testCreateAccount() throws CassandraException, InterruptedException
	{
		MockMailer mockMail = new MockMailer();
		MailerFactory.setMailer(mockMail);
		
		UserId user1 = new UserId("user1","mytoken1", UserId.UserType.USER_TYPE_SEEKER);
		UserId user2 = new UserId("user2","mytoken2", UserId.UserType.USER_TYPE_SEEKER);
		
		{
			UserInformation userInfo = new UserInformation("Nuser1", "mathieu.avila@laposte.net", "password", "Nprenomuser1");
			deleteAccountNoException("user1", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					user1,
					userInfo, "en"));
			account.validateAccount(user1.userName, user1.token);
			logger.info("logging in user1: " + account.loginAccount("user1", "password").getLoginStatus());
		}
		logger.info("");
		Thread.sleep(1000);
		{
			UserInformation userInfo = new UserInformation("Nuser2", "mathieu.avila@free.fr", "password", "Nprenomuser2");
			deleteAccountNoException("user2", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					user2,
					userInfo, "en"));
			account.validateAccount(user2.userName, user2.token);
			account.loginAccount(user2.userName, "password");
			logger.info("logging in user2: " + account.loginAccount("user2", "password").getLoginStatus());
		}
		Thread.sleep(1000);
		{
			ContactManager contactManager1 = new ContactManager(user1);
			ContactManager contactManager2 = new ContactManager(user2);
			// connect user1 and user2
			ContactInformation.ContactStatus newStatus = contactManager1.updateContactRequest(user2, true);
			assertEquals(ContactInformation.ContactStatus.CONTACT_REQUESTED, newStatus);
			newStatus = contactManager2.updateContactRequest(user1, true);
			assertEquals(ContactInformation.ContactStatus.CONTACT_OK, newStatus);
		}
		Thread.sleep(1000);
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
		Thread.sleep(1000);
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
		Thread.sleep(1000);
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
