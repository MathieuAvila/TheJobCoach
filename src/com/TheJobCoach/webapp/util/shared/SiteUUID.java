package com.TheJobCoach.webapp.util.shared;

//import java.text.SimpleDateFormat;
import java.util.Date;

public class SiteUUID {

	static int count = 0;
	
	public static String formatInteger(int v)
	{		
		if (v < 10) return "0" + String.valueOf(v);
		return String.valueOf(v);
	}
	
	public static String formatLongInteger(int v)
	{	
		String prepend = "";
		if (v < 10000) prepend += "0";
		if (v < 1000) prepend += "0";
		if (v < 100) prepend += "0";
		if (v < 10) prepend += "0";
		return prepend + String.valueOf(v);
	}
	
	@SuppressWarnings("deprecation")
	public static String dateFormatter(Date d)
	{
		count++;
		return 
				formatInteger(1900 + d.getYear()) + "-" 
				+ formatInteger(d.getMonth() + 1) + "-"
				+ formatInteger(d.getDate()) + "_"
				+ formatInteger(d.getHours()) + ":"
				+ formatInteger(d.getMinutes()) + ":"
				+ formatInteger(d.getSeconds()) + ":"
				+ formatLongInteger(count);
	}

	public static String getDateUuid()
	{
		Date d = new Date();		
		return dateFormatter(d);
	}
}
