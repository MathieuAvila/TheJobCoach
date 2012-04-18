package com.TheJobCoach.webapp.mainpage.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestUserId {
	
	@Test
	public void testCheckUserName()
	{
		assertEquals(false, UserId.checkUserName(null));
		assertEquals(false, UserId.checkUserName(""));
		assertEquals(false, UserId.checkUserName("a*a"));
		assertEquals(false, UserId.checkUserName("a a"));
		assertEquals(true, UserId.checkUserName("abc"));
	}
	
}
