package com.TheJobCoach.webapp.userpage.client.Document;

import java.util.Arrays;

import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.ErrorCatcherMessageBox;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentRevision;
import com.TheJobCoach.webapp.util.client.IEditResult;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.EventBuilder;

import static org.junit.Assert.*;
@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestEditUserDocument extends GwtTest {

	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int calls;

		@Override
		public void setUserDocument(UserId id, UserDocument contact,
				AsyncCallback<String> callback) throws CassandraException {
			assertEquals(userId.userName, id.userName);
			callback.onSuccess(String.valueOf(calls));
			calls++;
		}
	}

	static SpecialUserServiceAsync userService = null;

	HorizontalPanel p;

	UserDocumentId docId1 = new UserDocumentId("ID1", "updateId1", "name1", "fileName1", new Date(), new Date());
	UserDocumentId docId2 = new UserDocumentId("ID2", "updateId2", "name2", "fileName2", new Date(), new Date());
	Vector<UserDocumentId> docIdList = new Vector<UserDocumentId>(Arrays.asList(docId1, docId2));

	final String contact1 = "contact1";

	static String ud1_id = "doc1";
	static String ud2_id = "doc2";
	static String ud3_id = "doc3";
	static String ud4_id = "doc4";

	static UserDocumentRevision rev1 = new UserDocumentRevision(CoachTestUtils.getDate(2000, 12, 1), ud1_id, "file1");
	static UserDocument ule = new UserDocument(
			ud1_id, "ndoc1", "description1", CoachTestUtils.getDate(2000, 12, 1), "file1", 
			UserDocument.DocumentStatus.SECONDARY, UserDocument.DocumentType.RESUME, new Vector<UserDocumentRevision>(Arrays.asList(rev1)));

	static UserDocument ule_modified = new UserDocument(
			ud1_id, "ndoc1", "description1", CoachTestUtils.getDate(2000, 12, 1), "file1", 
			UserDocument.DocumentStatus.MASTER, UserDocument.DocumentType.RESUME, new Vector<UserDocumentRevision>(Arrays.asList(rev1)));

	class EditUserDocumentResultTest implements IEditResult<UserDocument>
	{
		UserDocument expect;
		EditUserDocumentResultTest(UserDocument expect)
		{
			this.expect = expect;
		}

		public int calls = 0;

		@Override
		public void setResult(UserDocument result)
		{
			assertNotNull(result);
			assertEquals(expect.description,      result.description);			
			assertEquals(expect.fileName,         result.fileName);			
			assertEquals(expect.name,             result.name);			
			assertEquals(expect.status,           result.status);		
			assertEquals(expect.type,             result.type);		

			calls++;
		}
	}
	private static ErrorCatcherMessageBox errorCatcher = new ErrorCatcherMessageBox();

	private EditUserDocument eud;

	@Before
	public void beforeEditUserDocument()
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
		p = new HorizontalPanel();
	}

	@Test
	public void testValidNewDoc() throws InterruptedException
	{
		userService.calls = 0;
		EditUserDocumentResultTest result = new EditUserDocumentResultTest(ule);
		eud = new EditUserDocument(
				p, userId, null, result
				);
		eud.onModuleLoad();	
		getBrowserSimulator().fireLoopEnd();

		// no file means error
		eud.okCancel.getOk().click();
		assertEquals(0, userService.calls);
		assertEquals(0, result.calls);
		assertTrue(errorCatcher.getCurrentMessageBox() != null);
		assertTrue(errorCatcher.getCurrentType() == MessageBox.TYPE.ERROR);
		errorCatcher.clearError();
		// close
		eud.okCancel.getCancel().click();
		getBrowserSimulator().fireLoopEnd();
		assertEquals(0, userService.calls);
		assertEquals(0, result.calls);
		Thread.sleep(1000);
	}

	@Test
	public void testNewDoc() throws InterruptedException
	{	
		userService.calls = 0;
		EditUserDocumentResultTest result = new EditUserDocumentResultTest(ule);
		eud = new EditUserDocument(
				p, userId, null, result
				);
		eud.onModuleLoad();	
		eud.fakeFileName = ule.fileName;
		eud.richTextAreaDescription.setHTML(ule.description);
		eud.txtbxTitle.setValue(ule.name);
		eud.comboBoxStatus.setSelectedIndex(ule.status.ordinal());
		eud.comboBoxType.setSelectedIndex(ule.type.ordinal());
		Event event = EventBuilder.create(Event.ONCHANGE).build();		
		eud.upload.onBrowserEvent(event);	
		eud.okCancel.getOk().click();
		assertEquals(1, userService.calls);
		assertEquals(1, result.calls);
	}

	@Test
	public void testUpdateDocNoFile() throws InterruptedException
	{	
		userService.calls = 0;
		EditUserDocumentResultTest result = new EditUserDocumentResultTest(ule_modified);
		eud = new EditUserDocument(
				p, userId, ule, result
				);
		eud.onModuleLoad();	
		eud.richTextAreaDescription.setHTML(ule_modified.description);
		eud.txtbxTitle.setValue(ule_modified.name);
		eud.comboBoxStatus.setSelectedIndex(ule_modified.status.ordinal());
		eud.comboBoxType.setSelectedIndex(ule_modified.type.ordinal());
		eud.okCancel.getOk().click();
		assertEquals(1, userService.calls);
		assertEquals(1, result.calls);
	}

}
