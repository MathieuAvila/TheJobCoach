package com.TheJobCoach.webapp.util.client;

import java.util.HashMap;
import java.util.Map;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

	static private void insertKeys(Map<String, String> values)
	{
		valuesCache.putAll(values);
	}

	public String getValueFromCache(String key)
	{
		return valuesCache.get(key);
	}

	public void preloadValueList(final String key, final ReturnValue result)
	{
		AsyncCallback<Map<String,String>> callback = new AsyncCallback<Map<String,String>>() {
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.messageBoxException(panel, caught.toString());
			}
			@Override
			public void onSuccess(Map<String,String> resultMap) {
				insertKeys(resultMap);
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
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.messageBoxException(panel, caught.toString());
			}
			@Override
			public void onSuccess(String resultMap) {
				if (result != null)
				{
					for (String key: map.keySet())
						result.notifyValue(true, key, map.get(key));
				}
			}
		};
		insertKeys(map);
		utilService.setValues(id, map, callback);
	}	

	public void setValue(String key, String value)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		setValues(map, null);
	}	

}
