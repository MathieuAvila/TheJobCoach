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

	@SuppressWarnings("deprecation")
	static public String toString(Date val)
	{
		if (val != null) return String.valueOf(val.getTime());
		return String.valueOf(Date.UTC(0, 0, 0, 0, 0, 0));
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
	
	static public double toDouble(String string) {
		if ("".equals(string))
		{
			return 0;
		}
		if (string == null) return 0;
		System.out.println("String: " + string + " converted to: " + Float.valueOf(string));
		return Float.valueOf(string);
	}
	
	static public boolean toBoolean(String v)
	{
		return "1".equals(v);
	}
	
}
