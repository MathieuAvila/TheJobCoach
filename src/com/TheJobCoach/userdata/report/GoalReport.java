package com.TheJobCoach.userdata.report;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.userdata.ConnectionLog;
import com.TheJobCoach.userdata.UserLogManager;
import com.TheJobCoach.userdata.UserOpportunityManager;
import com.TheJobCoach.webapp.userpage.shared.GoalReportInformation;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;

public class GoalReport
{
	static UserLogManager log = new UserLogManager();
	static UserOpportunityManager oppManager = new UserOpportunityManager();
	ConnectionLog connectionLog = new ConnectionLog();
	
	public GoalReportInformation getReport(UserId id, Date start, Date end) throws CassandraException 
	{
		GoalReportInformation result = new GoalReportInformation(start, end);
		
		// Get logs type on the period
		Vector<String> logs = log.getPeriodUserOppStatusChange(id, start, end);
		for (String logId: logs)
		{
			UserLogEntry realLog = log.getLogEntryLong(id, logId);
			Integer previous = result.log.get(realLog.type);
			Integer newValue = new Integer(previous.intValue() + 1);
			result.log.put(realLog.type, newValue);
		}
		
		// Build new opportunity counter
		Vector<UserOpportunity> oppList = oppManager.getOpportunitiesList(id, UserOpportunityManager.MANAGED_LIST);
		for (UserOpportunity opp: oppList)
		{
			if (start.before(opp.pubDate) && (end.after(opp.pubDate)))
			{
				result.newOpportunities++;
			}
		}
		
		// Time logger: number of days logged in.
		ConnectionLog.LogTimeResult logTime = connectionLog.getLogDays(id.userName, start, end);
		result.connectedDays = logTime.totalDay;
		result.succeedStartDay = logTime.startOk;
		result.succeedEndDay = logTime.endOk;
		
		return result;
	}
}
