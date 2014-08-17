package com.TheJobCoach.webapp.userpage.client.Connection;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentStatus;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentType;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.EventBuilder;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestDetailOpportunity extends GwtTest {

	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	static String opp1 = "opp1";
	static String opp2 = "opp2";
	static String opp3 = "opp3";
	static UserOpportunity opportunity1 = new UserOpportunity(opp1, CoachTestUtils.getDate(2000, 1, 1), CoachTestUtils.getDate(2000, 2, 1),
			"title1", "description1", "companyId1",
			"contractType1",  "1000.10",  
			CoachTestUtils.getDate(2000, 1, 1), CoachTestUtils.getDate(2000, 1, 1),
			false, "source1", "url1", "location1",
			UserOpportunity.ApplicationStatus.APPLIED, "note1");
	
	static UserOpportunity opportunity2 = new UserOpportunity(opp2, CoachTestUtils.getDate(2000, 1, 2), CoachTestUtils.getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  "2",  
			CoachTestUtils.getDate(2000, 1, 2), CoachTestUtils.getDate(2000, 1, 2),
			false, "source2", "url2", "location2",
			UserOpportunity.ApplicationStatus.NEW, "note2");

	static UserOpportunity opportunity3 = new UserOpportunity(opp3, CoachTestUtils.getDate(2000, 1, 2), CoachTestUtils.getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  "2",  
			CoachTestUtils.getDate(2000, 1, 2), CoachTestUtils.getDate(2000, 1, 2),
			false, "source2", "url2", "location2",
			UserOpportunity.ApplicationStatus.CLOSED, "note3");
	
	Vector<UserOpportunity> opportunityList =  new Vector<UserOpportunity>(Arrays.asList(opportunity1, opportunity2, opportunity3));

	static UserDocumentId docId1 = new UserDocumentId("id1", "id1", "name1", "fileName1", new Date(), new Date());
	static UserDocumentId docId2 = new UserDocumentId("id2", "id2", "name2", "fileName2", new Date(), new Date());
	static UserDocumentId docId3 = new UserDocumentId("id3", "id3", "name3", "fileName3", new Date(), new Date());
	static UserDocument doc1 = new UserDocument("id1", "name1", "", new Date(), "fileName1", DocumentStatus.NEW, DocumentType.RESUME, null);
	static UserDocument doc2 = new UserDocument("id2", "name2", "", new Date(), "fileName2", DocumentStatus.NEW, DocumentType.RESUME, null);
	static UserDocument doc3 = new UserDocument("id3", "name3", "", new Date(), "fileName3", DocumentStatus.NEW, DocumentType.RESUME, null);
	static Vector<UserDocumentId> docIdList = new Vector<UserDocumentId>(Arrays.asList(docId1, docId2, docId3));
	static Vector<UserDocumentId> docIdList0 = new Vector<UserDocumentId>();
	static Vector<UserDocumentId> docIdList1 = new Vector<UserDocumentId>(Arrays.asList(docId1));
	
	static String contact1 = "contact1";
	static String contact2 = "contact2";
	static String contact3 = "contact3";
	static ExternalContact external_contact1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	static ExternalContact external_contact2 = new ExternalContact(contact2, "firstName2", "lastName2", "email2", "phone2", "personalNote2", "organization2", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	static ExternalContact external_contact3 = new ExternalContact(contact3, "firstName3", "lastName3", "email3", "phone3", "personalNote3", "organization3", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	static Vector<ExternalContact> externalContactList0 = new Vector<ExternalContact>();
	static Vector<ExternalContact> externalContactList1 = new Vector<ExternalContact>(Arrays.asList(external_contact1));
	static Vector<ExternalContact> externalContactList = new Vector<ExternalContact>(Arrays.asList(external_contact1, external_contact2, external_contact3));
	static Vector<ExternalContact> externalContactList_Filtered = new Vector<ExternalContact>(Arrays.asList(external_contact1, external_contact3));

	static UserLogEntry userLog1 = new UserLogEntry("opp1", "log1", "title1", "description1", 
			CoachTestUtils.getDate(2000, 2, 1),
			LogEntryType.INTERVIEW, externalContactList, docIdList, "note1", false);
	
	static UserLogEntry userLog2 = new UserLogEntry("opp1", "log2", "title2", "description2", 
			CoachTestUtils.getDate(2000, 2, 1),
			LogEntryType.INFO, externalContactList0, docIdList0, "note2", false);
		
	static UserLogEntry userLog3 = new UserLogEntry("opp2", "log3", "title3", "description3", 
			CoachTestUtils.getDate(2000, 2, 1),
			LogEntryType.INFO, externalContactList1, docIdList1, "note3", true);

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsGetLog;

		String lastId;
		
		public void reset()
		{
			callsGet = callsGetLog = 0;
		}
		
		@Override
		public void getUserOpportunityList(UserId id, String list,
				AsyncCallback<Vector<UserOpportunity>> callback)
				{
			callsGet++;
			callback.onSuccess(opportunityList);
		}

		@Override
		public void getUserLogEntryList(UserId id, String oppId,
				AsyncCallback<Vector<UserLogEntry>> callback)
		{
			callsGetLog++;
			callback.onSuccess(new Vector<UserLogEntry>(Arrays.asList(userLog1, userLog2, userLog3)));
		}	
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();

	@Before
	public void beforeDetailOpportunity()
	{
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					return userService;
				}
				return null;
			}}
		);
	}
	
	static final int COLUMN_NAME         = 0;
	static final int COLUMN_ORGANIZATION = 1;
	static final int COLUMN_STATUS       = 2;
	static final int COLUMN_LOCATION     = 3;
	static final int COLUMN_SALARY       = 4;
	static final int COLUMN_CONTRACT     = 5;
	static final int COLUMN_DATE         = 6;
	static final int COLUMN_GOTO_LOG     = 7;
	static final int COLUMN_MAX          = 8;

	static final int COLUMN_LOG_NAME         = 0;
	static final int COLUMN_LOG_STATUS       = 1;
	static final int COLUMN_LOG_DATE         = 2;
	static final int COLUMN_LOG_FILES        = 3;
	static final int COLUMN_LOG_CONTACT      = 4;
	static final int COLUMN_LOG_MAX          = 5;

	@Test
	public void testGetAll() throws InterruptedException
	{
		DetailOpportunity cuo;		
		userService.reset();

		ContactInformation ci = new ContactInformation(ContactStatus.CONTACT_OK, userId.userName, "name1", "firstName1", 
				new Visibility(), 
				new Visibility(true, true, true, true));
		
		cuo = new DetailOpportunity(userId, ci);
		assertEquals(0, userService.callsGet);
		
		cuo.showPanelDetail();
		assertEquals(1, userService.callsGet);
		assertEquals(0, userService.callsGetLog);
		
		assertEquals(opportunityList.size(), cuo.cellTable.getRowCount());
		
		assertEquals(opp1, cuo.cellTable.getVisibleItem(0).ID);
		assertEquals(opp2, cuo.cellTable.getVisibleItem(1).ID);
		assertEquals(opp3, cuo.cellTable.getVisibleItem(2).ID);
		
		// Check columns values
		
		assertEquals(COLUMN_MAX, cuo.cellTable.getColumnCount());
		assertEquals(opportunity1.title,        cuo.cellTable.getColumn(COLUMN_NAME).getValue(opportunity1));
		assertEquals(opportunity1.companyId,    cuo.cellTable.getColumn(COLUMN_ORGANIZATION).getValue(opportunity1));
		assertEquals("Candidaté",               cuo.cellTable.getColumn(COLUMN_STATUS).getValue(opportunity1));
		assertEquals(opportunity1.location,     cuo.cellTable.getColumn(COLUMN_LOCATION).getValue(opportunity1));
		assertEquals(opportunity1.salary,		cuo.cellTable.getColumn(COLUMN_SALARY).getValue(opportunity1));
		assertEquals(opportunity1.contractType, cuo.cellTable.getColumn(COLUMN_CONTRACT).getValue(opportunity1));
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(opportunity1.lastUpdate),
												cuo.cellTable.getColumn(COLUMN_DATE).getValue(opportunity1));
		assertFalse(cuo.logPanel.isVisible());
		
		// Click on gotoLog element
		userService.reset();
		Event event = EventBuilder.create(Event.ONCLICK).build();		
		cuo.cellTable.getColumn(COLUMN_GOTO_LOG).onBrowserEvent(new Cell.Context(1, COLUMN_GOTO_LOG, opportunity2), cuo.cellTable.getElement(), opportunity2, event);
		// logs must appear after server call
		assertTrue(cuo.logPanel.isVisible());
		assertEquals(1, userService.callsGetLog);

		// check log table.		
		assertEquals(COLUMN_LOG_MAX, cuo.cellTableLog.getColumnCount());
		assertEquals(userLog1.title,        cuo.cellTableLog.getColumn(COLUMN_LOG_NAME).getValue(userLog1));
		assertEquals("Entretien",               cuo.cellTableLog.getColumn(COLUMN_LOG_STATUS).getValue(userLog1));
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(userLog1.eventDate),
				cuo.cellTableLog.getColumn(COLUMN_LOG_DATE).getValue(userLog1));
		assertEquals("fileName1<br/>fileName2<br/>fileName3",     cuo.cellTableLog.getColumn(COLUMN_LOG_FILES).getValue(userLog1));
		assertEquals("firstName1 lastName1<br/>firstName2 lastName2<br/>firstName3 lastName3",  cuo.cellTableLog.getColumn(COLUMN_LOG_CONTACT).getValue(userLog1));

		// check opportunity description
		assertTrue(cuo.oppDescription.getElement().toString().contains("description2"));
	}
	
	@Test
	public void testNoLog() throws InterruptedException
	{
		DetailOpportunity cuo;		
		userService.reset();

		ContactInformation ci = new ContactInformation(ContactStatus.CONTACT_OK, userId.userName, "name1", "firstName1", 
				new Visibility(), 
				new Visibility(true, true, true, false)); // no log
		
		cuo = new DetailOpportunity(userId, ci);
		cuo.showPanelDetail();
		assertEquals(1, userService.callsGet);
		assertEquals(0, userService.callsGetLog);
		assertEquals(opportunityList.size(), cuo.cellTable.getRowCount());
	
		// Check columns values
		assertEquals(COLUMN_MAX, cuo.cellTable.getColumnCount());
		Event event = EventBuilder.create(Event.ONCLICK).build();		
		cuo.cellTable.getColumn(COLUMN_GOTO_LOG).onBrowserEvent(new Cell.Context(1, COLUMN_GOTO_LOG, opportunity2), cuo.cellTable.getElement(), opportunity2, event);
		// NO log
		assertFalse(cuo.logPanel.isVisible());
		assertEquals(0, userService.callsGetLog);

	}

}
