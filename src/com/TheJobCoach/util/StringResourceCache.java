package com.TheJobCoach.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringResourceCache {

	static HashMap<String, String> cache = new HashMap<String, String>();
	static private StringResourceCache localObj = new StringResourceCache();
	static Logger logger = LoggerFactory.getLogger(StringResourceCache.class);

	static public String getStringResource(String key)
	{
		if (cache.containsKey(key))
		{
			return cache.get(key);
		};
		logger.info("Load file in cache from " + key);
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
