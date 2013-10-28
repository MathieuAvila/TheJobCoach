package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class TestUserJobSiteManager {
	
	static UserJobSiteManager manager = new UserJobSiteManager();
	
	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static UserId id2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);
	
	static String site1 = "site1";
	static String site2 = "site2";
	static String site3 = "site3";
		
	static String site12 = "site1";
	static String site22 = "site2";
	static String site32 = "site3";
		
	static UserJobSite ujs1 = new UserJobSite(site1, "site1", "URL1", "description1", "login1", "password1", new Date(1));
	static UserJobSite ujs2 = new UserJobSite(site2, "site2", "URL2", "description2", "login2", "password2", new Date(2));
	static UserJobSite ujs3 = new UserJobSite(site3, "site3", "URL3", "description3", "login3", "password3", new Date(3));
	
	static UserJobSite ujs21 = new UserJobSite(site12, "site21", "URL1", "description1", "login1", "password1", new Date(1));
	static UserJobSite ujs22 = new UserJobSite(site22, "site22", "URL2", "description2", "login2", "password2", new Date(2));
	static UserJobSite ujs23 = new UserJobSite(site32, "site23", "URL3", "description3", "login3", "password3", new Date(3));
	
	@Test
	public void testCleanUserSite() throws CassandraException
	{
		List<String> result = manager.getUserSiteList(id);
		for (String site: result)
		{
			manager.deleteUserSite(id, site);
		}
		result = manager.getUserSiteList(id2);
		for (String site: result)
		{
			manager.deleteUserSite(id2, site);
		}
		manager.setUserSite(id, ujs1);
		manager.setUserSite(id, ujs2);
		manager.setUserSite(id, ujs3);

		manager.setUserSite(id2, ujs21);
		manager.setUserSite(id2, ujs22);
		manager.setUserSite(id2, ujs23);	
	}
	
	@Test
	public void testGetUserSiteList() throws CassandraException {
		List<String> result = manager.getUserSiteList(id);
		assertEquals(3, result.size());
		assertTrue(result.contains(site1));
		assertTrue(result.contains(site2));
		assertTrue(result.contains(site3));

		List<String> result2 = manager.getUserSiteList(id2);
		assertEquals(3, result2.size());
		assertTrue(result2.contains(site12));
		assertTrue(result2.contains(site22));
		assertTrue(result2.contains(site32));

	}

	@Test
	public void testSetUserSite() throws CassandraException 
	{
		ujs1.ID = site1;
		ujs2.ID = site2;
		ujs3.ID = site3;
		manager.setUserSite(id, ujs1);
		manager.setUserSite(id, ujs2);
		manager.setUserSite(id, ujs3);
	}
	
	@Test
	public void testGetUserSite() throws CassandraException 
	{
		UserJobSite copy_ujs1 = manager.getUserSite(id, site1);
		UserJobSite copy_ujs2 = manager.getUserSite(id, site2);
		UserJobSite copy_ujs3 = manager.getUserSite(id, site3);

		assertEquals(ujs1.description, copy_ujs1.description);
		assertEquals(ujs1.login,       copy_ujs1.login);
		assertEquals(ujs1.password,    copy_ujs1.password);
		assertEquals(ujs1.name,        copy_ujs1.name);
		assertEquals(ujs1.URL,         copy_ujs1.URL);
		assertEquals(ujs1.ID,          copy_ujs1.ID);

		assertEquals(ujs2.description, copy_ujs2.description);
		assertEquals(ujs2.login,       copy_ujs2.login);
		assertEquals(ujs2.password,    copy_ujs2.password);
		assertEquals(ujs2.name,        copy_ujs2.name);
		assertEquals(ujs2.URL,         copy_ujs2.URL);
		assertEquals(ujs2.ID,          copy_ujs2.ID);

		assertEquals(ujs3.description, copy_ujs3.description);
		assertEquals(ujs3.login,       copy_ujs3.login);
		assertEquals(ujs3.password,    copy_ujs3.password);
		assertEquals(ujs3.name,        copy_ujs3.name);
		assertEquals(ujs3.URL,         copy_ujs3.URL);
		assertEquals(ujs3.ID,          copy_ujs3.ID);
	}

	@Test
	public void testDeleteUserSite() throws CassandraException 
	{
		manager.deleteUserSite(id, site2);
		manager.deleteUserSite(id2, site32);
		
		List<String> result = manager.getUserSiteList(id);
		assertEquals(2, result.size());
		assertTrue(result.contains(site1));
		assertTrue(result.contains(site3));

		List<String> result2 = manager.getUserSiteList(id2);
		assertEquals(2, result2.size());
		assertTrue(result2.contains(site12));
		assertTrue(result2.contains(site22));
	}
	
}
