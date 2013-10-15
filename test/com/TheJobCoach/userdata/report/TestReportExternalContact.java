package com.TheJobCoach.userdata.report;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.userdata.UserExternalContactManager;
import com.TheJobCoach.userdata.report.ReportExternalContact;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class TestReportExternalContact {

	static UserExternalContactManager manager = new UserExternalContactManager();
	
	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	static String contact1 = "contact1";
	static String contact2 = "contact2";
	static String contact3 = "contact3";

	static ExternalContact ujs1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	static ExternalContact ujs2 = new ExternalContact(contact2, "firstName2", "lastName2", "email2", "phone2", "personalNote2", "organization2", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	static ExternalContact ujs3 = new ExternalContact(contact3, "firstName3", "lastName3", "email3", "phone3", "personalNote3", "organization3", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	
	public static void prepareUserContextStatic() throws CassandraException
	{
		List<String> result = manager.getExternalContactListId(id);
		for (String contact: result)
		{
			manager.deleteExternalContact(id, contact);
		}		
		manager.setExternalContact(id, ujs1);
		manager.setExternalContact(id, ujs2);
		manager.setExternalContact(id, ujs3);
	}
	
	private class ReportExternalContactLocal extends ReportExternalContact
	{
		public Vector<ExternalContact> contacts = new Vector<ExternalContact>();
		
		public ReportExternalContactLocal(UserId user, String lang, boolean detail)
		{
			super(user, lang, detail);
		}

		public int head; 
		public int footer;
		
		@Override
		void includeTitle()
		{
			head++;
		}
		
		@Override
		void endDocument()
		{
			footer++;
		}
		
		@Override
		void contactHeader(ExternalContact contact) 
		{
			contacts.add(contact);
		}
	}
	
	@Before
	public void prepareUserContext() throws CassandraException
	{
		 prepareUserContextStatic();
	}

	@Test
	public void testFull() throws CassandraException, UnsupportedEncodingException 
	{
		prepareUserContextStatic();
		ReportExternalContactLocal report = new ReportExternalContactLocal(id, "FR", true);
		report.getReport();
		assertEquals(3, report.contacts.size());
		assertEquals(report.contacts.get(0).ID, "contact1");
		assertEquals(report.contacts.get(1).ID, "contact2");
		assertEquals(report.contacts.get(2).ID, "contact3");
		assertEquals(1, report.head);
		assertEquals(1, report.footer);
	}
}
