package com.TheJobCoach.webapp.util.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Panel;

public class ClientUserValuesUtils {

	static HashMap<String, String> valuesCache = new HashMap<String, String>();
	static UtilServiceAsync utilService =  GWT.create(UtilService.class);

	final Panel panel;
	final UserId id;

	public ClientUserValuesUtils(Panel rootPanel, UserId id)
	{
		this.panel = rootPanel;
		this.id = id;
	}

	public static interface ReturnValue
	{
		public void notifyValue(boolean set, String key, String value);
	}
	
	static HashMap<String, Vector<ReturnValue>> notifyValuesChange = new HashMap<String, Vector<ReturnValue>>();
	
	static private void insertKeys(Map<String, String> values, boolean set)
	{
		valuesCache.putAll(values);
		for (String key: values.keySet())
		{
			if (notifyValuesChange.containsKey(key))
			{
				Vector<ReturnValue> notifyList = notifyValuesChange.get(key);
				for (ReturnValue notify: notifyList) notify.notifyValue(set, key, values.get(key));
			}
		}
	}

	public String getValueFromCache(String key)
	{
		return valuesCache.get(key);
	}

	public void addListener(String key, ReturnValue listener)
	{
		Vector<ReturnValue> notifyList = notifyValuesChange.get(key);
		if (notifyList == null) notifyList = new Vector<ReturnValue>();
		notifyList.add(listener);		
		notifyValuesChange.put(key, notifyList);
	}
	
	public void preloadValueList(final String key, final ReturnValue result)
	{
		ServerCallHelper<Map<String,String>> callback = new ServerCallHelper<Map<String,String>>(panel) {
			@Override
			public void onSuccess(Map<String,String> resultMap) {
				insertKeys(resultMap, false);
				if (result != null)
				{
					for (String key: resultMap.keySet())
						result.notifyValue(false, key, resultMap.get(key));					
				}
			}
		};
		try
		{
			utilService.getValues(id, key, callback);
		}
		catch (CassandraException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SystemException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (CoachSecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setValues(final HashMap<String, String> map, final ReturnValue result)
	{
		ServerCallHelper<String> callback = new ServerCallHelper<String>(panel) {
			@Override
			public void onSuccess(String resultMap) {
				if (result != null)
				{
					for (String key: map.keySet())
						result.notifyValue(true, key, map.get(key));
				}
			}
		};
		insertKeys(map, true);
		try
		{
			utilService.setValues(id, map, callback);
		}
		catch (CassandraException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SystemException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (CoachSecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public void setValue(String key, String value)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(key, value);
		setValues(map, null);
	}	

}
