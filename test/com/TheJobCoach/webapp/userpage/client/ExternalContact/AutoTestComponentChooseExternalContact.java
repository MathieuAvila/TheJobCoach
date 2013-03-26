package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.EventBuilder;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestComponentChooseExternalContact extends GwtTest {


	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	private ComponentChooseExternalContact ccec;
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	static String contact1 = "contact1";
	static String contact2 = "contact2";
	static String contact3 = "contact3";
		
	static ExternalContact ec1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	static ExternalContact ec2 = new ExternalContact(contact2, "firstName2", "lastName2", "email2", "phone2", "personalNote2", "organization2", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	static ExternalContact ec3 = new ExternalContact(contact3, "firstName3", "lastName3", "email3", "phone3", "personalNote3", "organization3", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	
	Vector<ExternalContact> contactList =  new Vector<ExternalContact>(Arrays.asList(ec1, ec2, ec3));
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsSet, callsDelete;

		@Override
		public void getExternalContactList(UserId id,
				AsyncCallback<Vector<ExternalContact>> callback)
				throws CassandraException
		{
			callsGet++;
			callback.onSuccess(contactList);
		}

		@Override
		public void setExternalContact(UserId id, ExternalContact contact,
				AsyncCallback<String> callback) throws CassandraException
		{
			callsSet++;
			callback.onSuccess("");
		}

		@Override
		public void deleteExternalContact(UserId id, String contact,
				AsyncCallback<String> callback) throws CassandraException
		{
			callsDelete++;
			callback.onSuccess("");			
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
	
	public class ChooseResult implements IChooseExternalContact.ChooseExternalContactResult
	{
		public int count = 0;
		public ExternalContact lastValue = null;
		@Override
		public void setResult(ExternalContact result)
		{
			lastValue = result;
			count++;
		}
	}
	
	@Test
	public void testGetAll() throws InterruptedException
	{
		userService.callsGet = 0;
		
		ChooseResult result = new ChooseResult();
		
		ccec = new ComponentChooseExternalContact(
				p, userId, result);
		
		ccec.onModuleLoad();
		assertEquals(1, userService.callsGet);
		assertEquals(3, ccec.cellTable.getRowCount());
		
		// Check contact list
		assertEquals(contact1, ccec.cellTable.getVisibleItem(0).ID);
		assertEquals(contact2, ccec.cellTable.getVisibleItem(1).ID);
		assertEquals(contact3, ccec.cellTable.getVisibleItem(2).ID);
		
		// Check columns values
		assertEquals(3,                  ccec.cellTable.getColumnCount());
		assertEquals(ec1.firstName,      ccec.cellTable.getColumn(0).getValue(ec1));
		assertEquals(ec1.lastName,       ccec.cellTable.getColumn(1).getValue(ec1));
		assertEquals(ec1.organization,   ccec.cellTable.getColumn(2).getValue(ec1));

		// ok is disabled
		assertEquals(false,              ccec.okCancel.getOk().isEnabled());
		
		// Click on 2nd element
		Event event = EventBuilder.create(Event.ONCLICK).build();		
		ccec.cellTable.getColumn(0).onBrowserEvent(new Cell.Context(1, 0, ec2), ccec.cellTable.getElement(), ec2, event);
		assertEquals(true,              ccec.okCancel.getOk().isEnabled());
		
		// Click on OK.
		ccec.okCancel.getOk().click();
		assertEquals(1, result.count);
		assertEquals(ec2.ID, result.lastValue.ID);
		
		// Click on cancel
		ChooseResult result2 = new ChooseResult();
		ccec = new ComponentChooseExternalContact(
				p, userId, result2);
		
		ccec.onModuleLoad();
		ccec.okCancel.getOk().click();
		assertEquals(0, result2.count);
		assertEquals(null, result2);
	}	
}
