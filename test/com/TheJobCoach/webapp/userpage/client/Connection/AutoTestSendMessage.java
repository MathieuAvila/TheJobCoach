package com.TheJobCoach.webapp.userpage.client.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestSendMessage extends GwtTest {
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	final static String MESSAGE_TEXT = "mytext";
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int calls;
		
		@Override
		public void sendJobMail(UserId userContact, String message,
				AsyncCallback<Boolean> callback)
		{
			calls++;
			assertEquals(userContact.userName, ule.userName);
			assertEquals(MESSAGE_TEXT, message);
		}
		
	}
	
	static SpecialUserServiceAsync userService = null;
	
	HorizontalPanel p;
    
	UserDocumentId docId1 = new UserDocumentId("ID1", "updateId1", "name1", "fileName1", new Date(), new Date());
	UserDocumentId docId2 = new UserDocumentId("ID2", "updateId2", "name2", "fileName2", new Date(), new Date());
	Vector<UserDocumentId> docIdList = new Vector<UserDocumentId>(Arrays.asList(docId1, docId2));
	
	final String contact1 = "contact1";
	
	UserId ule = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	class EditExternalContactResultTest implements IChooseResult<ExternalContact> 
	{
		ExternalContact expect;
		EditExternalContactResultTest(ExternalContact expect)
		{
			this.expect = expect;
		}
		
		public int calls = 0;

		@Override
		public void setResult(ExternalContact result)
		{
			assertNotNull(result);
			assertEquals(expect.firstName, result.firstName);			
			assertEquals(expect.email, result.email);			
			assertEquals(expect.lastName, result.lastName);			
			assertEquals(expect.organization, result.organization);			
			assertEquals(expect.personalNote, result.personalNote);			
			assertEquals(expect.phone, result.phone);			
			//assertEquals(expect.update.last, result.update.last);			
			//assertEquals(expect.update.length, result.update.length);			
			//assertEquals(expect.update.periodType, result.update.periodType);	
			calls++;
		}
	}
	
	private SendMessage cud;

	@Before
	public void beforeAutoTestSendMessage()
	{
		if (userService == null) 
			userService = new SpecialUserServiceAsync();
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
		cud = new SendMessage();
		SendMessage sm = (SendMessage) cud.sendMessage(p, ule, "FN", "LN");
		
		sm.textAreaMessage.setText(MESSAGE_TEXT);
		sm.okCancel.getOk().click();
		assertEquals(1, userService.calls);
	}

	@Test
	public void testCancel() throws InterruptedException
	{
		userService.calls = 0;
		cud = new SendMessage();
		cud.sendMessage(p, ule, "FN", "LN");
		
		cud.okCancel.getCancel().click();
		assertEquals(0, userService.calls);
	}

	
}
