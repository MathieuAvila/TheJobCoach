package com.TheJobCoach.userdata;


import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.FormatUtil;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;


public class ConnectionLog {

	static ColumnFamilyDefinition cfDef = null;

	final static String COLUMN_FAMILY_NAME_CONNECTIONLOG = "connectionlog";

	public ConnectionLog()
	{
		cfDef = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CONNECTIONLOG, cfDef);
	}
	
	public void deleteAccount(String userName) throws CassandraException
	{
		if (userName != null)
		{
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CONNECTIONLOG, userName);
			System.out.println("DELETED CONNECTION LOG: " + userName);
		}
	}

	public void addLogTimeDay(String userName, Date start, int seconds) throws CassandraException
	{
		// Security: cannot be logged in more than 20 times a day
		String dStart = FormatUtil.getDateString(FormatUtil.startOfTheDay(start));
		String dEnd = FormatUtil.getDateString(FormatUtil.endOfTheDay(start));
		Map<String, String> res = CassandraAccessor.getColumnRange(COLUMN_FAMILY_NAME_CONNECTIONLOG, userName, dStart, dEnd, 30);
		if (res.size() > 20) return;
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(FormatUtil.getDateString(start), String.valueOf(seconds));	
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CONNECTIONLOG, userName, map);
	}

	public int getLogDays(String userName, Date start, Date end) throws CassandraException
	{
		String dStart = FormatUtil.getDateString(FormatUtil.startOfTheDay(start));
		String dEnd = FormatUtil.getDateString(FormatUtil.endOfTheDay(end));
		Map<String, String> res = CassandraAccessor.getColumnRange(COLUMN_FAMILY_NAME_CONNECTIONLOG, userName, dStart, dEnd, 10000);
		Set<Date> aggr = new HashSet<Date>();
		for (String d: res.keySet())
		{
			Date cDate = FormatUtil.startOfTheDay(FormatUtil.getStringDate(d));
			aggr.add(cDate);
		}
		return aggr.size();
	}
	
	public int getLogTimeDay(String userName, Date date) throws CassandraException
	{
		String dStart = FormatUtil.getDateString(FormatUtil.startOfTheDay(date));
		String dEnd = FormatUtil.getDateString(FormatUtil.endOfTheDay(date));
		Map<String, String> res = CassandraAccessor.getColumnRange(COLUMN_FAMILY_NAME_CONNECTIONLOG, userName, dStart, dEnd, 30);
		int count = 0;
		for (String v: res.values())
		{
			count += Integer.parseInt(v);
		}
		return count;
	}
}
