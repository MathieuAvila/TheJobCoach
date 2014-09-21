package com.TheJobCoach.userdata;


import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.UserId;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;


public class ConnectionLog implements IUserDataManager {

	static ColumnFamilyDefinition cfDef = null;

	final static String COLUMN_FAMILY_NAME_CONNECTIONLOG = "connectionlog";

	public ConnectionLog()
	{
		cfDef = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CONNECTIONLOG, cfDef);
	}
	
	static ConnectionLog instance = new ConnectionLog();

	@Override
	public void deleteUser(UserId user) throws CassandraException
	{
		if (user != null)
		{
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CONNECTIONLOG, user.userName);
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

	public class LogTimeResult
	{
		public int totalDay;
		public int startOk;
		public int endOk;
	}
	
	public LogTimeResult getLogDays(String userName, Date start, Date end) throws CassandraException
	{
		LogTimeResult result = new LogTimeResult();
		String dStart = FormatUtil.getDateString(FormatUtil.startOfTheDay(start));
		String dEnd = FormatUtil.getDateString(FormatUtil.endOfTheDay(end));
		Map<String, String> res = CassandraAccessor.getColumnRange(COLUMN_FAMILY_NAME_CONNECTIONLOG, userName, dStart, dEnd, 10000);
		Set<Date> aggr = new HashSet<Date>();
		Set<Date> aggrStartOk = new HashSet<Date>();
		Set<Date> aggrEndOk = new HashSet<Date>();
		Date currentStart= FormatUtil.sameDayTimeAs(start);
		Date currentEnd= FormatUtil.sameDayTimeAs(end);
		
		for (String d: res.keySet())
		{
			Date realDate = FormatUtil.getStringDate(d);
			Date cDate = FormatUtil.startOfTheDay(realDate);
			aggr.add(cDate);
			Date currentTime = FormatUtil.sameDayTimeAs(realDate);
			if (currentTime.before(currentStart))
				aggrStartOk.add(cDate);
			if (currentTime.before(currentEnd))
				aggrEndOk.add(cDate);
			else
				aggrEndOk.remove(cDate);
		}
		result.totalDay = aggr.size();
		result.startOk = aggrStartOk.size();
		result.endOk = aggrEndOk.size();
		return result;
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

	@Override
	public void createTestUser(UserId user, String lang)
	{
		
	}

	@Override
	public void createUserDefaults(UserId user, String lang)
	{
		/* nothing to do */
	}
}
