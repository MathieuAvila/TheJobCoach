package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.google.gwt.junit.client.GWTTestCase;

public class TestClientUserValuesUtils extends GWTTestCase 
{

	static ClientUserValuesUtils values = new ClientUserValuesUtils(null, new UserId("user", "", UserType.USER_TYPE_SEEKER));
	
	/**
	 * Must refer to a valid module that sources this class.
	 */
	public String getModuleName() 
	{                         
		return "com.TheJobCoach.webapp.util.Util";
	}
	
	
	public void testGetMultipleValues()
	{                                            
		values.preloadValueList("", new ReturnValue() {

			@Override
			public void notifyValue(boolean set, String key, String value) {
				System.out.println("SET " + set + " KEY " + key + " VALUE " + value);
			}});
	}

	/**
	 * Add as many tests as you like.
	 */
	public void testSimple() 
	{                                            
		assertTrue(true);
	}

}
