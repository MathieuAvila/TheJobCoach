package com.TheJobCoach.util;

import com.TheJobCoach.userdata.Account;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
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
			CreateAccountStatus status = account.createAccountWithToken(
					new UserId("user","mytoken", UserType.USER_TYPE_SEEKER),
					new UserInformation("nom", "toto@toto.com", "password", "prenom"), "en");
			System.out.println("Created account returned: " + status.toString());
			ValidateAccountStatus validate = account.validateAccount("user", "mytoken");
			System.out.println("Validate account returned: " + validate.toString());
			MainPageReturnLogin token = account.loginAccount("user", "password");
			System.out.println("Login account returned: " + token.getLoginStatus());
			System.out.println("with token: " + token.id.token);
		}
		{
			account.deleteAccount("admin");
			CreateAccountStatus status = account.createAccountWithToken(
					new UserId("admin","mytokenadmin", UserType.USER_TYPE_ADMIN),
					new UserInformation("nom", "admintoto@toto.com", "password", "admintoto@toto.com"), "en");
			System.out.println("Created account returned: " + status.toString());
			ValidateAccountStatus validate = account.validateAccount("admin", "mytokenadmin");
			System.out.println("Validate account returned: " + validate.toString());
			MainPageReturnLogin token = account.loginAccount("user", "password");
			System.out.println("Login account returned: " + token.getLoginStatus() + " with token: " + token.id.token);
		}
		{
			account.deleteAccount("coach");
			CreateAccountStatus status = account.createAccountWithToken(
					new UserId("coach","mytokencoach", UserType.USER_TYPE_COACH),
					new UserInformation("nom", "coachtoto@toto.com", "password", "coachtoto@toto.com"), "en");
			System.out.println("Created account returned: " + status.toString());
			ValidateAccountStatus validate = account.validateAccount("coach", "mytokencoach");
			System.out.println("Validate account returned: " + validate.toString());
			MainPageReturnLogin token = account.loginAccount("user", "password");	
			System.out.println("Login account returned: " + token.getLoginStatus() + " with token: " + token.id.token);
		}
	}

	public static void main (String[] args)
	{
		System.out.println("Totos");	
	}
}
