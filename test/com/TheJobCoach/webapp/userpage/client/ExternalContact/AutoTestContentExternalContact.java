package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestContentExternalContact extends GwtTest {


	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	private ContentExternalContact cud;
	
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
				
		{
			callsGet++;
			callback.onSuccess(contactList);
		}

		@Override
		public void setExternalContact(UserId id, ExternalContact contact,
				AsyncCallback<String> callback) 
		{
			callsSet++;
			callback.onSuccess("");
		}

		@Override
		public void deleteExternalContact(UserId id, String contact,
				AsyncCallback<String> callback) 
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
	
	@Test
	public void testGetAll() throws InterruptedException
	{
		userService.callsGet = 0;
		cud = new ContentExternalContact(
				p, userId);
		cud.onModuleLoad();
		assertEquals(1, userService.callsGet);
		assertEquals(3, cud.cellTable.getRowCount());
		
		assertEquals(contact1, cud.cellTable.getVisibleItem(0).ID);
		assertEquals(contact2, cud.cellTable.getVisibleItem(1).ID);
		assertEquals(contact3, cud.cellTable.getVisibleItem(2).ID);
	}	
}
