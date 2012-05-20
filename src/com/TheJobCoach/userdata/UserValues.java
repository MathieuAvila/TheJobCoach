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

import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;

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

		addField(new FieldDefinition("DEFINITION_ACCOUNT_TYPE", MAX_OPTION_LENGTH, false, "Freemium"));
		addField(new FieldDefinition("DEFINITION_OBJECTIVE"));
		addField(new FieldDefinition("DEFINITION_TITLE"));

		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_WEEKLY_RATIO));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_MONTHLY_RATIO));
		
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY_BIWEEKLY));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY_MONTHLY));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY_WEEKLY));

		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY_BIWEEKLY));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY_MONTHLY));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY_WEEKLY));
		
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW_BIWEEKLY));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW_MONTHLY));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW_WEEKLY));

		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL_BIWEEKLY));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL_MONTHLY));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL_WEEKLY));

		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_RECALL_GOAL_MIDDLE));
		
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
				if (start.equals("") || (start.compareTo(key) > 0))
				{ 
					start = key;
					end = start;
				}
				else end = key;
			}
		}
		if (start.equals("")) throw new SystemException();
		end = end + "Z";
		System.out.println("Start key: " + start + " End key: " + end);
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
