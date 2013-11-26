package com.TheJobCoach.userdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.common.collect.ImmutableMap;

public class TestTodoList
{
	Logger logger = LoggerFactory.getLogger(TestTodoList.class);
	
	static TodoList manager = new TodoList();

	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static UserId id2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);
	static UserId id_cb = new UserId("user_cb", "token", UserId.UserType.USER_TYPE_SEEKER);

	static String todoeventid1 = "1";
	static String todoeventid2 = "2";
	static String todoeventid3 = "3";

	static String todoeventid12 = "1";
	static String todoeventid22 = "2";
	static String todoeventid32 = "3slightdifference";

	final static String S1 = "s1";
	final static String S2 = "s2";
	
	static Subscriber s1 = null;
	static Subscriber s2 = null;
	
	{
		if (s1 == null)
		{
			s1 = new Subscriber(S1);
			s2 = new Subscriber(S2);
			TodoList.registerTodoListSubscriber(s1);
			TodoList.registerTodoListSubscriber(s2);
		}
	}
	
	static HashMap<String, String> systemlist1 = new HashMap<String, String>(ImmutableMap.of(
	        "b", "b_v",
	        "a", "a_v",
	        "c", "c_v"
	        ));
	static HashMap<String, String> systemlist2 = new HashMap<String, String>(ImmutableMap.of(
	        "b", "b_v2",
	        "a", "a_v2"
	        ));
	static TodoEvent todoevent1 = new TodoEvent(todoeventid1, "todo1", systemlist1,  "test", TodoEvent.Priority.URGENT,  new Date(1), TodoEvent.EventColor.BLUE, 11, 1, 2,3);
	static TodoEvent todoevent2 = new TodoEvent(todoeventid2, "todo2", systemlist2,  "", TodoEvent.Priority.NORMAL,  new Date(2), TodoEvent.EventColor.GREEN, 12, 2, 2,3);
	static TodoEvent todoevent3 = new TodoEvent(todoeventid3, "todo3", new HashMap<String, String>(),  "", TodoEvent.Priority.WARNING,  new Date(3), TodoEvent.EventColor.RED, 13, 3, 2,3);

	static TodoEvent todoevent21 = new TodoEvent(todoeventid12, "todo21", new HashMap<String, String>(),  "test", TodoEvent.Priority.URGENT,  new Date(1), TodoEvent.EventColor.BLUE, 11, 1, 2,3);
	static TodoEvent todoevent22 = new TodoEvent(todoeventid22, "todo22", new HashMap<String, String>(),  "", TodoEvent.Priority.NORMAL,  new Date(2), TodoEvent.EventColor.GREEN, 12, 2, 2,3);
	static TodoEvent todoevent23 = new TodoEvent(todoeventid32, "todo23", new HashMap<String, String>(), "", TodoEvent.Priority.WARNING,  new Date(3), TodoEvent.EventColor.RED, 13, 3, 2,3);
	
	static String todoeventid1_cb  = "1cb";
	static String todoeventid12_cb = "12cb";
	static String todoeventid2_cb  = "2cb";

	static HashMap<String, String> hash1 = new HashMap<String, String>(ImmutableMap.of(
			  "sys1", "A1",
			  "sys2", "B1",
			  "sys3", "C1"));
	static HashMap<String, String> hash2 = new HashMap<String, String>(ImmutableMap.of(
			  "sys21", "A2",
			  "sys22", "B2",
			  "sys23", "C2"));
	
	static TodoEvent todoevent1_callback = new TodoEvent(todoeventid1_cb, "todo21", hash1,  S1, TodoEvent.Priority.URGENT,  new Date(1), TodoEvent.EventColor.BLUE, 11, 1, 2,3);
	static TodoEvent todoevent12_callback = new TodoEvent(todoeventid12_cb, "todo21", hash1,  S1, TodoEvent.Priority.URGENT,  new Date(1), TodoEvent.EventColor.BLUE, 11, 1, 2,3);
	static TodoEvent todoevent2_callback = new TodoEvent(todoeventid2_cb, "todo22", hash2,  S2, TodoEvent.Priority.NORMAL,  new Date(2), TodoEvent.EventColor.GREEN, 12, 2, 2,3);

	class Subscriber implements TodoList.TodoListSubscriber 
	{
		String id = "";
		
		public Vector<String> done = new Vector<String>();
		public HashMap<String, Boolean> validList = new HashMap<String, Boolean>();
		
		public Subscriber(String id)
		{
			this.id = id;
		}

		@Override
		public String getSubscriberId()
		{
			return id;
		}

		@Override
		public void event(UserId id, TodoEvent event)
		{
			
		}
		
		int callsValid = 0;
		@Override
		public boolean isEventValid(UserId id, TodoEvent event)
		{
			//logger.info("check " + event.ID + " from "+ this);
			callsValid++;
			if (validList.containsKey(event.ID))
			{
				return validList.get(event.ID);
			}
			return true;
		}

		@Override
		public void eventDone(UserId id, TodoEvent event)
		{
			done.add(id.userName + "_" + event.ID);
		}

		public void reset()
		{
			done = new Vector<String>();
			validList = new HashMap<String, Boolean>();
			callsValid = 0;
		}
	}

	@Test
	public void testCleanTodoEvent() throws CassandraException
	{
		Vector<TodoEvent> result = manager.getTodoEventList(id, "FR");
		for (TodoEvent event: result)
		{
			manager.deleteTodoEvent(id, event.ID, false);
		}
		result = manager.getTodoEventList(id2, "FR");
		for (TodoEvent event: result)
		{
			manager.deleteTodoEvent(id2, event.ID, false);
		}
		manager.setTodoEvent(id, todoevent1);
		manager.setTodoEvent(id, todoevent2);
		manager.setTodoEvent(id, todoevent3);

		manager.setTodoEvent(id2, todoevent21);
		manager.setTodoEvent(id2, todoevent22);
		manager.setTodoEvent(id2, todoevent23);	

		result = manager.getTodoEventList(id, "FR");
		assertEquals(3, result.size());
		assertTrue(result.get(0).ID.equals(todoeventid1));
		assertTrue(result.get(1).ID.equals(todoeventid2));
		assertTrue(result.get(2).ID.equals(todoeventid3));

		Vector<TodoEvent> result2 = manager.getTodoEventList(id2, "FR");
		assertEquals(3, result2.size());
		assertTrue(result2.get(0).ID.equals(todoeventid12));
		assertTrue(result2.get(1).ID.equals(todoeventid22));
		assertTrue(result2.get(2).ID.equals(todoeventid32));
	}

	@Test
	public void testgetTodoEvent() throws CassandraException 
	{
		manager.setTodoEvent(id, todoevent1);
		manager.setTodoEvent(id, todoevent2);
		manager.setTodoEvent(id, todoevent3);

		TodoEvent copy_todoevent1 = manager.getTodoEvent(id, todoeventid1, "FR");
		TodoEvent copy_todoevent2 = manager.getTodoEvent(id, todoeventid2, "FR");
		TodoEvent copy_todoevent3 = manager.getTodoEvent(id, todoeventid3, "FR");

		assertEquals(todoevent1.text, copy_todoevent1.text);
		assertEquals(todoevent1.eventSubscriber,       copy_todoevent1.eventSubscriber);
		assertEquals(todoevent1.priority,    copy_todoevent1.priority);
		assertEquals(todoevent1.eventDate,        copy_todoevent1.eventDate);
		assertEquals(todoevent1.color,         copy_todoevent1.color);
		assertEquals(todoevent1.ID,          copy_todoevent1.ID);
		assertEquals(todoevent1.x,          copy_todoevent1.x);
		assertEquals(todoevent1.y,          copy_todoevent1.y);
		assertEquals(todoevent1.w,          copy_todoevent1.w);
		assertEquals(todoevent1.h,          copy_todoevent1.h);
		assertEquals(todoevent1.systemText,          copy_todoevent1.systemText);

		assertEquals(todoevent2.text, copy_todoevent2.text);
		assertEquals(todoevent2.eventSubscriber,       copy_todoevent2.eventSubscriber);
		assertEquals(todoevent2.priority,    copy_todoevent2.priority);
		assertEquals(todoevent2.eventDate,        copy_todoevent2.eventDate);
		assertEquals(todoevent2.color,         copy_todoevent2.color);
		assertEquals(todoevent2.ID,          copy_todoevent2.ID);
		assertEquals(todoevent2.x,          copy_todoevent2.x);
		assertEquals(todoevent2.y,          copy_todoevent2.y);
		assertEquals(todoevent2.w,          copy_todoevent1.w);
		assertEquals(todoevent2.h,          copy_todoevent1.h);
		assertEquals(todoevent2.systemText,          copy_todoevent2.systemText);

		assertEquals(todoevent3.text, copy_todoevent3.text);
		assertEquals(todoevent3.eventSubscriber,       copy_todoevent3.eventSubscriber);
		assertEquals(todoevent3.priority,    copy_todoevent3.priority);
		assertEquals(todoevent3.eventDate,        copy_todoevent3.eventDate);
		assertEquals(todoevent3.color,         copy_todoevent3.color);
		assertEquals(todoevent3.ID,          copy_todoevent3.ID);
		assertEquals(todoevent3.x,          copy_todoevent3.x);
		assertEquals(todoevent3.y,          copy_todoevent3.y);
		assertEquals(todoevent3.w,          copy_todoevent1.w);
		assertEquals(todoevent3.h,          copy_todoevent1.h);
		assertEquals(todoevent3.systemText,          copy_todoevent3.systemText);
	}
	
	@Test
	public void testgetInvalidTodoEvent() throws CassandraException 
	{
		boolean bad = false;
		try {
			manager.getTodoEvent(id, "fake", "FR");
		}
		catch (CassandraException e)
		{
			bad = true;
		}
		assertTrue(bad);
	}

	@Test
	public void testdeleteTodoEvent() throws CassandraException 
	{
		manager.deleteTodoEvent(id, todoeventid1, false);
		manager.deleteTodoEvent(id2, todoeventid12, false);

		Vector<TodoEvent> result = manager.getTodoEventList(id, "FR");
		assertEquals(2, result.size());
		assertTrue(result.get(0).ID.equals(todoeventid2));
		assertTrue(result.get(1).ID.equals(todoeventid3));

		Vector<TodoEvent> result2 = manager.getTodoEventList(id2, "FR");
		assertEquals(2, result2.size());
		assertTrue(result2.get(0).ID.equals(todoeventid22));
		assertTrue(result2.get(1).ID.equals(todoeventid32));		
	}	
	
	@Test
	public void testCallbackTodoEvent() throws CassandraException 
	{
		// clean up first
		try{manager.deleteTodoEvent(id_cb, todoeventid1_cb, false);} catch (Exception e) {}
		try{manager.deleteTodoEvent(id_cb, todoeventid12_cb, false);} catch (Exception e) {}
		try{manager.deleteTodoEvent(id_cb, todoeventid2_cb, false);} catch (Exception e) {}
		
		// check voidiness
		Vector<TodoEvent> result = manager.getTodoEventList(id_cb, "FR");
		assertEquals(0, result.size());
		
		// Normally no difference with normal case
		manager.setTodoEvent(id_cb, todoevent1_callback);
		manager.setTodoEvent(id_cb, todoevent12_callback);
		manager.setTodoEvent(id_cb, todoevent2_callback);
		
		// Get and check validity is checked
		s1.reset();
		s2.reset();

		result = manager.getTodoEventList(id_cb, "FR");
		assertEquals(3, result.size());
		assertTrue(result.get(1).ID.equals(todoeventid1_cb));
		assertTrue(result.get(0).ID.equals(todoeventid12_cb));
		assertTrue(result.get(2).ID.equals(todoeventid2_cb));
		logger.info("check " + s1 + " " + s1.callsValid);
		assertEquals(2, s1.callsValid);
		assertEquals(1, s2.callsValid);
		
		// If validity is false, removed definitively from list
		s1.reset();
		s2.reset();
		s1.validList.put(todoeventid1_cb, false);
		for (int loop = 0; loop != 2; loop++)
		{
			result = manager.getTodoEventList(id_cb, "FR");
			assertEquals(2, result.size());
			assertTrue(result.get(0).ID.equals(todoeventid12_cb));
			assertTrue(result.get(1).ID.equals(todoeventid2_cb));
		}
		
		// Check DONE callback
		s1.reset();
		s2.reset();
		manager.setTodoEvent(id_cb, todoevent1_callback);
		manager.setTodoEvent(id_cb, todoevent12_callback);
		manager.setTodoEvent(id_cb, todoevent2_callback);
		manager.deleteTodoEvent(id_cb, todoeventid12_cb, true);
		assertEquals(1, s1.done.size());
		assertEquals(0, s2.done.size());
		assertEquals(id_cb.userName + "_" + todoeventid12_cb, s1.done.get(0));
		s1.reset();
		s2.reset();
		manager.deleteTodoEvent(id_cb, todoeventid2_cb, true);
		assertEquals(1, s2.done.size());
		assertEquals(0, s1.done.size());
		assertEquals(id_cb.userName + "_" + todoeventid2_cb, s2.done.get(0));
		
		// Check doubleRegister
		s1.reset();
		s2.reset();
		TodoList.registerTodoListSubscriber(s1);
		manager.deleteTodoEvent(id_cb, todoeventid1_cb, true);
		assertEquals(1, s1.done.size());
		assertEquals(0, s2.done.size());
		assertEquals(id_cb.userName + "_" + todoeventid1_cb, s1.done.get(0));
	}

	@Test
	public void testInvalidTodoEventInList() throws CassandraException 
	{
		try{manager.deleteTodoEvent(id, todoeventid1, false);} catch (Exception e) {}
		try{manager.deleteTodoEvent(id, todoeventid2, false);} catch (Exception e) {}
		try{manager.deleteTodoEvent(id, todoeventid3, false);} catch (Exception e) {}
		manager.setTodoEvent(id, todoevent1);
		manager.setTodoEvent(id, todoevent2);
		manager.setTodoEvent(id, todoevent3);
		
		// RAW Destroy todoevent2
		String reqId = id.userName + "_" + todoeventid2;
		logger.info("raw delete:" + reqId);
		CassandraAccessor.deleteKey(TodoList.COLUMN_FAMILY_NAME_DATA, reqId);
		
		// Check not in list
		Vector<TodoEvent> result = manager.getTodoEventList(id, "FR");
		assertEquals(2, result.size());
		assertTrue(result.get(0).ID.equals(todoeventid1));
		assertTrue(result.get(1).ID.equals(todoeventid3));
		
	}

}

