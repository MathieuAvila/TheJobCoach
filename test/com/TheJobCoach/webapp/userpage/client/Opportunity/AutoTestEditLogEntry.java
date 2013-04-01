package com.TheJobCoach.webapp.userpage.client.Opportunity;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.GwtTestUtilsWrapper;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.Opportunity.EditLogEntry;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.TestFormatUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.internal.handlers.GwtTestGWTBridge;

import static org.junit.Assert.*;
@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestEditLogEntry extends GwtTest {
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int calls;
		
		@Override
		public void setUserLogEntry(UserId id, UserLogEntry opp,
				AsyncCallback<String> callback) throws CassandraException {
			assertEquals(userId.userName, id.userName);
			callback.onSuccess("");
			calls++;
		}
	}
	
	static SpecialUserServiceAsync userService = null;
	    
	UserDocumentId docId1 = new UserDocumentId("ID1", "updateId1", "name1", "fileName1", new Date(), new Date());
	UserDocumentId docId2 = new UserDocumentId("ID2", "updateId2", "name2", "fileName2", new Date(), new Date());
	Vector<UserDocumentId> docIdList = new Vector<UserDocumentId>(Arrays.asList(docId1, docId2));
	
	static String contact1 = "contact1";
	static String contact2 = "contact2";
	static String contact3 = "contact3";
	static ExternalContact ec1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	static ExternalContact ec2 = new ExternalContact(contact2, "firstName2", "lastName2", "email2", "phone2", "personalNote2", "organization2", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	static ExternalContact ec3 = new ExternalContact(contact3, "firstName3", "lastName3", "email3", "phone3", "personalNote3", "organization3", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	Vector<ExternalContact> contactList =  new Vector<ExternalContact>(Arrays.asList(ec1, ec2, ec3));
	Vector<ExternalContact> contactList_modified =  new Vector<ExternalContact>(Arrays.asList(ec1, ec3));
	
	UserLogEntry ule = new UserLogEntry("oppId", "logId", "title",
			"description", TestFormatUtil.getDate(2000, 1, 1, 12, 02, 03), LogEntryType.EVENT,
			contactList, new Vector<UserDocumentId>(),
			"note", true);
	
	UserLogEntry ule_modified = new UserLogEntry("oppId", "logId", "title2",
			"description2", TestFormatUtil.getDate(2001, 1, 1, 12, 02, 03), LogEntryType.INFO, // first
			contactList_modified, new Vector<UserDocumentId>(),
			"note2", false);
		
	class EditLogEntryResultTest implements EditLogEntry.EditLogEntryResult
	{
		UserLogEntry expect;
		EditLogEntryResultTest(UserLogEntry expect)
		{
			this.expect = expect;
		}
		
		public int calls = 0;
		
		@Override
		public void setResult(UserLogEntry result)
		{
			int index;
			assertNotNull(result);
			assertEquals(expect.title, result.title);
			assertEquals(expect.description, result.description);
			assertEquals(expect.done, result.done);
			assertEquals(expect.eventDate.toString(), result.eventDate.toString());
			assertEquals(expect.ID, result.ID);
			assertEquals(expect.opportunityId, result.opportunityId);
			assertEquals(expect.type, result.type);
			assertEquals(expect.note, result.note);
			assertEquals(expect.attachedDocumentId.size(), result.attachedDocumentId.size());
			for (index=0; index != expect.attachedDocumentId.size(); index++)
			{
				assertEquals(
						expect.attachedDocumentId.elementAt(index).ID, 
						result.attachedDocumentId.elementAt(index).ID);
				assertEquals(
						expect.attachedDocumentId.elementAt(index).updateId, 
						result.attachedDocumentId.elementAt(index).updateId);
			}
			assertEquals(expect.linkedExternalContact.size(), result.linkedExternalContact.size());
			for (index=0; index != expect.linkedExternalContact.size(); index++)
			{
				assertEquals(
						expect.linkedExternalContact.elementAt(index).ID, 
						result.linkedExternalContact.elementAt(index).ID);
			}
			calls++;
		}
	}

	@Before
	public void beforeEditLogEntry()
	{
		if (userService == null) userService = new SpecialUserServiceAsync();
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
	
	@After
	public void afterContentExternalContact() throws Throwable
	{
		GwtTestGWTBridge.get().afterTest();
	}
	
	@Test
	public void testValid() throws InterruptedException
	{
		HorizontalPanel p = new HorizontalPanel();
		userService.calls = 0;
		EditLogEntryResultTest result = new EditLogEntryResultTest(ule_modified);
		EditLogEntry cud = new EditLogEntry(
				p, ule, "test",
				userId, result
				);
		cud.onModuleLoad();	
		GwtTestUtilsWrapper.waitCallProcessor(this, getBrowserSimulator());	
		
		cud.txtbxTitle.setText(ule_modified.title);
		cud.dateBoxEvent.setValue(ule_modified.eventDate);
		cud.richTextAreaDescription.setText(ule_modified.description);
		cud.richTextAreaNote.setText(ule_modified.note);
		cud.doneBox.setValue(ule_modified.done);
		cud.comboBoxStatus.setSelectedIndex(0);
		cud.contactList = contactList_modified;
		cud.okCancel.getOk().click();
		GwtTestUtilsWrapper.waitCallProcessor(this, getBrowserSimulator());		
		assertEquals(1, userService.calls);
		assertEquals(1, result.calls);
	}
	
	@Test
	public void testCancel() throws InterruptedException
	{
		userService.calls = 0;
		HorizontalPanel p = new HorizontalPanel();
		EditLogEntryResultTest result2 = new EditLogEntryResultTest(ule_modified);
		EditLogEntry cud = new EditLogEntry(
				p, ule, "test",
				userId, result2
				);
		cud.onModuleLoad();
		GwtTestUtilsWrapper.waitCallProcessor(this, getBrowserSimulator());	
		cud.okCancel.getCancel().click();
		GwtTestUtilsWrapper.waitCallProcessor(this, getBrowserSimulator());	
		assertEquals(0, userService.calls);
		assertEquals(0, result2.calls);
	}

	
}
