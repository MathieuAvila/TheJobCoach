package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentStatus;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentType;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class TestUserLogManager {
	
	static UserLogManager manager = new UserLogManager();
	
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
	static UserOpportunity opportunity1 = new UserOpportunity("opp1", getDate(2000, 1, 1), getDate(2000, 2, 1),
			"title1", "description1", "companyId1",
			"contractType1",  1,  
			getDate(2000, 1, 1), getDate(2000, 1, 1),
			false, "source1", "url1", "location1",
			UserOpportunity.ApplicationStatus.APPLIED);
	
	static UserOpportunity opportunity2 = new UserOpportunity("opp2", getDate(2000, 1, 2), getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  2,  
			getDate(2000, 1, 2), getDate(2000, 1, 2),
			false, "source2", "url2", "location2",
			UserOpportunity.ApplicationStatus.NEW);
	
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
			getDate(2000, 2, 10),
			LogEntryType.INFO, null, docIdList);
	
	static UserLogEntry userLog1_less = new UserLogEntry("opp1", "log1", "title1", "description1", 
			getDate(2000, 2, 1),
			getDate(2000, 2, 10),
			LogEntryType.INFO, null, docIdList_less);
	
	static UserLogEntry userLog2 = new UserLogEntry("opp1", "log2", "title2", "description2", 
			getDate(2000, 2, 1),
			getDate(2000, 2, 10),
			LogEntryType.INFO, null, docIdListVoid);
		
	static UserLogEntry userLog3 = new UserLogEntry("opp2", "log3", "title3", "description3", 
			getDate(2000, 2, 1),
			getDate(2000, 2, 10),
			LogEntryType.INFO, null, docIdListVoid);
		
	private void checkUserLogEntry(UserLogEntry op1, UserLogEntry opRef)
	{
		assertEquals(op1.ID, opRef.ID);
		assertEquals(op1.creation, opRef.creation);
		assertEquals(op1.opportunityId, opRef.opportunityId);
		assertEquals(op1.title, opRef.title);		
		assertEquals(op1.description, opRef.description);
		assertEquals(op1.expectedFollowUp, opRef.expectedFollowUp);
		assertEquals(op1.type, opRef.type);
		
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
		checkUserLogEntry(opp1, userLog1);		
		UserLogEntry opp2 = manager.getLogEntryLong(id, userLog2.ID);
		checkUserLogEntry(opp2, userLog2);		
		UserLogEntry opp3 = manager.getLogEntryLong(id, userLog3.ID);
		checkUserLogEntry(opp3, userLog3);	
	}
	
	@Test
	public void testGetUserLogEntryLong_less() throws CassandraException {
		manager.setUserLogEntry(id, userLog1_less);
		UserLogEntry opp1 = manager.getLogEntryLong(id, userLog1.ID);
		checkUserLogEntry(opp1, userLog1_less);		
		
		manager.setUserLogEntry(id, userLog1);
		opp1 = manager.getLogEntryLong(id, userLog1.ID);
		checkUserLogEntry(opp1, userLog1);
	}
			
	@Test
	public void testDeleteLogEntry() throws CassandraException {
		manager.deleteUserLogEntry(id, "log1");
		Vector<UserLogEntry> result = manager.getLogList(id, "opp1");
		System.out.println(result);
		assertEquals(1, result.size());
		assertEquals(result.get(0).ID, "log2");
	}
	@Test
	public void testDeleteUserOpportunity() throws CassandraException {
		manager.deleteOpportunityLogList(id, "opp1");
		Vector<UserLogEntry> result = manager.getLogList(id, "opp1");
		System.out.println(result);
		assertEquals(0, result.size());
	}
}
