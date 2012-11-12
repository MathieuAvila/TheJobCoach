package com.TheJobCoach.webapp.util.shared;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class TestFormatUtil {

	@SuppressWarnings("deprecation")
	static public Date getDate(int year, int month, int day, int hours, int minutes, int seconds)
	{
		Date result = new Date(0);
		result.setHours(hours);
		result.setMinutes(minutes);
		result.setSeconds(seconds);
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}

	@Test
	public void testGetStringDate()
	{
		Date d = FormatUtil.getStringDate("2012_05_06_01_02_03");
		System.out.println(d);
		assertEquals(d, getDate(2012,05,06,01,02,03));
	}
	
	@Test
	public void testGetDateString()
	{
		String s = FormatUtil.getDateString(getDate(2012,05,06,01,02,03));
		System.out.println(s);
		assertEquals(s, "2012_05_06_01_02_03");
	}
	
	@Test
	public void testStartOfTheDay()
	{
		Date s = FormatUtil.startOfTheDay(getDate(2012,05,06,01,02,03));
		System.out.println(s);
		assertEquals(s, getDate(2012,05,06,00,00,00));
	}
	
	@Test
	public void testEndOfTheDay()
	{
		Date s = FormatUtil.endOfTheDay(getDate(2012,05,06,01,02,03));
		System.out.println(s);
		assertEquals(s, getDate(2012,05,06,23,59,59));
	}
	
}
