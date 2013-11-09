package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import org.junit.Test;

import com.TheJobCoach.userdata.ConnectionLog.LogTimeResult;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.TestFormatUtil;
import com.TheJobCoach.webapp.util.shared.UserId;


public class TestConnectionLog
{

	static String userName = "toto";
	static String user_no_mistakeName = "totoXX";
	ConnectionLog cl = new ConnectionLog();
	static UserId user = new UserId(userName, "password", UserId.UserType.USER_TYPE_SEEKER);
	static UserId user_no_mistake = new UserId(user_no_mistakeName, "password", UserId.UserType.USER_TYPE_SEEKER);

	@Test
	public void testDeleteAccount() throws CassandraException
	{
		cl.deleteUser(user);
	}

	@Test
	public void testFull() throws CassandraException
	{
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(2012, 5, 6, 1, 10, 20), 120);
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(2012, 5, 6, 1, 10, 20), 1200); // replace previous
		assertEquals(1200, cl.getLogTimeDay(userName, TestFormatUtil.getDate(2012, 5, 6, 1, 10, 20)));

		cl.addLogTimeDay(userName, TestFormatUtil.getDate(2012, 5, 7, 1, 10, 21), 100);
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(2012, 5, 7, 1, 11, 21), 1000); // adds
		assertEquals(1100, cl.getLogTimeDay(userName, TestFormatUtil.getDate(2012, 5, 7, 1, 10, 20)));

		cl.addLogTimeDay(userName, TestFormatUtil.getDate(2040, 5, 7, 1, 11, 21), 1000); // very far
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1940, 5, 7, 1, 11, 21), 1000); // very past

		cl.addLogTimeDay(userName, TestFormatUtil.getDate(2240, 5, 7, 1, 11, 21), 1000); // very very far
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1840, 5, 7, 1, 11, 21), 1000); // very very past

		cl.addLogTimeDay(user_no_mistakeName, TestFormatUtil.getDate(2012, 5, 7, 1, 10, 21), 100);  // another user

		assertEquals(100, cl.getLogTimeDay(user_no_mistakeName, TestFormatUtil.getDate(2012, 5, 7, 1, 10, 20)));		

		LogTimeResult ltr = cl.getLogDays(userName, TestFormatUtil.getDate(2012, 5, 6, 1, 11, 20), TestFormatUtil.getDate(2012, 5, 7, 1, 5, 20));

		assertEquals(2, ltr.totalDay);

		ltr = cl.getLogDays(userName, TestFormatUtil.getDate(1912, 5, 6, 1, 11, 20), TestFormatUtil.getDate(2112, 5, 7, 1, 5, 20));
		assertEquals(4, ltr.totalDay);

		cl.deleteUser(user);
		assertEquals(100, cl.getLogTimeDay(user_no_mistakeName, TestFormatUtil.getDate(2012, 5, 7, 1, 10, 20)));

		ltr = cl.getLogDays(userName, TestFormatUtil.getDate(1912, 5, 6, 1, 11, 20), TestFormatUtil.getDate(2112, 5, 7, 1, 5, 20));
		assertEquals(0, ltr.totalDay);

		cl.deleteUser(user_no_mistake);
		ltr = cl.getLogDays(userName, TestFormatUtil.getDate(1912, 5, 6, 1, 11, 20), TestFormatUtil.getDate(2112, 5, 7, 1, 5, 20));
		assertEquals(0, ltr.totalDay);
		ltr = cl.getLogDays(user_no_mistakeName, TestFormatUtil.getDate(1912, 5, 6, 1, 11, 20), TestFormatUtil.getDate(2112, 5, 7, 1, 5, 20));
		assertEquals(0, ltr.totalDay);
	}

	@Test
	public void testLogStartEnd() throws CassandraException
	{
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1999, 5, 7, 10, 0, 0), 100);
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1999, 5, 7, 13, 0, 0), 100);
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1999, 5, 7, 14, 0, 0), 100);
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1999, 5, 7, 16, 0, 0), 100);

		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1999, 5, 8, 13, 0, 0), 100);
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1999, 5, 8, 14, 0, 0), 100);
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1999, 5, 8, 16, 0, 0), 100);

		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1999, 5, 9, 10, 0, 0), 100);
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1999, 5, 9, 13, 0, 0), 100);
		cl.addLogTimeDay(userName, TestFormatUtil.getDate(1999, 5, 9, 14, 0, 0), 100);

		LogTimeResult ltr = cl.getLogDays(userName, TestFormatUtil.getDate(1999, 5, 7, 11, 10, 0), TestFormatUtil.getDate(1999, 5, 9, 15, 10, 0));
		assertEquals(3, ltr.totalDay);
		assertEquals(2, ltr.startOk);
		assertEquals(1, ltr.endOk);

	}
}
