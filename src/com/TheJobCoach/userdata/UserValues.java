package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;

public class UserValues  extends UserValuesCore implements IUserDataManager{

	final static String COLUMN_FAMILY_NAME_LIST = "uservalues";
	final static String COLUMN_FAMILY_NAME_LIST_UPDATED = "updateduservalues";

	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefListUpdated = null;

	public UserValues()
	{
		cfDefList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST, cfDefList);
		cfDefListUpdated = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST_UPDATED, cfDefListUpdated);
	}
	
	public String getValue(UserId id, String rootKey) throws CassandraException, SystemException 
	{
		Map<String, String> values = getValues(id, rootKey);
		if (values.containsKey(rootKey)) return values.get(rootKey);
		return "";
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
		if (result == null)
				result = new HashMap<String, String>(); 
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
			if (!keysName.contains(key)) 
				{
				throw new SystemException();
				}
			String val = values.get(key);
			FieldDefinition def = keysMap.get(key);
			def.check(val, client);
		}
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_LIST, id.userName, values);
		if (!client)
		{
			CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_LIST_UPDATED, id.userName, values);
		}
	}
	
	public Map<String,String> getUpdatedValues(UserId id) throws CassandraException
	{
		Map<String, String> result = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_LIST_UPDATED, id.userName);
		if (result == null)
			result = new TreeMap<String, String>();
		for (String key: result.keySet())
		{
			CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST_UPDATED, id.userName, key);
		}
		return result;
	}
	
	public void setValue(UserId id, String key, String value, boolean client) throws CassandraException, SystemException 
	{
		Map<String,String> values = new HashMap<String,String>();
		values.put(key, value);
		setValues(id, values, client);
	}

	@Override
	public void deleteUser(UserId id) throws CassandraException 
	{		
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST, id.userName);
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST_UPDATED, id.userName);
	}
	
	public long getForcedWaitTimeMs(UserId id, int minMs)
	{
		Map<String, String> value = null;
		try { value = getValues(id, UserValuesConstantsAccount.SECURITY_WAITING_TIME_REQUEST);}
		 catch(Exception e) { return 0;} 
		long time = 0;
		long result = 0;
		long current = new Date().getTime();
		if (value.containsKey(UserValuesConstantsAccount.SECURITY_WAITING_TIME_REQUEST))
		{
			time = Long.parseLong((String)value.get(UserValuesConstantsAccount.SECURITY_WAITING_TIME_REQUEST));
			result = minMs - (current - time);
			if (result < 0) result = 0;
		}
		// Set key to new time	
		value.put(UserValuesConstantsAccount.SECURITY_WAITING_TIME_REQUEST, String.valueOf(current));
		try { setValues(id, value, false); } catch(Exception e) {} // Gods of Code, please forgive me.
		return result;
	}

	@Override
	public void createTestUser(UserId user, String lang)
	{
	}

	@Override
	public void createUserDefaults(UserId user, String lang)
	{
	}
}
