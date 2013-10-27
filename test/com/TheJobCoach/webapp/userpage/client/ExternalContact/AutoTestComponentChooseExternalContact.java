package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.ErrorCatcherMessageBox;
import com.TheJobCoach.webapp.GwtTestUtilsWrapper;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.internal.handlers.GwtTestGWTBridge;
import com.googlecode.gwt.test.utils.events.Browser;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestComponentChooseExternalContact extends GwtTest {

	Logger logger = LoggerFactory.getLogger(AutoTestComponentChooseExternalContact.class);

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

		@SuppressWarnings("unchecked")
		@Override
		public void getExternalContactList(UserId id,
				AsyncCallback<Vector<ExternalContact>> callback)
		{
			callsGet++;
			callback.onSuccess((Vector<ExternalContact>)contactList.clone());
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

	static SpecialUserServiceAsync userService = null;	
	
	@Before
	public void beforeContentExternalContact() throws Throwable
	{
		GwtTestGWTBridge.get().afterTest();
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
	
	public class ChooseResult implements IChooseResult<ExternalContact>
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
		HorizontalPanel p = new HorizontalPanel();		
		
		ErrorCatcherMessageBox catcher = new ErrorCatcherMessageBox();

		userService.callsGet = 0;
		
		ChooseResult result = new ChooseResult();
		
		ComponentChooseExternalContact ccec = new ComponentChooseExternalContact(
				p, userId, result);
		
		ccec.onModuleLoad();
		GwtTestUtilsWrapper.waitCallProcessor(this, getBrowserSimulator());	

		assertNull(catcher.currentBox);	
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
		
		// Select 2nd element
		Browser.click(ccec.cellTable, ec2);
		
		// Now enabled
		assertEquals(true,              ccec.okCancel.getOk().isEnabled());
		
		// Click on OK.
		ccec.okCancel.getOk().click();
		getBrowserSimulator().fireLoopEnd();
		assertEquals(1, result.count);
		assertEquals(ec2.ID, result.lastValue.ID);
	}
	
	@Test
	public void testCancel() throws InterruptedException
	{
		HorizontalPanel p = new HorizontalPanel();		

		// Click on cancel
		ChooseResult result2 = new ChooseResult();
		ComponentChooseExternalContact ccec = new ComponentChooseExternalContact(
				p, userId, result2);
		ccec.onModuleLoad();
		GwtTestUtilsWrapper.waitCallProcessor(this, getBrowserSimulator());
		Browser.click(ccec.cellTable, ccec.cellTable.getVisibleItem(1));
		assertEquals(true, ccec.okCancel.getOk().isEnabled());		
		ccec.okCancel.getCancel().click();
		GwtTestUtilsWrapper.waitCallProcessor(this, getBrowserSimulator());	
		assertEquals(0, result2.count);		
	}	
}
