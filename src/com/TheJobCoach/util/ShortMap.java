package com.TheJobCoach.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ShortMap {
	
	Map<String, String> map = new HashMap<String,String>();
	
	public ShortMap add(String k, String v)
	{
		if (v != null)
			map.put(k, v);
		else
			map.put(k, "");
		return this;
	}
	
	public ShortMap add(String k, byte[] v)
	{
		if (v != null)
			map.put(k, v.toString());
		else
			map.put(k, "");
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
	
	public ShortMap add(String k, int v)
	{
		map.put(k, Integer.toString(v));		
		return this;
	}

	public ShortMap add(String k, double v)
	{
		System.out.println("Double: " + v + " converted to: " + Double.toString(v));	
		map.put(k, Double.toString(v));		
		return this;
	}
	
	public ShortMap add(String k, Date v)
	{
		if (v != null)
			map.put(k, new Long(v.getTime()).toString());
		else
			map.put(k, new Long(new Date().getTime()).toString());
		return this;
	}
	
	public static boolean getBoolean(String v)
	{
		return v.equals("1");
	}
	
	public static boolean getBoolean(String v, boolean def)
	{
		if (v == null) return def;
		return v.equals("1");
	}
	
	public static Date getDate(String v)
	{
		Date d = new Date(Long.decode(v));
		return d;
	}
	
	public Vector<String> getVector(String v)
	{
		Vector<String> result = new Vector<String>();
		String countStr = map.get(v);
		int total = Convertor.toInt(countStr);
		for (int c = 0; c != total; c++)
		{
			String tmp = map.get(v + "#" + String.valueOf(c));
			if (tmp == null) tmp = "";
			result.add(tmp);
		}
		return result;
	}
	
	public ShortMap addVector(String v, Vector<String> list)
	{
		map.put(v, Integer.toString(list.size()));
		int count = 0;
		for (String str: list)
		{
			map.put(v + "#" + Integer.toString(count), str);
			count++;
		}
		return this;
	}
}
