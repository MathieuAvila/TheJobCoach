package com.TheJobCoach.userdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Vector;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.TodoCommon;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;


public class TestUserExternalContactManager {
	
	Logger logger = LoggerFactory.getLogger(TestUserJobSiteManager.class);

	static UserExternalContactManager manager = new UserExternalContactManager();
	
	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static UserId id2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);
	
	static String contact1 = "contact1";
	static String contact2 = "contact2";
	static String contact3 = "contact3";
		
	static String contact12 = "contact1";
	static String contact22 = "contact2";
	static String contact32 = "contact3";

	static ExternalContact ujs1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	static ExternalContact ujs2 = new ExternalContact(contact2, "firstName2", "lastName2", "email2", "phone2", "personalNote2", "organization2", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	static ExternalContact ujs3 = new ExternalContact(contact3, "firstName3", "lastName3", "email3", "phone3", "personalNote3", "organization3", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	
	static ExternalContact ujs21 = new ExternalContact(contact12, "firstName12", "lastName12", "email12", "phone21", "personalNote12", "organization12", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	static ExternalContact ujs22 = new ExternalContact(contact22, "firstName22", "lastName22", "email22", "phone22", "personalNote22", "organization22", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	static ExternalContact ujs23 = new ExternalContact(contact32, "firstName32", "lastName32", "email32", "phone23", "personalNote32", "organization32", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));

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
	public void testCleanUsercontact() throws CassandraException, SystemException
	{
		List<String> result = manager.getExternalContactListId(id);
		for (String contact: result)
		{
			manager.deleteExternalContact(id, contact);
		}
		result = manager.getExternalContactListId(id2);
		for (String contact: result)
		{
			manager.deleteExternalContact(id2, contact);
		}

		// Check that we don't have any update yet
		TestUserValues.clean(id);
		TestUserValues.checkValue(id, UserValuesConstantsCoachMessages.COACH_USER_ACTION_CONTACT, "0");

		manager.setExternalContact(id, ujs1);
		manager.setExternalContact(id, ujs2);
		manager.setExternalContact(id, ujs3);

		manager.setExternalContact(id2, ujs21);
		manager.setExternalContact(id2, ujs22);
		manager.setExternalContact(id2, ujs23);	
		
		TestUserValues.checkValue(id, UserValuesConstantsCoachMessages.COACH_USER_ACTION_CONTACT, "1");

		// getExternalContactListId 
		List<String> result4 = manager.getExternalContactListId(id);
		assertEquals(3, result4.size());
		assertTrue(result4.contains(contact1));
		assertTrue(result4.contains(contact2));
		assertTrue(result4.contains(contact3));

		List<String> result2 = manager.getExternalContactListId(id2);
		assertEquals(3, result2.size());
		assertTrue(result2.contains(contact12));
		assertTrue(result2.contains(contact22));
		assertTrue(result2.contains(contact32));

	}

	@Test
	public void testsetExternalContact() throws CassandraException, SystemException 
	{
		TestTodoList todoInterface = new TestTodoList();
		UserExternalContactManager.todoList = todoInterface;
	
		ujs1.ID = contact1;
		ujs2.ID = contact2;
		ujs3.ID = contact3;
		
		manager.setExternalContact(id, ujs2);
		// no update: recall == false
		assertEquals(0, todoInterface.setEvents.size());
				
		
		manager.setExternalContact(id, ujs1);
		assertEquals(1, todoInterface.setEvents.size());
		// update: recall == true
		TodoEvent todoExternalContact1 = todoInterface.setEvents.get(0).result;
		assertEquals(TodoCommon.EXTERNALCONTACTMANAGER_SUBSCRIBER_ID, todoExternalContact1.eventSubscriber);
		assertEquals(ujs1.firstName, todoExternalContact1.systemText.get(TodoCommon.FIRSTNAME));
		assertEquals(ujs1.lastName, todoExternalContact1.systemText.get(TodoCommon.LASTNAME));
		assertEquals(ujs1.organization, todoExternalContact1.systemText.get(TodoCommon.COMPANY));
		assertEquals(ujs1.ID, todoExternalContact1.ID);
		assertEquals(FormatUtil.getDateString(ujs1.update.last), todoExternalContact1.systemText.get(TodoCommon.LAST));
		
		manager.setExternalContact(id, ujs3);
		
		// test getExternalContactList
		
		Vector<ExternalContact> result = manager.getExternalContactList(id);
		assertEquals(3, result.size());
		assertEquals(ujs1.ID, result.get(0).ID);		
		assertEquals(ujs2.ID, result.get(1).ID);		
		assertEquals(ujs3.ID, result.get(2).ID);		
	
		// test getExternalContact
		
		ExternalContact copy_ujs1 = manager.getExternalContact(id, contact1);
		ExternalContact copy_ujs2 = manager.getExternalContact(id, contact2);
		ExternalContact copy_ujs3 = manager.getExternalContact(id, contact3);

		assertEquals(ujs1.ID, copy_ujs1.ID);
		assertEquals(ujs1.firstName, copy_ujs1.firstName);
		assertEquals(ujs1.email, copy_ujs1.email);
		assertEquals(ujs1.lastName, copy_ujs1.lastName);
		assertEquals(ujs1.organization, copy_ujs1.organization);
		assertEquals(ujs1.personalNote, copy_ujs1.personalNote);
		assertEquals(ujs1.phone, copy_ujs1.phone);
		assertEquals(ujs1.update.length, copy_ujs1.update.length);
		assertEquals(ujs1.update.last, copy_ujs1.update.last);
		assertEquals(ujs1.update.periodType, copy_ujs1.update.periodType);
		assertEquals(ujs1.update.needRecall, copy_ujs1.update.needRecall);
		
		assertEquals(ujs2.ID, copy_ujs2.ID);
		assertEquals(ujs2.firstName, copy_ujs2.firstName);
		assertEquals(ujs2.email, copy_ujs2.email);
		assertEquals(ujs2.lastName, copy_ujs2.lastName);
		assertEquals(ujs2.organization, copy_ujs2.organization);
		assertEquals(ujs2.personalNote, copy_ujs2.personalNote);
		assertEquals(ujs2.phone, copy_ujs2.phone);
		assertEquals(ujs2.update.length, copy_ujs2.update.length);
		assertEquals(ujs2.update.last, copy_ujs2.update.last);
		assertEquals(ujs2.update.periodType, copy_ujs2.update.periodType);
		assertEquals(ujs2.update.needRecall, copy_ujs2.update.needRecall);
		
		assertEquals(ujs3.ID, copy_ujs3.ID);
		assertEquals(ujs3.firstName, copy_ujs3.firstName);
		assertEquals(ujs3.email, copy_ujs3.email);
		assertEquals(ujs3.lastName, copy_ujs3.lastName);
		assertEquals(ujs3.organization, copy_ujs3.organization);
		assertEquals(ujs3.personalNote, copy_ujs3.personalNote);
		assertEquals(ujs3.phone, copy_ujs3.phone);
		assertEquals(ujs3.update.length, copy_ujs3.update.length);
		assertEquals(ujs3.update.last, copy_ujs3.update.last);
		assertEquals(ujs3.update.periodType, copy_ujs3.update.periodType);
		assertEquals(ujs3.update.needRecall, copy_ujs3.update.needRecall);
		
		// test deleteExternalContact

		manager.deleteExternalContact(id, contact2);
		manager.deleteExternalContact(id2, contact32);
		
		List<String> result2 = manager.getExternalContactListId(id);
		assertEquals(2, result2.size());
		assertTrue(result2.contains(contact1));
		assertTrue(result2.contains(contact3));

		List<String> result3 = manager.getExternalContactListId(id2);
		assertEquals(2, result3.size());
		assertTrue(result3.contains(contact12));
		assertTrue(result3.contains(contact22));
		

		// check return not there anymore after delete
		assertTrue(manager.isEventValid(id, todoExternalContact1)); // There before delete
		manager.deleteExternalContact(id, contact1);//  delete
		assertTrue(!manager.isEventValid(id, todoExternalContact1));// Not There after delete

	}
	
}
