package com.TheJobCoach.userdata.report;

import static org.junit.Assert.*;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;

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
		System.out.println("TOTO" + rep);
		assertTrue(rep.contains("Rapport de contacts externes"));
		String[] sub = rep.split("<TABLE>");
		assertEquals(2, sub.length);
		String table = sub[1];
		//System.out.println("TOTO" + table);
		assertTrue(table.matches(".*Nom.*Prénom.*Organisation.*Téléphone.*E-mail.*Dernier contact / Prochain prévu.*Notes strictement personnelles.*"));
	}

	@Test
	public void testLogHeaderNoDetail() 
	{
		ReportExternalContactHtmlLocal report = new ReportExternalContactHtmlLocal(id, "FR", false);

		report.includeTitle();
		String rep = report.getReportString();
		System.out.println("TOTO" + rep);
		assertTrue(rep.contains("Rapport de contacts externes"));
		String[] sub = rep.split("<TABLE>");
		assertEquals(2, sub.length);
		String table = sub[1];
		//System.out.println("TOTO" + table);
		assertTrue(!table.matches(".*Notes strictement personnelles.*"));
	}

	@Test
	public void testContactHeader() 
	{
		ReportExternalContactHtmlLocal report = new ReportExternalContactHtmlLocal(id, "FR", true);

		report.contactHeader(uec1);
		String rep = report.getReportString();
		String[] sub = rep.split("<TD>");
		//System.out.println("TOTO" + rep);
		//System.out.println("TOTO" + sub[0]);
		System.out.println("TOTO" + sub[7]);
		assertTrue(sub[0].matches(".*orange.*"));
		assertTrue(sub[1].matches(".*" +  uec1.lastName + ".*"));
		assertTrue(sub[2].matches(".*" +  uec1.firstName + ".*"));
		assertTrue(sub[3].matches(".*" +  uec1.organization + ".*"));
		assertTrue(sub[4].matches(".*" +  uec1.phone + ".*"));
		assertTrue(sub[5].matches(".*" +  uec1.email + ".*"));
		assertTrue(sub[6].matches(".*2000.*2000.*"));
		assertTrue(sub[7].contains(uec1.personalNote));
			
		report.flush();
		report.contactHeader(uec2);
		rep = report.getReportString();
		sub = rep.split("<TD>");
		assertTrue(sub[0].matches(".*" + ReportExternalContactHtml.VALIDRECALL + ".*"));
		
		report.flush();
		report.contactHeader(uec3);
		rep = report.getReportString();
		sub = rep.split("<TD>");
		assertTrue(sub[0].matches(".*" + ReportExternalContactHtml.VALIDRECALL + ".*"));
		
		report.flush();
		report.contactHeader(uec4);
		rep = report.getReportString();
		sub = rep.split("<TD>");
		assertTrue(sub[0].matches(".*" + ReportExternalContactHtml.VALIDRECALL + ".*"));

	}
	
	@Test
	public void testContactHeaderNoDetail() 
	{
		ReportExternalContactHtmlLocal report = new ReportExternalContactHtmlLocal(id, "FR", false);

		report.contactHeader(uec1);
		String rep = report.getReportString();
		String[] sub = rep.split("<TD>");
		System.out.println("TOTO" + rep);
		//System.out.println("TOTO" + sub[0]);
		//System.out.println("TOTO" + sub[7]);
		assertEquals(7, sub.length);
		assertTrue(sub[0].matches(".*" + ReportExternalContactHtml.INVALIDRECALL + ".*"));
		assertTrue(sub[1].matches(".*" +  uec1.lastName + ".*"));
		assertTrue(sub[2].matches(".*" +  uec1.firstName + ".*"));
		assertTrue(sub[3].matches(".*" +  uec1.organization + ".*"));
		assertTrue(sub[4].matches(".*" +  uec1.phone + ".*"));
		assertTrue(sub[5].matches(".*" +  uec1.email + ".*"));
		assertTrue(sub[6].contains("2000"));
		//assertTrue(sub[7].contains(uec1.personalNote));
			
	}
}
