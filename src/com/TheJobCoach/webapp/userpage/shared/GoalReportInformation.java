package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GoalReportInformation implements Serializable
{

	private static final long serialVersionUID = -721348694020391393L;
	
	public Map<UserLogEntry.LogEntryType, Integer> log = new HashMap<UserLogEntry.LogEntryType, Integer>();
	public int newOpportunities;
	
	public int connectedDays;
	public int succeedStartDay;
	public int succeedEndDay;
	
	public Date start;
	public Date end;
	
	public GoalReportInformation()
	{
		
	}
	
	public GoalReportInformation(Date start, Date end)
	{
		this.start = start;
		this.end = end;
		for (UserLogEntry.LogEntryType type: UserLogEntry.LogEntryType.values())
		{
			log.put(type,  new Integer(0));
		}
	}
}
