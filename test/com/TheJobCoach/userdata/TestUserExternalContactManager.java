package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class TestUserExternalContactManager {
	
	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	static UserExternalContactManager manager = new UserExternalContactManager();
	
	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static UserId id2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);
	
	static String contact1 = "contact1";
	static String contact2 = "contact2";
	static String contact3 = "contact3";
		
	static String contact12 = "contact1";
	static String contact22 = "contact2";
	static String contact32 = "contact3";

	static ExternalContact ujs1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "personalNote1", "organization1", new UpdatePeriod(getDate(2000, 1, 1), 2, PeriodType.DAY));
	static ExternalContact ujs2 = new ExternalContact(contact2, "firstName2", "lastName2", "email2", "personalNote2", "organization2", new UpdatePeriod(getDate(2000, 1, 1), 2, PeriodType.DAY));
	static ExternalContact ujs3 = new ExternalContact(contact3, "firstName3", "lastName3", "email3", "personalNote3", "organization3", new UpdatePeriod(getDate(2000, 1, 1), 2, PeriodType.DAY));
	
	static ExternalContact ujs21 = new ExternalContact(contact12, "firstName12", "lastName12", "email12", "personalNote12", "organization12", new UpdatePeriod(getDate(2000, 1, 1), 2, PeriodType.DAY));
	static ExternalContact ujs22 = new ExternalContact(contact22, "firstName22", "lastName22", "email22", "personalNote22", "organization22", new UpdatePeriod(getDate(2000, 1, 1), 2, PeriodType.DAY));
	static ExternalContact ujs23 = new ExternalContact(contact32, "firstName32", "lastName32", "email32", "personalNote32", "organization32", new UpdatePeriod(getDate(2000, 1, 1), 2, PeriodType.DAY));
	
	@Test
	public void testCleanUsercontact() throws CassandraException
	{
		List<String> result = manager.getExternalContactListId(id);
		for (String contact: result)
		{
			//System.out.println("Try delete previous contact: " + contact);
			manager.deleteExternalContact(id, contact);
		}
		result = manager.getExternalContactListId(id2);
		for (String contact: result)
		{
			//System.out.println("Try delete previous contact2: " + contact);
			manager.deleteExternalContact(id2, contact);
		}
		manager.setExternalContact(id, ujs1);
		manager.setExternalContact(id, ujs2);
		manager.setExternalContact(id, ujs3);

		manager.setExternalContact(id2, ujs21);
		manager.setExternalContact(id2, ujs22);
		manager.setExternalContact(id2, ujs23);	
	}
	
	@Test
	public void testgetExternalContactListId() throws CassandraException {
		List<String> result = manager.getExternalContactListId(id);
		//System.out.println("***************** GET 1 " + result);
		assertEquals(3, result.size());
		assertTrue(result.contains(contact1));
		assertTrue(result.contains(contact2));
		assertTrue(result.contains(contact3));

		List<String> result2 = manager.getExternalContactListId(id2);
		//System.out.println("***************** GET 2 " + result2);
		assertEquals(3, result2.size());
		assertTrue(result2.contains(contact12));
		assertTrue(result2.contains(contact22));
		assertTrue(result2.contains(contact32));

	}

	@Test
	public void testsetExternalContact() throws CassandraException 
	{
		ujs1.ID = contact1;
		ujs2.ID = contact2;
		ujs3.ID = contact3;
		manager.setExternalContact(id, ujs1);
		manager.setExternalContact(id, ujs2);
		manager.setExternalContact(id, ujs3);
	}
	
	@Test
	public void testgetExternalContactList() throws CassandraException {
		Vector<ExternalContact> result = manager.getExternalContactList(id);
		//System.out.println("***************** GET 1 " + result);
		assertEquals(3, result.size());
		assertEquals(ujs1.ID, result.get(0).ID);		
		assertEquals(ujs2.ID, result.get(1).ID);		
		assertEquals(ujs3.ID, result.get(2).ID);		
	}

	@Test
	public void testgetExternalContact() throws CassandraException 
	{
		ExternalContact copy_ujs1 = manager.getExternalContact(id, contact1);
		ExternalContact copy_ujs2 = manager.getExternalContact(id, contact2);
		ExternalContact copy_ujs3 = manager.getExternalContact(id, contact3);

		assertEquals(ujs1.ID, copy_ujs1.ID);
		assertEquals(ujs1.firstName, copy_ujs1.firstName);
		assertEquals(ujs1.email, copy_ujs1.email);
		assertEquals(ujs1.lastName, copy_ujs1.lastName);
		assertEquals(ujs1.organization, copy_ujs1.organization);
		assertEquals(ujs1.personalNote, copy_ujs1.personalNote);
		assertEquals(ujs1.update.length, copy_ujs1.update.length);
		assertEquals(ujs1.update.last, copy_ujs1.update.last);
		assertEquals(ujs1.update.periodType, copy_ujs1.update.periodType);
		
		assertEquals(ujs2.ID, copy_ujs2.ID);
		assertEquals(ujs2.firstName, copy_ujs2.firstName);
		assertEquals(ujs2.email, copy_ujs2.email);
		assertEquals(ujs2.lastName, copy_ujs2.lastName);
		assertEquals(ujs2.organization, copy_ujs2.organization);
		assertEquals(ujs2.personalNote, copy_ujs2.personalNote);
		assertEquals(ujs2.update.length, copy_ujs2.update.length);
		assertEquals(ujs2.update.last, copy_ujs2.update.last);
		assertEquals(ujs2.update.periodType, copy_ujs2.update.periodType);
		
		assertEquals(ujs3.ID, copy_ujs3.ID);
		assertEquals(ujs3.firstName, copy_ujs3.firstName);
		assertEquals(ujs3.email, copy_ujs3.email);
		assertEquals(ujs3.lastName, copy_ujs3.lastName);
		assertEquals(ujs3.organization, copy_ujs3.organization);
		assertEquals(ujs3.personalNote, copy_ujs3.personalNote);
		assertEquals(ujs3.update.length, copy_ujs3.update.length);
		assertEquals(ujs3.update.last, copy_ujs3.update.last);
		assertEquals(ujs3.update.periodType, copy_ujs3.update.periodType);
		
		
	}

	@Test
	public void testdeleteExternalContact() throws CassandraException 
	{
		manager.deleteExternalContact(id, contact2);
		manager.deleteExternalContact(id2, contact32);
		
		List<String> result = manager.getExternalContactListId(id);
		//System.out.println("***************** GET 1 " + result);
		assertEquals(2, result.size());
		assertTrue(result.contains(contact1));
		assertTrue(result.contains(contact3));

		List<String> result2 = manager.getExternalContactListId(id2);
		//System.out.println("***************** GET 2 " + result2);
		assertEquals(2, result2.size());
		assertTrue(result2.contains(contact12));
		assertTrue(result2.contains(contact22));
	}
	
}
