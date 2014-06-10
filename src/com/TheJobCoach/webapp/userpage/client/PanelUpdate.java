package com.TheJobCoach.webapp.userpage.client;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Coach.GoalSignal;
import com.TheJobCoach.webapp.userpage.client.Coach.MessagePipe;
import com.TheJobCoach.webapp.userpage.shared.GoalReportInformation;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.UtilService;
import com.TheJobCoach.webapp.util.client.UtilServiceAsync;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.FormatUtil.PERIOD_TYPE;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class PanelUpdate  extends SimplePanel implements EntryPoint, ReturnValue {

	Label connectionTime;
	Panel rootPanel;
	UserId userId;

	int connectSec = 0;
	int previousTime = 0;
	boolean firstTime = true;
	Date today = new Date();

	MessagePipe message = null;
	ClientUserValuesUtils values = null;

	Date arrivalTime = null;
	Date arrivalTimeMore30minutes = null;
	
	Date departureTimeLess15minutes = null;
	Date departureTimeMore30minutes = null;
	
	int timePhase = 0;
	
	static UtilServiceAsync utilService =  GWT.create(UtilService.class);
	static UserServiceAsync userService =  GWT.create(UserService.class);

	// Set to this value if no gola was set or goal string is invalid.
	static final int GOAL_NOT_SET = -1000;
	
	// Start checking results after this time is elapsed. Doing so lowers the calls to server during the first seconds after logged in.
	static final int START_CHECK_GOAL_RESULT = 120;
	
	// Simple check list
	class SimpleCheckList
	{
		long time;
		boolean checked;
		String messageKey;
		String checkedKey;
		public SimpleCheckList(long time, String messageKey, String checkedKey){
			this.time = time;
			this.checked = false;
			this.messageKey = messageKey;
			this.checkedKey = checkedKey;
		}
		public void check(UserId userId, int totalTime)
		{
			if ((!checked)&&(totalTime > time))
			{
				// Not checked for the moment (during the day)
				if ("0".equals(values.getValueFromCache(checkedKey)))
				{
					// Send message.
					message.addMessage(messageKey);
					checked = true;
				}
			}
		};
	};

	SimpleCheckList[] checkList = {
			new SimpleCheckList(
					15*60, 
					UserValuesConstantsCoachMessages.COACH_OPP_NONE,
					UserValuesConstantsCoachMessages.COACH_USER_ACTION_OPPORTUNITY),
			new SimpleCheckList(
					20*60, 
					UserValuesConstantsCoachMessages.COACH_OPP_NO_LOG,
					UserValuesConstantsCoachMessages.COACH_USER_ACTION_LOG),
			new SimpleCheckList(
					30*60, 
					UserValuesConstantsCoachMessages.COACH_OPP_NO_CONTACT,
					UserValuesConstantsCoachMessages.COACH_USER_ACTION_CONTACT),
			new SimpleCheckList(
					40*60, 
					UserValuesConstantsCoachMessages.COACH_OPP_NO_APPLICATION,
					UserValuesConstantsCoachMessages.COACH_USER_ACTION_TYPE_LOG_APPLICATION)
	};
	
	void checkGoals()
	{
		Map<String, Integer> result = getGoalResult();
		if ((result != null)&&(previousResult != null))
		{
			// Look for different result
			for (String key: result.keySet())
			{
				Integer c = result.get(key);
				Integer p = previousResult.get(key);
				int goal = getIntFromValue(values.getValueFromCache(key));
				// value changed somehow, only check when it's in progress, and goal is set.
				System.out.println(c + " " + p + " " + key + " " + goal + " " + result + " " + previousResult);
				if ((c.compareTo(p) > 0) && (goal != GOAL_NOT_SET))
				{
					// message depends on key and goal
					if (goal == c) // we have reached the goal.
						message.addDirectParameterizedMessage(key + "_REACHED", new Vector<String>(Arrays.asList(
								String.valueOf(c))));
					else if (goal > c)// goal not reached, encourage.
						message.addDirectParameterizedMessage(key, new Vector<String>(Arrays.asList(
								String.valueOf(c),
								String.valueOf(goal-c))));
				}
			}
			previousResult = null; // treated.
		}
		// when no goal is set, invite user to set goals in My Goals
		if (result != null)
		{
			boolean set = false;
			for (String key: result.keySet())
			{
				Integer c = getIntFromValue(values.getValueFromCache(key));
				if (c != GOAL_NOT_SET)
				{
					set = true;
					break;
				}
			}
			if (!set)
			{
				message.addMessage(UserValuesConstantsCoachMessages.COACH_GOAL_NOT_SET);
			}
		}
	}

	void checkTime(UpdateResponse response)
	{
		int totalTime = response.totalDayTime;
		Date currentTime = new Date();
		if (firstTime) // send hello message, or reconnect message
		{
			if (totalTime <= 5) // really first time of the day
			{
				message.addMessage(UserValuesConstantsCoachMessages.COACH_WELCOME);
				message.addMessage(UserValuesConstantsCoachMessages.COACH_HELLO);
				// Check arrival time.
				if (arrivalTime != null)
				{
				System.out.println(arrivalTime);
				System.out.println(arrivalTimeMore30minutes);
				System.out.println(currentTime.after(arrivalTime));
				System.out.println(currentTime.after(arrivalTimeMore30minutes));
				System.out.println(timePhase);
				}
				if ((arrivalTime != null) && (timePhase == 0))
				{
					if (currentTime.after(arrivalTimeMore30minutes))
					{
						message.addMessage(UserValuesConstantsCoachMessages.COACH_VERY_LATE_ARRIVAL);
						timePhase = 1;
					} 
					else if (currentTime.after(arrivalTime))
					{
						message.addMessage(UserValuesConstantsCoachMessages.COACH_LATE_ARRIVAL);
						timePhase = 1;
					}
				}
			}
			else
			{
				message.addMessage(UserValuesConstantsCoachMessages.COACH_HELLO_AGAIN);
				timePhase = 1;
			}
			previousTime = totalTime;
			firstTime = false;
		}
		else
		{
			// Check logout time.
			if ((departureTimeLess15minutes != null && (currentTime.after(departureTimeLess15minutes))) && (timePhase == 1))
			{
				timePhase = 2;
				message.addMessage(UserValuesConstantsCoachMessages.COACH_DEPARTURE_TIME);
			} 
			else if ((departureTimeMore30minutes != null && (currentTime.after(departureTimeMore30minutes))) && (timePhase == 2))
			{
				timePhase = 3;
				message.addMessage(UserValuesConstantsCoachMessages.COACH_DEPARTURE_WARNING);
			}
		}
		
		// Check opportunity creation
		for (SimpleCheckList checker : checkList)
		{
			checker.check(userId, totalTime);
		}
		
		// Check something happened to goals
		if (connectSec > START_CHECK_GOAL_RESULT)
		{
			checkGoals();
		}
		
		// Store response. Send appropriate callbacks.	
		values.callbackServerSetValues(response.updatedValues);
	}
	
	public void fireTimer()
	{
		// Wait for next run.
		long total = connectSec + previousTime;
		long h = total / 60 / 60;
		long m = total / 60 - h * 60;
		long s = total % 60;
		if (!firstTime) connectionTime.setText(" " +
				((h != 0) ? (String.valueOf(h) + "h ") : new String()) + 
				((m != 0)  ? (String.valueOf(m) + "mn ") : new String()) + 
				String.valueOf(s) + "s");
		UpdateRequest request = new UpdateRequest(today, connectSec, firstTime);

		ServerCallHelper<UpdateResponse> callback =  new ServerCallHelper<UpdateResponse>(rootPanel){
			@Override
			public void onSuccess(UpdateResponse result)
			{
				result.totalDayTime += connectSec;
				checkTime(result);
			}
		};	
		utilService.sendUpdateList(userId, request, callback);
		connectSec+=5;
	}
	
	Timer timer = new Timer() 
	{
		public void run() 
		{
			fireTimer();
		};
	};

	public PanelUpdate(Panel rootPanel, UserId userId, Label connectionTime) 
	{
		this.connectionTime = connectionTime;
		this.rootPanel = rootPanel;
		this.userId = userId;
		message = MessagePipe.getMessagePipe(userId, rootPanel);
		// Get time goals.
		values = new ClientUserValuesUtils(rootPanel, userId);
		values.preloadValueList("PERFORMANCE", this);
	}

	int lastGoalSignal = 0;
	Map<String, Integer> previousResult = null;
	Map<String, Integer> currentResult = null;
	boolean receivedPerformanceKeys = false;
	
	private Map<String, Integer> getGoalResult()
	{
		// only if we have already received all keys. We should receive the full list of keys or nothing.
		if (!receivedPerformanceKeys)
		{
			return null;
		}
		
		ServerCallHelper<GoalReportInformation> callback = new ServerCallHelper<GoalReportInformation>(rootPanel) {
			@Override
			public void onSuccess(GoalReportInformation result)
			{
				previousResult = currentResult;
				currentResult = new HashMap<String, Integer>();
				currentResult.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY, result.newOpportunities);
				currentResult.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY, result.log.get(LogEntryType.APPLICATION));
				currentResult.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW, result.log.get(LogEntryType.INTERVIEW));
				currentResult.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL, result.log.get(LogEntryType.RECALL));
				currentResult.put(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL, result.log.get(LogEntryType.PROPOSAL));
			}
		};
		
		// Check if Results are accurate, and they did not changed.
		if ((currentResult != null)&&(lastGoalSignal == GoalSignal.getInstance().getCounter()))
			return currentResult;
		lastGoalSignal = GoalSignal.getInstance().getCounter();
		// From now on, currentResult is NULL (not inited) OR there was a change in the results => an action may have changed the results.
		
		// no result for the moment. Request them before doing anything. Next call will succeed.
		String periodString = values.getValueFromCache(UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD);
		PERIOD_TYPE period = UserValuesConstantsMyGoals.mapStringPeriod.get(periodString);
		if (period == null) period = PERIOD_TYPE.PERIOD_TYPE_WEEK;
		// Set times in previousDate and nextDate with goal values.	
		Date nextDateValue = new Date();
		Date previousDateValue = new Date();
		FormatUtil.getPeriod(period, 0, new Date(), previousDateValue, nextDateValue);
		previousDateValue = new Date(FormatUtil.startOfTheDay(previousDateValue).getTime());
		nextDateValue = new Date(FormatUtil.startOfTheDay(nextDateValue).getTime());
		userService.getUserGoalReport(userId, previousDateValue, nextDateValue, callback);

		return null;
	}
	
	@Override
	public void onModuleLoad() {
		connectSec = 0;
		timer.scheduleRepeating(5000);
		setSize("0","0");
	}

	Integer getIntFromValue(String value)
	{
		if (value == null) return GOAL_NOT_SET;
		if ("".equals(value)) return GOAL_NOT_SET;
		int result;
		try {
			result = Integer.decode(value);
		} catch (Exception e) { return GOAL_NOT_SET;}
		return result;
	}

	static final HashSet<String> performanceKeys = new HashSet<String>(Arrays.asList(
			UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY,
			UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY,
			UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW,
			UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL,
			UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL));

			
	@Override
	public void notifyValue(boolean set, String key, String value)
	{
		Date currentTime = new Date();
		if (key.equals(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR))
		{
			System.out.println("arrivalTime is: " + value);
			Integer v = FormatUtil.getIntegerFromString(value);
			if (v != null)
			{
				arrivalTime = new Date(FormatUtil.startOfTheDay(currentTime).getTime() + v);
				arrivalTimeMore30minutes = new Date(FormatUtil.startOfTheDay(currentTime).getTime() + v + 30*60*1000);
				System.out.println("arrivalTime is: " + arrivalTime + " late is " + arrivalTimeMore30minutes);
				//if (currentTime.after(arrivalTime)) timePhase = 1;
				//if (currentTime.after(arrivalTimeMore30minutes)) timePhase = 2;
				timePhase = 0;
			}
		}
		else if (key.equals(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR))
		{
			System.out.println("departureTime time is: " + value);
			Integer v = FormatUtil.getIntegerFromString(value);
			if (v != null)
			{
				departureTimeLess15minutes = new Date(FormatUtil.startOfTheDay(currentTime).getTime() + v - 15*60*1000);
				departureTimeMore30minutes = new Date(FormatUtil.startOfTheDay(currentTime).getTime() + v + 30*60*1000);
				if (currentTime.after(departureTimeLess15minutes)) timePhase = 2;
				if (currentTime.after(departureTimeMore30minutes)) timePhase = 3;
			}
		}
		// If results are already received and we have a change, then user has changed his preferences, so that they are invalid now.
		// => flush all results, now and previous. Next call will trigger a server call to retrieve new performance results.
		if ((currentResult != null) && (receivedPerformanceKeys) && performanceKeys.contains(key))
		{
			currentResult = null;
			previousResult = null;
		}
		// normally received in 1 chunk. If 1 key arrives, the others will follow
		if (performanceKeys.contains(key))
			receivedPerformanceKeys = true;
	}

}
