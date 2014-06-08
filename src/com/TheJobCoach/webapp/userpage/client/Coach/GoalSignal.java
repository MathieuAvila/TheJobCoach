package com.TheJobCoach.webapp.userpage.client.Coach;

import java.util.Date;

/**
 * @brief Signaling when something may have changed the goal
 *
 */
public class GoalSignal
{
	static GoalSignal instance = null;
	
	Date lastEvent = new Date();
	int counter = 0;
	
	public static GoalSignal getInstance()
	{
		if (instance == null)
			instance = new GoalSignal();
		return instance;
	}
	
	public void newEvent()
	{
		System.out.println("A new coach event was sent. Counter is " + counter);
		lastEvent = new Date();
		counter++;
	}
	
	public int getCounter()
	{
		return counter;
	}
	
	public Date getLastEvent()
	{
		return lastEvent;
	}
	
	// For UT only.
	public static void reset()
	{
		instance = null;
	}
	
}
