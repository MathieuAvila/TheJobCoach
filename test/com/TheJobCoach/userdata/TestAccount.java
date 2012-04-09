package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MockMailer;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;

public class TestAccount
{

	static Account account = new Account();

	static String id =  "toto" + UUID.randomUUID().hashCode();
	static MockMailer mockMail = new MockMailer();
	static String token;

	@Test
	public void testCreateAccount() throws CassandraException
	{
		MailerFactory.setMailer(mockMail);
		CreateAccountStatus status = account.createAccountWithToken(
				new UserId(id, "mytoken", UserId.UserType.USER_TYPE_SEEKER),
				new UserInformation("nom", "toto@toto.com", "password","prenom"), "en");
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
	public void testValidateAccount() throws CassandraException
	{	
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_NOT_VALIDATED, account.loginAccount(id, "password").getLoginStatus());
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(id, token));
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_UNKNOWN, account.validateAccount("nop", token));
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_PASSWORD, account.loginAccount(id, "passworderror").getLoginStatus());
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, account.loginAccount(id, "password").getLoginStatus());
	}

	@Test
	public void testCreateAccountType() throws CassandraException
	{
		{
			String idAdmin =  "admin" + UUID.randomUUID().hashCode();
			String tokenAdmin = "tokenAdmin" + UUID.randomUUID().hashCode();
			MailerFactory.setMailer(mockMail);			
			CreateAccountStatus status = account.createAccountWithToken(
					new UserId(idAdmin, tokenAdmin, UserId.UserType.USER_TYPE_ADMIN),
					new UserInformation("nom", "toto@toto.com", "password", "prenom"), "en");
			
			assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
			assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(idAdmin, tokenAdmin));
			MainPageReturnLogin loginCred = account.loginAccount(idAdmin, "password");
			assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
			assertEquals(UserId.UserType.USER_TYPE_ADMIN, loginCred.id.type);
		}
		{
			String idCoach =  "coach" + UUID.randomUUID().hashCode();
			String tokenCoach = "tokenCoach" + UUID.randomUUID().hashCode();
			MailerFactory.setMailer(mockMail);
			CreateAccountStatus status = account.createAccountWithToken(
					new UserId(idCoach, tokenCoach, UserId.UserType.USER_TYPE_COACH),
					new UserInformation("nom", "toto@toto.com", "password", "prenom"), "en");
			assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
			assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(idCoach, tokenCoach));
			MainPageReturnLogin loginCred = account.loginAccount(idCoach, "password");
			assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
			assertEquals(UserId.UserType.USER_TYPE_COACH, loginCred.id.type);
		}
		{
			String idSeeker =  "seeker" + UUID.randomUUID().hashCode();
			String tokenSeeker = "tokenSeeker" + UUID.randomUUID().hashCode();
			MailerFactory.setMailer(mockMail);
			CreateAccountStatus status = account.createAccountWithToken(
					new UserId(idSeeker, tokenSeeker, UserId.UserType.USER_TYPE_SEEKER),
					new UserInformation("nom", "toto@toto.com", "password", "prenom"), "en");
			assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
			assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(idSeeker, tokenSeeker));
			MainPageReturnLogin loginCred = account.loginAccount(idSeeker, "password");
			assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
			assertEquals(UserId.UserType.USER_TYPE_SEEKER, loginCred.id.type);
		}
	}

}
