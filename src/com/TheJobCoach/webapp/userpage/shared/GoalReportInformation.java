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
	
	public Date start = new Date();
	public Date end = new Date();
	
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

	@SuppressWarnings("deprecation")
	public GoalReportInformation(GoalReportInformation c)
	{
		start = new Date();
		start.setDate(c.start.getDate());
		end = new Date();
		end.setDate(c.end.getDate());
		log = new HashMap<UserLogEntry.LogEntryType, Integer>(c.log);
		newOpportunities = c.newOpportunities;
		connectedDays = c.connectedDays;
		succeedStartDay = c.succeedStartDay;
		succeedEndDay = c.succeedEndDay;
		
	}

}
