package com.TheJobCoach.webapp.util.shared;

import java.util.Date;

public class FormatUtil 
{

	public static String trueString = "1";
	public static String falseString = "0";

	public enum PERIOD_TYPE 
	{ 
		PERIOD_TYPE_WEEK,
		PERIOD_TYPE_2WEEKS,
		PERIOD_TYPE_MONTH
	};

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
		Date r = new Date(0);
		r.setYear(d.getYear());
		r.setMonth(d.getMonth());
		r.setDate(d.getDate());
		r.setHours(0);
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

	final static long MS_PER_DAY = 1000 * 60 * 60 * 24;
	final static long MS_PER_WEEK = MS_PER_DAY * 7;

	@SuppressWarnings("deprecation")
	public static void getPeriod(PERIOD_TYPE type, int count, Date current, Date periodStart, Date periodEnd)
	{
		switch (type)
		{
		case PERIOD_TYPE_MONTH:
		{
			periodStart.setTime(current.getTime());
			periodStart.setDate(1);
			periodStart.setTime(startOfTheDay(periodStart).getTime());
			periodStart.setMonth(periodStart.getMonth() + count);
			periodEnd.setTime(periodStart.getTime());
			periodEnd.setMonth(periodEnd.getMonth() + 1);
			periodEnd.setTime(periodEnd.getTime() - 1);
		}
		break;
		case PERIOD_TYPE_2WEEKS:
		case PERIOD_TYPE_WEEK:
		{
			int interval = (type == PERIOD_TYPE.PERIOD_TYPE_WEEK) ? 1 : 2;
			Date fixedDate = getStringDate("2013_10_11_0_0_0");
			long fixedRef = fixedDate.getTime();
			long currentRef = current.getTime();

			long diffWeek = (currentRef - fixedRef) / MS_PER_WEEK + ((currentRef - fixedRef) < 0 ? -1*interval : 0);

			long boundary = diffWeek % interval;

			long startWeekMs = fixedRef + diffWeek * MS_PER_WEEK;
			long startPeriodMs = startWeekMs + (count * interval - boundary ) * MS_PER_WEEK;
			long endPeriodMs = startPeriodMs + interval * MS_PER_WEEK;
			periodStart.setTime(startPeriodMs);
			periodEnd.setTime(endPeriodMs -1);
		}
		break;
		default:
			break;
		}
	}

	static public Integer getIntegerFromString(String v)
	{
		Integer r = null;
		if (v == null) return null;
		try {
			r = Integer.parseInt(v);
		} catch (Exception e)	{}
		return r;			
	}
	
	static public Date dateAddDays(Date current, int days)
	{
		return new Date(current.getTime() + FormatUtil.MS_PER_DAY  * days);
	}
}
