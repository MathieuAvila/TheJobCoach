package com.TheJobCoach.webapp.util.shared;

import java.util.Arrays;
import java.util.List;

public class UserValuesConstantsMyGoals 
{	
	public final static String PERFORMANCE_EVALUATION_PERIOD        = "PERFORMANCE_EVALUATION_PERIOD";
	public final static String PERFORMANCE_EVALUATION_PERIOD__WEEK  = "WEEK";
	public final static String PERFORMANCE_EVALUATION_PERIOD__2WEEK = "2WEEKS";
	public final static String PERFORMANCE_EVALUATION_PERIOD__MONTH = "MONTH";
	public final static List<String> PERFORMANCE_EVALUATION_PERIOD_LIST  = Arrays.asList(
			PERFORMANCE_EVALUATION_PERIOD__WEEK, 
			PERFORMANCE_EVALUATION_PERIOD__2WEEK, 
			PERFORMANCE_EVALUATION_PERIOD__MONTH);

	public final static String PERFORMANCE_CREATEOPPORTUNITY        = "PERFORMANCE_CREATEOPPORTUNITY";
	public final static String PERFORMANCE_CANDIDATEOPPORTUNITY     = "PERFORMANCE_CANDIDATEOPPORTUNITY";	
	public final static String PERFORMANCE_INTERVIEW                = "PERFORMANCE_INTERVIEW";	
	public final static String PERFORMANCE_PHONECALL                = "PERFORMANCE_PHONECALL";
	public final static String PERFORMANCE_PROPOSAL                 = "PERFORMANCE_PROPOSAL";

	public final static String PERFORMANCE_RECALL_GOAL_MIDDLE                = "PERFORMANCE_RECALL_GOAL_MIDDLE";
	
	public final static String PERFORMANCE_CONNECT_BEFORE_HOUR               = "PERFORMANCE_CONNECT_BEFORE_HOUR";
	public final static String PERFORMANCE_CONNECT_NOT_AFTER_HOUR            = "PERFORMANCE_CONNECT_NOT_AFTER_HOUR";
	public final static String PERFORMANCE_CONNECT_RATIO                     = "PERFORMANCE_CONNECT_RATIO";

}
