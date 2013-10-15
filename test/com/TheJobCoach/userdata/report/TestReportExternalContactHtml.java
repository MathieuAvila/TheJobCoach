package com.TheJobCoach.userdata.report;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.userdata.UserExternalContactManager;
import com.TheJobCoach.userdata.report.ReportActionHtml;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentStatus;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentType;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;

public class TestReportExternalContactHtml {

	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static String contact1 = "contact1";
	static ExternalContact uec1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", 
			new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	
	static ExternalContact uec2 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", 
			new UpdatePeriod(CoachTestUtils.getDate(2100, 1, 1), 2, PeriodType.DAY, true));
	
	static ExternalContact uec3 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", 
			new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	
	static ExternalContact uec4 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", 
			new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 10000, PeriodType.MONTH, true));

	private class ReportExternalContactHtmlLocal extends ReportExternalContactHtml
	{
		public ReportExternalContactHtmlLocal(UserId user, String lang, boolean detail)
		{
			super(user, lang, detail);
		}
		public String getReportString()
		{
			return content;
		}
		public void flush()
		{
			content = "";
		}
	}

	@Test
	public void testLogHeader() 
	{
		ReportExternalContactHtmlLocal report = new ReportExternalContactHtmlLocal(id, "FR", true);

		report.includeTitle();
		String rep = report.getReportString();
		//System.out.println("TOTO" + rep);
		assertTrue(rep.contains("Rapport de contacts externes"));
		String[] sub = rep.split("<TABLE>");
		assertEquals(2, sub.length);
		String table = sub[1];
		//System.out.println("TOTO" + table);
		assertTrue(table.matches(".*Nom.*Prénom.*Organisation.*Téléphone.*E-mail.*Dernier contact / Prochain prévu.*Notes strictement personnelles.*"));
	}

	@Test
	public void testContactHeader() 
	{
		ReportExternalContactHtmlLocal report = new ReportExternalContactHtmlLocal(id, "FR", true);

		report.contactHeader(uec1);
		String rep = report.getReportString();
		String[] sub = rep.split("<TD>");
		//System.out.println("TOTO" + rep);
		System.out.println("TOTO" + sub[0]);
		//System.out.println("TOTO" + sub[6]);
		assertTrue(sub[0].matches(".*orange.*"));
		assertTrue(sub[1].matches(".*" +  uec1.lastName + ".*"));
		assertTrue(sub[2].matches(".*" +  uec1.firstName + ".*"));
		assertTrue(sub[3].matches(".*" +  uec1.organization + ".*"));
		assertTrue(sub[4].matches(".*" +  uec1.phone + ".*"));
		assertTrue(sub[5].matches(".*" +  uec1.email + ".*"));
		assertTrue(sub[6].matches(".*" +  uec1.personalNote + ".*"));
		assertTrue(sub[7].matches(".*2000.*2000.*"));
			
		report.flush();
		report.contactHeader(uec2);
		rep = report.getReportString();
		sub = rep.split("<TD>");
		assertTrue(sub[0].matches(".*brown.*"));
		
		report.flush();
		report.contactHeader(uec3);
		rep = report.getReportString();
		sub = rep.split("<TD>");
		assertTrue(sub[0].matches(".*brown.*"));
		
		report.flush();
		report.contactHeader(uec4);
		rep = report.getReportString();
		sub = rep.split("<TD>");
		assertTrue(sub[0].matches(".*brown.*"));

	}
}
