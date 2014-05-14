package com.TheJobCoach.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ShortMap {
	
	Map<String, String> map;
	
	public ShortMap()
	{
		map = new HashMap<String,String>();
	}
			
	public ShortMap(Map<String,String> from)
	{
		map = from;
	}
	
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
		map.put(k, Convertor.toString(v));
		return this;
	}
	
	public ShortMap add(String k, int v)
	{
		map.put(k, Integer.toString(v));		
		return this;
	}

	public ShortMap add(String k, double v)
	{
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
	
	public String getString(String key)
	{
		return Convertor.toString(map.get(key));
	}
	
	
	public boolean getBoolean(String v)
	{
		return Convertor.toBoolean(map.get(v));
	}
	/*
	public static boolean getBoolean(String v, boolean def)
	{
		if (v == null) return def;
		return v.equals("1");
	}
	*/
	public Date getDate(String key)
	{
		return Convertor.toDate(map.get(key));
	}
	
	public int getInt(String key)
	{
		return Convertor.toInt(map.get(key));
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
	

	public HashMap<String, String> getMap(String v)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		Vector<String> vector = getVector(v);
		for (int count = 0; count < vector.size(); count+=2)
		{
			result.put(vector.elementAt(count), vector.elementAt(count + 1));
		}
		return result;
	}
	
	public ShortMap addMap(String v, HashMap<String, String> localMap)
	{
		Vector<String> vector = new Vector<String>();
		for (String k: localMap.keySet())
		{
			vector.add(k);
			vector.add(localMap.get(k));
		}
		addVector(v, vector);
		return this;
	}
}
