package com.TheJobCoach.userdata;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;

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
	
	void logHeader(UserLogEntry log, boolean includeLogDetail) 
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
				oppManager.getOpportunitiesShortList(user, UserOpportunityManager.MANAGED_LIST);
		for (UserOpportunity opp: oppList)
		{
			opportunityHeader(opp, includeOpportunityDetail, includeLogDetail);
			Vector<UserLogEntry> logList = logManager.getLogList(user, opp.ID);
			for (UserLogEntry log: logList)
			{
				logHeader(log, includeLogDetail);
			}
		}
		return content.getBytes();
	}	

	public ReportAction(UserId user, String lang)
	{
		this.lang = lang;
		this.user = user;
	}
	
}
