package com.TheJobCoach.userdata.report;

import java.util.Date;

import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UserId;

public class ReportExternalContactHtml extends ReportExternalContact {

	LangReportAction lang;
	
	public final static String VALIDRECALL = "azure";
	public final static String INVALIDRECALL = "orange";
	
	public ReportExternalContactHtml(UserId user, String lang, boolean includeContactDetail)
	{
		super(user, lang, includeContactDetail);
		this.lang = new LangReportAction(lang);
	}
	
	@Override
	void includeTitle()
	{
		content += ReportHtml.getHead() + "<H1>www.TheJobCoach.com - " + lang.getExternalContactReport() + "</H1>\n";
		content += 
				"<TABLE><TR>" 
						+ "<TH>" + lang.getLastName() + "</TH>" 
						+ "<TH>" + lang.getFirstName() + "</TH>"
						+ "<TH>" + lang.getOrganization() + "</TH>"
						+ "<TH>" + lang.getPhone() + "</TH>"
						+ "<TH>" + lang.getEmail() + "</TH>"
						+ "<TH>" + lang.getRecall() + "</TH>"
						+ (includeContactDetail ? ("<TH>" + lang.getNote() + "</TH>"):"")
						+ "</TR>";
	}
	
	@Override
	void endDocument()
	{
		content += ReportHtml.getFooter() + "\n";
	}

	@Override
	void contactHeader(ExternalContact contact) 
	{
		String logDetail = "";
		String BGCOLOR = contact.update.needRecall ? (new Date().before(contact.update.getNextCall()) ? VALIDRECALL : INVALIDRECALL) : VALIDRECALL;
		
		if (includeContactDetail)
		{
			logDetail = "<TD>" + ReportHtml.writeToString(contact.personalNote) + "</TD>";
		}
		content += "<TR BGCOLOR=\"" + BGCOLOR + "\">"
				+ "<TD>" + ReportHtml.writeToString(contact.lastName) + "</TD>"
				+ "<TD>" + ReportHtml.writeToString(contact.firstName) + "</TD>"
				+ "<TD>" + ReportHtml.writeToString(contact.organization) + "</TD>"
				+ "<TD>" + ReportHtml.writeToString(contact.phone) + "</TD>"
				+ "<TD>" + ReportHtml.writeToString(contact.email) + "</TD>"
				+ "<TD>" + ReportHtml.getDate(super.lang, contact.update.last) + " / " + ReportHtml.getDate(super.lang, contact.update.getNextCall()) + "</TD>"
				+ logDetail
				+ "</TD></TR>\n";
	}

}
