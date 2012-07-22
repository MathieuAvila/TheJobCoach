package com.TheJobCoach.webapp.util.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DefaultUtilServiceAsync implements UtilServiceAsync
{
	static Map<String, String> values = new HashMap<String, String>();
	
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
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_WEEKLY_RATIO);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_MONTHLY_RATIO);
		
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY_BIWEEKLY);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY_MONTHLY);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY_WEEKLY);

		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY_BIWEEKLY);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY_MONTHLY);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY_WEEKLY);
		
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW_BIWEEKLY);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW_MONTHLY);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW_WEEKLY);

		addValue(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL_BIWEEKLY);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL_MONTHLY);
		addValue(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL_WEEKLY);

		addValue(UserValuesConstantsMyGoals.PERFORMANCE_RECALL_GOAL_MIDDLE);
			
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
		callback.onSuccess(hm);
	}
	
	@Override
	public void setValues(UserId user, Map<String, String> map,
			AsyncCallback<String> callback)
	{
		for (String k: map.keySet())
		{
			if (values.containsKey(k)) values.put(k, map.get(k));
		}
		callback.onSuccess("");
	}
};