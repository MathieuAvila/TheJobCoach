package com.TheJobCoach.webapp.util.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class ClientUserValuesUtils {

	HashMap<String, String> valuesCache = new HashMap<String, String>();
	UtilServiceAsync utilService =  GWT.create(UtilService.class);

	final UserId id;

	private ClientUserValuesUtils(UserId id)
	{
		this.id = id;
	}
	
	static public Map<String, ClientUserValuesUtils> instance = null;

	public static ClientUserValuesUtils getInstance(UserId id)
	{
		if (instance == null) instance = new HashMap<String, ClientUserValuesUtils>();
		if (!instance.containsKey(id.userName)) 
			instance.put(id.userName, new ClientUserValuesUtils(id));
		return instance.get(id.userName);
	}

	public interface ReturnValue
	{
		public void notifyValue(boolean set, String key, String value);
	}
	
	HashMap<String, Vector<ReturnValue>> notifyValuesChange = new HashMap<String, Vector<ReturnValue>>();
	
	private void insertKeys(Map<String, String> values, boolean set)
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
		ServerCallHelper<Map<String,String>> callback = new ServerCallHelper<Map<String,String>>(RootPanel.get()) {
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
		utilService.getValues(id, key, callback);
	}

	public void setValues(final HashMap<String, String> map, final ReturnValue result)
	{
		ServerCallHelper<String> callback = new ServerCallHelper<String>(RootPanel.get()) {
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
		utilService.setValues(id, map, callback);
	}	

	public void callbackServerSetValues(Map<String, String> map)
	{
		if (map != null && map.size() != 0)
		{
			System.out.println("Received keys update: " + map.toString());
			insertKeys(map, true);
		}
	}	

	public void setValue(String key, String value)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(key, value);
		setValues(map, null);
	}	

}
