package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MockMailer;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;

public class TestAccount
{

	static Account account = new Account();

	static String id =  "toto" + UUID.randomUUID().hashCode();
	static MockMailer mockMail = new MockMailer();
	static String token;
	
	@Test
	public void testCreateAccount()
	{
		MailerFactory.setMailer(mockMail);
		CreateAccountStatus status = account.createAccountWithToken("mytoken", id, "nom", "prenom", "toto@toto.com", "password", "en");
		assertEquals( CreateAccountStatus.CREATE_STATUS_OK, status);
		String mail = mockMail.lastBody;
		assertEquals(true, mail.contains("token="));
		String sb1 = mail.substring(mail.indexOf("token=") + new String("token=").length());
		token = sb1.substring(0, sb1.indexOf("\n\nWe"));
		assertEquals("mytoken", token);
		System.out.println("Token:" + token);
	}

	@Test
	public void testExistsAccount()
	{	
		assertEquals(false, account.existsAccount("fake one"));
		assertEquals(true, account.existsAccount(id));
	}
	
	@Test
	public void testValidateAccount()
	{	
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_NOT_VALIDATED, account.loginAccount(id, "password").getLoginStatus());
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(id, token));
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_UNKNOWN, account.validateAccount("nop", token));
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, account.loginAccount(id, "password").getLoginStatus());
	}
	
	
	
}
