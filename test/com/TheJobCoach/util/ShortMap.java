package com.TheJobCoach.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ShortMap {
	
	Map<String, String> map = new HashMap<String,String>();
	
	public ShortMap add(String k, String v)
	{
		map.put(k, v);
		return this;
	}
	
	public Map<String, String> get()
	{
		return map;
	}
	
	public ShortMap add(String k, boolean v)
	{
		if (v) 
			map.put(k, "1");
		else 
			map.put(k, "0");
		return this;
	}
	
	public ShortMap add(String k, Date v)
	{
		map.put(k, new Long(v.getTime()).toString());		
		return this;
	}
	
	public static boolean getBoolean(String v)
	{
		return v.equals("1");
	}
	
	public static Date getDate(String v)
	{
		Date d = new Date(Long.decode(v));
		return d;
	}
}
