package com.TheJobCoach.webapp.userpage.client.Account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.DefaultUtilServiceAsync;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestContentAccount extends GwtTest {

	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsMessage, callsUpdate, callsSearch, callsUpdateShares;
		public UserId userContact;
		public boolean ok;
		public String message;

		Logger logger = LoggerFactory.getLogger(SpecialUserServiceAsync.class);

		public void reset()
		{
			callsGet = callsMessage = callsUpdate = callsSearch = 0;
			ok = false;
			userContact = null;
			message = null;
		}
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();
	static DefaultUtilServiceAsync utilService = null;

	@Before
	public void beforeContentExternalContact()
	{
		if ( utilService == null) utilService = new DefaultUtilServiceAsync();
	
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					return userService;
				}
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.util.client.UtilService"))
				{
					return utilService;
				}
				return null;
			}}
				);
	}

	@Test
	public void testNames() throws InterruptedException
	{
		ClientUserValuesUtils.instance = null;
		utilService.values.clear();
		utilService.values.put(UserValuesConstantsAccount.ACCOUNT_FIRSTNAME, "firstname");
		utilService.values.put(UserValuesConstantsAccount.ACCOUNT_LASTNAME, "lastname");
		ContentAccount ca = new ContentAccount(userId);
		assertFalse(ca.applyReset.isApplyEnabled());
		assertFalse(ca.applyReset.isResetEnabled());

		// enable dangerous settings.
		assertFalse(ca.dangerousSettings.isVisible());
		ca.btnShowDangerousSettings.click();
		assertTrue(ca.dangerousSettings.isVisible());
		
		// change name and validate
		ca.tfLastName.setValue("lasttoto");
		ca.tfFirstName.setValue("firsttoto");
		assertTrue(ca.applyReset.isApplyEnabled());
		assertTrue(ca.applyReset.isResetEnabled());
		ca.applyReset.testApply();
		
		// name is changed, values are reset
		assertFalse(ca.applyReset.isApplyEnabled());
		assertFalse(ca.applyReset.isResetEnabled());
		assertEquals("firsttoto", utilService.values.get(UserValuesConstantsAccount.ACCOUNT_FIRSTNAME));
		assertEquals("lasttoto", utilService.values.get(UserValuesConstantsAccount.ACCOUNT_LASTNAME));
	}

}
