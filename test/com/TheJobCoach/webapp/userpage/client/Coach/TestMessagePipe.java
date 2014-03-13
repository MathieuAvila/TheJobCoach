package com.TheJobCoach.webapp.userpage.client.Coach;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.util.client.DefaultUtilServiceAsync;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class TestMessagePipe  extends GwtTest {
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	class SpecialUtilServiceAsync extends DefaultUtilServiceAsync
	{
		public int callsGet, callsSet, callsDelete;
	}

	static SpecialUtilServiceAsync utilService = null;
	
	HorizontalPanel p;
	
	@Before
	public void beforeContentExternalContact()
	{
		if (utilService == null) utilService = new SpecialUtilServiceAsync();
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.util.client.UtilService"))
				{
					return utilService;
				}
				return null;
			}}
		);
		p = new HorizontalPanel();		
	}
	
	@Test
	public void test_getMessage()
	{
		MessagePipe mp = new MessagePipe(userId, null);
		mp.strings = new ICoachStrings()
		{

			@Override
			public String getMessage(String key, String coach)
			{
				// TODO Auto-generated method stub
				return key;
			}
		};
		// Welcome only once.
		mp.addMessage(UserValuesConstantsCoachMessages.COACH_WELCOME);
		assertEquals("COACH_WELCOME",mp.getMessage());
		mp.addMessage(UserValuesConstantsCoachMessages.COACH_WELCOME);
		assertEquals(null,mp.getMessage());

		// Hello after 1st time.
		mp.addMessage(UserValuesConstantsCoachMessages.COACH_HELLO);
		assertEquals(null,mp.getMessage());
		mp.addMessage(UserValuesConstantsCoachMessages.COACH_HELLO);
		assertEquals("COACH_HELLO",mp.getMessage());
		mp.addMessage(UserValuesConstantsCoachMessages.COACH_HELLO);
		assertEquals("COACH_HELLO",mp.getMessage());
		
		// Check stacking works.
		mp.addMessage(UserValuesConstantsCoachMessages.COACH_HELLO_AGAIN);
		mp.addMessage(UserValuesConstantsCoachMessages.COACH_LATE_ARRIVAL);
		mp.addMessage(UserValuesConstantsCoachMessages.COACH_LATE_DEPARTURE);
		assertEquals("COACH_HELLO_AGAIN",mp.getMessage());
		assertEquals("COACH_LATE_ARRIVAL",mp.getMessage());
		assertEquals("COACH_LATE_DEPARTURE",mp.getMessage());
		assertEquals(null,mp.getMessage());
	}
	
}
