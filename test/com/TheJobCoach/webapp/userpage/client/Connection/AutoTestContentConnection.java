package com.TheJobCoach.webapp.userpage.client.Connection;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.CatcherMessageBoxTriState;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
import com.TheJobCoach.webapp.util.client.IconsCell;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.EventBuilder;

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
	}

	static final int COLUMN_STATUS        = 0;
	static final int COLUMN_MESSAGE       = 1;
	static final int COLUMN_NAME          = 2;

	class SendMessageTest implements ISendMessage
	{
		UserId contact; 
		String firstName;
		String lastName;
		int counter = 0;
		
		@Override
		public void sendMessage(Panel panel, UserId contact, String firstName,
				String lastName)
		{
			this.contact = contact; 
			this.firstName = firstName;
			this.lastName = lastName;
			counter++;
		}
	}
	
	@Test
	public void testAll() throws InterruptedException
	{
		CatcherMessageBoxTriState mbTriStateCatcher = new CatcherMessageBoxTriState();
		
		SendMessageTest sendMessage = new SendMessageTest();
		
		ContentConnection cud = new ContentConnection(userId, sendMessage);
		assertEquals(1, userService.callsGet);
		assertEquals(3, cud.cellTable.getRowCount());

		assertEquals(ci1.userName, cud.cellTable.getVisibleItem(0).userName);
		assertEquals(ci2.userName, cud.cellTable.getVisibleItem(1).userName);
		assertEquals(ci3.userName, cud.cellTable.getVisibleItem(2).userName);

		// Check columns values
		
		// C2

		assertEquals(3, cud.cellTable.getColumnCount());
		assertEquals("lastName1 firstName1",           cud.cellTable.getColumn(COLUMN_NAME).getValue(ci1));
		
		// C0 = status

		@SuppressWarnings("unchecked")
		IconsCell<ContactInformation> c0 = (IconsCell<ContactInformation>)cud.cellTable.getColumn(COLUMN_STATUS).getCell();
		Vector<ImageResource> v0 = c0.getIcons.getIcons(ci1);
		assertEquals(1, v0.size());
		assertEquals(ContentConnection.contactOk, v0.get(0));
		
		v0 = c0.getIcons.getIcons(ci2);
		assertEquals(1, v0.size());
		assertEquals(ContentConnection.contactAwaiting, v0.get(0));
		
		v0 = c0.getIcons.getIcons(ci3);
		assertEquals(1, v0.size());
		assertEquals(ContentConnection.contactRequested, v0.get(0));

		ContentConnection.FieldUpdaterContactInformation fuCI = (ContentConnection.FieldUpdaterContactInformation)cud.cellTable.getColumn(0).getFieldUpdater();
		fuCI.update(0, ci2, ci2);
		
		// C1 = message
		@SuppressWarnings("unchecked")
		IconsCell<ContactInformation> c1 = (IconsCell<ContactInformation>)cud.cellTable.getColumn(COLUMN_MESSAGE).getCell();
		Vector<ImageResource> v1 = c1.getIcons.getIcons(ci1);
		// message box for OK contact
		assertEquals(1, v1.size());
		assertEquals(ContentConnection.messageIcon, v1.get(0));
		// no message box for NOK contact 1
		v1 = c1.getIcons.getIcons(ci2);
		assertEquals(0, v1.size());
		// no message box for NOK contact 2
		v1 = c1.getIcons.getIcons(ci3);
		assertEquals(0, v1.size());

		// Send message to ci2/ci3 => impossible
		Event event = EventBuilder.create(Event.ONCLICK).build();		
		cud.cellTable.getColumn(COLUMN_MESSAGE).onBrowserEvent(new Cell.Context(1, COLUMN_MESSAGE, ci2), cud.cellTable.getElement(), ci2, event);
		assertEquals(0, sendMessage.counter);
		cud.cellTable.getColumn(COLUMN_MESSAGE).onBrowserEvent(new Cell.Context(1, COLUMN_MESSAGE, ci3), cud.cellTable.getElement(), ci3, event);
		assertEquals(0, sendMessage.counter);
		// send message to ci1
		cud.cellTable.getColumn(COLUMN_MESSAGE).onBrowserEvent(new Cell.Context(1, COLUMN_MESSAGE, ci1), cud.cellTable.getElement(), ci1, event);
		assertEquals(1, sendMessage.counter);
		mbTriStateCatcher.closeBox();
		
		// Search engine
		
	}	
}
