package com.TheJobCoach;
import java.util.Date;


public class CoachTestUtils
{
	@SuppressWarnings("deprecation")
	public static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month - 1);
		result.setYear(year - 1900);
		return result;
	}

	@SuppressWarnings("deprecation")
	public static boolean isDateEqualForDay(Date d1, Date d2)
	{
		System.out.println(
				d1.getDate() + " " + d2.getDate() 
				+ " - "	+ d1.getMonth() + " " + d2.getMonth()
				+ " - " + d1.getYear() + " " + d2.getYear());
		return (d1.getDate() == d2.getDate())
				&& (d1.getMonth() == d2.getMonth()) 
				&& (d1.getYear() == d2.getYear());
	}
	
}
