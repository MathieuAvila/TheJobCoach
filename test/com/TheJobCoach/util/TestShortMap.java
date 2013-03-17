package com.TheJobCoach.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

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
	
	@Test
	public void test_AddMap_GetMap()
	{
		HashMap<String, String> map1 = new HashMap<String, String>(ImmutableMap.of(
	        "b", "b_v",
	        "a", "a_v",
	        "c", "c_v"
	        ));
		HashMap<String, String> map2 = new HashMap<String, String>(ImmutableMap.of(
		        "b", "b_v",
		        "a", "a_v",
		        "c", "c3_v",
		        "d", "d_v"
		        ));
		
		HashMap<String, String> map_void = new HashMap<String, String>();
		
		ShortMap sm = new ShortMap();
		sm.addMap("str1", map1);
		sm.addMap("str2", map2);
		sm.addMap("str3", map_void);

		HashMap<String, String> t1_result = sm.getMap("str1");
		assertEquals(t1_result, map1);
		
		HashMap<String, String> t2_result = sm.getMap("str2");
		assertEquals(t2_result, map2);
		
		HashMap<String, String> t_void = sm.getMap("str3");
		assertNotNull(t_void);
		assertEquals(0, t_void.size());

		t_void = sm.getMap("str_null");
		assertNotNull(t_void);
		assertEquals(0, t_void.size());
	}

}
