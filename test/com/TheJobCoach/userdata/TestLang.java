package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.text.DateFormat;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;

public class TestLang
{
	@Test
	public void getTestName()
	{
		assertEquals(Lang.getTestName("fr"), "Jean");
		assertEquals(Lang.getTestName("en"), "John");
		assertEquals(Lang.getTestName(""), "Jean");
		assertEquals(Lang.getTestName(null), "Jean");
	}	
	
	@Test
	public void test_getDateFormat()
	{
		DateFormat df = Lang.getDateFormat("en");
		assertEquals("Jan 2, 2014", df.format(CoachTestUtils.getDate(2014, 1, 2)));
		df = Lang.getDateFormat("FR");
		assertEquals("2 janv. 2014", df.format(CoachTestUtils.getDate(2014, 1, 2)));
	}
}
