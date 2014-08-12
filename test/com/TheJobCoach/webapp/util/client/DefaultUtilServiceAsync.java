package com.TheJobCoach.webapp.util.client;

import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import com.TheJobCoach.userdata.UserValuesCore;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.slf4j.Logger;

public class DefaultUtilServiceAsync extends UserValuesCore implements UtilServiceAsync
{
	public Map<String, String> values = new HashMap<String, String>();
	
	static Logger logger = LoggerFactory.getLogger(DefaultUtilServiceAsync.class);

	public int calls = 0;
	
	 {
		for (FieldDefinition field: keys)
		{
			addValue(field.getName(), field.getDefaultValue());
		}
	}
	
	public void addValue(String key, String value)
	{
		values.put(key, value);
	}
	public void addValue(String key)
	{
		values.put(key, "");
	}
	
	@Override
	public void getValues(UserId user, String rootValue,
			AsyncCallback<Map<String, String>> callback) {
		assert(user != null);
		HashMap<String, String> hm = new HashMap<String, String>();
		for (String k : values.keySet())
		{
			if (k.startsWith(rootValue)) hm.put(k, values.get(k));
		}
		logger.info("getValues root " + rootValue);
		logger.info("getValues result " + hm);
		calls++;
		callback.onSuccess(hm);
	}
	
	@Override
	public void setValues(UserId user, Map<String, String> map,
			AsyncCallback<String> callback)
	{
		for (String k: map.keySet())
		{
			if (values.containsKey(k)) 
			{
				values.put(k, map.get(k));
				logger.info("Set " + k + " to: " + map.get(k));
			}
			else
			{
				logger.info("Unknown key: " + k);
			}
		}
		calls++;
		callback.onSuccess("");
	}
	
	@Override
	public void sendUpdateList(UserId user, UpdateRequest request,
			AsyncCallback<UpdateResponse> callback)
	{
		callback.onSuccess(new UpdateResponse());
	}
};