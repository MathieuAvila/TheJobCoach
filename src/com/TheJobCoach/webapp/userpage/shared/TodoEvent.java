package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;

public class TodoEvent implements Serializable {

	private static final long serialVersionUID = 1115255124511443730L;

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
	public String eventSubscriber;
	public Priority priority;
	public Date eventDate;
	public EventColor color;
	public int x, y;		

	public TodoEvent(String ID, String text, String eventSubscriber,
			Priority priority, Date eventDate, EventColor color, int x,
			int y)
	{
		this.ID = ID;
		this.text = text;
		this.eventSubscriber = eventSubscriber;
		this.priority = priority;
		this.eventDate = eventDate;
		this.color = color;
		this.x = x;
		this.y = y;
	}	
}

