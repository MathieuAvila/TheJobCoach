package com.TheJobCoach.userdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.util.ByteResourceCache;
import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.ImageUtil;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MailerInterface.Attachment;
import com.TheJobCoach.util.MockMailer;
import com.TheJobCoach.webapp.adminpage.shared.UserReport;
import com.TheJobCoach.webapp.adminpage.shared.UserSearchResult;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin.LoginStatus;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;


public class TestAccountManager
{
	AccountManager account = new AccountManager();

	String id =  "toto" + UUID.randomUUID().hashCode();
	String email =  id + "@toto.com";
	MockMailer mockMail = new MockMailer();
	String token;

	String idAdmin =  "admin" + UUID.randomUUID().hashCode();
	String tokenAdmin = "tokenAdmin" + UUID.randomUUID().hashCode();

	String idCoach =  "coach" + UUID.randomUUID().hashCode();
	String tokenCoach = "tokenCoach" + UUID.randomUUID().hashCode();

	String idSeeker =  "seeker" + UUID.randomUUID().hashCode();
	String tokenSeeker = "tokenSeeker" + UUID.randomUUID().hashCode();
	UserId userIdSeeker = new UserId(idSeeker, tokenSeeker, UserId.UserType.USER_TYPE_SEEKER);
	
	UserValues userValues = new UserValues();
	
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
	public void test_basic() throws CassandraException
	{
		account.deleteAccount(id);
		fakeManager.reset();
		UserId userId = new UserId(id, "mytoken", UserId.UserType.USER_TYPE_SEEKER);
		MailerFactory.setMailer(mockMail);
		CreateAccountStatus status = account.createAccountWithToken(
				userId,
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
		
		// get language
		String lang = account.getUserLanguage(userId);
		assertEquals("FR", lang);
		
		// test01ExistsAccount() throws CassandraException	
		assertEquals(false, account.existsAccount("fake one"));
		//test00CreateAccount();
		assertEquals(true, account.existsAccount(id));
		
		// test02GetUsernameFromEmail()	
		assertEquals(id, account.getUsernameFromEmail(email));
		assertNull(account.getUsernameFromEmail("nop@nop.com"));
	
		// test03ValidateAccount() throws CassandraException
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
					userIdSeeker,
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
					userIdSeeker,
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
	public void test_upgradePassword() throws CassandraException 
	{
		account.deleteAccount(idSeeker);
		MailerFactory.setMailer(mockMail);
		CreateAccountStatus status = account.createAccountWithToken(
				userIdSeeker,
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
	public void test_upgradeName() throws CassandraException, SystemException 
	{
		account.deleteAccount(idSeeker);
		MailerFactory.setMailer(mockMail);
		String realEmail = email + "seeker";
		CreateAccountStatus status = account.createAccountWithToken(
				userIdSeeker,
				new UserInformation("nom", realEmail, "password", "prenom"), "en", 1);
		assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(idSeeker, tokenSeeker));

		// check firstName columnn
		String firstName = userValues.getValue(userIdSeeker, UserValuesConstantsAccount.ACCOUNT_FIRSTNAME);	
		String lastName = userValues.getValue(userIdSeeker, UserValuesConstantsAccount.ACCOUNT_LASTNAME);	
		String email = userValues.getValue(userIdSeeker, UserValuesConstantsAccount.ACCOUNT_EMAIL);	
		System.out.println("was: "+firstName);
		System.out.println("was: "+lastName);
		System.out.println("was: "+email);
		assertEquals("", firstName);
		assertEquals("", lastName);
		assertEquals("", email);
			
		// Login successfully should trigger an upgrade
		MainPageReturnLogin loginCred = account.loginAccount(idSeeker, "password");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
		assertEquals(UserId.UserType.USER_TYPE_SEEKER, loginCred.id.type);

		// Now check it's been upgraded
		firstName = userValues.getValue(userIdSeeker, UserValuesConstantsAccount.ACCOUNT_FIRSTNAME);	
		lastName = userValues.getValue(userIdSeeker, UserValuesConstantsAccount.ACCOUNT_LASTNAME);	
		email = userValues.getValue(userIdSeeker, UserValuesConstantsAccount.ACCOUNT_EMAIL);	
		System.out.println("was: "+firstName);
		System.out.println("was: "+lastName);
		assertEquals("prenom", firstName);
		assertEquals("nom", lastName);
		assertEquals(realEmail, email);
		
		// Creating a new account should automatically set these.
		account.deleteAccount(idSeeker);
		MailerFactory.setMailer(mockMail);
		status = account.createAccountWithToken(
				userIdSeeker,
				new UserInformation("nom", realEmail, "password", "prenom"), "en", AccountManager.LAST_VERSION);
		assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, account.validateAccount(idSeeker, tokenSeeker));
		// check firstName columnn
		firstName = userValues.getValue(userIdSeeker, UserValuesConstantsAccount.ACCOUNT_FIRSTNAME);	
		lastName = userValues.getValue(userIdSeeker, UserValuesConstantsAccount.ACCOUNT_LASTNAME);	
		email = userValues.getValue(userIdSeeker, UserValuesConstantsAccount.ACCOUNT_EMAIL);	
		System.out.println("was: "+firstName);
		System.out.println("was: "+lastName);
		assertEquals("prenom", firstName);
		assertEquals("nom", lastName);
		assertEquals(realEmail, email);
		// and login will work either.
		loginCred = account.loginAccount(idSeeker, "password");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
		assertEquals(UserId.UserType.USER_TYPE_SEEKER, loginCred.id.type);
	}


	@Test
	public void testUpdatePassword() throws CassandraException 
	{
		account.deleteAccount(idSeeker);
		MailerFactory.setMailer(mockMail);
		UserId id = userIdSeeker;
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
				0); // startRange
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

		// clean-up DB
		for (UserDef user: users)
		{
			account.deleteAccount(user.id.userName);
		}
	}

	@Test
	public void test_getUserRange() throws CassandraException, SystemException
	{
		// clean-up
		Vector<String> range = new Vector<String>();
		String last = "";
		do
		{
			 last = account.getUserRange(last, 100, range);
			 for (String userName: range) account.deleteAccount(userName);			 
		} 
		while (range.size()!=0);
		
		// create a few accounts
		for (int i= 0; i != 50; i++)
		{
			CoachTestUtils.createOneAccount(new UserId("test_range_" + i));
		}
		
		// try to get all through different requests.
		Vector<String> fullRange = new Vector<String>();
		last = "";
		do
		{
			 last = account.getUserRange(last, 12, range);
			 for (String userName: range) 
				 fullRange.add(userName);
		} 
		while (range.size()!=0);
		
		// clean up
		for (int i= 0; i != 50; i++)
		{
			account.deleteAccount("test_range_" + i);
		}
	}

	@Test
	public void test_toggleAccountDeletion() throws CassandraException, SystemException
	{
		MailerFactory.setMailer(mockMail);
		
		UserId id = new UserId("toggledeletion", "tokentoggledeletion", UserId.UserType.USER_TYPE_SEEKER);
		CoachTestUtils.createOneAccount(id);
		UserReport user = account.getUserReport(id);
		assertFalse(user.dead);
		assertFalse(user.toggleDelete);
		boolean deleted = account.checkDeletionAccount(id, new Date());
		assertFalse(deleted);
		
		// toggle to delete
		account.toggleAccountDeletion(id, true);
		user = account.getUserReport(id);
		assertFalse(user.dead);
		assertTrue(user.toggleDelete);
		Date deletionDate = FormatUtil.dateAddDays(new Date(), 15);
		assertTrue(CoachTestUtils.isDateEqualForDay(deletionDate, user.deletionDate));
		// won't delete now.
		deleted = account.checkDeletionAccount(id, new Date());
		assertFalse(deleted);
		// check email
		String mail = mockMail.lastBody;
		assertTrue(mail.contains("You have requested to delete your account. This will be effective on " + Lang.getDateFormat("en").format(deletionDate)));
		assertTrue(mail.contains("firstName"));
		assertTrue(mail.contains("lastName"));
		System.out.println(mail);
		System.out.println(mockMail.lastSubject);
		mockMail.reset();
		
		// toggle undelete
		account.toggleAccountDeletion(id, false);
		user = account.getUserReport(id);
		assertFalse(user.dead);
		assertFalse(user.toggleDelete);
		assertTrue(CoachTestUtils.isDateEqualForDay(FormatUtil.dateAddDays(new Date(), 15), user.deletionDate));
		// won't delete, even in 100 days.
		deleted = account.checkDeletionAccount(id, FormatUtil.dateAddDays(new Date(), 100));
		assertFalse(deleted);
		user = account.getUserReport(id);
		assertFalse(user.dead);
		assertFalse(user.toggleDelete);
		// no mail
		assertNull(mockMail.lastBody);
		
		// I should still be able to log in.
		MainPageReturnLogin loginCred = account.loginAccount(id.userName, "");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred.getLoginStatus());
		
		// going for real wipe-out. no way back.
		account.toggleAccountDeletion(id, true);
		deleted = account.checkDeletionAccount(id, FormatUtil.dateAddDays(new Date(), 16));
		assertTrue(deleted);
		// won't log in.
		loginCred = account.loginAccount(id.userName, "");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_UNKNOWN_USER, loginCred.getLoginStatus());
		user = account.getUserReport(id);
		assertTrue(user.dead);
	}
	

	@Test
	public void test_updateNames() throws CassandraException, SystemException
	{	
		UserId id = new UserId("updatename", "tokenupdatename", UserId.UserType.USER_TYPE_SEEKER);
		CoachTestUtils.createOneAccount(id);
		UserInformation info = new UserInformation();
		boolean res = account.getUserInformation(id, info);
		assertTrue(res);
		assertEquals("firstNameupdatename", info.firstName);
		assertEquals("lastNameupdatename", info.name);

		// change name of id
		userValues.setValueSafe(id, UserValuesConstantsAccount.ACCOUNT_FIRSTNAME, "new_first", true);
		userValues.setValueSafe(id, UserValuesConstantsAccount.ACCOUNT_LASTNAME, "new_last", true);

		// check info has the correct new names.
		res = account.getUserInformation(id, info);
		assertTrue(res);
		assertEquals("new_first", info.firstName);
		assertEquals("new_last", info.name);
	}
	
	@Test
	public void test_image() throws CassandraException, SystemException
	{	
		UserId id = new UserId("imagename", "tokenimagename", UserId.UserType.USER_TYPE_SEEKER);
		CoachTestUtils.createOneAccount(id);
		
		byte[] none = ByteResourceCache.getByteResource("/com/TheJobCoach/userdata/data/noman.jpg");

		// check get std image on void account
		assertEquals(none, account.getUserImage(id, "256"));
		
		// check set image in error + get return fake one.
		account.setUserImage(id, null);
		assertEquals(none, account.getUserImage(id, "256"));
		
		account.setUserImage(id, new byte[100]);
		assertEquals(none, account.getUserImage(id, "256"));

		// set final user image and check result
		byte[] imgSrc = ByteResourceCache.getByteResource("/com/TheJobCoach/util/test/test1_97x140.png");
		byte[] imgSrc256 = ImageUtil.resizeImage("testAccount", imgSrc, 256);
		byte[] imgSrc48 = ImageUtil.resizeImage("testAccount", imgSrc, 48);
		account.setUserImage(id, imgSrc);
		assertTrue(Arrays.equals(account.getUserImage(id, "256"), imgSrc256));
		assertTrue(Arrays.equals(account.getUserImage(id, "48"), imgSrc48));
			
		// delete account. Check there is no more account image.
		account.markUserAccountDeleted(id);
		assertEquals(none, account.getUserImage(id, "256"));

		// complete deletion.
		account.setUserImage(id, imgSrc);
		assertTrue(Arrays.equals(account.getUserImage(id, "256"), imgSrc256));
		account.deleteAccount(id.userName);
		assertEquals(none, account.getUserImage(id, "256"));
	}
}
