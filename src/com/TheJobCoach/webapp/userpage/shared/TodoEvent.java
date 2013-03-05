package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

public class TodoEvent implements Serializable {

	private static final long serialVersionUID = 1115255124511443730L;

	public static int NO_PLACE = -10000;
	
	public enum Priority { 
		URGENT, 
		WARNING, 
		NORMAL, 
		LOWPRIORITY		
	};		

	public enum EventColor { 
		WHITE, GREEN, BLUE, RED, LIGHTGREEN, LIGHTBLUE, LIGHTRED, YELLOW, ORANGE;
	};

	public String ID;
	public String text;
	public String trText;
	public String eventSubscriber;
	public Priority priority;
	public Date eventDate;
	public EventColor color;
	public int x, y, w, h;		

	public TodoEvent(String ID, String text, String trText, String eventSubscriber,
			Priority priority, Date eventDate, EventColor color, 
			int x,	int y, int w, int h)
	{
		this.ID = ID;
		this.text = text;
		this.trText = trText;
		this.eventSubscriber = eventSubscriber;
		this.priority = priority;
		this.eventDate = eventDate;
		this.color = color;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public TodoEvent()
	{
	}
	
	public String getHtmlColor(EventColor color)
	{
		switch (color)
		{
		case WHITE: return "#EEEEEE"; 
		case GREEN: return "#FF99FF"; 
		case BLUE: return "#00CCFF"; 
		case RED: return "#FF6666"; 
		case LIGHTGREEN: return "#CCFFCC"; 
		case LIGHTBLUE: return "#00FFFF"; 
		case LIGHTRED: return "#FFCCCC"; 
		case YELLOW: return "#FF99FF"; 
		case ORANGE: return "#FFCC99"; 
		};
		return "";
	}
	
	public String getHtmlColor()
	{
		return getHtmlColor(color);
	}

	public EventColor getNextColor()
	{
		switch (color)
		{
		case WHITE: return EventColor.GREEN; 
		case GREEN: return EventColor.BLUE; 
		case BLUE: return EventColor.RED; 
		case RED: return EventColor.LIGHTGREEN; 
		case LIGHTGREEN: return EventColor.LIGHTBLUE; 
		case LIGHTBLUE: return EventColor.LIGHTRED; 
		case LIGHTRED: return EventColor.YELLOW; 
		case YELLOW: return EventColor.ORANGE; 
		case ORANGE: return EventColor.WHITE; 
		};
		return EventColor.GREEN;
	}
	
	static final private int MARGIN = 100;
	
	private static boolean checkCol(int p1, int w1, int p2, int w2)
	{
		if ((p1 - MARGIN <= p2) && (p1+w1 > p2 - MARGIN)) return true;
		if ((p2 - MARGIN <= p1) && (p2+w2 > p1 - MARGIN)) return true;
		return false;
	}
	
	/**
	 * @brief Find one free place for one todo event, with some place surrounding
	 **/
	public static void orderOneTodoEvent(Collection<TodoEvent> actual, TodoEvent newOne, int maxPageSize)
	{
		newOne.x = 0;
		newOne.y = 0;
		while (true)
		{
			boolean ok = true;
			// Is this a valid place to be ? Check against every existing note
			for (TodoEvent index: actual)
			{
				System.out.println(index.x+ " " +   index.w+ " " +   newOne.x+ " " +   newOne.w + " " +   index.y+ " " +    index.h+ " " +   newOne.y+ " " +   newOne.h);
				System.out.println(checkCol(index.x, index.w, newOne.x, newOne.w) + " " + checkCol(index.y, index.h, newOne.y, newOne.h));
				if (checkCol(index.x, index.w, newOne.x, newOne.w) && 
					checkCol(index.y, index.h, newOne.y, newOne.h))
				{
					ok = false;
					break;
				}					
			}
			if (ok)
			{
				actual.add(newOne);
				return;
			}
			newOne.x += 50;
			if (newOne.x > maxPageSize)
			{
				newOne.x = 0;
				newOne.y += 50;
			}
		}
	}
	
	/**
	 * @brief Reorder all todo events and find one place for each of them. 
	 *        The vector's order can be changed.
	 **/
	public static void orderTodoEvent(Collection<TodoEvent> origin, int maxPageSize)
	{
		Vector<TodoEvent> resultOk = new Vector<TodoEvent>();
		Vector<TodoEvent> resultNew = new Vector<TodoEvent>();
		for (TodoEvent index: origin)
		{
			if ((index.y == NO_PLACE)||(index.y == NO_PLACE))
				resultNew.add(index);
			else
				resultOk.add(index);
		}
		for (TodoEvent index: resultNew)
			orderOneTodoEvent(resultOk, index, maxPageSize);
		origin.clear();
		origin.addAll(resultOk);
	}
	
}

