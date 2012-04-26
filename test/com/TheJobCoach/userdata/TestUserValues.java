package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;

public class TestUserValues {
	
	static UserValues values = new UserValues();
	
	static UserId id = new UserId("uservalues", "tokenuservalues", UserId.UserType.USER_TYPE_SEEKER);
	
	protected void setUp() throws CassandraException	
	{
		// Clear...
		values.deleteUser(id);
		System.out.println("Deleted user...");
	}
	
	@Test
	public void testFetchUserValuesFromVoid() throws CassandraException, SystemException
	{
		Map<String, String> val = values.getValues(id, "test1");
		assertEquals(val.size(), 2);
		assertEquals("", val.get("test1.test1"));
		assertEquals("", val.get("test1.test2"));

		val = values.getValues(id, "test2");
		assertEquals(val.size(), 2);
		assertEquals("", val.get("test2.test1"));
		assertEquals("", val.get("test2.test2"));

		val.clear();
		boolean excp = false;
		try
		{
			val = values.getValues(id, "test3");
		}
		catch (SystemException e)
		{
			excp = true;
		}
		assertEquals(0, val.size());
		assertEquals(excp, true);
	}
	
	@Test
	public void testSetUserValues() throws CassandraException, SystemException
	{
		Map<String,String> toSet = new HashMap<String, String>();
		values.setValues(id, toSet); // No throw
		toSet.put("test3.toot", "ff");
		boolean excp = false;
		try 
		{
			values.setValues(id, toSet);
		}
		catch (SystemException e)
		{
			excp = true;
		}
		assertEquals(excp, true);
		
		
		excp = false;
		toSet.clear();
		toSet.put("test1.test1", null);
		try 
		{
			values.setValues(id, toSet);
		}
		catch (SystemException e)
		{
			excp = true;
		}
		assertEquals(excp, true);
		
		excp = false;
		toSet.clear();
		String v = "";
		for (int i = 0; i < 200; i++) v+="i";
		toSet.put("test1.test1", v);
		try 
		{
			values.setValues(id, toSet);
		}
		catch (SystemException e)
		{
			excp = true;
		}
		assertEquals(excp, true);
		
		excp = false;
		toSet.clear();
		toSet.put("test1.test1", "value1");
		toSet.put("test1.test2", "value2");
		try 
		{
			values.setValues(id, toSet);
		}
		catch (SystemException e)
		{
			excp = true;
		}
		assertEquals(excp, false);
					
	}
	
}
