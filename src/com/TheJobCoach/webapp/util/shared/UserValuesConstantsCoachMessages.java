package com.TheJobCoach.webapp.util.shared;

import java.util.HashMap;

public class UserValuesConstantsCoachMessages 
{	
	public final static String COACH_ROOT                     = "COACH_";
	public final static String COACH_WELCOME                  = "COACH_WELCOME";
	public final static String COACH_PRESENTING               = "COACH_PRESENTING";
	public final static String COACH_HELLO                    = "COACH_HELLO";
	public final static String COACH_HELLO_AGAIN              = "COACH_HELLO_AGAIN";
	public final static String COACH_LATE_ARRIVAL             = "COACH_LATE_ARRIVAL";
	public final static String COACH_LATE_DEPARTURE           = "COACH_LATE_DEPARTURE";
	public final static String COACH_VERY_LATE_ARRIVAL        = "COACH_VERY_LATE_ARRIVAL";
	public final static String COACH_VERY_LATE_DEPARTURE      = "COACH_VERY_LATE_DEPARTURE";
	public final static String COACH_DEPARTURE_WARNING        = "COACH_DEPARTURE_WARNING";
	public final static String COACH_DEPARTURE_TIME           = "COACH_DEPARTURE_TIME";
	public final static String COACH_GOAL_NOT_SET             = "COACH_GOAL_NOT_SET";
	public final static String COACH_GOAL_END_PERIOD          = "COACH_GOAL_END_PERIOD";
	public final static String COACH_GOAL_SEND_EMAIL          = "COACH_GOAL_SEND_EMAIL";
	public final static String COACH_OPP_NONE                 = "COACH_OPP_NONE";
	public final static String COACH_OPP_NO_LOG               = "COACH_OPP_NO_LOG";
	public final static String COACH_OPP_NO_APPLICATION       = "COACH_OPP_NO_APPLICATION";
	public final static String COACH_OPP_NO_CONTACT           = "COACH_OPP_NO_CONTACT";
	public final static String COACH_OPP_NO_JOB_SITE          = "COACH_OPP_NO_JOB_SITE";
	public final static String COACH_LOG_RECALL               = "COACH_LOG_RECALL";
	public final static String COACH_LOG_INTERVIEW            = "COACH_LOG_INTERVIEW";
	public final static String COACH_LOG_FAILURE              = "COACH_LOG_FAILURE";
	public final static String COACH_LOG_SUCCESS              = "COACH_LOG_SUCCESS";
	public final static String COACH_PERSONAL_NOTE            = "COACH_PERSONAL_NOTE";
	public final static String COACH_PASSWORD_WARNING         = "COACH_PASSWORD_WARNING";

	/* This is set by the server the first time the user creates an opportunity */
	public final static String COACH_USER_ACTION_OPPORTUNITY  = "COACH_USER_ACTION_OPPORTUNITY";
	/* This is set by the server the first time the user creates a log */
	public final static String COACH_USER_ACTION_LOG          = "COACH_USER_ACTION_LOG";
	/* This is set by the server the first time the user creates a contact */
	public final static String COACH_USER_ACTION_CONTACT      = "COACH_USER_ACTION_CONTACT";
	/* This is set by the server the first time the user creates a job site */
	public final static String COACH_USER_ACTION_JOB_SITE     = "COACH_USER_ACTION_JOB SITE";
	/* This is set by the server the first time the user creates a log of type application */
	public static final String COACH_USER_ACTION_TYPE_LOG_APPLICATION = "COACH_USER_ACTION_TYPE_LOG_APPLICATION";
	/* This is set by the server the first time the user creates a log of type failure */
	public static final String COACH_USER_ACTION_TYPE_LOG_FAILURE = "COACH_USER_ACTION_TYPE_LOG_FAILURE";
	/* This is set by the server the first time the user creates a log of type success */
	public static final String COACH_USER_ACTION_TYPE_LOG_SUCCESS = "COACH_USER_ACTION_TYPE_LOG_SUCCESS";
	/* This is set by the server the first time the coach creates a post-it for a job site */
	public static final String COACH_USER_ACTION_POST_IT_JOB_SITE      = "COACH_USER_ACTION_POST_IT_JOB_SITE";
	/* This is set by the server the first time the coach creates a post-it for an external contact */
	public static final String COACH_USER_ACTION_POST_IT_CONTACT      = "COACH_USER_ACTION_POST_IT_CONTACT";

	static public class messageMinMax 
	{
		public int min;
		public int max;
		public messageMinMax(int min, int max)
		{
			this.min = min;
			this.max = max;
		}
	}
	
	public static HashMap<String, messageMinMax> minMaxKeyMap = new HashMap<String, messageMinMax>();
	
	static {
		minMaxKeyMap.put(COACH_WELCOME,                 new messageMinMax(0, 1));
		minMaxKeyMap.put(COACH_HELLO,                   new messageMinMax(1, 0));
		//minMaxKeyMap.put(COACH_HELLO_AGAIN,             new messageMinMax(0, 0));
		minMaxKeyMap.put(COACH_GOAL_END_PERIOD,         new messageMinMax(1, 0));
		minMaxKeyMap.put(COACH_GOAL_SEND_EMAIL,         new messageMinMax(5, 10));
		minMaxKeyMap.put(COACH_OPP_NONE,                new messageMinMax(0, 5));
		minMaxKeyMap.put(COACH_OPP_NO_LOG,              new messageMinMax(0, 5));
		minMaxKeyMap.put(COACH_OPP_NO_APPLICATION,      new messageMinMax(0, 5));
		minMaxKeyMap.put(COACH_PERSONAL_NOTE,           new messageMinMax(0, 3));
		minMaxKeyMap.put(COACH_PASSWORD_WARNING,        new messageMinMax(0, 3));
		minMaxKeyMap.put(COACH_LOG_RECALL,              new messageMinMax(0, 3));
		minMaxKeyMap.put(COACH_GOAL_NOT_SET,            new messageMinMax(0, 5)); // this is important enough for bothering the user.
		//minMaxKeyMap.put(COACH_LOG_INTERVIEW,           new messageMinMax(0, 0));
		//minMaxKeyMap.put(COACH_LOG_FAILURE,             new messageMinMax(0, 0));
		//minMaxKeyMap.put(COACH_LOG_SUCCESS,             new messageMinMax(0, 0));
	}	
}
