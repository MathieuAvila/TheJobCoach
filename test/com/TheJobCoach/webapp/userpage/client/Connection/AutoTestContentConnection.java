package com.TheJobCoach.webapp.userpage.client.Connection;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestContentConnection extends GwtTest {


	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	ContactInformation ci1 = new ContactInformation(ContactStatus.CONTACT_OK, 
			"u1", "firstName1", "lastName1", new Visibility(), new Visibility());
	ContactInformation ci2 = new ContactInformation(ContactStatus.CONTACT_AWAITING, 
			"u2", "firstName2", "lastName2", new Visibility(), new Visibility());
	ContactInformation ci3 = new ContactInformation(ContactStatus.CONTACT_REQUESTED, 
			"u3", "firstName3", "lastName3", new Visibility(), new Visibility());
	
	Vector<ContactInformation> contactList =  new Vector<ContactInformation>(Arrays.asList(ci1, ci2, ci3));
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsMessage, callsUpdate;
		public UserId userContact;
		public boolean ok;
		public String message;
		
		public void reset()
		{
			callsGet = callsMessage = callsUpdate = 0;
			ok = false;
			userContact = null;
			message = null;
		}
		
		@Override
		public void updateContactRequest(UserId userContact, boolean ok,
				AsyncCallback<ContactStatus> callback)
		{
			this.ok = ok;
			this.userContact = userContact;
			callsUpdate++;
			callback.onSuccess(ContactStatus.CONTACT_OK);
		}
		
		@Override
		public void getContactList(
				AsyncCallback<Vector<ContactInformation>> callback)
		{
			callsGet++;
			callback.onSuccess(contactList);
		}
		@Override
		public void sendJobMail(UserId userContact, String message,
				AsyncCallback<Boolean> callback)
		{
			this.userContact = userContact;
			this.message = message;
			callsMessage++;
			callback.onSuccess(true);
		}
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();
	
	HorizontalPanel p;
	
	@Before
	public void beforeContentExternalContact()
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

	static final int COLUMN_DELETE       = 0;
	static final int COLUMN_UPDATE       = 1;

	@Test
	public void testGetAll() throws InterruptedException
	{
		ContentConnection cud = new ContentConnection(userId);
		assertEquals(1, userService.callsGet);
		assertEquals(3, cud.cellTable.getRowCount());

		assertEquals(ci1.userName, cud.cellTable.getVisibleItem(0).userName);
		assertEquals(ci2.userName, cud.cellTable.getVisibleItem(1).userName);
		assertEquals(ci3.userName, cud.cellTable.getVisibleItem(2).userName);

		// Check columns values
		assertEquals(3, cud.cellTable.getColumnCount());
		assertEquals(ci1,                              cud.cellTable.getColumn(0).getValue(ci1));
		assertEquals(ci1,                              cud.cellTable.getColumn(1).getValue(ci1));
		assertEquals("lastName1 firstName1",           cud.cellTable.getColumn(2).getValue(ci1));
	}	
}
