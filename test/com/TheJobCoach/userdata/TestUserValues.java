package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.userdata.UserValues.FieldDefinition;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;


public class TestUserValues {
	
	static UserValues values = new UserValues();
	
	static UserId id = new UserId("uservalues", "tokenuservalues", UserId.UserType.USER_TYPE_SEEKER);
	
	@Before
	public void setUp() throws CassandraException	
	{
		// Clear...
		values.deleteUser(id);
		
		UserValues.addField(new FieldDefinition("test1.test1"));
		UserValues.addField(new FieldDefinition("test1.test1"));
		UserValues.addField(new FieldDefinition("test1.test2"));
		UserValues.addField(new FieldDefinition("test2.test1"));
		UserValues.addField(new FieldDefinition("test2.test2"));
		UserValues.addField(new FieldDefinition("test4.test4"));
		UserValues.addField(new FieldDefinition("test3.testsystem", 10, false, "DEFAULT"));
		UserValues.addField(new FieldDefinition("test5.testsystem", 10, false, "DEFAULT"));
		UserValues.addField(new FieldDefinition("test5.notserver", 10, true, "DEFAULT"));
	}
	
	// Helper for other tests.
	public static void clean(UserId id) throws CassandraException, SystemException
	{
		values.deleteUser(id);
	}

	// Helper for other tests.
	public static void checkValue(UserId id, String key, String value) throws CassandraException, SystemException
	{
		Map<String, String> setUp = values.getValues(id, key);
		assertEquals(1, setUp.size());
		assertEquals(value, setUp.get(key));
	}
	
	@Test
	public void testFetchUserValuesFromVoid() throws CassandraException, SystemException
	{
		setUp();
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
			val = values.getValues(id, "testvoid");
		}
		catch (SystemException e)
		{
			excp = true;
		}
		assertEquals(0, val.size());
		assertEquals(excp, true);
	}
	
	@Test
	public void testGetUserValuesDefault() throws CassandraException, SystemException
	{
		Map<String, String> val = values.getValues(id, "test3");
		assertEquals(val.size(), 1);
		assertEquals("DEFAULT", val.get("test3.testsystem"));
	}

	@Test
	public void test_getUserValue() throws CassandraException, SystemException
	{
		Map<String,String> toSet = new HashMap<String, String>();
		toSet.put("test1.test1", "DEFAULT_GET_SINGLE");
		values.setValues(id, toSet, true);

		String val = values.getValue(id, "test1.test1");
		assertEquals("DEFAULT_GET_SINGLE", val);
	}

	
	@Test(expected=SystemException.class)
	public void testSetUserValuesNotExist() throws CassandraException, SystemException
	{
		Map<String,String> toSet = new HashMap<String, String>();
		values.setValues(id, toSet, true); // No throw
		toSet.put("test3.toot", "ff");		
		values.setValues(id, toSet, true);		
	}
	
	@Test(expected=SystemException.class)
	public void testSetUserValuesNull() throws CassandraException, SystemException
	{
		Map<String,String> toSet = new HashMap<String, String>();
		values.setValues(id, toSet, true); // No throw
		toSet.put("test1.test1", null);
		values.setValues(id, toSet, true);		
	}
	
	@Test(expected=SystemException.class)
	public void testSetUserValuesTooBig() throws CassandraException, SystemException
	{
		Map<String,String> toSet = new HashMap<String, String>();
		String v = "";
		for (int i = 0; i < 200; i++) v+="i";
		toSet.put("test1.test1", v);
		values.setValues(id, toSet, true);		
	}

	@Test(expected=SystemException.class)
	public void testSetUserValuesSystem() throws CassandraException, SystemException
	{
		Map<String,String> toSet = new HashMap<String, String>();
		String v = "TOTO";
		toSet.put("test3.testsystem", v);
		values.setValues(id, toSet, true);		
	}
		
	@Test
	public void testSetUserValues() throws CassandraException, SystemException
	{
		Map<String,String> toSet = new HashMap<String, String>();
		values.setValues(id, toSet, true); // No throw
		toSet.put("test3.toot", "ff");
		
		toSet.clear();
		toSet.put("test1.test1", "value1");
		toSet.put("test1.test2", "value2");		
		values.setValues(id, toSet, true);
	}
	
	@Test
	public void testSetUserValue() throws CassandraException, SystemException
	{
		values.setValue(id, "test4.test4", "value1", true); // No throw
		Map<String, String> val = values.getValues(id, "test4");
		assertEquals(val.size(), 1);
		assertEquals("value1", val.get("test4.test4"));
	}
	
	@Test
	public void test_getForcedWaitTimeMs() throws InterruptedException
	{
		values.getForcedWaitTimeMs(id, 1000); // Just set current time.
		// now tell me how long to wait.
		Thread.sleep(500);
		long wait = values.getForcedWaitTimeMs(id, 1000);
		System.out.println(wait);
		assertTrue(wait < 500);
		assertTrue(wait > 300);
		Thread.sleep(500);
		wait = values.getForcedWaitTimeMs(id, 1000);
		System.out.println(wait);
		assertTrue(wait < 500);
		assertTrue(wait > 300);
	}
	
	@Test
	public void test_getUpdatedValues() throws CassandraException, SystemException
	{
		values.deleteUser(id);
		Map<String,String> update = values.getUpdatedValues(id);
		assertEquals(0, update.size());
		values.setValue(id, "test3.testsystem", "toto", false);
		values.setValue(id, "test5.testsystem", "toto", false);
		values.setValue(id, "test5.notserver", "toto", true);
		
		update = values.getUpdatedValues(id);
		assertEquals(2, update.size());
		assertTrue(update.containsKey("test3.testsystem"));
		assertTrue(update.containsKey("test5.testsystem"));
		
		update = values.getUpdatedValues(id);
		assertEquals(0, update.size());
	}
}
