package com.TheJobCoach.webapp.userpage.client;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.Coach.ICoachStrings;
import com.TheJobCoach.webapp.userpage.client.Coach.MessagePipe;
import com.TheJobCoach.webapp.util.client.DefaultUtilServiceAsync;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestPanelUpdate extends GwtTest {

	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static Logger logger = LoggerFactory.getLogger(AutoTestPanelUpdate.class);

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsSet, callsDelete;

		String lastId;
		
		public void reset()
		{
			callsGet = callsSet =  callsDelete = 0;
		}
		
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();

	class SpecialUtilServiceAsync extends DefaultUtilServiceAsync
	{
		public int callsGet, callsSet, callsDelete;

		String lastId;
		
		public void reset()
		{
			callsGet = callsSet =  callsDelete = 0;
		}

		int time = 0;
		Map<String, String> updatedValues = new HashMap<String, String>();
		
		@Override
		public void sendUpdateList(UserId user, UpdateRequest request,
				AsyncCallback<UpdateResponse> callback)
		{
			logger.info("sendUpdateList");
			UpdateResponse response = new UpdateResponse(updatedValues);
			response.totalDayTime = time;
			callback.onSuccess(response);
		}
	}

	SpecialUtilServiceAsync utilService = new SpecialUtilServiceAsync();

	MessagePipe msg;
	
	@Before
	public void before()
	{
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
		msg = MessagePipe.getMessagePipe(userId, null);
	}

	void reset()
	{
		MessagePipe.instance = null;
		msg = MessagePipe.getMessagePipe(userId, null);
		userService.callsGet = 0;
	}
	
	@Test
	public void testUpdate() throws InterruptedException
	{
		PanelUpdate cul;
		HorizontalPanel p = new HorizontalPanel();
		Label connectionTime = new Label();
		reset();
		MessagePipe.strings = new ICoachStrings()
		{
			@Override
			public String getMessage(String key, String coach)
			{
				return key;
			}
		};

		// first update must trigger a few updates and message WELCOME.
		reset();
		cul = new PanelUpdate(p, userId, connectionTime);
		cul.onModuleLoad();
		cul.fireTimer();
		assertEquals(msg.getMessage(), "COACH_WELCOME");
		assertEquals(msg.getMessage(), null);
		
		// Second connection says HELLO.
		reset();
		cul = new PanelUpdate(p, userId, connectionTime);
		cul.onModuleLoad();
		cul.fireTimer();
		assertEquals(msg.getMessage(), "COACH_HELLO");
		assertEquals(msg.getMessage(), null);
		
		// Third connection on the same day
		reset();
		utilService.time = 10;
		cul = new PanelUpdate(p, userId, connectionTime);
		cul.onModuleLoad();
		cul.fireTimer();
		assertEquals(msg.getMessage(), "COACH_HELLO_AGAIN");
		assertEquals(msg.getMessage(), null);
		
		long currentTimeSec = new Date().getTime() - FormatUtil.startOfTheDay(new Date()).getTime();

		// now set-up incoming hour and check what happens when we connect 1/2 hour after it.
		reset();
		utilService.time = 0;
		DefaultUtilServiceAsync.values.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, 
				String.valueOf(currentTimeSec - 60*31*1000));
		cul = new PanelUpdate(p, userId, connectionTime);
		cul.onModuleLoad();
		cul.fireTimer();
		assertEquals(msg.getMessage(), "COACH_HELLO");
		assertEquals(msg.getMessage(), "COACH_VERY_LATE_ARRIVAL");
		assertEquals(msg.getMessage(), null);
		
		// now set-up incoming hour and check what happens when we connect 10 sec after it.
		reset();
		utilService.time = 0;
		DefaultUtilServiceAsync.values.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, 
				String.valueOf(currentTimeSec - 10*1000));
		cul = new PanelUpdate(p, userId, connectionTime);
		cul.onModuleLoad();
		cul.fireTimer();
		assertEquals(msg.getMessage(), "COACH_HELLO");
		assertEquals(msg.getMessage(), "COACH_LATE_ARRIVAL");
		assertEquals(msg.getMessage(), null);
		
		// now set-up incoming hour and check what happens when we connect before it.
		reset();
		utilService.time = 0;
		DefaultUtilServiceAsync.values.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, 
				String.valueOf(currentTimeSec + 10*1000));
		cul = new PanelUpdate(p, userId, connectionTime);
		cul.onModuleLoad();
		cul.fireTimer();
		assertEquals(msg.getMessage(), "COACH_HELLO");
		assertEquals(msg.getMessage(), null);
		
		// set time back to "".
		DefaultUtilServiceAsync.values.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, "");
		
		// Before COACH_OPP_NONE
		cul.checkTime(new UpdateResponse(15*60 - 1));
		assertEquals(msg.getMessage(), null);
		
		// After COACH_OPP_NONE
		cul.checkTime(new UpdateResponse(15*60 + 1));
		assertEquals(msg.getMessage(), "COACH_OPP_NONE");
		assertEquals(msg.getMessage(), null);

		// Other simple keys behave the same way => no test.
	}	
}
