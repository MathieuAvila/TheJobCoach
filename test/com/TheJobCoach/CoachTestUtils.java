package com.TheJobCoach;
import java.util.Date;


public class CoachTestUtils
{
	@SuppressWarnings("deprecation")
	public static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
}
