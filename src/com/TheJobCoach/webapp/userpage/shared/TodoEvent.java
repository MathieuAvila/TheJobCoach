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
	
	
}

