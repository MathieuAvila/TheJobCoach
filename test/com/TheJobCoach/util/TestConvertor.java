package com.TheJobCoach.util;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;

public class TestConvertor
{

	@Test
	public void testToString()
	{
		assertEquals("v", Convertor.toString("v", "nop"));
		assertEquals("nop", Convertor.toString(null, "nop"));
		assertEquals("", Convertor.toString((String)null));
		assertEquals("v", Convertor.toString("v"));
	}
	
	@Test
	public void testToDate()
	{
		Date today = new Date();
		Date oneDay = CoachTestUtils.getDate(2000, 10, 15);
		Date anotherDay = CoachTestUtils.getDate(2001, 10, 15);
		assertTrue(CoachTestUtils.isDateEqualForDay(oneDay, Convertor.toDate(Convertor.toString(oneDay))));
		assertTrue(CoachTestUtils.isDateEqualForDay(today, Convertor.toDate(null)));

		assertTrue(CoachTestUtils.isDateEqualForDay(oneDay, Convertor.toDate(Convertor.toString(oneDay), anotherDay)));
		assertTrue(CoachTestUtils.isDateEqualForDay(anotherDay, Convertor.toDate(null, anotherDay)));
		
		assertEquals("0", Convertor.toString((Date)null));
	}
	
	@Test
	public void testToBoolean()
	{
		assertEquals(true, Convertor.toBoolean("1"));
		assertEquals(false, Convertor.toBoolean("0"));
		assertEquals(false, Convertor.toBoolean(null));
		assertEquals(false, Convertor.toBoolean("popo"));

		assertEquals(true, Convertor.toBoolean("1", true));
		assertEquals(false, Convertor.toBoolean("0", true));
		assertEquals(true, Convertor.toBoolean(null, true));
		assertEquals(false, Convertor.toBoolean("popo", true));
	}
	
	@Test
	public void testToInt()
	{
		assertEquals(1, Convertor.toInt("1"));
		assertEquals(100, Convertor.toInt("100"));
		assertEquals(0, Convertor.toInt(null));
		assertEquals(0, Convertor.toInt("popo"));
	}

	@Test
	public void testToDouble()
	{
		assertEquals(1.0, Convertor.toDouble("1"), 0.1);
		assertEquals(100.0, Convertor.toDouble("100"), 0.0);
		assertEquals(0.0, Convertor.toDouble(null), 0.0);
		assertEquals(0.0, Convertor.toDouble("popo"), 0.0);
		assertEquals(1.12, Convertor.toDouble("1.12"), 0.1);
	}
	
	@Test
	public void testGetDateStringUnique()
	{
		Date today = new Date();
		Date d = CoachTestUtils.getDate(2000,31,12);
		String prev="";
		for (int i=0; i != 1000; i++)
			{
			String r = Convertor.getDateStringUnique(d);
			assertTrue(!r.equals(prev));
			CoachTestUtils.isDateEqualForDay(d, Convertor.getStringDateUnique(r));
			prev = r;
			}
		assertTrue(CoachTestUtils.isDateEqualForDay(today, Convertor.getStringDateUnique(null)));
		assertEquals("", Convertor.getDateStringUnique(null));
		
		//Coverage, no assertion
		Convertor.getStringDateUnique("");
		new Convertor();
	}
}
