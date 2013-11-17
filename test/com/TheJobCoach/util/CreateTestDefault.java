package com.TheJobCoach.util;

import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;


import org.junit.Test;

public class CreateTestDefault {

	static AccountManager account = new AccountManager();

	@Test
	public void testCreateAccount() throws CassandraException
	{
		{
			MockMailer mockMail = new MockMailer();
			MailerFactory.setMailer(mockMail);
			account.deleteAccount("user");
			account.createAccountWithToken(
					new UserId("user","mytoken", UserId.UserType.USER_TYPE_SEEKER),
					new UserInformation("nom", "toto@toto.com", "password", "prenom"), "en");
			account.validateAccount("user", "mytoken");
			account.loginAccount("user", "password");
		}
		{
			account.deleteAccount("admin");
			account.createAccountWithToken(
					new UserId("admin","mytokenadmin", UserId.UserType.USER_TYPE_ADMIN),
					new UserInformation("nom", "admintoto@toto.com", "password", "admintoto@toto.com"), "en");
			account.validateAccount("admin", "mytokenadmin");
			account.loginAccount("admin", "password");
		}
		{
			account.deleteAccount("coach");
			account.createAccountWithToken(
					new UserId("coach","mytokencoach", UserId.UserType.USER_TYPE_COACH),
					new UserInformation("nom", "coachtoto@toto.com", "password", "coachtoto@toto.com"), "en");
			account.validateAccount("coach", "mytokencoach");
			account.loginAccount("coach", "password");			
		}
	}
}
