package com.TheJobCoach.webapp.util.shared;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class TestSiteUUID {
	
	@SuppressWarnings("deprecation")
	Date getDate(int year, int month, int day)
	{
		Date result = new Date(0);
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}

	@Test
	public void testGetUUID()
	{
		String d = SiteUUID.dateFormatter(getDate(1970, 2, 5));
		System.out.println(d);
		assertEquals(true, d.contains("1970-03-05"));
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetDateUuid()
	{
		Date cur = new Date();
		String d = SiteUUID.getDateUuid();
		System.out.println(d);
		assertEquals(true, d.contains(String.valueOf(cur.getYear() + 1900)));		
	}
	
}
