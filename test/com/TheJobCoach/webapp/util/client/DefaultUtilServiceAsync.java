package com.TheJobCoach.webapp.util.client;

import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachSettings;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.slf4j.Logger;

public class DefaultUtilServiceAsync implements UtilServiceAsync
{
	static public Map<String, String> values = new HashMap<String, String>();
	
	static Logger logger = LoggerFactory.getLogger(DefaultUtilServiceAsync.class);

	static public int calls = 0;
	
	{
		addValue(UserValuesConstantsAccount.ACCOUNT_TYPE, "Freemium");
		addValue(UserValuesConstantsAccount.ACCOUNT_MODEL,  UserValuesConstantsAccount.ACCOUNT_MODEL_LIST.get(0));
		addValue(UserValuesConstantsAccount.ACCOUNT_TITLE);
		addValue(UserValuesConstantsAccount.ACCOUNT_STATUS, UserValuesConstantsAccount.ACCOUNT_STATUS_LIST__ACTIVE_SEARCH);
		addValue(UserValuesConstantsAccount.ACCOUNT_KEYWORDS);
		addValue(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__DEFAULT_MAN);

		addValue(UserValuesConstantsAccount.ACCOUNT_PUBLISH_SEEKER, "YES");
		addValue(UserValuesConstantsAccount.ACCOUNT_PUBLISH_COACH, "YES");
		addValue(UserValuesConstantsAccount.ACCOUNT_PUBLISH_RECRUITER, "YES");

		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO);
		
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD);

		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL);

		addValue(UserValuesConstantsMyGoals.PERFORMANCE_RECALL_GOAL_MIDDLE);

		addValue(UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_SITE_DELAY, "3");
		addValue(UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_CONTACT_DELAY, "3");
		addValue(UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_OPPORTUNITY_RECALL, "3");
		addValue(UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_INTERVIEW, "3");
		addValue(UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_EVENT, "3");	
		
		addValue(UserValuesConstantsCoachMessages.COACH_WELCOME, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_HELLO, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_HELLO_AGAIN, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_PRESENTING, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_LATE_ARRIVAL, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_LATE_DEPARTURE, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_VERY_LATE_ARRIVAL, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_VERY_LATE_DEPARTURE, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_DEPARTURE_WARNING, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_DEPARTURE_TIME, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_GOAL_END_PERIOD, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_GOAL_SEND_EMAIL, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_OPP_NONE, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_OPP_NO_LOG, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_OPP_NO_APPLICATION, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_LOG_RECALL, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_LOG_INTERVIEW, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_LOG_FAILURE, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_LOG_SUCCESS, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_PERSONAL_NOTE, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_PASSWORD_WARNING, "0");

		addValue(UserValuesConstantsCoachMessages.COACH_WELCOME,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_HELLO,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_HELLO_AGAIN,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_PRESENTING,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_LATE_ARRIVAL,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_LATE_DEPARTURE,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_VERY_LATE_ARRIVAL,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_VERY_LATE_DEPARTURE,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_DEPARTURE_WARNING,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_DEPARTURE_TIME,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_GOAL_END_PERIOD,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_GOAL_SEND_EMAIL,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_OPP_NONE,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_OPP_NO_LOG,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_OPP_NO_APPLICATION,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_LOG_RECALL,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_LOG_INTERVIEW,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_LOG_FAILURE,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_LOG_SUCCESS,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_PERSONAL_NOTE,"0");
		addValue(UserValuesConstantsCoachMessages.COACH_PASSWORD_WARNING,"0");

		addValue(UserValuesConstantsCoachMessages.COACH_USER_ACTION_OPPORTUNITY, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_USER_ACTION_LOG, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_USER_ACTION_CONTACT, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_USER_ACTION_JOB_SITE, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_USER_ACTION_TYPE_LOG_APPLICATION, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_USER_ACTION_TYPE_LOG_SUCCESS, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_USER_ACTION_TYPE_LOG_FAILURE, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_USER_ACTION_POST_IT_CONTACT, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_USER_ACTION_POST_IT_JOB_SITE, "0");
		addValue(UserValuesConstantsCoachMessages.COACH_USER_ACTION_LOG, "0");

	}
	
	public static void addValue(String key, String value)
	{
		values.put(key, value);
	}
	public static void addValue(String key)
	{
		values.put(key, "");
	}
	
	@Override
	public void getValues(UserId user, String rootValue,
			AsyncCallback<Map<String, String>> callback) {
		assert(user != null);
		HashMap<String, String> hm = new HashMap<String, String>();
		for (String k : values.keySet())
		{
			if (k.startsWith(rootValue)) hm.put(k, values.get(k));
		}
		logger.info("getValues root " + rootValue);
		logger.info("getValues result " + hm);
		calls++;
		callback.onSuccess(hm);
	}
	
	@Override
	public void setValues(UserId user, Map<String, String> map,
			AsyncCallback<String> callback)
	{
		for (String k: map.keySet())
		{
			if (values.containsKey(k)) 
			{
				values.put(k, map.get(k));
				logger.info("Set " + k + " to: " + map.get(k));
			}
			else
			{
				logger.info("Unknown key: " + k);
			}
		}
		calls++;
		callback.onSuccess("");
	}
	
	@Override
	public void sendUpdateList(UserId user, UpdateRequest request,
			AsyncCallback<UpdateResponse> callback)
	{
		callback.onSuccess(new UpdateResponse());
	}
};