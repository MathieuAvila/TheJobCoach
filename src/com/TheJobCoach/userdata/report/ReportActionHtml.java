package com.TheJobCoach.userdata.report;

import java.util.Date;

import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.UserId;

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
		String dates = "";
		if (!FormatUtil.getDateString(Start).equals(FormatUtil.getDateString(FormatUtil.startOfTheUniverse())))
			dates = " (" + ReportHtml.getDate(super.lang, Start) + " - " + ReportHtml.getDate(super.lang, end) + ")";
		content += ReportHtml.getHead() + "<H1>www.TheJobCoach.com - " + lang.getTimeReport() + dates + "</H1>\n";
	}
		
	@Override
	void opportunityHeader(UserOpportunity opp, boolean includeOpportunityDetail, boolean includeLogDetail)
	{
		String detail = "";
		content += "<H2>" + ReportHtml.writeToString(opp.title) + " - " + lang.getOpportunityStatusName(UserOpportunity.applicationStatusToString(opp.status)) + "</H2>";
		if (includeOpportunityDetail)
		{
			detail = ReportHtml.addWithSeparator(detail, "<div style=\"border-width:1px; border-color:black ; border-style:solid\">" + opp.description + "</div>", "", "</BR>");
			detail = ReportHtml.addWithSeparatorCheck(detail, "<a href=\""+opp.url+"\">" + opp.url + "</a>", "", "</BR>", opp.url);
			detail = ReportHtml.addWithSeparator(detail, ReportHtml.writeToString(opp.location), "", "</BR>");
		}
		String logHeader = "";
		if (includeLogDetail)
		{
			logHeader = 
					"<TH>" + lang.getDescription() + "</TH>"
							+		"<TH>" + lang.getDone() + "</TH>"
							+		"<TH>" + lang.getFilename() + "</TH>";
		}
		content += detail;
		content += "<TABLE CELLPADDING=8 BORDER=2 WIDTH=\"100%\"><TR BGCOLOR=red>"
				+ "<TH>" + lang.getDate() + "</TH>"
				+ "<TH>" + lang.getType() + "</TH>"
				+ "<TH>" + lang.getAction() + "</TH>"
				+ logHeader
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
		String BGCOLOR = inSpanDate ? "orange" : "brown";
		String logDetail = "";
		if (includeLogDetail)
		{
			logDetail = 
					"<TD>" + log.description + "</TD>" + 
					"<TD>" + (log.done ? "X": "") + "</TD><TD>";
			for (UserDocumentId file: log.attachedDocumentId)
			{
				logDetail += file.name + "(" + file.fileName + ")<br/>";
			}
			logDetail+="</TD>";
		}
		content += "<TR BGCOLOR=\"" + BGCOLOR + "\">"
				+ "<TD>" + ReportHtml.getDate(super.lang, log.eventDate) + "</TD>"
				+ "<TD>" + ReportHtml.writeToString(lang.getActionName(UserLogEntry.entryTypeToString(log.type))) + "</TD>"
				+ "<TD>" + ReportHtml.writeToString(log.title)
				+ logDetail
				+ "</TD></TR>\n";
	}

	@Override
	void endDocument()
	{
		content += ReportHtml.getFooter() + "\n";
	}
}
