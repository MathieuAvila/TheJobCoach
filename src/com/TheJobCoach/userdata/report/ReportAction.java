package com.TheJobCoach.userdata.report;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.userdata.UserLogManager;
import com.TheJobCoach.userdata.UserOpportunityManager;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;

public class ReportAction 
{
	String content = "";
	String lang;
	UserId user;
	
	void includeTitle(Date Start, Date end)
	{
	}
	
	void endDocument()
	{
	}
	
	void opportunityHeader(UserOpportunity opp, boolean includeOpportunityDetail, boolean includeLogDetail)
	{
	}
	
	void opportunityFooter(UserOpportunity opp, boolean includeOpportunityDetail)
	{
	}
	
	void logHeader(UserLogEntry log, boolean includeLogDetail, boolean inSpanDate) 
	{
		
	}

	public byte[] getReport(
			Date start, Date end, 
			boolean includeOpportunityDetail,
			boolean includeLogDetail,
			boolean onlyLogOnPeriod) throws CassandraException
	{
		UserOpportunityManager oppManager = new UserOpportunityManager();
		UserLogManager logManager = new UserLogManager();
		includeTitle(start, end);
		Vector<UserOpportunity> oppList = 
				oppManager.getOpportunitiesList(user, UserOpportunityManager.MANAGED_LIST);
		for (UserOpportunity opp: oppList)
		{
			Vector<UserLogEntry> logList = logManager.getLogList(user, opp.ID);
			boolean add = false;
			for (UserLogEntry log: logList)
			{
				if (log.eventDate.after(start) && log.eventDate.before(end))
				{
					add = true;
					break;
				}
			}
			if (add)
			{
				opportunityHeader(opp, includeOpportunityDetail, includeLogDetail);
				for (UserLogEntry log: logList)
				{
					boolean inSpanDate = (log.eventDate.after(start) && log.eventDate.before(end));
					if (!onlyLogOnPeriod || inSpanDate)
						logHeader(log, includeLogDetail, inSpanDate);
				}
				opportunityFooter(opp, includeOpportunityDetail);
			}
		}
		endDocument();
		return content.getBytes();
	}

	public ReportAction(UserId user, String lang)
	{
		this.lang = lang;
		this.user = user;
	}
	
}
