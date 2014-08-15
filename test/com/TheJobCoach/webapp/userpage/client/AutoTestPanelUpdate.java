package com.TheJobCoach.webapp.userpage.client;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.Coach.GoalSignal;
import com.TheJobCoach.webapp.userpage.client.Coach.ICoachStrings;
import com.TheJobCoach.webapp.userpage.client.Coach.MessagePipe;
import com.TheJobCoach.webapp.userpage.shared.GoalReportInformation;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.DefaultUtilServiceAsync;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
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

	static class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsSet, callsDelete;

		public GoalReportInformation currentReport = new GoalReportInformation(new Date(), new Date());
		
		String lastId;
		
		@Override
		public void getUserGoalReport(UserId id, Date start, Date end,
				AsyncCallback<GoalReportInformation> callback)
		{
			logger.info("getUserGoalReport " + id.userName);
			callback.onSuccess(new GoalReportInformation(currentReport));
		}
		
		public void reset()
		{
			callsGet = callsSet =  callsDelete = 0;
		}
		
	}

	static SpecialUserServiceAsync userService = new SpecialUserServiceAsync();

	static class SpecialUtilServiceAsync extends DefaultUtilServiceAsync
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
			logger.info("sendUpdateList " + time);
			UpdateResponse response = new UpdateResponse(updatedValues);
			response.totalDayTime = time;
			callback.onSuccess(response);
		}
	}

	static SpecialUtilServiceAsync utilService = new SpecialUtilServiceAsync();

	MessagePipe msg;
	
	HorizontalPanel p;

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
		CoachTestUtils.resetClientState();
	}
	
	void reset()
	{ 
		p = new HorizontalPanel();
		userService.callsGet = 0;
		MessagePipe.instance = null;
		MessagePipe.getMessagePipe(userId, p).strings = new ICoachStrings()
		{
			@Override
			public String getMessage(String key, String coach)
			{
				return key;
			}
		};
		msg = MessagePipe.getMessagePipe(userId, p);
	}
	
	@Test
	public void testUpdate() throws InterruptedException
	{		
		PanelUpdate cul;
		Label connectionTime = new Label();

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
		utilService.values.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, 
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
		utilService.values.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, 
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
		utilService.values.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, 
				String.valueOf(currentTimeSec + 10*1000));
		cul = new PanelUpdate(p, userId, connectionTime);
		cul.onModuleLoad();
		cul.fireTimer();
		assertEquals(msg.getMessage(), "COACH_HELLO");
		assertEquals(msg.getMessage(), null);
		
		// set time back to "".
		utilService.values.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, "");
		
		// Before COACH_OPP_NONE
		cul.checkTime(new UpdateResponse(15*60 - 1));
		assertEquals(msg.getMessage(), null);
		
		// After COACH_OPP_NONE
		cul.checkTime(new UpdateResponse(15*60 + 1));
		assertEquals(msg.getMessage(), "COACH_OPP_NONE");
		assertEquals(msg.getMessage(), null);

		// Other simple keys behave the same way => no test.
	}
	
	@Test
	public void testGoals() throws InterruptedException
	{
		PanelUpdate cul;
		HorizontalPanel p = new HorizontalPanel();
		ClientUserValuesUtils values = ClientUserValuesUtils.getInstance(userId);
		reset();
		MessagePipe.getMessagePipe(userId, p).strings = new ICoachStrings()
		{
			@Override
			public String getMessage(String key, String coach)
			{
				if (key.contains("_REACHED"))
					return key + " %1";
				if (key.contains("PERFORMANCE_"))
					return key + " %1 %2";
				return key;
			}
		};
		
		cul = new PanelUpdate(p, userId, new Label());
		
		// this will trigger a call to UserValues.
		cul.onModuleLoad();
		
		cul.checkGoals();
		// nothing yet.
		assertEquals(msg.getMessage(), null);
		
		// send update about goals NOT SET. First shot is not valid because result are not yet retrieved.
		GoalSignal.getInstance().newEvent();
		cul.checkGoals();
		assertEquals(null, msg.getMessage());
		cul.checkGoals();
		assertEquals(UserValuesConstantsCoachMessages.COACH_GOAL_NOT_SET, msg.getMessage());
		
		// ****************** CREATE OPPORTUNITY RESULT *********************

		// send update about goals CHANGED: createOpportunity goal is increased.
		values.setValue(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY, "3");
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);
		
		// Now a new opportunity is created. Check Coach encourages.
		userService.currentReport.newOpportunities = 1;
		GoalSignal.getInstance().newEvent();
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);
		cul.checkGoals();
		assertEquals(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY + " 1 2", msg.getMessage()); // 2 = how many left to do ?
		
		// New opportunity is deleted (result lowers). Check Coach says nothing.
		userService.currentReport.newOpportunities = 0;
		GoalSignal.getInstance().newEvent();
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);

		// New opportunity counter is reached (result is OK). Check Coach says goal is reached.
		userService.currentReport.newOpportunities = 3;
		GoalSignal.getInstance().newEvent();
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);
		cul.checkGoals();
		assertEquals(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY + "_REACHED 3", msg.getMessage());

		// ****************** NOW THE SAME WITH A LOG TYPE RESULT *********************

		// send update about goals CHANGED: createOpportunity goal is increased.
		values.setValue(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY, "3");
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);
		
		// Now a new opportunity is created. Check Coach encourages.
		userService.currentReport.log.put(UserLogEntry.LogEntryType.APPLICATION, new Integer(1));
		GoalSignal.getInstance().newEvent();
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);
		cul.checkGoals();
		assertEquals(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY + " 1 2", msg.getMessage()); // 2 = how many left to do ?
		
		// New opportunity is deleted (result lowers). Check Coach says nothing.
		userService.currentReport.log.put(UserLogEntry.LogEntryType.APPLICATION, new Integer(0));
		GoalSignal.getInstance().newEvent();
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);

		// New opportunity counter is reached (result is OK). Check Coach says goal is reached.
		userService.currentReport.log.put(UserLogEntry.LogEntryType.APPLICATION, new Integer(3));
		GoalSignal.getInstance().newEvent();
		cul.checkGoals();
		assertEquals(msg.getMessage(), null);
		cul.checkGoals();
		assertEquals(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY + "_REACHED 3", msg.getMessage());
	}
	
}
