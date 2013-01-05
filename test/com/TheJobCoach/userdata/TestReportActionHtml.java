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

public class TestReportActionHtml {

	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	static UserId user = new UserId("user", "password", UserId.UserType.USER_TYPE_SEEKER);
	
	static UserOpportunity opportunity1 = new UserOpportunity("opp1", getDate(2000, 1, 1), getDate(2000, 2, 1),
			"title1", "description1", "companyId1",
			"contractType1",  1,  
			getDate(2000, 1, 1), getDate(2000, 1, 1),
			false, "source1", "url1", "location1",
			UserOpportunity.ApplicationStatus.APPLIED, "");

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
			LogEntryType.INFO, null, docIdList, "", false);
	
	@Test
	public void testOpportunityHeader() {
		ReportActionHtml report = new ReportActionHtml(user, "FR");
		report.opportunityHeader(opportunity1, true, true);
		System.out.println(report.content);
		assertEquals(true, report.content.contains("title1"));
		assertEquals(true, report.content.contains("description1"));
	}

	@Test
	public void testLogHeader() {
		ReportActionHtml report = new ReportActionHtml(user, "FR");
		report.logHeader(userLog1, true);
		System.out.println(report.content);
		assertEquals(true, report.content.contains("title1"));
		assertEquals(true, report.content.contains("description1"));
	}

	@Test
	public void testAddWithSeparatorStringStringStringString() {
		assertEquals(ReportActionHtml.addWithSeparator("SRC", "APPEND", "-", "="), "SRC-APPEND=");
		assertEquals(ReportActionHtml.addWithSeparator("", "APPEND", "-", "="), "APPEND=");
	}

	@Test
	public void testAddWithSeparatorStringStringString() {
		//fail("Not yet implemented");
	}

}
