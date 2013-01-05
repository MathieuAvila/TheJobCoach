package com.TheJobCoach.webapp.util.shared;

import java.util.Arrays;
import java.util.List;

public class ConstantsMyReports 
{
	public final static String FORMAT_HTML                               = "HTML";
	public final static List<String> FORMAT_LIST  = Arrays.asList(
			FORMAT_HTML);
	
	public final static String PERIOD_LAST_WEEK                               = "WEEK";
	public final static String PERIOD_LAST_2WEEKS                             = "2WEEKS";
	public final static String PERIOD_LAST_MONTH                              = "MONTH";
	public final static String PERIOD_LAST_2MONTHS                            = "2MONTHS";
	public final static String PERIOD_SET                                     = "SET";
	public final static List<String> PERIOD_LIST  = Arrays.asList(
			PERIOD_LAST_WEEK,
			PERIOD_LAST_2WEEKS,
			PERIOD_LAST_MONTH,
			PERIOD_LAST_2MONTHS,
			PERIOD_SET);
	
	public final static String INCLUDE_LOG_OPPORTUNITY                        = "HTML";

}
