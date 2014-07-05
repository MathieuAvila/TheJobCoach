package com.TheJobCoach.webapp.util.shared;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserValuesConstants
{

	public final static String YES = "YES";
	public final static String NO = "NO";

	public final static List<String> YES_NO_LIST  = Arrays.asList(YES, NO);
	public final static Map<Boolean, String> YES_NO_MAP  = new HashMap<Boolean, String>();

	static 
	{
		YES_NO_MAP.put(true, YES);
		YES_NO_MAP.put(false, NO);
	}


}
