package com.TheJobCoach.userdata;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import org.apache.commons.lang.StringEscapeUtils;

public class ReportActionHtml extends ReportAction {

	LangReportAction lang;
		
	public ReportActionHtml(UserId user, String lang)
	{
		super(user, lang);
		this.lang = new LangReportAction(lang);
	}

	@Override
	void includeTitle(Date Start, Date end)
	{
		content += ReportHtml.getHead() + "<H1>" + lang.getTimeReport() + "</H1>\n";
	}
		
	@Override
	void opportunityHeader(UserOpportunity opp, boolean includeOpportunityDetail, boolean includeLogDetail)
	{
		String detail = "";
		detail = ReportHtml.addWithSeparator(detail, ReportHtml.writeToString(opp.location), " - ");
		content += "<H2>" + ReportHtml.writeToString(opp.title) + "</H2>";
		if (includeOpportunityDetail)
		{
			detail = ReportHtml.addWithSeparator(detail, ReportHtml.writeToString(opp.description), "", "</BR>");
			detail = ReportHtml.addWithSeparator(detail, ReportHtml.writeToString(opp.url), "", "</BR>");
		}
		content += detail;
		content += "<TABLE CELLPADDING=8 BORDER=2 WIDTH=\"100%\"><TR BGCOLOR=red>"
				+ "<TD>" + lang.getDate() + "</TD>"
				+ "<TD>" + lang.getType() + "</TD>"
				+ "<TD>" + lang.getAction() + "</TD>"
				+ "</TR>\n";
	}
	
	@Override
	void opportunityFooter(UserOpportunity opp, boolean includeOpportunityDetail)
	{
		content += "</TABLE><HR>\n";
	}
	
	@Override
	void logHeader(UserLogEntry log, boolean includeLogDetail, boolean inSpanDate) 
	{ 
		String BGCOLOR = inSpanDate ? "#CCCC99" : "";
		content += "<TR BGCOLOR=\"" + BGCOLOR + "\">"
				+ "<TD>" + ReportHtml.getDate(super.lang, log.eventDate) + "</TD>"
				+ "<TD>" + ReportHtml.writeToString(lang.getActionName(UserLogEntry.entryTypeToString(log.type))) + "</TD>"
				+ "<TD>" + ReportHtml.writeToString((log.title + (includeLogDetail ? log.description : "" )))
				+ "</TD></TR>\n";
	}

	@Override
	void endDocument()
	{
		content += ReportHtml.getFooter() + "\n";
	}
}
