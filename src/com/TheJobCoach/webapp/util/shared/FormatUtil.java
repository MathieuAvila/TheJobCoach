package com.TheJobCoach.webapp.util.shared;

import java.util.Date;

public class FormatUtil 
{

	public static String fillWithZeroCount(int i, int nbDigit)
	{
		String v = String.valueOf(i);
		while (v.length() < nbDigit) v = "0" + v;
		return v;
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
	
	@SuppressWarnings({ "deprecation" })
	public static String getDateString(Date d)
	{
		if (d == null) return "";
		return fillWithZeroCount(d.getYear() + 1900 ,4) + "_" + 
				fillWithZeroCount(d.getMonth(),2) + "_" +
				fillWithZeroCount(d.getDate(), 2) + "_" +
				fillWithZeroCount(d.getHours(),2) + "_" +
				fillWithZeroCount(d.getMinutes(),2) + "_" +
				fillWithZeroCount(d.getSeconds(),2);
	}
	
	static int getIntFromIndex(int[] table, int index)
	{
		if (index < table.length) return table[index];
		return 0;
	}
	
	@SuppressWarnings({ "deprecation" })
	public static Date getStringDate(String s)
	{
		if (s == null) return new Date();
		int[] table = readIntegersFromString(s);
		Date d = new Date(0);
		d.setYear(getIntFromIndex(table, 0) - 1900);
		d.setMonth(getIntFromIndex(table, 1));
		d.setDate(getIntFromIndex(table, 2));
		d.setHours(getIntFromIndex(table, 3));
		d.setMinutes(getIntFromIndex(table, 4));
		d.setSeconds(getIntFromIndex(table, 5));
		return d;
	}
	
	@SuppressWarnings("deprecation")
	public static Date startOfTheDay(Date d)
	{
		Date r = new Date(d.getTime());
		r.setHours(0);
		r.setMinutes(0);
		r.setSeconds(0);
		return r;
	}
	
	@SuppressWarnings("deprecation")
	public static Date sameDayTimeAs(Date d)
	{
		Date r = new Date();
		r.setHours(d.getHours());
		r.setMinutes(d.getMinutes());
		r.setSeconds(d.getSeconds());
		return r;
	}

	@SuppressWarnings("deprecation")
	public static Date endOfTheDay(Date d)
	{
		Date r = new Date(d.getTime());
		r.setHours(23);
		r.setMinutes(59);
		r.setSeconds(59);
		return r;
	}
	
	@SuppressWarnings("deprecation")
	public static Date startOfTheUniverse()
	{
		Date r = new Date();
		r.setHours(0);
		r.setMinutes(0);
		r.setSeconds(0);
		r.setYear(0);
		r.setMonth(0);
		r.setDate(1);
		return r;
	}

	public static String trueString = "1";
	public static String falseString = "0";
}
