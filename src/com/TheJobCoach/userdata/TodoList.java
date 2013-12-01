package com.TheJobCoach.userdata;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent.EventColor;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent.Priority;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;

public class TodoList implements ITodoList
{	
	private static Logger logger = LoggerFactory.getLogger(TodoList.class);
	
	private static HashMap<Priority, String> priority2String = new HashMap<Priority, String>();
	private static HashMap<String, Priority> string2Priority = new HashMap<String, Priority>();

	static
	{
		priority2String.put(Priority.URGENT, "U");
		priority2String.put(Priority.WARNING, "W");
		priority2String.put(Priority.NORMAL, "N");
		priority2String.put(Priority.LOWPRIORITY, "L");
		for (Priority s: priority2String.keySet()) string2Priority.put(priority2String.get(s), s);
	}

	private static HashMap<EventColor, String> eventColor2String = new HashMap<EventColor, String>();
	private static HashMap<String, EventColor> string2EventColor = new HashMap<String, EventColor>();
	static
	{
		eventColor2String.put(EventColor.WHITE, "W");
		eventColor2String.put(EventColor.GREEN, "G");
		eventColor2String.put(EventColor.BLUE, "B");
		eventColor2String.put(EventColor.RED, "R");
		eventColor2String.put(EventColor.LIGHTGREEN, "g");
		eventColor2String.put(EventColor.LIGHTBLUE, "b");
		eventColor2String.put(EventColor.LIGHTRED, "r");
		eventColor2String.put(EventColor.YELLOW, "Y");
		eventColor2String.put(EventColor.ORANGE, "O");
		for (EventColor s: eventColor2String.keySet()) string2EventColor.put(eventColor2String.get(s), s);
	}

	public interface TodoListSubscriber
	{
		public String getSubscriberId();
		public void event(UserId id, TodoEvent event);
		public boolean isEventValid(UserId id, TodoEvent event);
		public void eventDone(UserId id, TodoEvent event);
	}
	
	static ColumnFamilyDefinition cfDef = null;
	static ColumnFamilyDefinition cfDefData = null;

	final static String COLUMN_FAMILY_NAME_TODOLIST = "todolistlist";
	final static String COLUMN_FAMILY_NAME_DATA = "todolistdata";

	public TodoList()
	{
		cfDef = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_TODOLIST, cfDef);
		cfDefData = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_DATA, cfDefData);
	}
	
	static private HashMap<String, TodoListSubscriber> subscriberMap = new HashMap<String, TodoListSubscriber>();
		
	static public void registerTodoListSubscriber(TodoListSubscriber subscriber)
	{
		if (!subscriberMap.containsKey(subscriber.getSubscriberId()))
		{
			subscriberMap.put(subscriber.getSubscriberId(), subscriber);
		}
	}

	public TodoEvent getTodoEvent(UserId id, String ID, String lang) throws CassandraException 
	{
		String reqId = id.userName + "_" + ID;
		
		Map<String, String> res = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (res == null) throw new CassandraException();
		ShortMap resultReq = new ShortMap(res);
		
		logger.info("subscriber " +resultReq.getString("subscriber"));
		logger.info("text " + resultReq.getString("text"));
		
		TodoEvent te = new TodoEvent(
				ID, 
				Convertor.toString(resultReq.getString("text")),
				resultReq.getMap("sysmap"),
				resultReq.getString("subscriber"),
				string2Priority.get(resultReq.getString("priority")),
				resultReq.getDate("eventdate"),
				string2EventColor.get(Convertor.toString(resultReq.getString("color"))),
				resultReq.getInt("x"),
				resultReq.getInt("y"),
				resultReq.getInt("w"),
				resultReq.getInt("h")
				);
		return te;
	}
	
	public void setTodoEvent(UserId id, TodoEvent result) throws CassandraException 
	{
		logger.info("subscriber " +result.eventSubscriber);
		logger.info("text " + result.text);
		
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_TODOLIST, id.userName, (new ShortMap()).add(result.ID, result.ID).get());
		String reqId = id.userName + "_" + result.ID;
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				reqId, 
				(new ShortMap())
				.add("id", result.ID)
				.add("text", result.text)
				.addMap("sysmap", result.systemText)
				.add("subscriber", result.eventSubscriber)
				.add("priority", priority2String.get(result.priority))
				.add("eventdate", Convertor.toString(result.eventDate))
				.add("color", eventColor2String.get(result.color))
				.add("x", result.x)
				.add("y", result.y)
				.add("w", result.w)
				.add("h", result.h)
				.get());		
	}

	public Vector<TodoEvent> getTodoEventList(UserId id, String lang) throws CassandraException
	{
		Vector<TodoEvent> result = new Vector<TodoEvent>();
		Map<String,String> resultReq = null;
		resultReq = CassandraAccessor.getRow(
				COLUMN_FAMILY_NAME_TODOLIST, 
				id.userName);
		if (resultReq == null) return result;
		for (String key: resultReq.keySet())
		{
			try
			{
				TodoEvent todo = getTodoEvent(id, key, lang);
				TodoListSubscriber subscriber = subscriberMap.get(todo.eventSubscriber);
				boolean valid = true;
				if (subscriber != null)
				{
					// Check still validity
					valid = subscriber.isEventValid(id, todo);
				}
				if (valid) result.add(todo);
				else rawDeleteTodo(id, key);
			}
			catch (CassandraException e) // Happens if key does not exist anymore in table. Purge bad entry. 
			{
				logger.info("purge invalid todo key " + key + " for user:" + id.userName);
				deleteTodoEvent(id, key, false);
			}			
		}
		return result;
	}
	
	private void rawDeleteTodo(UserId id, String todoKey) throws CassandraException
	{
		String reqId = id.userName + "_" + todoKey;
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_TODOLIST, id.userName, todoKey);
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, reqId);
	}
	
	public void deleteTodoEvent(UserId id, String todoKey, boolean markDone) throws CassandraException
	{	
		try {
		TodoEvent event = getTodoEvent(id, todoKey, "");
		TodoListSubscriber subscriber = subscriberMap.get(event.eventSubscriber);
		if (subscriber != null)
		{
			// call for deletion
			subscriber.eventDone(id, event);
		}
		}
		catch (CassandraException e) {
			// possible if partially destroyed
		}
		rawDeleteTodo(id, todoKey);
	}

}
