package com.TheJobCoach.util;

import com.TheJobCoach.userdata.Account;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;

import org.junit.Test;

public class CreateTestDefault {

	static Account account = new Account();

	@Test
	public void testCreateAccount()
	{
		MockMailer mockMail = new MockMailer();
		MailerFactory.setMailer(mockMail);	
		CreateAccountStatus status = account.createAccountWithToken("mytoken", "user", "nom", "prenom", "toto@toto.com", "password", "en");
		System.out.println("Created account returned: " + status.toString());
		ValidateAccountStatus validate = account.validateAccount("user", "mytoken");
		System.out.println("Validate account returned: " + validate.toString());
		MainPageReturnLogin token = account.loginAccount("user", "password");
		System.out.println("Login account returned: " + token.getLoginStatus() + " with token: " + token.getToken());
		
	}
}
