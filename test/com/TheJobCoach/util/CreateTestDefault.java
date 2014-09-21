package com.TheJobCoach.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.userdata.ContactManager;
import com.TheJobCoach.userdata.UserDocumentManager;
import com.TheJobCoach.userdata.UserExternalContactManager;
import com.TheJobCoach.userdata.UserLogManager;
import com.TheJobCoach.userdata.UserOpportunityManager;
import com.TheJobCoach.userdata.UserValues;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentRevision;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;

public class CreateTestDefault {

	static AccountManager account = new AccountManager();
	private static Logger logger = LoggerFactory.getLogger(CreateTestDefault.class);

	public static void deleteAccountNoException(String userName, String email)
	{
		try { 
			account.deleteAccount(userName);
			String userMail = account.getUsernameFromEmail(email);
			if (userMail != null)
				account.deleteAccount(userMail);
			}
		catch (Exception e) {};
	}

	public static void testCreateAccount() throws CassandraException, InterruptedException, SystemException
	{
		MockMailer mockMail = new MockMailer();
		MailerFactory.setMailer(mockMail);

		UserId user1 = new UserId("user1","mytoken1", UserId.UserType.USER_TYPE_SEEKER);
		UserId user2 = new UserId("user2","mytoken2", UserId.UserType.USER_TYPE_SEEKER);
		UserId user3 = new UserId("user3","mytoken3", UserId.UserType.USER_TYPE_SEEKER);

		Thread.sleep(1000);
		{
			UserInformation userInfo = new UserInformation("Nuser1", "mathieu.avila@laposte.net", "password", "Nprenomuser1");
			deleteAccountNoException("user1", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					user1,
					userInfo, "en"));
			account.validateAccount(user1.userName, user1.token);
			logger.info("logging in user1: " + account.loginAccount("user1", "password").getLoginStatus());
		}
		Thread.sleep(1000);
		{
			UserInformation userInfo = new UserInformation("Nuser2", "mathieu.avila@free.fr", "password", "Nprenomuser2");
			deleteAccountNoException("user2", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					user2,
					userInfo, "en"));
			account.validateAccount(user2.userName, user2.token);
			account.loginAccount(user2.userName, "password");
			logger.info("logging in user2: " + account.loginAccount("user2", "password").getLoginStatus());
		}
		Thread.sleep(1000);
		{
			UserInformation userInfo = new UserInformation("Nuser3", "mathieu.avila@google.com", "password", "Nprenomuser3");
			deleteAccountNoException("user3", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					user3,
					userInfo, "en"));
			account.validateAccount(user3.userName, user3.token);
			account.loginAccount(user3.userName, "password");
			logger.info("logging in user3: " + account.loginAccount("user3", "password").getLoginStatus());
		}
		Thread.sleep(1000);
		{
			ContactManager contactManager1 = new ContactManager(user1);
			ContactManager contactManager2 = new ContactManager(user2);
			ContactManager contactManager3 = new ContactManager(user3);
			// connect user1 and user2
			ContactInformation.ContactStatus newStatus = contactManager1.updateContactRequest(user2, true);
			assertEquals(ContactInformation.ContactStatus.CONTACT_REQUESTED, newStatus);
			newStatus = contactManager2.updateContactRequest(user1, true);
			assertEquals(ContactInformation.ContactStatus.CONTACT_OK, newStatus);
			// change 1 => 2 clearance
			Visibility v_OLCD = new Visibility(true,true,true,true);
			contactManager1.setUserClearance(user2.userName, v_OLCD);

			// connect user1 and user3
			newStatus = contactManager1.updateContactRequest(user3, true);
			assertEquals(ContactInformation.ContactStatus.CONTACT_REQUESTED, newStatus);
			newStatus = contactManager3.updateContactRequest(user1, true);
			assertEquals(ContactInformation.ContactStatus.CONTACT_OK, newStatus);
			// connect user2 and user3
			newStatus = contactManager2.updateContactRequest(user3, true);
			assertEquals(ContactInformation.ContactStatus.CONTACT_REQUESTED, newStatus);
			newStatus = contactManager3.updateContactRequest(user2, true);
			assertEquals(ContactInformation.ContactStatus.CONTACT_OK, newStatus);

			// change 3 => 2 clearance
			Visibility v_olCD = new Visibility(true,true,false,false);
			contactManager3.setUserClearance(user2.userName, v_olCD);			
		}
		{
			// set external contact
			String contact1 = "contact1";
			String contact2 = "contact2";
			String contact3 = "contact3";
			ExternalContact external_contact1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
			ExternalContact external_contact2 = new ExternalContact(contact2, "firstName2", "lastName2", "email2", "phone2", "personalNote2", "organization2", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
			ExternalContact external_contact3 = new ExternalContact(contact3, "firstName3", "lastName3", "email3", "phone3", "personalNote3", "organization3", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));

			UserExternalContactManager contactManager1 = new UserExternalContactManager();
			contactManager1.setExternalContact(user1, external_contact1);
			contactManager1.setExternalContact(user1, external_contact2);
			contactManager1.setExternalContact(user1, external_contact3);

			// set documents
			UserDocumentManager docManager = UserDocumentManager.getInstance();

			String ud1_id = "doc1";
			String ud2_id = "doc2";
			String ud3_id = "doc3";
			byte[] c1 = { 0, 1, 2};
			byte[] c2 = { 3, 4, 5};
			byte[] c3 = { 6, 7, 8};

			UserDocumentRevision rev1 = new UserDocumentRevision(CoachTestUtils.getDate(2000, 12, 1), ud1_id, "file1");
			UserDocument ud1 = new UserDocument(
					ud1_id, "ndoc1", "description1", CoachTestUtils.getDate(2000, 12, 1), "file1", 
					UserDocument.DocumentStatus.NEW, UserDocument.DocumentType.RESUME, new Vector<UserDocumentRevision>(Arrays.asList(rev1)));

			UserDocumentRevision rev2 = new UserDocumentRevision(CoachTestUtils.getDate(2000, 12, 2), ud2_id, "file2");
			UserDocument ud2 = new UserDocument(
					ud2_id, "ndoc2", "description2", CoachTestUtils.getDate(2000, 12, 2), "file2", 
					UserDocument.DocumentStatus.OUTDATED, UserDocument.DocumentType.MOTIVATION, new Vector<UserDocumentRevision>(Arrays.asList(rev2)));

			UserDocumentRevision rev3 = new UserDocumentRevision(CoachTestUtils.getDate(2000, 12, 3), ud3_id, "file3");
			UserDocument ud3 = new UserDocument(
					ud3_id, "ndoc1", "description1", CoachTestUtils.getDate(2000, 12, 3), "file3", 
					UserDocument.DocumentStatus.SECONDARY, UserDocument.DocumentType.OTHER, new Vector<UserDocumentRevision>(Arrays.asList(rev3)));

			docManager.setUserDocument(user1, ud1);
			docManager.setUserDocument(user1, ud2);
			docManager.setUserDocument(user1, ud3);

			docManager.setUserDocumentContent(user1, ud1_id, "f1", c1);
			docManager.setUserDocumentContent(user1, ud2_id, "f2", c2);
			docManager.setUserDocumentContent(user1, ud3_id, "f3", c3);

			UserDocument doc1_1_id = docManager.getUserDocument(user1, ud1_id);
			UserDocumentId docId1_1 = docManager.getUserDocumentId(user1, doc1_1_id.revisions.get(doc1_1_id.revisions.size() - 1).ID);
			UserDocument doc1_2_id = docManager.getUserDocument(user1, ud2_id);
			UserDocumentId docId1_2 = docManager.getUserDocumentId(user1, doc1_2_id.revisions.get(doc1_2_id.revisions.size() - 1).ID);
			UserDocument doc1_3_id = docManager.getUserDocument(user1, ud3_id);
			UserDocumentId docId1_3 = docManager.getUserDocumentId(user1, doc1_3_id.revisions.get(doc1_3_id.revisions.size() - 1).ID);

			// User 1 opportunities / logs
			UserOpportunityManager managerOpportunity1 = new UserOpportunityManager();
			UserOpportunity opportunity1_1 = new UserOpportunity("opp1", CoachTestUtils.getDate(2000, 1, 1), CoachTestUtils.getDate(2000, 2, 1),
					"title1", "description1", "companyId1",
					"contractType1",  "1",  
					CoachTestUtils.getDate(2000, 1, 1), CoachTestUtils.getDate(2000, 1, 1),
					false, "source1", "url1", "location1",
					UserOpportunity.ApplicationStatus.APPLIED, "note1");

			UserOpportunity opportunity1_2 = new UserOpportunity("opp2", CoachTestUtils.getDate(2000, 1, 2), CoachTestUtils.getDate(2000, 2, 2),
					"title2", "description2", "companyId2",
					"contractType2",  "2",  
					CoachTestUtils.getDate(2000, 1, 2), CoachTestUtils.getDate(2000, 1, 2),
					false, "source2", "url2", "location2",
					UserOpportunity.ApplicationStatus.NEW, "note2");
			managerOpportunity1.setUserOpportunity(user1, opportunity1_1, "managed");
			managerOpportunity1.setUserOpportunity(user1, opportunity1_2, "managed");

			// set associated logs
			Vector<ExternalContact> externalContactList_void = new Vector<ExternalContact>();
			Vector<ExternalContact> externalContactList_2 = 
					new Vector<ExternalContact>(Arrays.asList(external_contact1));
			Vector<ExternalContact> externalContactList_3 = 
					new Vector<ExternalContact>(Arrays.asList(external_contact2, external_contact3));
			Vector<UserDocumentId> docIdList = new Vector<UserDocumentId>(Arrays.asList(docId1_1, docId1_2, docId1_3));
			Vector<UserDocumentId> docIdListVoid = new Vector<UserDocumentId>();

			UserLogEntry userLog1_1 = new UserLogEntry("opp1", "log1", "title1", "description1", 
					CoachTestUtils.getDate(2000, 2, 1),
					LogEntryType.INFO, externalContactList_void, docIdList, "note1", false);

			UserLogEntry userLog1_2 = new UserLogEntry("opp1", "log2", "title2", "description1", 
					CoachTestUtils.getDate(2000, 2, 1),
					LogEntryType.INFO, externalContactList_2, docIdListVoid, "note1_less", false);

			UserLogEntry userLog1_3 = new UserLogEntry("opp1", "log3", "title3", "description3", 
					CoachTestUtils.getDate(2000, 2, 1),
					LogEntryType.INFO, externalContactList_3, docIdListVoid, "note2", false);
			UserLogManager logManager1 = new UserLogManager();
			logManager1.setUserLogEntry(user1, userLog1_1);
			logManager1.setUserLogEntry(user1, userLog1_2);
			logManager1.setUserLogEntry(user1, userLog1_3);
			
			// some interesting user values
			UserValues values = new UserValues();
			values.setValue(user1, UserValuesConstantsAccount.ACCOUNT_TITLE, "title1", false);
			values.setValue(user1, UserValuesConstantsAccount.ACCOUNT_KEYWORDS, "skill1\nskill2\nskill3", false);
			values.setValue(user1, UserValuesConstantsAccount.ACCOUNT_STATUS, UserValuesConstantsAccount.ACCOUNT_STATUS_LIST__ACTIVE_SEARCH, false);
		}
		Thread.sleep(1000);
		{
			UserInformation userInfo = new UserInformation("nom", "testtoto@toto.com", "password", "prenom");
			deleteAccountNoException("test_user", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					new UserId("test_user","test_token", UserId.UserType.USER_TYPE_SEEKER, true),
					new UserInformation("nom", "testtoto@toto.com", "password", "prenom"), "en"));
			account.markTestAccount("test_user");
			account.validateAccount("test_user", "test_token");
			account.loginAccount("test_user", "password");
			logger.info("logging in test_user: " + account.loginAccount("test_user", "password").getLoginStatus());
		}
		Thread.sleep(1000);
		{
			UserInformation userInfo = new UserInformation("nom", "admintoto@toto.com", "password", "admintoto@toto.com");
			deleteAccountNoException("admin", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					new UserId("admin","mytokenadmin", UserId.UserType.USER_TYPE_ADMIN),
					new UserInformation("nom", "admintoto@toto.com", "password", "admintoto@toto.com"), "en"));
			account.validateAccount("admin", "mytokenadmin");
			account.loginAccount("admin", "password");
			logger.info("logging in admin: " + account.loginAccount("admin", "password").getLoginStatus());
		}
		Thread.sleep(1000);
		{
			UserInformation userInfo = new UserInformation("nom", "coachtoto@toto.com", "password", "coachtoto@toto.com");
			deleteAccountNoException("coach", userInfo.email);
			logger.info("create account " + account.createAccountWithToken(
					new UserId("coach","mytokencoach", UserId.UserType.USER_TYPE_COACH),
					new UserInformation("nom", "coachtoto@toto.com", "password", "coachtoto@toto.com"), "en"));
			account.validateAccount("coach", "mytokencoach");
			account.loginAccount("coach", "password");			
			logger.info("logging in coach: " + account.loginAccount("coach", "password").getLoginStatus());
		}
	}
	
	public static void main(String arg[])
	{
		try
		{
			testCreateAccount();
		}
		catch (CassandraException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SystemException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
