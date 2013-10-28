package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
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

	static final int YES_NO_LENGTH = 4;
	
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

		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_TYPE, MAX_OPTION_LENGTH, false, "Freemium"));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_MODEL, MAX_OPTION_LENGTH, false, UserValuesConstantsAccount.ACCOUNT_MODEL_LIST.get(0)));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_TITLE));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_STATUS, MAX_OPTION_LENGTH, true, UserValuesConstantsAccount.ACCOUNT_STATUS_LIST__ACTIVE_SEARCH));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_KEYWORDS));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, MAX_OPTION_LENGTH, true, UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__DEFAULT_MAN));

		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_PUBLISH_SEEKER, YES_NO_LENGTH, true, "YES"));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_PUBLISH_COACH, YES_NO_LENGTH, true, "YES"));
		addField(new FieldDefinition(UserValuesConstantsAccount.ACCOUNT_PUBLISH_RECRUITER, YES_NO_LENGTH, true, "YES"));

		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO));
		
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY));		
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_RECALL_GOAL_MIDDLE));
		addField(new FieldDefinition(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL));
		
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
			if (key.matches(rootKey + ".*"))
			{
				subSet.add(key);
				if (start.equals("") || (start.compareTo(key) > 0))
				{ 
					start = key;
				}				
				if (end.equals("") || key.compareTo(end) > 0)
				{						
					end = key;
				}
			}
		}
		if (start.equals("")) throw new SystemException();
		end = end + "Z";
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
