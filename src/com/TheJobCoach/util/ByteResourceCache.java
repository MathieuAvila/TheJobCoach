package com.TheJobCoach.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteResourceCache {

	HashMap<String, byte[]> cache = new HashMap<String, byte[]>();
	static private ByteResourceCache localObj = new ByteResourceCache();
	static Logger logger = LoggerFactory.getLogger(ByteResourceCache.class);

	static public ByteResourceCache getInstance()
	{
		return localObj;
	}
	
	static public byte[] getByteResource(String key)
	{
		if (localObj.cache.containsKey(key))
		{
			return localObj.cache.get(key);
		};
		logger.info("Load file in cache from " + key);
		InputStream contentFile = localObj.getClass().getResourceAsStream(key);
		try
		{
			byte[] myString = IOUtils.toByteArray(contentFile);
			localObj.cache.put(key, myString);
			return myString;
		}
		catch (IOException e)
		{
			logger.warn("Failed to load file in cache from " + key);
			return new byte[0];
		}
	}

	// for test purposes only.
	public void setByteResource(String key, byte[] value)
	{
		cache.put(key, value);
	}
	
	public void clean()
	{
		cache = new HashMap<String, byte[]>();
	}
}
