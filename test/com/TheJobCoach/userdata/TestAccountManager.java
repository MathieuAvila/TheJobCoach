package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MockMailer;
import com.TheJobCoach.util.MailerInterface.Attachment;
import com.TheJobCoach.webapp.adminpage.shared.UserSearchResult;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin.LoginStatus;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;


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

	static class FakeDataManager implements IUserDataManager
	{
		public int deleteCount = 0;
		public int createTestCount = 0;
		public int createDefaultCount = 0;

		public void reset()
		{
			deleteCount = 0;
			createTestCount = 0;
			createDefaultCount = 0;
		}

		@Override
		public void deleteUser(UserId user) throws CassandraException
		{
			deleteCount++;
		}

		@Override
		public void createTestUser(UserId user, String lang)
		{
			createTestCount++;
		}

		@Override
		public void createUserDefaults(UserId user, String lang)
		{
			createDefaultCount++;
		}

	}

	static FakeDataManager fakeManager = new FakeDataManager();

	static {
		UserDataCentralManager.addManager(fakeManager);
	}

	@Test
	public void test00CreateAccount() throws CassandraException
	{
		account.deleteAccount(id);
		fakeManager.reset();

		MailerFactory.setMailer(mockMail);
		CreateAccountStatus status = account.createAccountWithToken(
				new UserId(id, "mytoken", UserId.UserType.USER_TYPE_SEEKER),
				new UserInformation("nom", email, "password","prenom"), 
				"FR");
		assertEquals( CreateAccountStatus.CREATE_STATUS_OK, status);
		String mail = mockMail.lastBody;
		assertEquals(true, mail.contains("token=mytoken"));
		assertEquals(true, mail.contains("nom"));
		assertEquals(true, mail.contains("prenom"));
		assertEquals(true, mail.contains("charset=utf-8")); // encoding
		assertEquals(true, mail.contains("demandé")); // encoding
		assertEquals(1, mockMail.lastParts.size()); // image header
		String sb1 = mail.substring(mail.indexOf("token=") + new String("token=").length());
		//System.out.println("OUTPUT BODY :"+mail);
		//System.out.println("OUTPUT BODY SB :"+sb1);
		token = sb1.substring(0, sb1.indexOf("</a>"));
		assertEquals("mytoken", token);
		// Check data manager is called.
		assertEquals(1, fakeManager.createDefaultCount);
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

		// Check create + delete CentralDataManager calls & counters
		fakeManager.reset();
		MailerFactory.setMailer(mockMail);
		CreateAccountStatus status = account.createAccountWithToken(
				new UserId(id, "mytoken", UserId.UserType.USER_TYPE_SEEKER),
				new UserInformation("nom", email, "password","prenom"), 
				"FR");
		assertEquals( CreateAccountStatus.CREATE_STATUS_OK, status);
		assertEquals(0, fakeManager.deleteCount);
		account.deleteAccount(id);
		// Check data manager is called.
		assertEquals(1, fakeManager.deleteCount);				
	}

	public String getPasswordFromMail(String mail)
	{
		String split[] = mail.split("Mot de passe : <strong>");
		assertEquals(2, split.length);
		String split2[] = split[1].split("</strong>");
		assertTrue(split2.length >= 2);
		return split2[0];
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
		assertTrue(mail.contains(idSeeker));
		assertFalse(mail.contains("passwordXXX"));
		//assertEquals(true, mail.contains("prenom"));
		assertTrue(mail.contains("charset=utf-8")); // encoding
		assertTrue(mail.contains("demandé")); // encoding
		assertEquals(1, mockMail.lastParts.size()); // image header

		Map<String, Attachment> lastParts = mockMail.lastParts;
		assertEquals(1, lastParts.size());

		// Now try to log in with new password
		String newPassword = getPasswordFromMail(mail);
		System.out.println("NEW PASSWORD: " + newPassword);
		MainPageReturnLogin loginCred = account.loginAccount(idSeeker, newPassword);
		assertEquals(loginCred.getLoginStatus(), LoginStatus.CONNECT_STATUS_OK);
		loginCred = account.loginAccount(idSeeker, "toto11");
		assertEquals(loginCred.getLoginStatus(), LoginStatus.CONNECT_STATUS_PASSWORD);
	}

	@Test
	public void testUpgradePassword() throws CassandraException 
	{
		account.deleteAccount(idSeeker);
		MailerFactory.setMailer(mockMail);
		CreateAccountStatus status = account.createAccountWithToken(
				new UserId(idSeeker, tokenSeeker, UserId.UserType.USER_TYPE_SEEKER),
				new UserInformation("nom", email + "seeker", "password", "prenom"), "en", 0);
		assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(idSeeker, tokenSeeker));

		// check password columnn
		Map<String, String> accountTable = CassandraAccessor.getRow(AccountManager.COLUMN_FAMILY_NAME_ACCOUNT, idSeeker);	
		System.out.println(accountTable);
		assertEquals(accountTable.get("password"), "password"); 


		// Login successfully should trigger an upgrade
		MainPageReturnLogin loginCred = account.loginAccount(idSeeker, "password");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
		assertEquals(UserId.UserType.USER_TYPE_SEEKER, loginCred.id.type);

		// Now check it's been upgraded
		accountTable = CassandraAccessor.getRow(AccountManager.COLUMN_FAMILY_NAME_ACCOUNT, idSeeker);	
		assertEquals(null, accountTable.get("password")); 
		// Check we still can login after upgrade
		loginCred = account.loginAccount(idSeeker, "password");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
		assertEquals(UserId.UserType.USER_TYPE_SEEKER, loginCred.id.type);

	}


	@Test
	public void testUpdatePassword() throws CassandraException 
	{
		account.deleteAccount(idSeeker);
		MailerFactory.setMailer(mockMail);
		UserId id = new UserId(idSeeker, tokenSeeker, UserId.UserType.USER_TYPE_SEEKER);
		CreateAccountStatus status = account.createAccountWithToken(
				id,
				new UserInformation("nom", email + "seeker", "password", "prenom"), "en");
		assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(idSeeker, tokenSeeker));

		// Login successfully
		MainPageReturnLogin loginCred = account.loginAccount(idSeeker, "password");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
		assertEquals(UserId.UserType.USER_TYPE_SEEKER, loginCred.id.type);

		// Now change
		account.setPassword(id, "NEWPASS");

		// Check new login with NEW password
		loginCred = account.loginAccount(idSeeker, "NEWPASS");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
		assertEquals(UserId.UserType.USER_TYPE_SEEKER, loginCred.id.type);

		// Check failure with previous pass
		loginCred = account.loginAccount(idSeeker, "password");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_PASSWORD, loginCred.getLoginStatus());
	}

	void checkOneSearchResult(UserSearchResult result, int pos, String id, String lastName, String firstName, String job, UserId.UserType type)
	{
		assertTrue(result.entries.size() > pos);
		assertEquals(id, result.entries.get(pos).userName);
		assertEquals(lastName, result.entries.get(pos).lastName);
		assertEquals(firstName, result.entries.get(pos).firstName);
		assertEquals(type, result.entries.get(pos).type);
		assertEquals(job, result.entries.get(pos).job);
	}

	@Test
	public void searchUsersTest() throws CassandraException, SystemException
	{
		final int TOTAL_RANGE_SIZE = 100;
		UserValues userValues = new UserValues();
		class UserDef {

			public UserDef(UserId id, UserInformation info,
					boolean allowSeeker, boolean allowCoach,
					boolean allowRecruiter)
			{
				super();
				this.id = id;
				this.info = info;
				this.allowSeeker = allowSeeker;
				this.allowCoach = allowCoach;
				this.allowRecruiter = allowRecruiter;
				this.setValues = true;
			}

			public UserDef(UserId id, UserInformation info)
			{
				super();
				this.id = id;
				this.info = info;
				this.setValues = false;
			}

			public UserDef(UserId id, UserInformation info, String job)
			{
				super();
				this.id = id;
				this.info = info;
				this.setValues = false;
				this.job = job;
			}

			public UserId id;
			public UserInformation info;
			public String job;

			public boolean allowSeeker;
			public boolean allowCoach;
			public boolean allowRecruiter;
			public boolean setValues;

		}
		// seeker user, used for search
		UserDef seekerSearch = new UserDef(
				new UserId("u_seeker", "u_seeker", UserId.UserType.USER_TYPE_SEEKER), 
				new UserInformation("SEEKER USER", "", "", "SEEKER USER"));
		// coach user, used for search
		UserDef coachSearch = new UserDef(
				new UserId("u_coach", "u_coach", UserId.UserType.USER_TYPE_COACH), 
				new UserInformation("COACH USER", "", "", "COACH USER"));

		UserDef static_users[] = {
				seekerSearch,
				coachSearch,
				// OK for ALL
				new UserDef(
						new UserId("u1", "u1", UserId.UserType.USER_TYPE_SEEKER), 
						new UserInformation("TEST_U NameA", "", "", "TEST_U FirstNameA"), 
						true, true, true)
				,				
				// NOT OK for COACH
				new UserDef(
						new UserId("u2", "u2", UserId.UserType.USER_TYPE_SEEKER), 
						new UserInformation("TEST_U NameB", "", "", "TEST_U FirstNameB"), 
						true, false, true)
				,
				// NOT OK for SEEKER
				new UserDef(
						new UserId("u3", "u3", UserId.UserType.USER_TYPE_SEEKER), 
						new UserInformation("TEST_U NameC", "", "", "TEST_U FirstNameC"), 
						false, true, true)
				,
				// Values NOT SET
				new UserDef(
						new UserId("u4", "u4", UserId.UserType.USER_TYPE_SEEKER), 
						new UserInformation("TEST_U NameD", "", "", "TEST_U FirstNameD"))
				,
				// NameE - FirstNameE
				new UserDef(
						new UserId("u5", "u5", UserId.UserType.USER_TYPE_SEEKER), 
						new UserInformation("TEST_U NameE", "", "", "TEST_U FirstNameE"), 
						true, true, true)
				,
				// NameE - FirstNameF
				new UserDef(
						new UserId("u6", "u6", UserId.UserType.USER_TYPE_SEEKER), 
						new UserInformation("TEST_U NameE", "", "", "TEST_U FirstNameF"),
						true, true, true)
				,
				// NameF - FirstNameF - Job is set - Type is COACH
				new UserDef(
						new UserId("u7", "u7", UserId.UserType.USER_TYPE_COACH), 
						new UserInformation("TEST_U NameF", "", "", "TEST_U FirstNameF"), 
						"this is my job"),
						// "Jean-Noël" "De la République"
						new UserDef(
								new UserId("u_accent", "u_accent", UserId.UserType.USER_TYPE_SEEKER), 
								new UserInformation("De la République", "", "", "Jean-Noël"), 
								"")
		};

		// Range checking
		Vector<UserDef> users = new Vector<UserDef>(Arrays.asList(static_users));
		for (int i = 0; i != TOTAL_RANGE_SIZE; i++)
		{
			String val = String.format("%05d", i);
			users.add(new UserDef(
					new UserId("urange"+val, "urange"+val, UserId.UserType.USER_TYPE_SEEKER), 
					new UserInformation("TEST_U RangeName" + val, "", "", "TEST_U RangeFirstName" + val)));
		}

		for (UserDef user: users)
		{
			account.deleteAccount(user.id.userName);
			MailerFactory.setMailer(mockMail);
			user.info.email = user.id.userName + "@toto.com";
			CreateAccountStatus status = account.createAccountWithToken(user.id, user.info, "en");
			assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
			ValidateAccountStatus validated = account.validateAccount(user.id.userName, user.id.token);
			assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, validated);
			if (user.setValues)
			{
				userValues.setValue(user.id, UserValuesConstantsAccount.ACCOUNT_PUBLISH_COACH, user.allowCoach ? "YES":"NO", false);
				userValues.setValue(user.id, UserValuesConstantsAccount.ACCOUNT_PUBLISH_SEEKER, user.allowSeeker ? "YES":"NO", false);
				userValues.setValue(user.id, UserValuesConstantsAccount.ACCOUNT_PUBLISH_RECRUITER, user.allowRecruiter ? "YES":"NO", false);
			}
			if (user.job != null)
			{
				userValues.setValue(user.id, UserValuesConstantsAccount.ACCOUNT_TITLE, user.job, false);
			}
		}

		// 1/ normal search - as a SEEKER
		UserSearchResult result = account.searchUsers(seekerSearch.id, "TEST_U FirstName", "TEST_U Name", 100 /* sizeRange */, 0 /* startRange*/);
		assertEquals(6, result.totalCount);
		assertEquals(6, result.entries.size());

		checkOneSearchResult(result, 0, "u1", "TEST_U NameA", "TEST_U FirstNameA", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 1, "u2", "TEST_U NameB", "TEST_U FirstNameB", "", UserId.UserType.USER_TYPE_SEEKER);
		// EXCLUDING SEEKER - checkOneSearchResult(result, 2, "u3", "TEST_U NameC", "TEST_U FirstNameC", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 2, "u4", "TEST_U NameD", "TEST_U FirstNameD", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 3, "u5", "TEST_U NameE", "TEST_U FirstNameE", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 4, "u6", "TEST_U NameE", "TEST_U FirstNameF", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 5, "u7", "TEST_U NameF", "TEST_U FirstNameF", "this is my job", UserId.UserType.USER_TYPE_COACH);

		// 2/ normal search - as a COACH
		result = account.searchUsers(coachSearch.id, "TEST_U FirstName", "TEST_U Name", 100 /* sizeRange */, 0 /* startRange*/);
		assertEquals(6, result.totalCount);
		assertEquals(6, result.entries.size());
		// EXCLUDING COACH - checkOneSearchResult(result, 1, "u2", "TEST_U NameB", "TEST_U FirstNameB", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 1, "u3", "TEST_U NameC", "TEST_U FirstNameC", "", UserId.UserType.USER_TYPE_SEEKER);

		// With special characters
		result = account.searchUsers(seekerSearch.id, "jean-noel", "republique", 10 /* sizeRange */, 0 /* startRange*/);
		assertEquals(1, result.totalCount);
		assertEquals(1, result.entries.size());
		checkOneSearchResult(result, 0, "u_accent", "De la République", "Jean-Noël", "", UserId.UserType.USER_TYPE_SEEKER);

		// 3/ Range search
		// From 0 to 9
		result = account.searchUsers(seekerSearch.id, "TEST_U RangeFirstName", "TEST_U RangeName", 
				10, // sizeRange
				0 // startRange);
				assertEquals(TOTAL_RANGE_SIZE, result.totalCount);
		assertEquals(10, result.entries.size());
		checkOneSearchResult(result, 0, "urange00000", "TEST_U RangeName00000", "TEST_U RangeFirstName00000", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 1, "urange00001", "TEST_U RangeName00001", "TEST_U RangeFirstName00001", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 9, "urange00009", "TEST_U RangeName00009", "TEST_U RangeFirstName00009", "", UserId.UserType.USER_TYPE_SEEKER);

		// From 10 to 19
		result = account.searchUsers(seekerSearch.id, "TEST_U RangeFirstName", "TEST_U RangeName", 
				10, // sizeRange
				10 // startRange
				);
		assertEquals(TOTAL_RANGE_SIZE, result.totalCount);
		assertEquals(10, result.entries.size());
		checkOneSearchResult(result, 0, "urange00010", "TEST_U RangeName00010", "TEST_U RangeFirstName00010", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 1, "urange00011", "TEST_U RangeName00011", "TEST_U RangeFirstName00011", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 9, "urange00019", "TEST_U RangeName00019", "TEST_U RangeFirstName00019", "", UserId.UserType.USER_TYPE_SEEKER);

		// From 95 to 99
		result = account.searchUsers(seekerSearch.id, "TEST_U RangeFirstName", "TEST_U RangeName", 
				10, // sizeRange, 
				95 // startRange
				);
		assertEquals(TOTAL_RANGE_SIZE, result.totalCount);
		assertEquals(5, result.entries.size());
		checkOneSearchResult(result, 0, "urange00095", "TEST_U RangeName00095", "TEST_U RangeFirstName00095", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 1, "urange00096", "TEST_U RangeName00096", "TEST_U RangeFirstName00096", "", UserId.UserType.USER_TYPE_SEEKER);
		checkOneSearchResult(result, 4, "urange00099", "TEST_U RangeName00099", "TEST_U RangeFirstName00099", "", UserId.UserType.USER_TYPE_SEEKER);
	}

}
