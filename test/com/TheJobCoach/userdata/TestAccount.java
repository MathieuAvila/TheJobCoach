package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MockMailer;
import com.TheJobCoach.util.MailerInterface.Attachment;
import com.TheJobCoach.webapp.adminpage.shared.UserReport;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.TheJobCoach.webapp.util.shared.SystemException;

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
	public void test01ExistsAccount()
	{	
		assertEquals(false, account.existsAccount("fake one"));
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
	public void test06GetTestAccountList() throws CassandraException
	{
		Map<String, String> accountList = account.getTestAccountList();
		Collection<String> accountNames = accountList.values();
		account.purgeTestAccount(0);
		accountList = account.getTestAccountList();
		accountNames = accountList.values();
		assertEquals(0, accountNames.size());
	}
	
	@Test
	public void test07CreateTestAccount() throws CassandraException, SystemException, InterruptedException
	{
		String beforeTime = SiteUUID.getDateUuid();		
		UserId user1 = account.createTestAccount("FR", UserId.UserType.USER_TYPE_SEEKER);
		String afterTime = SiteUUID.getDateUuid();		
		Map<String, String> accountList = account.getTestAccountList();
		assertEquals(1, accountList.size());
		Vector<String> accountNames = new Vector<String>(accountList.values());
		Vector<String> accountTimes = new Vector<String>(accountList.keySet());
		assertEquals(1, accountNames.size());
		assertEquals(user1.userName, accountNames.get(0));
		assertEquals(1, accountTimes.size());
		String time = accountTimes.get(0);
		assertTrue(beforeTime.compareTo(time) < 0);
		assertTrue(time.compareTo(afterTime) < 0);
		
		Thread.currentThread();
		Thread.sleep(2000);
		
		UserId user2 = account.createTestAccount("FR", UserId.UserType.USER_TYPE_SEEKER);
		String afterTime2 = SiteUUID.getDateUuid();		
		accountList = account.getTestAccountList();
		assertEquals(2, accountList.size());
		assertTrue(accountList.values().contains(user1.userName));
		assertTrue(accountList.values().contains(user2.userName));
		Vector<String> accountTimes2 = new Vector<String>(accountList.keySet());
		assertTrue(accountTimes2.get(0).equals(time));
		String time2 = accountTimes2.get(1);
		assertTrue(afterTime.compareTo(time2) < 0);
		assertTrue(time2.compareTo(afterTime2) < 0);
						
		MainPageReturnLogin loginCred1 = account.loginAccount(user1.userName, "recherche");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred1.getLoginStatus());
		
		MainPageReturnLogin loginCred2 = account.loginAccount(user2.userName, "recherche");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred2.getLoginStatus());

		account.purgeTestAccount(1);
		accountList = account.getTestAccountList();
		Vector<String> accountNames2 = new Vector<String>(accountList.values());
		assertEquals(1, accountNames2.size());
		assertEquals(user2.userName, accountNames2.get(0));
		
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
		assertEquals(1, mockMail.lastParts.size());
	}

}
