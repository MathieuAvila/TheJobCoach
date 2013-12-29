package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;


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
			"contractType1",  "1",  
			getDate(2000, 1, 1), getDate(2000, 1, 1),
			false, "source1", "url1", "location1",
			UserOpportunity.ApplicationStatus.APPLIED, "note1");

	static UserOpportunity opportunity2 = new UserOpportunity("opp2", getDate(2000, 1, 2), getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  "2",  
			getDate(2000, 1, 2), getDate(2000, 1, 2),
			false, "source2", "url2", "location2",
			UserOpportunity.ApplicationStatus.NEW, "note2");

	static UserOpportunity opportunity3 = new UserOpportunity("opp3", getDate(2000, 1, 2), getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  "2",  
			getDate(2000, 1, 2), getDate(2000, 1, 2),
			false, "source2", "url2", "location2",
			UserOpportunity.ApplicationStatus.CLOSED, "note3");

	private void checkOpportunity(UserOpportunity op1, UserOpportunity opRef, boolean shortDefinition)
	{
		assertEquals(op1.ID, opRef.ID);
		assertEquals(op1.firstSeen, opRef.firstSeen);
		assertEquals(op1.lastUpdate, opRef.lastUpdate);
		assertEquals(op1.title, opRef.title);
		if (shortDefinition)
		{
			assertEquals(op1.description, "");
			assertEquals(op1.systemSource, true);			
			assertEquals(op1.source, "");
		}
		else
		{
			assertEquals(op1.description, opRef.description);
			assertEquals(op1.systemSource, opRef.systemSource);						
			assertEquals(op1.source, opRef.source);
		}
		assertEquals(op1.url, opRef.url);
		assertEquals(op1.companyId, opRef.companyId);
		assertEquals(op1.contractType, opRef.contractType);
		assertEquals(op1.salary, opRef.salary);
		assertEquals(op1.startDate, opRef.startDate);
		assertEquals(op1.endDate, opRef.endDate);
		assertEquals(op1.location, opRef.location);
		assertEquals(op1.status, opRef.status);
		assertEquals(op1.note, opRef.note);
	}

	@Test
	public void testCleanup() throws CassandraException
	{
		Vector<UserOpportunity> idList = manager.getOpportunitiesList(id, "managed");
		for (UserOpportunity oppId : idList)
		{
			manager.deleteUserOpportunity(id, oppId.ID);
		}
		idList = manager.getOpportunitiesList(id2, "managed");
		for (UserOpportunity oppId : idList)
		{
			manager.deleteUserOpportunity(id2, oppId.ID);
		}
	}

	@Test
	public void testAddUserOpportunity() throws CassandraException
	{		
		manager.setUserOpportunity(id, opportunity1, "managed");
		manager.setUserOpportunity(id, opportunity2, "managed");
		manager.setUserOpportunity(id2, opportunity3, "managed");

		Vector<UserOpportunity> result = manager.getOpportunitiesList(id, "managed");
		assertEquals(2, result.size());
		assertEquals(result.get(0).ID, "opp1");
		assertEquals(result.get(1).ID, "opp2");
		result = manager.getOpportunitiesList(id2, "managed");
		assertEquals(1, result.size());
		assertEquals(result.get(0).ID, "opp3");		

		UserOpportunity opp1 = manager.getOpportunityShort(id, opportunity1.ID);
		checkOpportunity(opp1, opportunity1, true);		
		UserOpportunity opp2 = manager.getOpportunityShort(id, opportunity2.ID);
		checkOpportunity(opp2, opportunity2, true);		
		UserOpportunity opp3 = manager.getOpportunityShort(id2, opportunity3.ID);
		checkOpportunity(opp3, opportunity3, true);		

		opp1 = manager.getOpportunityLong(id, opportunity1.ID);
		checkOpportunity(opp1, opportunity1, false);		
		opp2 = manager.getOpportunityLong(id, opportunity2.ID);
		checkOpportunity(opp2, opportunity2, false);		
		opp3 = manager.getOpportunityLong(id2, opportunity3.ID);
		checkOpportunity(opp3, opportunity3, false);		

		manager.deleteUserOpportunity(id, "opp1");
		result = manager.getOpportunitiesList(id, "managed");
		assertEquals(1, result.size());
		assertEquals(result.get(0).ID, "opp2");

		result = manager.getOpportunitiesList(id, "managed");
		assertEquals(1, result.size());
		manager.deleteUser(id);
		result = manager.getOpportunitiesList(id, "managed");
		assertEquals(0, result.size());		
	}
}
