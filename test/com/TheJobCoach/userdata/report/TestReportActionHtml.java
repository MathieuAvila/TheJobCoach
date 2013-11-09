package com.TheJobCoach.userdata.report;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.userdata.report.ReportActionHtml;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentStatus;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentType;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.util.shared.UserId;


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
			"oppTitle1", "oppDescription1", "companyId1",
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
			LogEntryType.EVENT, null, docIdList, "", false);
	
	static UserLogEntry userLog2 = new UserLogEntry("opp1", "log2", "title2", "description2", 
			getDate(2000, 2, 2),
			LogEntryType.RECALL, null, docIdList_less, "", true);
		
	@Test
	public void testOpportunityHeader() {
		ReportActionHtml report = new ReportActionHtml(user, "FR");
		report.opportunityHeader(opportunity1, true, true);
		assertEquals(true, report.content.contains(opportunity1.title));
		assertEquals(true, report.content.contains(opportunity1.description));
	}

	@Test
	public void testLogHeader() {
		
		// With detail
		ReportActionHtml report = new ReportActionHtml(user, "FR");
		report.logHeader(userLog1, true, true);
		assertEquals(true, report.content.contains("title1"));
		assertEquals(true, report.content.contains("description1"));
		assertEquals(true, report.content.contains("Ev&egrave;nement"));
		
		assertEquals(true, report.content.contains("name1"));
		assertEquals(true, report.content.contains("name2"));
		assertEquals(true, report.content.contains("fileName1"));
		assertEquals(true, report.content.contains("fileName2"));
		
		// NO detail
		report = new ReportActionHtml(user, "FR");
		report.logHeader(userLog1, false, true);
		assertEquals(true, report.content.contains("title1"));
		assertEquals(false, report.content.contains("description1"));

		assertEquals(false, report.content.contains("name1"));
		assertEquals(false, report.content.contains("name2"));
		assertEquals(false, report.content.contains("fileName1"));
		assertEquals(false, report.content.contains("fileName2"));

		// With detail & done
		report = new ReportActionHtml(user, "FR");
		report.logHeader(userLog2, true, true);
		assertEquals(true, report.content.contains("X"));
		assertEquals(true, report.content.contains("orange"));
		
		// Outside date span
		report = new ReportActionHtml(user, "FR");
		report.logHeader(userLog2, false, false);
		assertEquals(true, report.content.contains("brown"));
	}
}
