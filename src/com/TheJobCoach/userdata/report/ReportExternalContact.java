package com.TheJobCoach.userdata.report;

import java.util.Vector;

import com.TheJobCoach.userdata.UserExternalContactManager;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class ReportExternalContact 
{
	String content = "";
	String lang;
	UserId user;
	boolean includeContactDetail;
	
	void includeTitle()
	{
	}
	
	void endDocument()
	{
	}

	void contactHeader(ExternalContact contact) 
	{
	}

	public byte[] getReport() throws CassandraException
	{
		UserExternalContactManager contactManager = new UserExternalContactManager();
		includeTitle();
		Vector<ExternalContact> contactList = 
				contactManager.getExternalContactList(user);
		for (ExternalContact contact: contactList)
		{			
			contactHeader(contact);
		}
		endDocument();
		return content.getBytes();
	}

	public ReportExternalContact(UserId user, String lang, boolean includeContactDetail)
	{
		this.lang = lang;
		this.user = user;
		this.includeContactDetail = includeContactDetail;
	}
	
}
