package com.TheJobCoach.util;

import com.TheJobCoach.userdata.Account;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;

import org.junit.Test;

public class CreateTestDefault {

	static Account account = new Account();

	@Test
	public void testCreateAccount()
	{
		{
		MockMailer mockMail = new MockMailer();
		MailerFactory.setMailer(mockMail);
		CreateAccountStatus status = account.createAccountWithToken("mytoken", "user", "nom", "prenom", "toto@toto.com", "password", "en", UserType.USER_TYPE_SEEKER);
		System.out.println("Created account returned: " + status.toString());
		ValidateAccountStatus validate = account.validateAccount("user", "mytoken");
		System.out.println("Validate account returned: " + validate.toString());
		MainPageReturnLogin token = account.loginAccount("user", "password");
		System.out.println("Login account returned: " + token.getLoginStatus() + " with token: " + token.id.token);
		}
		{
		CreateAccountStatus status = account.createAccountWithToken("mytokenadmin", "admin", "nom", "prenom", "toto@toto.com", "passwordadmin", "en", UserType.USER_TYPE_ADMIN);
		System.out.println("Created account returned: " + status.toString());
		ValidateAccountStatus validate = account.validateAccount("user", "mytoken");
		System.out.println("Validate account returned: " + validate.toString());
		MainPageReturnLogin token = account.loginAccount("user", "password");
		System.out.println("Login account returned: " + token.getLoginStatus() + " with token: " + token.id.token);
		}
		{
		CreateAccountStatus status = account.createAccountWithToken("mytokencoach", "coach", "nom", "prenom", "toto@toto.com", "passwordcoach", "en", UserId.UserType.USER_TYPE_COACH);
		System.out.println("Created account returned: " + status.toString());
		ValidateAccountStatus validate = account.validateAccount("user", "mytoken");
		System.out.println("Validate account returned: " + validate.toString());
		MainPageReturnLogin token = account.loginAccount("user", "password");
		System.out.println("Login account returned: " + token.getLoginStatus() + " with token: " + token.id.token);
		}
	}
}
