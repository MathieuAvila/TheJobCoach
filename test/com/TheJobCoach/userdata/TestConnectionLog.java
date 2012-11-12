package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import org.junit.Test;

import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.TestFormatUtil;

public class TestConnectionLog
{
	
	String userName = "**";
	ConnectionLog cl = new ConnectionLog();
	
	@Test
	public void testDeleteAccount() throws CassandraException
	{
		cl.deleteAccount(userName);
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
		
		cl.addLogTimeDay(userName + "no_mistake", TestFormatUtil.getDate(2012, 5, 7, 1, 10, 21), 100);  // another user
		 
		assertEquals(100, cl.getLogTimeDay(userName + "no_mistake", TestFormatUtil.getDate(2012, 5, 7, 1, 10, 20)));		
		
		assertEquals(2, cl.getLogDays(userName, TestFormatUtil.getDate(2012, 5, 6, 1, 11, 20), TestFormatUtil.getDate(2012, 5, 7, 1, 5, 20)));
		assertEquals(4, cl.getLogDays(userName, TestFormatUtil.getDate(1912, 5, 6, 1, 11, 20), TestFormatUtil.getDate(2112, 5, 7, 1, 5, 20)));
		
		cl.deleteAccount(userName);
		assertEquals(100, cl.getLogTimeDay(userName + "no_mistake", TestFormatUtil.getDate(2012, 5, 7, 1, 10, 20)));		
		assertEquals(0, cl.getLogDays(userName, TestFormatUtil.getDate(1912, 5, 6, 1, 11, 20), TestFormatUtil.getDate(2112, 5, 7, 1, 5, 20)));
		
		cl.deleteAccount(userName + "no_mistake");
		assertEquals(0, cl.getLogDays(userName, TestFormatUtil.getDate(1912, 5, 6, 1, 11, 20), TestFormatUtil.getDate(2112, 5, 7, 1, 5, 20)));
		assertEquals(0, cl.getLogDays(userName + "no_mistake", TestFormatUtil.getDate(1912, 5, 6, 1, 11, 20), TestFormatUtil.getDate(2112, 5, 7, 1, 5, 20)));
		
	}
}
