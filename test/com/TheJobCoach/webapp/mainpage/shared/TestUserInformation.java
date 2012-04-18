package com.TheJobCoach.webapp.mainpage.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestUserInformation {
	
	@Test
	public void testCheckEmail()
	{
		assertEquals(false, UserInformation.checkEmail(null));
		assertEquals(false, UserInformation.checkEmail(""));
		assertEquals(false, UserInformation.checkEmail("a*a"));
		assertEquals(false, UserInformation.checkEmail("aa"));
		assertEquals(false, UserInformation.checkEmail("aa@"));
		assertEquals(false, UserInformation.checkEmail("@aa"));
		assertEquals(true, UserInformation.checkEmail("aa.aa@aa.aa"));
	}
	
}
