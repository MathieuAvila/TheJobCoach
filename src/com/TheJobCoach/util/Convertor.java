package com.TheJobCoach.util;

import java.util.Date;
import java.util.Random;

public final class Convertor {

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

	static public String toString(Date val)
	{
		if (val != null) return String.valueOf(val.getTime());
		return String.valueOf(0);
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
			Date d = new Date(new Long(val));
			return d;
		}
		return def;
	}

	static public int toInt(String string) {
		if (string == null) return 0;
		try
		{
			return Integer.decode(string);
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	static public double toDouble(String string) {
		if (string == null) return 0;
		try
		{
		return Float.valueOf(string);
		}
		catch (Exception e)
		{
			return 0.0;
		}
	}

	static public boolean toBoolean(String v)
	{
		return "1".equals(v);
	}

	static public String toString(boolean b)
	{
		return (b ? "1":"0");
	}
	
	static public boolean toBoolean(String v, boolean defaultValue)
	{
		if (v == null) return defaultValue;
		return "1".equals(v);
	}

	public static String fillWithZeroCount(int i, int nbDigit)
	{
		String v = String.valueOf(i);
		while (v.length() < nbDigit) v = "0" + v;
		return v;
	}

	static Random rnd = new Random();

	@SuppressWarnings({ "deprecation" })
	public static String getDateStringUnique(Date d)
	{
		int count = rnd.nextInt(1000000);
		if (d == null) return "";
		return fillWithZeroCount(d.getYear() + 1900 ,4) + "_" + 
		fillWithZeroCount(d.getMonth(),2) + "_" +
		fillWithZeroCount(d.getDate(), 2) + "_" +
		fillWithZeroCount(d.getHours(),2) + "_" +
		fillWithZeroCount(d.getMinutes(),2) + "_" +
		fillWithZeroCount(d.getSeconds(),2) + "_" +
		fillWithZeroCount(count, 6);
	}

	public static int[] readIntegersFromString(String s)
	{
		String[] st = s.split("_");
		int[] result = new int[st.length];
		for (int count = 0; count != st.length; count++)
		{
			try{
				result[count] = Integer.parseInt(st[count]);
			}
			catch(Exception e)
			{
				result[count] = 0;
			}
		}
		return result;
	}

	static int getIntFromIndex(int[] table, int index)
	{
		if (index < table.length) return table[index];
		return 0;
	}

	@SuppressWarnings({ "deprecation" })
	public static Date getStringDateUnique(String s)
	{
		if (s == null) return new Date();
		int[] table = readIntegersFromString(s);
		Date d = new Date();
		d.setYear(getIntFromIndex(table, 0) - 1900);
		d.setMonth(getIntFromIndex(table, 1));
		d.setDate(getIntFromIndex(table, 2));
		d.setHours(getIntFromIndex(table, 3));
		d.setMinutes(getIntFromIndex(table, 4));
		d.setSeconds(getIntFromIndex(table, 5));
		return d;
	}


}
