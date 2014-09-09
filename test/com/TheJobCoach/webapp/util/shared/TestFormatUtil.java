package com.TheJobCoach.webapp.util.shared;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.util.shared.FormatUtil.PERIOD_TYPE;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class TestFormatUtil extends GwtTest {

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
		assertEquals(d, getDate(2012,05,06,01,02,03));
	}
	
	@Test
	public void testGetDateString()
	{
		String s = FormatUtil.getDateString(getDate(2012,05,06,01,02,03));
		assertEquals(s, "2012_05_06_01_02_03");
	}
	
	@Test
	public void testStartOfTheDay()
	{
		Date s = FormatUtil.startOfTheDay(getDate(2012,05,06,01,02,03));
		assertEquals(s, getDate(2012,05,06,00,00,00));
	}
	
	@Test
	public void testEndOfTheDay()
	{
		Date s = FormatUtil.endOfTheDay(getDate(2012,05,06,01,02,03));
		assertEquals(s, getDate(2012,05,06,23,59,59));
	}
	
	void checkExpectGetPeriod(PERIOD_TYPE period, int count, Date current, Date periodStart, Date periodEnd)
	{
		Date checkStart = (Date)periodStart.clone();
		Date checkEnd = (Date)periodEnd.clone();
		FormatUtil.getPeriod(period, count, current, checkStart, checkEnd);
		System.out.println(current + " " + periodStart + " " + checkStart);
		System.out.println(current + " " + periodEnd + " " + checkEnd);
		assertTrue(CoachTestUtils.isDateEqualForDay(checkStart, periodStart));
		assertTrue(CoachTestUtils.isDateEqualForDay(checkEnd, periodEnd));
	}
	
	@Test
	public void testGetPeriod()
	{
		Date current = CoachTestUtils.getDate(2013, 11, 6);
		
		checkExpectGetPeriod(PERIOD_TYPE.PERIOD_TYPE_WEEK, 0, current, CoachTestUtils.getDate(2013, 11, 4), CoachTestUtils.getDate(2013, 11, 10));
		checkExpectGetPeriod(PERIOD_TYPE.PERIOD_TYPE_WEEK, -1, current, CoachTestUtils.getDate(2013, 10, 28), CoachTestUtils.getDate(2013, 11, 3));
		checkExpectGetPeriod(PERIOD_TYPE.PERIOD_TYPE_WEEK, 1, current, CoachTestUtils.getDate(2013, 11, 11), CoachTestUtils.getDate(2013, 11, 17));
		
		checkExpectGetPeriod(PERIOD_TYPE.PERIOD_TYPE_2WEEKS, 0, current, CoachTestUtils.getDate(2013, 10, 28), CoachTestUtils.getDate(2013, 11, 10));
		checkExpectGetPeriod(PERIOD_TYPE.PERIOD_TYPE_2WEEKS, -1, current, CoachTestUtils.getDate(2013, 10, 14), CoachTestUtils.getDate(2013, 10, 27));
		checkExpectGetPeriod(PERIOD_TYPE.PERIOD_TYPE_2WEEKS, 1, current, CoachTestUtils.getDate(2013, 11, 11), CoachTestUtils.getDate(2013, 11, 24));
		
		checkExpectGetPeriod(PERIOD_TYPE.PERIOD_TYPE_MONTH, 0, current, CoachTestUtils.getDate(2013, 11, 1), CoachTestUtils.getDate(2013, 11, 30));
		checkExpectGetPeriod(PERIOD_TYPE.PERIOD_TYPE_MONTH, -1, current, CoachTestUtils.getDate(2013, 10, 1), CoachTestUtils.getDate(2013, 10, 31));
		checkExpectGetPeriod(PERIOD_TYPE.PERIOD_TYPE_MONTH, 1, current, CoachTestUtils.getDate(2013, 12, 1), CoachTestUtils.getDate(2013, 12, 31));
	 
		current = CoachTestUtils.getDate(2013, 11, 26);
		checkExpectGetPeriod(PERIOD_TYPE.PERIOD_TYPE_WEEK, 0, current, CoachTestUtils.getDate(2013, 11, 25), CoachTestUtils.getDate(2013, 12, 1));
	}
	
	@Test
	public void test_dateAddDays()
	{
		Date current = CoachTestUtils.getDate(2013, 11, 6);
		Date currentPlus3 = FormatUtil.dateAddDays(current, 6);
		
		assertTrue(CoachTestUtils.isDateEqualForDay(currentPlus3, CoachTestUtils.getDate(2013, 11, 12)));
	}
	
	@Test
	public void test_getFormattedDate()
	{
		Date current = CoachTestUtils.getDate(2013, 11, 6);
		String formatted = FormatUtil.getFormattedDate(current);
		
		assertEquals("2013 Nov 6", formatted);
	}
	
	
}
