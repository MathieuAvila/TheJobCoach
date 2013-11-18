package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestUtilSecurity
{
	@Test
	public void testSalt()
	{
		String s1 = UtilSecurity.getSalt();
		assertTrue(s1.length() > 32);
		String s2 = UtilSecurity.getSalt();
		assertTrue(s2.length() > 32);
		assertTrue(!s1.equals(s2));
	}	

	@Test
	public void testHash()
	{
		String password = "password";
		String s1 = UtilSecurity.getHash(password);
		System.out.println(s1);
		assertTrue(s1.length() > 32);
		String s2 = UtilSecurity.getHash(password);
		assertTrue(s2.length() > 32);
		assertTrue(s1.equals(s2));
	}
	
	@Test
	public void testCompare()
	{
		String password = "password";
		String s = UtilSecurity.getSalt();
		String r = UtilSecurity.getHash(s + password);
		assertTrue(UtilSecurity.compareHashedSaltedPassword(password, s, r));
	}
}
