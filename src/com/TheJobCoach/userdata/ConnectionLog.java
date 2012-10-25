package com.TheJobCoach.userdata;


import java.util.Date;
import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;


public class ConnectionLog {

	static ColumnFamilyDefinition cfDef = null;

	final static String COLUMN_FAMILY_NAME_CONNECTIONLOG = "connectionlog";

	public ConnectionLog()
	{
		cfDef = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CONNECTIONLOG, cfDef);
	}

	@SuppressWarnings({ "unused", "deprecation" })
	private String getDateString(Date d)
	{
		String monthZ = d.getMonth() < 10 ? "0":"";
		String dayZ = d.getDate() < 10 ? "0":"";
		return String.valueOf(d.getYear()) + "_" +  monthZ + String.valueOf(d.getMonth()) + "_" + dayZ + String.valueOf(d.getDate());
	}
	
	public void deleteAccount(String userName) throws CassandraException
	{
		if (userName != null)
		{
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CONNECTIONLOG, userName);
			System.out.println("DELETED CONNECTION LOG: " + userName);
		}
	}

	public void addLogTimeDay(String userName, int seconds)
	{
		
	}

	public int getLogDays(String userName, Date start, Date end)
	{
		return 0;
	}
	
	public int getLogTimeDay(String userName, Date date)
	{
		return 0;
	}
}
