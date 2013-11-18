package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.UUID;
import org.junit.Test;

import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MockMailer;
import com.TheJobCoach.util.MailerInterface.Attachment;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;


public class TestAccountManager
{

	static AccountManager account = new AccountManager();

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
	public void test00CreateAccount() throws CassandraException
	{
		account.deleteAccount(id);
		
		MailerFactory.setMailer(mockMail);
		CreateAccountStatus status = account.createAccountWithToken(
				new UserId(id, "mytoken", UserId.UserType.USER_TYPE_SEEKER),
				new UserInformation("nom", email, "password","prenom"), "FR");
		assertEquals( CreateAccountStatus.CREATE_STATUS_OK, status);
		String mail = mockMail.lastBody;
		assertEquals(true, mail.contains("token=mytoken"));
		assertEquals(true, mail.contains("nom"));
		assertEquals(true, mail.contains("prenom"));
		assertEquals(true, mail.contains("charset=utf-8")); // encoding
		assertEquals(true, mail.contains("demandé")); // encoding
		assertEquals(1, mockMail.lastParts.size()); // image header
		String sb1 = mail.substring(mail.indexOf("token=") + new String("token=").length());
		System.out.println("OUTPUT BODY :"+mail);
		System.out.println("OUTPUT BODY SB :"+sb1);
		token = sb1.substring(0, sb1.indexOf("</a>"));
		assertEquals("mytoken", token);
	}

	@Test
	public void test01ExistsAccount() throws CassandraException
	{	
		assertEquals(false, account.existsAccount("fake one"));
		test00CreateAccount();
		assertEquals(true, account.existsAccount(id));
	}

	@Test
	public void test02GetUsernameFromEmail()
	{	
		assertEquals(id, account.getUsernameFromEmail(email));
		assertNull(account.getUsernameFromEmail("nop@nop.com"));
	}

	@Test
	public void test03ValidateAccount() throws CassandraException
	{
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_NOT_VALIDATED, account.loginAccount(id, "password").getLoginStatus());
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(id, token));
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_UNKNOWN, account.validateAccount("nop", token));
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_PASSWORD, account.loginAccount(id, "passworderror").getLoginStatus());
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, account.loginAccount(id, "password").getLoginStatus());
	}

	@Test
	public void test04CreateAccountType() throws CassandraException
	{
		{
			account.deleteAccount(idAdmin);
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
			account.deleteAccount(idCoach);
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
			account.deleteAccount(idSeeker);
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
	public void test05DeleteAccount() throws CassandraException
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

	@Test
	public void testLostCredentials() throws CassandraException 
	{	
		MailerFactory.setMailer(mockMail);

		Boolean testRes = account.lostCredentials("nimportnawak", "FR");
		assertEquals(false, testRes.booleanValue());
		
		{			
			account.createAccountWithToken(
					new UserId(idSeeker, tokenSeeker, UserId.UserType.USER_TYPE_SEEKER),
					new UserInformation("nom", email + "seeker", "passwordXXX", "prenom"), "en");
			account.validateAccount(idSeeker, tokenSeeker);
		}

		testRes = account.lostCredentials(email + "seeker", "FR");
		assertEquals(true, testRes.booleanValue());
		
		String mail = mockMail.lastBody;
		System.out.println("CRED MAIL: " + mail);
		assertEquals(true, mail.contains(idSeeker));
		assertEquals(true, mail.contains("passwordXXX"));
		//assertEquals(true, mail.contains("prenom"));
		assertEquals(true, mail.contains("charset=utf-8")); // encoding
		assertEquals(true, mail.contains("demandé")); // encoding
		assertEquals(1, mockMail.lastParts.size()); // image header
		
		Map<String, Attachment> lastParts = mockMail.lastParts;
		assertEquals(1, lastParts.size());
	}

}
