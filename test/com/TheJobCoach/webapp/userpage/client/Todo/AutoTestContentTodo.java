package com.TheJobCoach.webapp.userpage.client.Todo;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.ErrorCatcherMessageBox;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.TodoCommon;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestContentTodo extends GwtTest {

	static Logger logger = LoggerFactory.getLogger(AutoTestContentTodo.class);

	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}

	private ContentTodo cud;

	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	String todoeventid1 = "1";
	String todoeventid2 = "2";
	String todoeventid3 = "3";

	HashMap<String, String> systemlist1 = new HashMap<String, String>(ImmutableMap.of(
			"b", "b_v",
			"a", "a_v",
			"c", "c_v"
			));
	HashMap<String, String> systemlist2 = new HashMap<String, String>(ImmutableMap.of(
			"b", "b_v2",
			"a", "a_v2"
			));

	TodoEvent todoevent1 = new TodoEvent(todoeventid1, "todo1", systemlist1,  
			TodoCommon.PERSO_SUBSCRIBER_ID, TodoEvent.Priority.URGENT,  new Date(1), TodoEvent.EventColor.BLUE, 
			11, 1, 2,3);
	
	TodoEvent todoevent2 = new TodoEvent(todoeventid2, "todo2", systemlist2,  
			TodoCommon.SITEMANAGER_SUBSCRIBER_ID, TodoEvent.Priority.NORMAL,  
			new Date(2), 
			TodoEvent.EventColor.GREEN, 
			12, 2, 2,3);
	
	TodoEvent todoevent3 = new TodoEvent(todoeventid3, "todo3", new HashMap<String, String>(),  
			TodoCommon.PERSO_SUBSCRIBER_ID, TodoEvent.Priority.WARNING,  new Date(3), 
			TodoEvent.EventColor.RED, 
			13, 3, 2,3);

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		int calls;
		Vector<TodoEvent> toGet = new Vector<TodoEvent>();
		String deleted;
		String setTodo;
		
		public void reset()
		{
			calls = 0;
			toGet.clear();
			toGet.add(todoevent1);
			toGet.add(todoevent2);
			toGet.add(todoevent3);
			deleted = "";
			setTodo = "";
		}
		
		@Override
		public void getTodoEventList(UserId id, String lang,
				AsyncCallback<Vector<TodoEvent>> callback)
		{
			calls++;
			logger.info("getTodoEventList " + id.userName);
			assertEquals(userId.userName, id.userName);
			callback.onSuccess(toGet);
		}
		
		@Override
		public void deleteTodoEvent(UserId id, String todo, boolean done,
				AsyncCallback<Boolean> callback)
		{
			calls++;
			logger.info("deleteTodoEvent " + id.userName);
			assertEquals(userId.userName, id.userName);
			deleted = todo;
			callback.onSuccess(true);
		}
		
		@Override
		public void setTodoEvent(UserId id, TodoEvent todo,
				AsyncCallback<Boolean> callback)
		{
			calls++;
			logger.info("setTodoEvent " + id.userName);
			assertEquals(userId.userName, id.userName);
			setTodo = todo.ID;
			callback.onSuccess(true);
		}
	}

	static SpecialUserServiceAsync userService;

	HorizontalPanel p;

	@Before
	public void beforeContentTodo()
	{
		if ( userService == null) userService = new SpecialUserServiceAsync();
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					return userService;
				}
				return null;
			}}
				);
		p = new HorizontalPanel();		
	}

	class TodoContainerTest extends FlowPanel implements ITodoContainer
	{
		Vector<TodoEvent> received;
		Vector<TodoEvent> created;
		Vector<TodoEvent> removed;
		Vector<TodoEvent> confirmed;
		
		void reset()
		{
			received = new Vector<TodoEvent>();
			created = new Vector<TodoEvent>();
			removed = new Vector<TodoEvent>();
			confirmed = new Vector<TodoEvent>();
		}
		
		@Override
		public void onTodoEventReceived(TodoEvent event)
		{
			received.add(event);
			logger.info("onTodoEventReceived " + event.ID);
		}

		@Override
		public void onTodoEventCreated(TodoEvent event)
		{
			created.add(event);
			logger.info("onTodoEventCreated " + event.ID);
		}

		@Override
		public void onRemoveTodoEvent(TodoEvent event)
		{
			removed.add(event);
			logger.info("onRemoveTodoEvent " + event.ID);
		}

		@Override
		public void confirmRemoveTodoEvent(TodoEvent event)
		{
			confirmed.add(event);
			logger.info("confirmRemoveTodoEvent " + event.ID);
		}

		@Override
		public void notifyVisibility(String key, int valueInt)
		{
			// TODO Auto-generated method stub
			
		}
	}

	@Test
	public void testAllValue()
	{
		userService.reset();		
		ErrorCatcherMessageBox mbCatcher = new ErrorCatcherMessageBox();
		
		cud = new ContentTodo(
				p, userId);
		TodoContainerTest container = new TodoContainerTest();
		container.reset();
		cud.surface = container;
		cud.onModuleLoad();		
		assertEquals(1, userService.calls);
		assertEquals(3, container.received.size());
		assertEquals(0, container.created.size());
		assertEquals(0, container.removed.size());
		assertEquals(0, container.confirmed.size());
		@SuppressWarnings("unchecked")
		Vector<TodoEvent> recvSave = (Vector<TodoEvent>)container.received.clone();
		
		// Create new ONE
		container.reset();
		userService.reset();		
		cud.button.click();
		assertEquals(1, userService.calls);
		assertEquals(0, container.received.size());
		assertEquals(1, container.created.size());
		assertEquals(0, container.removed.size());
		assertEquals(0, container.confirmed.size());

		// Ask for update
		container.reset();
		userService.calls = 0;
		cud.setTodoEvent(recvSave.get(0));
		assertEquals(1, userService.calls);
		assertEquals(todoeventid1, userService.setTodo);
				
		// Ask for deletion
		container.reset();
		userService.calls = 0;
		cud.deleteTodoEvent(recvSave.get(0));
		assertNotNull(mbCatcher.currentBox);
		assertEquals(mbCatcher.type, MessageBox.TYPE.QUESTION);
		assertTrue(mbCatcher.title.contains("Confirmer la suppression"));
		assertTrue(mbCatcher.message.contains("supprimer le post-it"));
		mbCatcher.currentBox.clickOk();
		assertEquals(1, userService.calls);
		assertEquals(todoeventid1, userService.deleted);
		assertEquals(0, container.received.size());
		assertEquals(0, container.created.size());
		assertEquals(0, container.removed.size());
		assertEquals(1, container.confirmed.size());
		assertEquals(todoeventid1, container.confirmed.get(0).ID);
		
		userService.reset();
	}

}
