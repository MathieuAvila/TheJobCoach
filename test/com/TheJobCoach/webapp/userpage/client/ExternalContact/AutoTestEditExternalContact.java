package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestEditExternalContact extends GwtTest {
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int calls;
		
		@Override
		public void setExternalContact(UserId id, ExternalContact contact,
				AsyncCallback<String> callback)  {
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
	
	final String contact1 = "contact1";
	
	ExternalContact ule = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	ExternalContact ule_modified = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
		
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
	
	private EditExternalContact cud;

	@Before
	public void beforeEditExternalContact()
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
		EditExternalContactResultTest result = new EditExternalContactResultTest(ule_modified);
		cud = new EditExternalContact(
				p, ule, userId, result
				);
		cud.onModuleLoad();	
		
		cud.okCancel.getOk().click();
		assertEquals(0, userService.calls);
		assertEquals(1, result.calls);
	}

	@Test
	public void testCancel() throws InterruptedException
	{
		userService.calls = 0;
		EditExternalContactResultTest result = new EditExternalContactResultTest(ule_modified);
		cud = new EditExternalContact(
				p, ule, userId, result
				);
		cud.onModuleLoad();	
		cud.okCancel.getCancel().click();
		assertEquals(0, userService.calls);
		assertEquals(0, result.calls);
	}

	
}
