package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import org.junit.Test;

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
}
