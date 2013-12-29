package com.TheJobCoach.userdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Vector;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.shared.TodoCommon;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;


public class TestUserJobSiteManager {
	
	Logger logger = LoggerFactory.getLogger(TestUserJobSiteManager.class);

	static UserJobSiteManager manager = new UserJobSiteManager();
	
	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static UserId id2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);
	
	static String site1 = "site1";
	static String site2 = "site2";
	static String site3 = "site3";
		
	static String site12 = "site1";
	static String site22 = "site2";
	static String site32 = "site3";
		
	static UserJobSite ujs1 = new UserJobSite(site1, "site1", "URL1", "description1", "login1", "password1", 
			new UpdatePeriod(CoachTestUtils.getDate(2013, 12, 1), 10, PeriodType.DAY, true));
	static UserJobSite ujs2 = new UserJobSite(site2, "site2", "URL2", "description2", "login2", "password2", 
			new UpdatePeriod(CoachTestUtils.getDate(2013, 12, 3), 10, PeriodType.DAY, false));
	static UserJobSite ujs3 = new UserJobSite(site3, "site3", "URL3", "description3", "login3", "password3", 
			new UpdatePeriod(CoachTestUtils.getDate(2013, 12, 10), 10, PeriodType.DAY, true));
	
	static UserJobSite ujs21 = new UserJobSite(site12, "site21", "URL1", "description1", "login1", "password1");
	static UserJobSite ujs22 = new UserJobSite(site22, "site22", "URL2", "description2", "login2", "password2");
	static UserJobSite ujs23 = new UserJobSite(site32, "site23", "URL3", "description3", "login3", "password3");
	
	class TestTodoList implements ITodoList
	{
		class ListSet {
			UserId id;
			TodoEvent result;
			public ListSet(UserId id, TodoEvent result) {this.id = id; this.result = result;}
		}
		
		public Vector<ListSet> setEvents = new Vector<ListSet>();
		
		public void setTodoEvent(UserId id, TodoEvent result)
				throws CassandraException
		{
			logger.info("setTodoEvent ID:" + result.ID);
			setEvents.add(new ListSet(id, result));
		}

		public void reset()
		{
			setEvents = new Vector<ListSet>();
		}
		
	}

	@Test
	public void testSetUserSite() throws CassandraException 
	{
		manager.deleteUser(id);
		
		TestTodoList todoInterface = new TestTodoList();
		
		UserJobSiteManager.todoList = todoInterface;
		
		
		ujs1.ID = site1;
		ujs2.ID = site2;
		ujs3.ID = site3;
		manager.setUserSite(id, ujs1);
		
		// Check TodoList is set appropriately
		assertEquals(1, todoInterface.setEvents.size());
		logger.info(todoInterface.setEvents.get(0).id.userName);
		// logger.info(todoInterface.setEvents.get(0).result.eventSubscriber, TodoCommon.SITEMANAGER_SUBSCRIBER_ID);
		assertEquals("user",                               	todoInterface.setEvents.get(0).id.userName);
		assertEquals(TodoCommon.SITEMANAGER_SUBSCRIBER_ID, 	todoInterface.setEvents.get(0).result.eventSubscriber);
		assertEquals(ujs1.update.getNextCall(),           	todoInterface.setEvents.get(0).result.eventDate);
		assertEquals(ujs1.ID,        				    	todoInterface.setEvents.get(0).result.ID);
		
		// Set 2 other sites
		todoInterface.reset();
		manager.setUserSite(id, ujs2);
		// no update: recall == false
		assertEquals(0, todoInterface.setEvents.size());
		
		manager.setUserSite(id, ujs3);
		assertEquals(1, todoInterface.setEvents.size());
		TodoEvent eventSite3 = todoInterface.setEvents.get(0).result;
		
		// Get each site and check value
		UserJobSite copy_ujs1 = manager.getUserSite(id, site1);
		UserJobSite copy_ujs2 = manager.getUserSite(id, site2);
		UserJobSite copy_ujs3 = manager.getUserSite(id, site3);

		assertEquals(ujs1.description, copy_ujs1.description);
		assertEquals(ujs1.login,       copy_ujs1.login);
		assertEquals(ujs1.password,    copy_ujs1.password);
		assertEquals(ujs1.name,        copy_ujs1.name);
		assertEquals(ujs1.URL,         copy_ujs1.URL);
		assertEquals(ujs1.ID,          copy_ujs1.ID);
		assertEquals(ujs1.update,      copy_ujs1.update);

		assertEquals(ujs2.description, copy_ujs2.description);
		assertEquals(ujs2.login,       copy_ujs2.login);
		assertEquals(ujs2.password,    copy_ujs2.password);
		assertEquals(ujs2.name,        copy_ujs2.name);
		assertEquals(ujs2.URL,         copy_ujs2.URL);
		assertEquals(ujs2.ID,          copy_ujs2.ID);
		assertEquals(ujs2.update,      copy_ujs2.update);

		assertEquals(ujs3.description, copy_ujs3.description);
		assertEquals(ujs3.login,       copy_ujs3.login);
		assertEquals(ujs3.password,    copy_ujs3.password);
		assertEquals(ujs3.name,        copy_ujs3.name);
		assertEquals(ujs3.URL,         copy_ujs3.URL);
		assertEquals(ujs3.ID,          copy_ujs3.ID);
		assertEquals(ujs3.update,      copy_ujs3.update);
		assertEquals(ujs3.update,      copy_ujs3.update);

		// Delete site, no Todo call
		todoInterface.reset();
		manager.deleteUserSite(id, site2);

		// delete for another user, same ID
		manager.deleteUserSite(id2, site3);

		List<String> result = manager.getUserSiteList(id);
		assertEquals(2, result.size());
		assertTrue(result.contains(site1));
		assertTrue(result.contains(site3));

		List<String> result2 = manager.getUserSiteList(id2);
		assertEquals(2, result2.size());
		assertTrue(result2.contains(site12));
		assertTrue(result2.contains(site22));
		
		// check return not there anymore after delete
		assertTrue(manager.isEventValid(id, eventSite3)); // There before delete
		manager.deleteUserSite(id, site3);//  delete
		assertTrue(!manager.isEventValid(id, eventSite3));// Not There after delete
	}

	@Test
	public void testDeleteUser() throws CassandraException 
	{
		ujs1.ID = site1;
		ujs2.ID = site2;
		ujs3.ID = site3;
		manager.setUserSite(id, ujs1);
		manager.setUserSite(id, ujs2);
		manager.setUserSite(id, ujs3);
		
		manager.deleteUser(id);

		List<String> result = manager.getUserSiteList(id);
		assertEquals(0, result.size());
		
		int hasError = 0;
		int isNull = 0;
		try{ UserJobSite ujs = manager.getUserSite(id, site1); if (ujs == null) isNull++; } catch (CassandraException e) { hasError++; };
		try{ UserJobSite ujs = manager.getUserSite(id, site2); if (ujs == null) isNull++;  } catch (CassandraException e) { hasError++; };
		try{ UserJobSite ujs = manager.getUserSite(id, site3); if (ujs == null) isNull++;  } catch (CassandraException e) { hasError++; };
		assertEquals(0, hasError);
		assertEquals(3, isNull);
	}
	
}
