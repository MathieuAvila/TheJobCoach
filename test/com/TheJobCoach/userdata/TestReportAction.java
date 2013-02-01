package com.TheJobCoach.userdata;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentStatus;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentType;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class TestReportAction {

	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	static UserId user = new UserId("user_report", "password", UserId.UserType.USER_TYPE_SEEKER);
	
	static UserOpportunity opportunity1 = new UserOpportunity("opp1", getDate(2000, 1, 1), getDate(2000, 2, 1),
			"title1", "description1", "companyId1",
			"contractType1",  1,  
			getDate(2000, 1, 1), getDate(2000, 1, 1),
			false, "source1", "url1", "location1",
			UserOpportunity.ApplicationStatus.APPLIED, "mynote1");

	static UserOpportunity opportunity2 = new UserOpportunity("opp2", getDate(2000, 1, 1), getDate(2000, 2, 1),
			"title2", "description2", "companyId2",
			"contractType2",  1,  
			getDate(2000, 1, 1), getDate(2000, 1, 1),
			false, "source2", "url2", "location2",
			UserOpportunity.ApplicationStatus.APPLIED, "mynote2");

	static UserDocumentId docId1 = new UserDocumentId("id1", "id1", "name1", "fileName1", new Date(), new Date());
	static UserDocumentId docId2 = new UserDocumentId("id2", "id2", "name2", "fileName2", new Date(), new Date());
	static UserDocumentId docId3 = new UserDocumentId("id3", "id3", "name3", "fileName3", new Date(), new Date());
	static UserDocument doc1 = new UserDocument("id1", "name1", "", new Date(), "fileName1", DocumentStatus.NEW, DocumentType.RESUME, null);
	static UserDocument doc2 = new UserDocument("id2", "name2", "", new Date(), "fileName2", DocumentStatus.NEW, DocumentType.RESUME, null);
	static UserDocument doc3 = new UserDocument("id3", "name3", "", new Date(), "fileName3", DocumentStatus.NEW, DocumentType.RESUME, null);
	static Vector<UserDocumentId> docIdList = new Vector<UserDocumentId>(Arrays.asList(docId1, docId2, docId3));
	static Vector<UserDocumentId> docIdList_less = new Vector<UserDocumentId>(Arrays.asList(docId1, docId2));
	static Vector<UserDocumentId> docIdListVoid = new Vector<UserDocumentId>();
	
	static UserLogEntry userLog11 = new UserLogEntry("opp1", "log11", "title11", "description11", 
			getDate(2000, 1, 1),
			LogEntryType.APPLICATION, null, docIdList, "note11", false);
	
	static UserLogEntry userLog12 = new UserLogEntry("opp1", "log12", "title12", "description12", 
			getDate(2000, 10, 1),
			LogEntryType.INTERVIEW, null, docIdList, "note12", true);

	static UserLogEntry userLog21 = new UserLogEntry("opp2", "log21", "title21", "description21", 
			getDate(2000, 2, 1),
			LogEntryType.PROPOSAL, null, docIdList, "note21", false);
	
	static UserLogEntry userLog22 = new UserLogEntry("opp2", "log22", "title22", "description22", 
			getDate(2000, 12, 2),
			LogEntryType.RECALL, null, docIdList, "note22", true);
	
	public static void prepareUserContextStatic() throws CassandraException
	{
		UserOpportunityManager manager = new UserOpportunityManager();	
		Vector<UserOpportunity> idList = manager.getOpportunitiesShortList(user, "managed");
		for (UserOpportunity oppId : idList)
		{
			manager.deleteUserOpportunity(user, oppId.ID);
		}
		manager.setUserOpportunity(user, opportunity1, "managed");
		manager.setUserOpportunity(user, opportunity2, "managed");
		
		UserLogManager logmanager = new UserLogManager();
		logmanager.setUserLogEntry(user, userLog11);
		logmanager.setUserLogEntry(user, userLog12);
		logmanager.setUserLogEntry(user, userLog21);
		logmanager.setUserLogEntry(user, userLog22);
		
	}
	
	@Before
	public void prepareUserContext() throws CassandraException
	{
		 prepareUserContextStatic();
	}
	
	@Test
	public void testOpportunityHeader() {
		ReportActionHtml report = new ReportActionHtml(user, "FR");
		report.opportunityHeader(opportunity1, true, true);
		//System.out.println(report.content);
		assertEquals(true, report.content.contains("title1"));
		assertEquals(true, report.content.contains("description1"));
	}

	@Test
	public void testLogHeader() {
		ReportActionHtml report = new ReportActionHtml(user, "FR");
		report.logHeader(userLog11, true, true);
		System.out.println("LOG HEADER...." + report.content);
		assertEquals(true, report.content.contains("title1"));
		assertEquals(true, report.content.contains("description1"));
	}

	@Test
	public void testFull() throws CassandraException, UnsupportedEncodingException {
		ReportActionHtml report = new ReportActionHtml(user, "FR");
		byte[] result = report.getReport(getDate(2000, 1, 1), getDate(2100, 1, 1), true, true, true);
		System.out.println(new String(result, "UTF-8"));
	}
}
