package com.TheJobCoach.webapp.util.shared;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SiteUUID {
	
	static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	
	public static String dateFormatter(Date d)
	{
		return fmt.format(d);
	}

	public static String getDateUuid()
	{
		Date d = new Date();		
		return dateFormatter(d);
	}
}
