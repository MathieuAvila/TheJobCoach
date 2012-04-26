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
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class TestAccount
{

	static Account account = new Account();

	static String id =  "toto" + UUID.randomUUID().hashCode();
	static String email =  id + "@toto.com";
	static MockMailer mockMail = new MockMailer();
	static String token;

	static String idAdmin =  "admin" + UUID.randomUUID().hashCode();
	static String tokenAdmin = "tokenAdmin" + UUID.randomUUID().hashCode();
	
	static String idCoach =  "coach" + UUID.randomUUID().hashCode();
	static String tokenCoach = "tokenCoach" + UUID.randomUUID().hashCode();
	
	static String idSeeker =  "seeker" + UUID.randomUUID().hashCode();
	static String tokenSeeker = "tokenSeeker" + UUID.randomUUID().hashCode();
	
	@Test
	public void testCreateAccount() throws CassandraException
	{
		MailerFactory.setMailer(mockMail);
		CreateAccountStatus status = account.createAccountWithToken(
				new UserId(id, "mytoken", UserId.UserType.USER_TYPE_SEEKER),
				new UserInformation("nom", email, "password","prenom"), "en");
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
	public void testGetUsernameFromEmail()
	{	
		assertEquals(id, account.getUsernameFromEmail(email));
		assertNull(account.getUsernameFromEmail("nop@nop.com"));
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
			MailerFactory.setMailer(mockMail);			
			CreateAccountStatus status = account.createAccountWithToken(
					new UserId(idAdmin, tokenAdmin, UserId.UserType.USER_TYPE_ADMIN),
					new UserInformation("nom", email + "admin", "password", "prenom"), "en");
			
			assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
			assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(idAdmin, tokenAdmin));
			MainPageReturnLogin loginCred = account.loginAccount(idAdmin, "password");
			assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
			assertEquals(UserId.UserType.USER_TYPE_ADMIN, loginCred.id.type);
		}
		{
			MailerFactory.setMailer(mockMail);
			CreateAccountStatus status = account.createAccountWithToken(
					new UserId(idCoach, tokenCoach, UserId.UserType.USER_TYPE_COACH),
					new UserInformation("nom", email + "coach", "password", "prenom"), "en");
			assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
			assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(idCoach, tokenCoach));
			MainPageReturnLogin loginCred = account.loginAccount(idCoach, "password");
			assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
			assertEquals(UserId.UserType.USER_TYPE_COACH, loginCred.id.type);
		}
		{
			MailerFactory.setMailer(mockMail);
			CreateAccountStatus status = account.createAccountWithToken(
					new UserId(idSeeker, tokenSeeker, UserId.UserType.USER_TYPE_SEEKER),
					new UserInformation("nom", email + "seeker", "password", "prenom"), "en");
			assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
			assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(idSeeker, tokenSeeker));
			MainPageReturnLogin loginCred = account.loginAccount(idSeeker, "password");
			assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
			assertEquals(UserId.UserType.USER_TYPE_SEEKER, loginCred.id.type);
		}
	}
	
	@Test
	public void testDeleteAccount() throws CassandraException
	{
		account.deleteAccount("toto"); // no throw
		account.deleteAccount(idAdmin);
		MainPageReturnLogin loginCred = account.loginAccount(idAdmin, "password");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_UNKNOWN_USER, loginCred.getLoginStatus());	
		
		account.deleteAccount(idAdmin);
		account.deleteAccount(idSeeker);
		account.deleteAccount(idCoach);
		account.deleteAccount(id);
				
	}

}
