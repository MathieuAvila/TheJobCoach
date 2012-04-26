package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;

public class UserValues {

	final static String COLUMN_FAMILY_NAME_LIST = "uservalues";

	static ColumnFamilyDefinition cfDefList = null;

	static final String[] allowedKeysArray = 
		{
		"test1.test1",
		"test1.test2",
		"test2.test1",
		"test2.test2",
		"coach.advice.general.createopportunity",
		"coach.advice.general.createdocument",
		"coach.advice.general.jobsite",
		};
	static final int MAX_OPTION_LENGTH = 100;
	
	public UserValues()
	{
		cfDefList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST, cfDefList);
	}

	public Map<String, String> getValues(UserId id, String rootKey) throws CassandraException, SystemException 
	{
		String start = "";
		String end = "";
		List<String> subSet = new ArrayList<String>();
		for (String key: allowedKeysArray)
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
			if (result.get(key) == null) result.put(key, "");
		}
		return result;
	}

	private static List<String> allowedKeyList = null;
	
	private List<String> getList()
	{
		if (allowedKeyList == null) 
		{
			allowedKeyList = new ArrayList<String>(Arrays.asList(allowedKeysArray));
		}
		return allowedKeyList;		
	}
	
	public void setValues(UserId id, Map<String,String> values) throws CassandraException, SystemException 
	{
		for (String key: values.keySet())
		{
			if (key == null) throw new SystemException();
			if (!getList().contains(key)) throw new SystemException();
			String val = values.get(key);
			if (val == null) throw new SystemException();
			if (val.length() >= MAX_OPTION_LENGTH) throw new SystemException();
		}
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_LIST, id.userName, values);
	}

	public void deleteUser(UserId id) throws CassandraException 
	{		
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST, id.userName);
	}
}
