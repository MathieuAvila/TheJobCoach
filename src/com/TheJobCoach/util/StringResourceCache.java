package com.TheJobCoach.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

public class StringResourceCache {

	static HashMap<String, String> cache = new HashMap<String, String>();
	static private StringResourceCache localObj = new StringResourceCache();
	
	static public String getStringResource(String key)
	{
		if (cache.containsKey(key))
		{
			return cache.get(key);
		};
		InputStream contentFile = localObj.getClass().getResourceAsStream(key);
		try
		{
			String myString = IOUtils.toString(contentFile, "UTF-8");
			cache.put(key, myString);
			return myString;
		}
		catch (IOException e)
		{
			return "";
		}
	}

}
