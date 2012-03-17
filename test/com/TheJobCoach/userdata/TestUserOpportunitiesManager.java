package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;;

public class TestUserOpportunitiesManager {
	
	static UserOpportunityManager manager = new UserOpportunityManager();
	
	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static UserId id2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);
	
	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	static UserOpportunity opportunity1 = new UserOpportunity("opp1", getDate(2000, 1, 1), getDate(2000, 2, 1),
			"title1", "description1", "companyId1",
			"contractType1",  1,  
			getDate(2000, 1, 1), getDate(2000, 1, 1),
			false, "source1", "url1", "location1");
	
	static UserOpportunity opportunity2 = new UserOpportunity("opp2", getDate(2000, 1, 2), getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  2,  
			getDate(2000, 1, 2), getDate(2000, 1, 2),
			false, "source2", "url2", "location2");

	static UserOpportunity opportunity3 = new UserOpportunity("opp3", getDate(2000, 1, 2), getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  2,  
			getDate(2000, 1, 2), getDate(2000, 1, 2),
			false, "source2", "url2", "location2");
	
	private void checkOpportunity(UserOpportunity op1, UserOpportunity op2)
	{
		assertEquals(op1.ID, op2.ID);
		assertEquals(op1.firstSeen, op2.firstSeen);
		assertEquals(op1.lastUpdate, op2.lastUpdate);
		assertEquals(op1.title, op2.title);
		assertEquals(op1.description, op2.description);
		assertEquals(op1.companyId, op2.companyId);
		assertEquals(op1.contractType, op2.contractType);
		assertEquals(op1.salary, op2.salary);
		assertEquals(op1.startDate, op2.startDate);
		assertEquals(op1.endDate, op2.endDate);
		assertEquals(op1.systemSource, op2.systemSource);
		assertEquals(op1.source, op2.source);
		assertEquals(op1.url, op2.url);
		assertEquals(op1.location, op2.location);
	}
	
	@Test
	public void testCleanup() throws CassandraException
	{
		List<String> idList = manager.getOpportunitiesList(id);
		for (String oppId : idList)
		{
			manager.deleteUserOpportunity(id, oppId);
		}
		idList = manager.getOpportunitiesList(id2);
		for (String oppId : idList)
		{
			manager.deleteUserOpportunity(id2, oppId);
		}
	}
	
	@Test
	public void testAddUserOpportunity() throws CassandraException
	{		
		manager.setUserOpportunity(id, opportunity1);
		manager.setUserOpportunity(id, opportunity2);
		manager.setUserOpportunity(id2, opportunity3);
	}
	
	@Test
	public void testGetUserOpportunityList() throws CassandraException {
		List<String> result = manager.getOpportunitiesList(id);
		assertEquals(2, result.size());
		assertEquals(result.get(0), "opp2");		
		assertEquals(result.get(1), "opp1");		
		result = manager.getOpportunitiesList(id2);
		assertEquals(1, result.size());
		assertEquals(result.get(0), "opp3");		
	}
	
	@Test
	public void testGetUserOpportunity() throws CassandraException {
		UserOpportunity opp1 = manager.getOpportunity(id, opportunity1.ID);
		checkOpportunity(opp1, opportunity1);		
		UserOpportunity opp2 = manager.getOpportunity(id, opportunity2.ID);
		checkOpportunity(opp2, opportunity2);		
		UserOpportunity opp3 = manager.getOpportunity(id2, opportunity3.ID);
		checkOpportunity(opp3, opportunity3);		
	}
	
	@Test
	public void testDeleteUserOpportunity() throws CassandraException {
		manager.deleteUserOpportunity(id, "opp1");
		List<String> result = manager.getOpportunitiesList(id);
		assertEquals(1, result.size());
		assertEquals(result.get(0), "opp2");
	}
}
