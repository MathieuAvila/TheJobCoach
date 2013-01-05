package com.TheJobCoach.userdata;

import java.util.Date;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;

public class ReportActionHtml extends ReportAction {

	public ReportActionHtml(UserId user, String lang)
	{
		super(user, lang);
	}

	@Override
	void includeTitle(Date Start, Date end)
	{
		content+="<HTML><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"><BODY><H1>Rapport de temps</H1>";
	}
	
	static String addWithSeparator(String src, String append, String sep, String end)
	{
		if (append != "")
		{
			if (src != "") src += sep;
			src += append + end;
		}
		return src;
	}

	static String addWithSeparator(String src, String append, String sep)
	{
		return addWithSeparator(src, append, sep, "");
	}

	
	@Override
	void opportunityHeader(UserOpportunity opp, boolean includeOpportunityDetail, boolean includeLogDetail)
	{
		String detail = "";
		detail = addWithSeparator(detail, opp.location, " - ");
		content += "<H2>" + opp.title + "</H2>";
		if (includeOpportunityDetail)
		{
			detail = addWithSeparator(detail, opp.description, "", "</BR>");
			detail = addWithSeparator(detail, opp.url, "", "</BR>");
		}
		content += detail;
		content += "<TABLE><TR>"
				+ "<TD>Date</TD>"
				+ "<TD>Type</TD>"
				+ "<TD>Action</TD>"
				+ "</TR>";
	}
	
	@Override
	void opportunityFooter(UserOpportunity opp, boolean includeOpportunityDetail)
	{
		content += "</TABLE><HR>";
	}
	
	@Override
	void logHeader(UserLogEntry log, boolean includeLogDetail) 
	{
		content += "<TR>"
				+ "<TD>" + log.eventDate + "</TD>"
				+ "<TD>" + UserLogEntry.entryTypeToString(log.type) + "</TD>"
				+ "<TD>" + (log.title + (includeLogDetail ? log.description : "" ))
				+ "</TD>";
	}

	@Override
	void endDocument()
	{
		content+="</BODY></HTML>";
	}
}
