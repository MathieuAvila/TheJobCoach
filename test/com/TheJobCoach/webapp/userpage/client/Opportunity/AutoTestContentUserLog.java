package com.TheJobCoach.webapp.userpage.client.Opportunity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.ErrorCatcherMessageBox;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.Coach.GoalSignal;
import com.TheJobCoach.webapp.userpage.client.Coach.MessagePipe;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentStatus;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentType;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.DefaultUtilServiceAsync;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.EventBuilder;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestContentUserLog extends GwtTest {

	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

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

	static UserOpportunity opportunity1 = new UserOpportunity("opp1", CoachTestUtils.getDate(2000, 1, 1), CoachTestUtils.getDate(2000, 2, 1),
			"title1", "description1", "companyId1",
			"contractType1",  "1",  
			CoachTestUtils.getDate(2000, 1, 1), CoachTestUtils.getDate(2000, 1, 1),
			false, "source1", "url1", "location1",
			UserOpportunity.ApplicationStatus.APPLIED, "note1");

	static UserOpportunity opportunity2 = new UserOpportunity("opp2", CoachTestUtils.getDate(2000, 1, 2), CoachTestUtils.getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  "2",  
			CoachTestUtils.getDate(2000, 1, 2), CoachTestUtils.getDate(2000, 1, 2),
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
	static Vector<UserDocumentId> docIdList0 = new Vector<UserDocumentId>();
	static Vector<UserDocumentId> docIdList1 = new Vector<UserDocumentId>(Arrays.asList(docId1));
	
	static UserLogEntry userLog1 = new UserLogEntry("opp1", "log1", "title1", "description1", 
			CoachTestUtils.getDate(2000, 2, 1),
			LogEntryType.INTERVIEW, externalContactList, docIdList, "note1", false);
	
	static UserLogEntry userLog1_less = new UserLogEntry("opp1", "log1", "title1", "description1", 
			CoachTestUtils.getDate(2000, 2, 1),
			LogEntryType.INFO, externalContactList_Filtered, docIdList_less, "note1_less", false);
	
	static UserLogEntry userLog2 = new UserLogEntry("opp1", "log2", "title2", "description2", 
			CoachTestUtils.getDate(2000, 2, 1),
			LogEntryType.INFO, externalContactList0, docIdList0, "note2", false);
		
	static UserLogEntry userLog3 = new UserLogEntry("opp2", "log3", "title3", "description3", 
			CoachTestUtils.getDate(2000, 2, 1),
			LogEntryType.INFO, externalContactList1, docIdList1, "note3", true);

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsSet, callsDelete;

		String lastId;
		
		public void reset()
		{
			callsGet = callsSet =  callsDelete = 0;
		}
		
		@Override
		public void getUserLogEntryList(UserId id, String oppId,
				AsyncCallback<Vector<UserLogEntry>> callback)
		{
			callsGet++;
			callback.onSuccess(new Vector<UserLogEntry>(Arrays.asList(userLog1, userLog2, userLog3)));
		}	
		
		@Override
		public void deleteUserLogEntry(UserId id, String logId,
				AsyncCallback<String> callback) {
			callsDelete++;
			lastId = logId;
			callback.onSuccess("");
		}
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();
	DefaultUtilServiceAsync utilService = new DefaultUtilServiceAsync();
	
	HorizontalPanel p;
	MessagePipe msg;
	
	@Before
	public void beforeContentUserOpportunity()
	{
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					return userService;
				}
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.util.client.UtilService"))
				{
					return utilService;
				}
				return null;
			}}
		);
		p = new HorizontalPanel();
		msg = MessagePipe.getMessagePipe(userId, null);	
	}
	
	static final int COLUMN_DELETE       = 0;
	static final int COLUMN_UPDATE       = 1;
	static final int COLUMN_URL          = 2;
	static final int COLUMN_EDIT_LOGS    = 10;

	final Vector<TestEditDialog> creationStack =  new Vector<TestEditDialog>();
	final Vector<TestContentUserOpportunity> userLogStack =  new Vector<TestContentUserOpportunity>();

	class TestEditDialog implements IEditLogEntry
	{
		public int loaded;
		public Panel rootPanel;
		public UserId userId;
		public UserLogEntry edition;
		public IChooseResult<UserLogEntry> result;
		
		public TestEditDialog(Panel rootPanel,	UserId userId, UserLogEntry edition, IChooseResult<UserLogEntry> result2)
		{
			this.rootPanel = rootPanel;
			this.userId = userId;
			this.edition = edition;
			this.result = result2;
		}
		
		public TestEditDialog()
		{
		}

		@Override
		public void onModuleLoad()
		{
			loaded++;
		}

		@Override
		public TestEditDialog clone(Panel panel, UserLogEntry _currentLogEntry,
				String _oppId, UserId _user,
				IChooseResult<UserLogEntry> editLogEntryResult)
		{
			TestEditDialog fnResult = new TestEditDialog(panel, _user, _currentLogEntry, editLogEntryResult);
			creationStack.add(fnResult);
			return fnResult;
		}
	}

	class TestContentUserOpportunity implements IContentUserOpportunity
	{
		public int loaded;
		public Panel rootPanel;
		public UserId userId;
		
		public TestContentUserOpportunity(Panel rootPanel,	UserId userId)
		{
			this.rootPanel = rootPanel;
			this.userId = userId;
		}
		
		public TestContentUserOpportunity()
		{
		}

		@Override
		public IContentUserOpportunity clone(Panel panel, UserId _user)
		{
			IContentUserOpportunity fnResult = new TestContentUserOpportunity(rootPanel, userId);
			userLogStack.add((TestContentUserOpportunity) fnResult);
			return fnResult;
		}

		@Override
		public void onModuleLoad()
		{
			loaded++;
		}
	}
	
	@Test
	public void testGetAll() throws InterruptedException
	{
		ContentUserLog cul;
		int goalSignalCounter = GoalSignal.getInstance().getCounter();

		ErrorCatcherMessageBox mbCatcher = new ErrorCatcherMessageBox();
		userService.callsGet = 0;
		cul = new ContentUserLog(
				p, userId, opportunity1, new TestEditDialog(), new TestContentUserOpportunity());
		cul.onModuleLoad();
		assertEquals(1, userService.callsGet);
		
		assertEquals(3, cul.cellTable.getRowCount());
		
		assertEquals(userLog1.ID, cul.cellTable.getVisibleItem(0).ID);
		assertEquals(userLog2.ID, cul.cellTable.getVisibleItem(1).ID);
		assertEquals(userLog3.ID, cul.cellTable.getVisibleItem(2).ID);
		
		// Check columns values
		
		assertEquals(7, cul.cellTable.getColumnCount());
		assertEquals(opportunity1.title,                                         cul.cellTable.getColumn(2).getValue(userLog1));
		assertEquals("Entretien",                                                cul.cellTable.getColumn(3).getValue(userLog1));
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(userLog1.eventDate), cul.cellTable.getColumn(4).getValue(userLog1));
		
		assertEquals("fileName1<br/>fileName2<br/>fileName3",                                          cul.cellTable.getColumn(5).getValue(userLog1));
		assertEquals("firstName1 lastName1<br/>firstName2 lastName2<br/>firstName3 lastName3",         cul.cellTable.getColumn(6).getValue(userLog1));
		
		assertEquals("",                                                                               cul.cellTable.getColumn(5).getValue(userLog2));
		assertEquals("",                                                                               cul.cellTable.getColumn(6).getValue(userLog2));

		assertEquals("fileName1",                                                                      cul.cellTable.getColumn(5).getValue(userLog3));
		assertEquals("firstName1 lastName1",                                                           cul.cellTable.getColumn(6).getValue(userLog3));

		// Click on delete element
		userService.reset();
		mbCatcher.clearError();
		Event event = EventBuilder.create(Event.ONCLICK).build();		
		cul.cellTable.getColumn(COLUMN_DELETE).onBrowserEvent(new Cell.Context(1, COLUMN_DELETE, userLog2), cul.cellTable.getElement(), userLog2, event);
		assertEquals(0, userService.callsDelete);
		assertNotNull(mbCatcher.currentBox);
		assertEquals(mbCatcher.type, MessageBox.TYPE.QUESTION);
		assertTrue(mbCatcher.title.contains("Supprimer le journal"));
		assertTrue(mbCatcher.message.contains("Etes-vous sûr de vouloir supprimer le journal:"));
		assertTrue(mbCatcher.message.contains(userLog2.title));
		mbCatcher.currentBox.clickOk();
		
		assertEquals(1, userService.callsDelete);
		assertEquals(1, userService.callsGet);
		assertEquals(userLog2.ID, userService.lastId);
	
		// Click on edit element
		userService.reset();
		mbCatcher.clearError();
		event = EventBuilder.create(Event.ONCLICK).build();		
		cul.cellTable.getColumn(COLUMN_UPDATE).onBrowserEvent(new Cell.Context(1, COLUMN_UPDATE, userLog2), cul.cellTable.getElement(), userLog2, event);
		assertEquals(1, creationStack.size());
		TestEditDialog editDialog = creationStack.get(0);
		assertEquals(1, editDialog.loaded);
		assertEquals(userLog2.ID, editDialog.edition.ID);
		creationStack.clear();
		// If edition is validated, ... it triggers an update, otherwise nothing.
		editDialog.result.setResult(null); // try nothing
		assertEquals(0, userService.callsGet);
		editDialog.result.setResult(userLog2); // try something now
		assertEquals(1, userService.callsGet);

		// Click on new
		creationStack.clear();
		userService.reset();
		mbCatcher.clearError();
		event = EventBuilder.create(Event.ONCLICK).build();		
		cul.buttonNewLogEntry.click();
		assertEquals(1, creationStack.size());
		editDialog = creationStack.get(0);
		assertEquals(1, editDialog.loaded);
		assertNull(editDialog.edition);
		// If edition is validated, ... it triggers an update, otherwise nothing.
		editDialog.result.setResult(null); // try nothing
		assertEquals(0, userService.callsGet);
		
		goalSignalCounter = GoalSignal.getInstance().getCounter();		
		editDialog.result.setResult(userLog2); // try something now
		// check it signals the coach that something may have changed.
		assertEquals(goalSignalCounter +1, GoalSignal.getInstance().getCounter());
		assertEquals(1, userService.callsGet);

		// Click on back to opportunity
		userService.reset();
		mbCatcher.clearError();
		event = EventBuilder.create(Event.ONCLICK).build();		
		cul.buttonBack.click();
		assertEquals(1, userLogStack.size());
		TestContentUserOpportunity userLog = userLogStack.get(0);
		assertEquals(1, userLog.loaded);

		// TODO check what is loaded/freed
	}	
}
