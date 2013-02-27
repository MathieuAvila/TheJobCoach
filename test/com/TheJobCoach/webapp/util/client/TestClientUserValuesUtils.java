package com.TheJobCoach.webapp.util.client;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.util.Util")
public class TestClientUserValuesUtils extends GwtTest 
{

	static ClientUserValuesUtils values = null;
	
	/**
	 * Must refer to a valid module that sources this class.
	 */
	public String getModuleName() 
	{                         
		return "com.TheJobCoach.webapp.util.Util";
	}
	
	@Before
	public void beforeClientUserValuesUtils()
	{		
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				System.out.println(arg0.getCanonicalName());
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.util.client.UtilService"))
				{
					return new DefaultUtilServiceAsync();
				}
				return null;
			}}
		);
		VerticalPanel vp = new VerticalPanel();
		values = new ClientUserValuesUtils(vp, new UserId("user", "", UserType.USER_TYPE_SEEKER));
	};
	
	@Test
	public void testGetMultipleValues()
	{		
		final HashMap<String, String> result = new HashMap<String, String>();
		values.preloadValueList("ACCOUNT", new ReturnValue() {
			@Override
			public void notifyValue(boolean set, String key, String value) {
				System.out.println("SET " + set + " KEY " + key + " VALUE " + value);
				result.put(key, value);
			}});
		assertEquals(9, result.size());
	}
	
	@Test
	public void testSetValues()
	{
		final Vector<String> count = new Vector<String>();
		final Vector<String> count2 = new Vector<String>();
		values.addListener("BAD", new ReturnValue() {
			public void notifyValue(boolean set, String key, String value) {
				System.out.println("Notify 1");
				count.add(key);
			}});
		values.addListener("ACCOUNT_MODEL", new ReturnValue() {
			public void notifyValue(boolean set, String key, String value) {
				System.out.println("Notify 2");
				count.add(key);
			}});
		values.addListener("ACCOUNT_MODEL", new ReturnValue() {
			public void notifyValue(boolean set, String key, String value) {
				System.out.println("Notify 3");
				count2.add(key);
			}});
		values.setValue("ACCOUNT_MODEL", "TATA");
		assertEquals(1, count.size());
		assertEquals(1, count2.size());
	}
	
}
