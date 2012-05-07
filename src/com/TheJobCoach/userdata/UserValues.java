package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;

public class UserValues {

	final static String COLUMN_FAMILY_NAME_LIST = "uservalues";

	static ColumnFamilyDefinition cfDefList = null;

	static protected class FieldDefinition
	{
		String name;
		public int length;
		boolean clientSideFree;
		String defaultValue;
		public FieldDefinition(String name) 
		{ 
			this.name = name;
			this.length = MAX_OPTION_LENGTH;
			this.clientSideFree = true;
			this.defaultValue = "";
		};
		public FieldDefinition(String name, int length, boolean clientSideFree, String defaultValue) 
		{ 
			this.name = name;
			this.length = length;
			this.clientSideFree = clientSideFree;
			this.defaultValue = defaultValue;
		}
		public void check(String val, boolean client) throws SystemException 
		{
			if (val == null) throw new SystemException();
			if (val.length() >= length) throw new SystemException();
			if (client && !clientSideFree) throw new SystemException();
		};
	}
	
	static final int MAX_OPTION_LENGTH = 100;
	
	static List<FieldDefinition> keys;
	static Map<String, FieldDefinition> keysMap;
	static Set<String> keysName;
	static List<String> keysNameList;
	
	static public void addField(FieldDefinition f)
	{
		keys.add(f);
		keysName.add(f.name);
		keysNameList.add(f.name);
		keysMap.put(f.name, f);
	}
	
	{
		keys = new ArrayList<FieldDefinition>();
		keysName = new HashSet<String>();
		keysNameList = new ArrayList<String>();
		keysMap = new HashMap<String, FieldDefinition>();
		
		addField(new FieldDefinition("coach.advice.general.createopportunity"));
		addField(new FieldDefinition("coach.advice.general.createdocument"));
		addField(new FieldDefinition("coach.advice.general.jobsite"));
		addField(new FieldDefinition("performance.createopportunity.monthly"));
		addField(new FieldDefinition("performance.createopportunity.weekly"));
		addField(new FieldDefinition("performance.candidateopportunity.monthly"));
		addField(new FieldDefinition("performance.candidateopportunity.weekly"));
		addField(new FieldDefinition("performance.interview.monthly"));
		addField(new FieldDefinition("performance.interview.weekly"));
		addField(new FieldDefinition("performance.phonecall.monthly"));
		addField(new FieldDefinition("performance.phonecall.weekly"));
		addField(new FieldDefinition("performance.connect.weekly"));
	}
	
	public UserValues()
	{
		cfDefList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST, cfDefList);
	}

	public Map<String, String> getValues(UserId id, String rootKey) throws CassandraException, SystemException 
	{
		String start = "";
		String end = "";
		List<String> subSet = new ArrayList<String>();
		for (String key: keysNameList)
		{
			System.out.println("KEY "+ key + " " + rootKey);
			if (key.matches(rootKey + ".*"))
			{
				subSet.add(key);
				if (start.equals("")) 
				{ 
					start = key + ".";
					end = start;
				}
				else end = key;
			}
		}
		if (start.equals("")) throw new SystemException();
		Map<String, String> result = CassandraAccessor.getColumnRange(COLUMN_FAMILY_NAME_LIST, id.userName, start, end, 1000);
		for (String key: subSet)
		{
			if (result.get(key) == null) result.put(key, keysMap.get(key).defaultValue);
		}
		return result;
	}

	public void setValues(UserId id, Map<String,String> values, boolean client) throws CassandraException, SystemException 
	{
		for (String key: values.keySet())
		{
			if (key == null) throw new SystemException();
			if (!keysName.contains(key)) throw new SystemException();
			String val = values.get(key);
			FieldDefinition def = keysMap.get(key);
			def.check(val, client);
		}
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_LIST, id.userName, values);
	}

	public void deleteUser(UserId id) throws CassandraException 
	{		
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST, id.userName);
	}
}
