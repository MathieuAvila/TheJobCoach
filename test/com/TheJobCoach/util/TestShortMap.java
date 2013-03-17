package com.TheJobCoach.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Vector;

import org.junit.Test;

public class TestShortMap
{
/*
	@Test
	public void testAddStringString()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testAddStringByteArray()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGet()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testAddStringBoolean()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testAddStringInt()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testAddStringDouble()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testAddStringDate()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetBooleanString()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetBooleanStringBoolean()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetDate()
	{
		fail("Not yet implemented");
	}
*/
	@Test
	public void test_AddVector_GetVector()
	{
		Vector<String> t1 = new Vector<String>(Arrays.asList("str1_1", "str1_2", "str1_3"));
		Vector<String> t2 = new Vector<String>(Arrays.asList("str2_1", "str2_2", "str2_3"));
				
		ShortMap sm = new ShortMap();
		sm.addVector("str1", t1);
		sm.addVector("str2", t2);
		
		Vector<String> t1_result = sm.getVector("str1");
		assertEquals(t1_result, t1);
		
		Vector<String> t2_result = sm.getVector("str2");
		assertEquals(t2_result, t2);
		
		Vector<String> t_void = sm.getVector("void");
		assertNotNull(t_void);
		assertEquals(0, t_void.size());
	}

}
