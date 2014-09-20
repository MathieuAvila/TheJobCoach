package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserValuesConstants;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachSettings;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;

public class UserValuesCore {

	static protected class FieldDefinition
	{
		String name;
		public int length;
		boolean clientSideFree;
		String defaultValue;
		boolean restrictedUserAccess;
		
		public String getName()
		{
			return name;
		}
		public boolean getClientSideFree()
		{
			return clientSideFree;
		}
		public String getDefaultValue()
		{
			return defaultValue;
		}
		
		public boolean getRestrictedUserAccess()
		{
			return restrictedUserAccess;
		}
		
		public FieldDefinition(String name) 
		{ 
			this.name = name;
			this.length = MAX_OPTION_LENGTH;
			this.clientSideFree = true;
			this.defaultValue = "";
			this.restrictedUserAccess = true;
		};
		public FieldDefinition(String name, boolean restrictedUserAccess) 
		{ 
			this.name = name;
			this.length = MAX_OPTION_LENGTH;
			this.clientSideFree = true;
			this.defaultValue = "";
			this.restrictedUserAccess = restrictedUserAccess;
		};
		public FieldDefinition(String name, int length, boolean clientSideFree, String defaultValue) 
		{ 
			this.name = name;
			this.length = length;
			this.clientSideFree = clientSideFree;
			this.defaultValue = defaultValue;
			this.restrictedUserAccess = true;
		}
		public FieldDefinition(String name, int length, boolean clientSideFree, String defaultValue, boolean restrictedUserAccess) 
		{ 
			this.name = name;
			this.length = length;
			this.clientSideFree = clientSideFree;
			this.defaultValue = defaultValue;
			this.restrictedUserAccess = restrictedUserAccess;
		}
		
		public void check(String val, boolean client) throws SystemException 
		{
			if (val == null) throw new SystemException();
			if (val.length() >= length) throw new SystemException();
			if (client && !clientSideFree) throw new SystemException();
		};
	}
	
	static final int MAX_OPTION_LENGTH = 100;
	static final int MAX_OPTION_LENGTH_INT = 5;

	static final int YES_NO_LENGTH = 4;
	
	protected static List<FieldDefinition> keys;
	static Map<String, FieldDefinition> keysMap;
	static Set<String> keysName;
	static List<String> keysNameList;
	
	static public void addField(FieldDefinition f)
	{
		keys.add(f);
		keysName.add(f.name);
		keysNameList.add(f.name);
		keysMap.put(f.name, f);
	}
	
	static
	{
		keys = new ArrayList<FieldDefinition>();
		keysName = new HashSet<String>();
		keysNameList = new ArrayList<String>();
		keysMap = new HashMap<String, FieldDefinition>();

		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_TYPE, MAX_OPTION_LENGTH, false, "Freemium"));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_MODEL, MAX_OPTION_LENGTH, false, UserValuesConstantsAccount.ACCOUNT_MODEL_LIST.get(0)));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_TITLE, false /* not restricted */));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_STATUS, MAX_OPTION_LENGTH, true, UserValuesConstantsAccount.ACCOUNT_STATUS_LIST__ACTIVE_SEARCH, false /* not restricted */));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_KEYWORDS, false /* not restricted */));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_LANGUAGE));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, MAX_OPTION_LENGTH, true, UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__DEFAULT_MAN));
		
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_DELETION, YES_NO_LENGTH, false, UserValuesConstants.NO));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_DELETION_DATE, MAX_OPTION_LENGTH, false, ""));

		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_PUBLISH_SEEKER, YES_NO_LENGTH, true, UserValuesConstants.YES));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_PUBLISH_COACH, YES_NO_LENGTH, true, UserValuesConstants.YES));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_PUBLISH_RECRUITER, YES_NO_LENGTH, true, UserValuesConstants.YES));
		
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_FIRSTNAME));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_LASTNAME));
		
		addField(new FieldDefinition(UserValuesConstantsAccount.SECURITY_WAITING_TIME_REQUEST, MAX_OPTION_LENGTH, false, "0"));

		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_LAST_EMAIL, MAX_OPTION_LENGTH, false, ""));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_RECEIVE_EMAIL, YES_NO_LENGTH, true, UserValuesConstants.NO));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD, MAX_OPTION_LENGTH, true, UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD__MONTH));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO));
		
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY));		
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_RECALL_GOAL_MIDDLE));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL));

		addField(new FieldDefinition(UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_SITE_DELAY, MAX_OPTION_LENGTH, true, "3"));
		addField(new FieldDefinition(UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_CONTACT_DELAY, MAX_OPTION_LENGTH, true, "3"));
		addField(new FieldDefinition(UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_OPPORTUNITY_RECALL, MAX_OPTION_LENGTH, true, "3"));
		addField(new FieldDefinition(UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_INTERVIEW, MAX_OPTION_LENGTH, true, "3"));
		addField(new FieldDefinition(UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_EVENT, MAX_OPTION_LENGTH, true, "3"));	
		
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_WELCOME, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_HELLO, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_HELLO_AGAIN, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_PRESENTING, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_LATE_ARRIVAL, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_LATE_DEPARTURE, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_VERY_LATE_ARRIVAL, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_VERY_LATE_DEPARTURE, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_DEPARTURE_WARNING, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_DEPARTURE_TIME, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_GOAL_NOT_SET, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_GOAL_END_PERIOD, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_GOAL_SEND_EMAIL, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_OPP_NONE, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_OPP_NO_LOG, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_OPP_NO_APPLICATION, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_LOG_RECALL, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_LOG_INTERVIEW, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_LOG_FAILURE, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_LOG_SUCCESS, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_PERSONAL_NOTE, MAX_OPTION_LENGTH_INT, true, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_PASSWORD_WARNING, MAX_OPTION_LENGTH_INT, true, "0"));

		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_USER_ACTION_OPPORTUNITY, MAX_OPTION_LENGTH_INT, false, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_USER_ACTION_LOG, MAX_OPTION_LENGTH_INT, false, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_USER_ACTION_CONTACT, MAX_OPTION_LENGTH_INT, false, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_USER_ACTION_JOB_SITE, MAX_OPTION_LENGTH_INT, false, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_USER_ACTION_TYPE_LOG_APPLICATION, MAX_OPTION_LENGTH_INT, false, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_USER_ACTION_TYPE_LOG_SUCCESS, MAX_OPTION_LENGTH_INT, false, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_USER_ACTION_TYPE_LOG_FAILURE, MAX_OPTION_LENGTH_INT, false, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_USER_ACTION_POST_IT_CONTACT, MAX_OPTION_LENGTH_INT, false, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_USER_ACTION_POST_IT_JOB_SITE, MAX_OPTION_LENGTH_INT, false, "0"));
		addField(new FieldDefinition(UserValuesConstantsCoachMessages.COACH_USER_ACTION_LOG, MAX_OPTION_LENGTH_INT, false, "0"));
	}
}
