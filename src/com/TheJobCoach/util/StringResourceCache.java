package com.TheJobCoach.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringResourceCache {

	HashMap<String, String> cache = new HashMap<String, String>();
	static private StringResourceCache localObj = new StringResourceCache();
	static Logger logger = LoggerFactory.getLogger(StringResourceCache.class);

	static public StringResourceCache getInstance()
	{
		return localObj;
	}
	
	static public String getStringResource(String key)
	{
		if (localObj.cache.containsKey(key))
		{
			return localObj.cache.get(key);
		};
		logger.info("Load file in cache from " + key);
		InputStream contentFile = localObj.getClass().getResourceAsStream(key);
		try
		{
			String myString = IOUtils.toString(contentFile, "UTF-8");
			localObj.cache.put(key, myString);
			return myString;
		}
		catch (IOException e)
		{
			return "";
		}
	}

	// for test purposes only.
	public void setStringResource(String key, String value)
	{
		cache.put(key, value);
	}
	
	public void clean()
	{
		cache = new HashMap<String, String>();
	}
}
