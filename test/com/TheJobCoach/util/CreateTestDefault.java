package com.TheJobCoach.util;

import com.TheJobCoach.userdata.Account;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.util.shared.CassandraException;

import org.junit.Test;

public class CreateTestDefault {

	static Account account = new Account();

	@Test
	public void testCreateAccount() throws CassandraException
	{
		{
			MockMailer mockMail = new MockMailer();
			MailerFactory.setMailer(mockMail);
			account.deleteAccount("user");
			account.createAccountWithToken(
					new UserId("user","mytoken", UserType.USER_TYPE_SEEKER),
					new UserInformation("nom", "toto@toto.com", "password", "prenom"), "en");
			account.validateAccount("user", "mytoken");
			account.loginAccount("user", "password");
		}
		{
			account.deleteAccount("admin");
			account.createAccountWithToken(
					new UserId("admin","mytokenadmin", UserType.USER_TYPE_ADMIN),
					new UserInformation("nom", "admintoto@toto.com", "password", "admintoto@toto.com"), "en");
			account.validateAccount("admin", "mytokenadmin");
			account.loginAccount("admin", "password");
		}
		{
			account.deleteAccount("coach");
			account.createAccountWithToken(
					new UserId("coach","mytokencoach", UserType.USER_TYPE_COACH),
					new UserInformation("nom", "coachtoto@toto.com", "password", "coachtoto@toto.com"), "en");
			account.validateAccount("coach", "mytokencoach");
			account.loginAccount("coach", "password");			
		}
	}
}
