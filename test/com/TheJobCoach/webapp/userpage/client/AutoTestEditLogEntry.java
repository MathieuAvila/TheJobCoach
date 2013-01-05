package com.TheJobCoach.webapp.userpage.client;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.TestFormatUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.octo.gwt.test.GwtCreateHandler;
import com.octo.gwt.test.GwtTest;

import static org.junit.Assert.*;

public class AutoTestEditLogEntry extends GwtTest {
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int calls;
		
		@Override
		public void setUserLogEntry(UserId id, UserLogEntry opp,
				AsyncCallback<String> callback) throws CassandraException {
			System.out.println("Set user log entry....");
			assertEquals(userId.userName, id.userName);
			callback.onSuccess("");
			calls++;
		}
	}
	
	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();
	
	HorizontalPanel p;
    
	UserDocumentId docId1 = new UserDocumentId("ID1", "updateId1", "name1", "fileName1", new Date(), new Date());
	UserDocumentId docId2 = new UserDocumentId("ID2", "updateId2", "name2", "fileName2", new Date(), new Date());
	Vector<UserDocumentId> docIdList = new Vector<UserDocumentId>(Arrays.asList(docId1, docId2));
	
	UserLogEntry ule = new UserLogEntry("oppId", "logId", "title",
			"description", TestFormatUtil.getDate(2000, 1, 1, 12, 02, 03), LogEntryType.EVENT,
			new Vector<String>(), new Vector<UserDocumentId>(),
			"note", true);
	
	UserLogEntry ule_modified = new UserLogEntry("oppId", "logId", "title2",
			"description2", TestFormatUtil.getDate(2001, 1, 1, 12, 02, 03), LogEntryType.INFO, // first
			new Vector<String>(), new Vector<UserDocumentId>(),
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
			System.out.println("Finished with result ...");
			assertNotNull(result);
			assertEquals(expect.title, result.title);
			assertEquals(expect.description, result.description);
			assertEquals(expect.done, result.done);
			//System.out.println(" " + expect.eventDate + " " + result.eventDate);
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
			calls++;
		}
	}
	
	private EditLogEntry cud;

	@Override
	public String getModuleName() {		
		return "com.TheJobCoach.webapp.userpage.UserPage";
	}

	@Before
	public void beforeEditLogEntry()
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
		p = new HorizontalPanel();
	}

	@Test
	public void testValid() throws InterruptedException
	{
		userService.calls = 0;
		EditLogEntryResultTest result = new EditLogEntryResultTest(ule_modified);
		cud = new EditLogEntry(
				p, ule, "test",
				userId, result
				);
		cud.onModuleLoad();	
		
		cud.txtbxTitle.setText(ule_modified.title);
		cud.dateBoxEvent.setValue(ule_modified.eventDate);
		cud.richTextAreaDescription.setText(ule_modified.description);
		cud.richTextAreaNote.setText(ule_modified.note);
		cud.doneBox.setValue(ule_modified.done);
		cud.comboBoxStatus.setSelectedIndex(0);
		cud.okCancel.getOk().click();
		assertEquals(1, userService.calls);
		assertEquals(1, result.calls);
	}

	@Test
	public void testCancel() throws InterruptedException
	{
		userService.calls = 0;
		EditLogEntryResultTest result = new EditLogEntryResultTest(ule_modified);
		cud = new EditLogEntry(
				p, ule, "test",
				userId, result
				);
		cud.onModuleLoad();	
		cud.okCancel.getCancel().click();
		assertEquals(0, userService.calls);
		assertEquals(0, result.calls);
	}

	
}
