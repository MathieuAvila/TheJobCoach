package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class TestTodoList {
	
	static TodoList manager = new TodoList();
	
	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static UserId id2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);
	
	static String todoeventid1 = "1";
	static String todoeventid2 = "2";
	static String todoeventid3 = "3";
		
	static String todoeventid12 = "1";
	static String todoeventid22 = "2";
	static String todoeventid32 = "3";

	static TodoEvent todoevent1 = new TodoEvent(todoeventid1, "todo1", "eventsub1", TodoEvent.Priority.URGENT,  new Date(1), TodoEvent.EventColor.BLUE, 11, 1);
	static TodoEvent todoevent2 = new TodoEvent(todoeventid2, "todo2", "eventsub2", TodoEvent.Priority.NORMAL,  new Date(2), TodoEvent.EventColor.GREEN, 12, 2);
	static TodoEvent todoevent3 = new TodoEvent(todoeventid3, "todo3", "eventsub3", TodoEvent.Priority.WARNING,  new Date(3), TodoEvent.EventColor.RED, 13, 3);

	static TodoEvent todoevent21 = new TodoEvent(todoeventid12, "todo21", "eventsub21", TodoEvent.Priority.URGENT,  new Date(1), TodoEvent.EventColor.BLUE, 11, 1);
	static TodoEvent todoevent22 = new TodoEvent(todoeventid22, "todo22", "eventsub22", TodoEvent.Priority.NORMAL,  new Date(2), TodoEvent.EventColor.GREEN, 12, 2);
	static TodoEvent todoevent23 = new TodoEvent(todoeventid32, "todo23", "eventsub23", TodoEvent.Priority.WARNING,  new Date(3), TodoEvent.EventColor.RED, 13, 3);
	
	@Test
	public void testCleanUserSite() throws CassandraException
	{
		Vector<TodoEvent> result = manager.getTodoEventList(id);
		for (TodoEvent site: result)
		{
			//System.out.println("Try delete previous site: " + site);
			manager.deleteTodoEvent(id, site.ID);
		}
		result = manager.getTodoEventList(id2);
		for (TodoEvent site: result)
		{
			//System.out.println("Try delete previous site2: " + site);
			manager.deleteTodoEvent(id2, site.ID);
		}
		manager.setTodoEvent(id, todoevent1);
		manager.setTodoEvent(id, todoevent2);
		manager.setTodoEvent(id, todoevent3);

		manager.setTodoEvent(id2, todoevent21);
		manager.setTodoEvent(id2, todoevent22);
		manager.setTodoEvent(id2, todoevent23);	
	}
	
	@Test
	public void testgetTodoEventList() throws CassandraException {
		Vector<TodoEvent> result = manager.getTodoEventList(id);
		//System.out.println("***************** GET 1 " + result);
		assertEquals(3, result.size());
		assertTrue(result.get(0).ID.equals(todoeventid1));
		assertTrue(result.get(1).ID.equals(todoeventid2));
		assertTrue(result.get(2).ID.equals(todoeventid3));

		Vector<TodoEvent> result2 = manager.getTodoEventList(id2);
		//System.out.println("***************** GET 2 " + result2);
		assertEquals(3, result2.size());
		assertTrue(result.get(0).ID.equals(todoeventid12));
		assertTrue(result.get(1).ID.equals(todoeventid22));
		assertTrue(result.get(2).ID.equals(todoeventid32));
	}

	@Test
	public void testsetTodoEvent() throws CassandraException 
	{
		manager.setTodoEvent(id, todoevent1);
		manager.setTodoEvent(id, todoevent2);
		manager.setTodoEvent(id, todoevent3);
	}
	
	@Test
	public void testgetTodoEvent() throws CassandraException 
	{
		TodoEvent copy_todoevent1 = manager.getTodoEvent(id, todoeventid1);
		TodoEvent copy_todoevent2 = manager.getTodoEvent(id, todoeventid2);
		TodoEvent copy_todoevent3 = manager.getTodoEvent(id, todoeventid3);
		
		assertEquals(todoevent1.text, copy_todoevent1.text);
		assertEquals(todoevent1.eventSubscriber,       copy_todoevent1.eventSubscriber);
		assertEquals(todoevent1.priority,    copy_todoevent1.priority);
		assertEquals(todoevent1.eventDate,        copy_todoevent1.eventDate);
		assertEquals(todoevent1.color,         copy_todoevent1.color);
		assertEquals(todoevent1.ID,          copy_todoevent1.ID);
		assertEquals(todoevent1.x,          copy_todoevent1.x);
		assertEquals(todoevent1.y,          copy_todoevent1.y);

		assertEquals(todoevent2.text, copy_todoevent2.text);
		assertEquals(todoevent2.eventSubscriber,       copy_todoevent2.eventSubscriber);
		assertEquals(todoevent2.priority,    copy_todoevent2.priority);
		assertEquals(todoevent2.eventDate,        copy_todoevent2.eventDate);
		assertEquals(todoevent2.color,         copy_todoevent2.color);
		assertEquals(todoevent2.ID,          copy_todoevent2.ID);
		assertEquals(todoevent2.x,          copy_todoevent2.x);
		assertEquals(todoevent2.y,          copy_todoevent2.y);

		assertEquals(todoevent3.text, copy_todoevent3.text);
		assertEquals(todoevent3.eventSubscriber,       copy_todoevent3.eventSubscriber);
		assertEquals(todoevent3.priority,    copy_todoevent3.priority);
		assertEquals(todoevent3.eventDate,        copy_todoevent3.eventDate);
		assertEquals(todoevent3.color,         copy_todoevent3.color);
		assertEquals(todoevent3.ID,          copy_todoevent3.ID);
		assertEquals(todoevent3.x,          copy_todoevent3.x);
		assertEquals(todoevent3.y,          copy_todoevent3.y);
	}

	@Test
	public void testdeleteTodoEvent() throws CassandraException 
	{
		manager.deleteTodoEvent(id, todoeventid1);
		manager.deleteTodoEvent(id2, todoeventid12);
		
		Vector<TodoEvent> result = manager.getTodoEventList(id);
		//	System.out.println("***************** GET 1 " + result);
		assertEquals(2, result.size());
		assertTrue(result.get(0).ID.equals(todoeventid2));
		assertTrue(result.get(1).ID.equals(todoeventid3));

		Vector<TodoEvent> result2 = manager.getTodoEventList(id2);
		//System.out.println("***************** GET 2 " + result2);
		assertEquals(2, result2.size());
		assertTrue(result.get(0).ID.equals(todoeventid22));
		assertTrue(result.get(1).ID.equals(todoeventid32));		
	}
	
}
