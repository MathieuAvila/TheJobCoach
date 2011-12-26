package com.TheJobCoach.util;

import java.util.Date;

public class Convertor {

	static public String toString(String val, String def)
	{
		if (val != null) return val;
		return def;
	}

	static public String toString(String val)
	{
		if (val != null) return val;
		return "";
	}

	static public Date toDate(String val)
	{
		if (val != null)
		{			
			@SuppressWarnings("deprecation")
			Date d = new Date(new Long(val));
			return d;
		}
		return new Date();
	}
	
	static public Date toDate(String val, Date def)
	{
		if (val != null)
		{			
			@SuppressWarnings("deprecation")
			Date d = new Date(val);
			return d;
		}
		return def;
	}

}
