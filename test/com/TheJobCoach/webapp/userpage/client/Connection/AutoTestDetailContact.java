package com.TheJobCoach.webapp.userpage.client.Connection;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestDetailContact extends GwtTest {

	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	static String contact1 = "contact1";
	static String contact2 = "contact2";
	static String contact3 = "contact3";
	static ExternalContact external_contact1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	static ExternalContact external_contact2 = new ExternalContact(contact2, "firstName2", "lastName2", "email2", "phone2", "personalNote2", "organization2", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	static ExternalContact external_contact3 = new ExternalContact(contact3, "firstName3", "lastName3", "email3", "phone3", "personalNote3", "organization3", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	static Vector<ExternalContact> externalContactList = new Vector<ExternalContact>(Arrays.asList(external_contact1, external_contact2, external_contact3));

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet;

		String lastId;
		
		public void reset()
		{
			callsGet = 0;
		}
		
		@Override
		public void getExternalContactList(UserId id,
				AsyncCallback<Vector<ExternalContact>> callback)
				
		{
			callsGet++;
			callback.onSuccess(externalContactList);
		}
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();

	@Before
	public void beforeDetailOpportunity()
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
	
	static final int COLUMN_NAME         = 0;
	static final int COLUMN_FIRSTNAME    = 1;
	static final int COLUMN_ORGANIZATION = 2;
	static final int COLUMN_PHONE        = 3;
	static final int COLUMN_EMAIL        = 4;
	static final int COLUMN_MAX          = 5;

	@Test
	public void testGetAll() throws InterruptedException
	{
		DetailContact cuo;		
		userService.reset();

		ContactInformation ci = new ContactInformation(ContactStatus.CONTACT_OK, userId.userName, "name1", "firstName1", 
				new Visibility(), 
				new Visibility(true, true, true, true));
		
		cuo = new DetailContact(userId, ci);
		assertEquals(0, userService.callsGet);
		
		cuo.showPanelDetail();
		assertEquals(1, userService.callsGet);
	
		assertEquals(externalContactList.size(), cuo.cellTable.getRowCount());
		
		assertEquals(external_contact1.ID, cuo.cellTable.getVisibleItem(0).ID);
		assertEquals(external_contact2.ID, cuo.cellTable.getVisibleItem(1).ID);
		assertEquals(external_contact3.ID, cuo.cellTable.getVisibleItem(2).ID);
		
		// Check columns values
		
		assertEquals(COLUMN_MAX, cuo.cellTable.getColumnCount());
		assertEquals(external_contact1.firstName,        cuo.cellTable.getColumn(COLUMN_NAME).getValue(external_contact1));
		assertEquals(external_contact1.lastName,        cuo.cellTable.getColumn(COLUMN_FIRSTNAME).getValue(external_contact1));
		assertEquals(external_contact1.organization,    cuo.cellTable.getColumn(COLUMN_ORGANIZATION).getValue(external_contact1));
		assertEquals(external_contact1.phone,    cuo.cellTable.getColumn(COLUMN_PHONE).getValue(external_contact1));
		assertEquals(external_contact1.email,    cuo.cellTable.getColumn(COLUMN_EMAIL).getValue(external_contact1));
	}

}
