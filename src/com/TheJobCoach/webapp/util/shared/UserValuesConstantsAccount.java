package com.TheJobCoach.webapp.util.shared;

import java.util.Arrays;
import java.util.List;

public class UserValuesConstantsAccount 
{
	public final static List<String> YES_NO_LIST  = Arrays.asList("YES", "NO");
	
	public final static String ACCOUNT_TYPE        = "ACCOUNT_TYPE";
	public final static String ACCOUNT_MODEL       = "ACCOUNT_MODEL";
	public final static List<String> ACCOUNT_MODEL_LIST  = Arrays.asList("FREEMIUM");
	
	public final static String ACCOUNT_TITLE        = "ACCOUNT_TITLE";
	public final static String ACCOUNT_STATUS       = "ACCOUNT_STATUS";
	public final static List<String> ACCOUNT_STATUS_LIST  = Arrays.asList("ACTIVE_SEARCH", "WORK_ACTIVE", "LISTENING");
	
	public final static String ACCOUNT_KEYWORDS     = "ACCOUNT_KEYWORDS";
	
	public final static String ACCOUNT_COACH_AVATAR       = "ACCOUNT_COACH_AVATAR";
	public final static String ACCOUNT_COACH_AVATAR__DEFAULT_MAN         = "DEFAULT_MAN";
	public final static String ACCOUNT_COACH_AVATAR__DEFAULT_WOMAN       = "DEFAULT_WOMAN";
	public final static String ACCOUNT_COACH_AVATAR__COACH_SURFER        = "COACH_SURFER";
	public final static String ACCOUNT_COACH_AVATAR__COACH_MILITARY      = "COACH_MILITARY";
	public final static List<String> ACCOUNT_COACH_AVATAR_LIST  = Arrays.asList(
			ACCOUNT_COACH_AVATAR__DEFAULT_MAN, 
			ACCOUNT_COACH_AVATAR__DEFAULT_WOMAN, 
			ACCOUNT_COACH_AVATAR__COACH_SURFER, 
			ACCOUNT_COACH_AVATAR__COACH_MILITARY);
	
	public final static String ACCOUNT_PUBLISH_SEEKER       = "ACCOUNT_PUBLISH_SEEKER";
	public final static String ACCOUNT_PUBLISH_COACH        = "ACCOUNT_PUBLISH_COACH";
	public final static String ACCOUNT_PUBLISH_RECRUITER    = "ACCOUNT_PUBLISH_RECRUITER";

}
