package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentStatus;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentType;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class TestUserLogManager {
	
	static UserLogManager manager = new UserLogManager();
	static UserExternalContactManager contactManager = new UserExternalContactManager();
	
	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	static String contact1 = "contact1";
	static String contact2 = "contact2";
	static String contact3 = "contact3";
	static ExternalContact external_contact1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	static ExternalContact external_contact2 = new ExternalContact(contact2, "firstName2", "lastName2", "email2", "phone2", "personalNote2", "organization2", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	static ExternalContact external_contact3 = new ExternalContact(contact3, "firstName3", "lastName3", "email3", "phone3", "personalNote3", "organization3", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	static Vector<ExternalContact> externalContactList_void = new Vector<ExternalContact>();
	static Vector<ExternalContact> externalContactList = new Vector<ExternalContact>(Arrays.asList(external_contact1, external_contact2, external_contact3));
	static Vector<ExternalContact> externalContactList_Filtered = new Vector<ExternalContact>(Arrays.asList(external_contact1, external_contact3));

	static UserOpportunity opportunity1 = new UserOpportunity("opp1", getDate(2000, 1, 1), getDate(2000, 2, 1),
			"title1", "description1", "companyId1",
			"contractType1",  1,  
			getDate(2000, 1, 1), getDate(2000, 1, 1),
			false, "source1", "url1", "location1",
			UserOpportunity.ApplicationStatus.APPLIED, "note1");

	static UserOpportunity opportunity2 = new UserOpportunity("opp2", getDate(2000, 1, 2), getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  2,  
			getDate(2000, 1, 2), getDate(2000, 1, 2),
			false, "source2", "url2", "location2",
			UserOpportunity.ApplicationStatus.NEW, "note2");

	static UserDocumentId docId1 = new UserDocumentId("id1", "id1", "name1", "fileName1", new Date(), new Date());
	static UserDocumentId docId2 = new UserDocumentId("id2", "id2", "name2", "fileName2", new Date(), new Date());
	static UserDocumentId docId3 = new UserDocumentId("id3", "id3", "name3", "fileName3", new Date(), new Date());
	static UserDocument doc1 = new UserDocument("id1", "name1", "", new Date(), "fileName1", DocumentStatus.NEW, DocumentType.RESUME, null);
	static UserDocument doc2 = new UserDocument("id2", "name2", "", new Date(), "fileName2", DocumentStatus.NEW, DocumentType.RESUME, null);
	static UserDocument doc3 = new UserDocument("id3", "name3", "", new Date(), "fileName3", DocumentStatus.NEW, DocumentType.RESUME, null);
	static Vector<UserDocumentId> docIdList = new Vector<UserDocumentId>(Arrays.asList(docId1, docId2, docId3));
	static Vector<UserDocumentId> docIdList_less = new Vector<UserDocumentId>(Arrays.asList(docId1, docId2));
	static Vector<UserDocumentId> docIdListVoid = new Vector<UserDocumentId>();
	
	static UserLogEntry userLog1 = new UserLogEntry("opp1", "log1", "title1", "description1", 
			getDate(2000, 2, 1),
			LogEntryType.INFO, externalContactList, docIdList, "note1", false);
	
	static UserLogEntry userLog1_less = new UserLogEntry("opp1", "log1", "title1", "description1", 
			getDate(2000, 2, 1),
			LogEntryType.INFO, externalContactList_void, docIdList_less, "note1_less", false);
	
	static UserLogEntry userLog2 = new UserLogEntry("opp1", "log2", "title2", "description2", 
			getDate(2000, 2, 1),
			LogEntryType.INFO, externalContactList_void, docIdListVoid, "note2", false);
		
	static UserLogEntry userLog3 = new UserLogEntry("opp2", "log3", "title3", "description3", 
			getDate(2000, 2, 1),
			LogEntryType.INFO, externalContactList_void, docIdListVoid, "note3", true);
	
	private void checkContactList(Vector<ExternalContact> op1, Vector<ExternalContact> op2)
	{
		assertEquals(op1.size(), op2.size());
		for (int count = 0; count != op1.size(); count++)
		{
			assertEquals(op1.get(count).ID, op2.get(count).ID);
		}
	}

	private void checkUserLogEntry(UserLogEntry op1, UserLogEntry opRef, boolean checkContact)
	{
		assertEquals(op1.ID, opRef.ID);
		assertEquals(op1.eventDate, opRef.eventDate);
		assertEquals(op1.opportunityId, opRef.opportunityId);
		assertEquals(op1.title, opRef.title);		
		assertEquals(op1.description, opRef.description);
		assertEquals(op1.type, opRef.type);
		assertEquals(op1.note, opRef.note);
		assertEquals(op1.done, opRef.done);
		
		assertEquals(op1.attachedDocumentId.size(), opRef.attachedDocumentId.size());
		for (UserDocumentId doc : op1.attachedDocumentId)
		{
			UserDocumentId found = null;
			for (UserDocumentId chkDoc : opRef.attachedDocumentId)
			{
				if (chkDoc.ID.equals(doc.ID))
				{
					found = chkDoc;
					break;
				}				
			}
			assertNotNull(found);
			assertEquals(found.name, doc.name);
			assertEquals(found.updateId, doc.updateId);
			assertEquals(found.fileName, doc.fileName);
		}
		
		if (checkContact)
		{
			checkContactList(op1.linkedExternalContact, opRef.linkedExternalContact);
		}
	}
	
	@Before
	public void setTest() throws CassandraException
	{
		// Create necessary external contact
		contactManager.deleteUser(id);
		contactManager.setExternalContact(id, external_contact1);
		contactManager.setExternalContact(id, external_contact2);
		contactManager.setExternalContact(id, external_contact3);
	}
	
	@Test
	public void testCleanup() throws CassandraException
	{
		Vector<UserLogEntry> idList = manager.getLogList(id, "opp1");
		for (UserLogEntry logId : idList)
		{
			manager.deleteUserLogEntry(id, logId.ID);
		}
		idList = manager.getLogList(id, "opp2");
		for (UserLogEntry logId : idList)
		{
			manager.deleteUserLogEntry(id, logId.ID);
		}
		UserDocumentManager.getInstance().setUserDocument(id, doc1);
		UserDocumentManager.getInstance().setUserDocument(id, doc2);
		UserDocumentManager.getInstance().setUserDocument(id, doc3);
	}
	
	@Test
	public void testAddUserLogEntry() throws CassandraException
	{		
		manager.setUserLogEntry(id, userLog1);
		manager.setUserLogEntry(id, userLog2);
		manager.setUserLogEntry(id, userLog3);		
	}

	@Test
	public void testGetUserLogEntryLong() throws CassandraException {
		UserLogEntry opp1 = manager.getLogEntryLong(id, userLog1.ID);
		checkUserLogEntry(opp1, userLog1, true);		
		UserLogEntry opp2 = manager.getLogEntryLong(id, userLog2.ID);
		checkUserLogEntry(opp2, userLog2, true);		
		UserLogEntry opp3 = manager.getLogEntryLong(id, userLog3.ID);
		checkUserLogEntry(opp3, userLog3, true);	
	}
	
	@Test
	public void testGetUserLogEntryLong_less() throws CassandraException {
		manager.setUserLogEntry(id, userLog1_less);
		UserLogEntry opp1 = manager.getLogEntryLong(id, userLog1.ID);
		checkUserLogEntry(opp1, userLog1_less, true);		
		
		manager.setUserLogEntry(id, userLog1);
		opp1 = manager.getLogEntryLong(id, userLog1.ID);
		checkUserLogEntry(opp1, userLog1, true);
	}
			
	@Test
	public void testDeleteLogEntry() throws CassandraException {
		manager.deleteUserLogEntry(id, "log1");
		Vector<UserLogEntry> result = manager.getLogList(id, "opp1");
		assertEquals(1, result.size());
		assertEquals(result.get(0).ID, "log2");
	}

	@Test
	public void testDeleteUserOpportunity() throws CassandraException {
		manager.deleteOpportunityLogList(id, "opp1");
		Vector<UserLogEntry> result = manager.getLogList(id, "opp1");
		assertEquals(0, result.size());
	}
	
	@Test
	public void testDeletedContact() throws CassandraException {
		manager.setUserLogEntry(id, userLog1);
		contactManager.deleteExternalContact(id, external_contact2.ID);
		UserLogEntry log1 = manager.getLogEntryLong(id, userLog1.ID);
		checkUserLogEntry(log1, userLog1, false);
		checkContactList(log1.linkedExternalContact, externalContactList_Filtered);	
	}
}
