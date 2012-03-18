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

	static public int toInt(String string) {
		if ("".equals(string))
		{
			return 0;
		}
		if (string == null) return 0;
		return Integer.decode(string);
	}
	
	static public boolean toBoolean(String v)
	{
		return "1".equals(v);
	}
	
}
