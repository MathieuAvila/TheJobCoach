package com.TheJobCoach.webapp.userpage.client.Opportunity;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.google.gwt.user.client.ui.Panel;

public interface IEditLogEntry
{	
	public IEditLogEntry clone(Panel panel, UserLogEntry _currentLogEntry, String _oppId, UserId _user, IChooseResult<UserLogEntry> editLogEntryResult);
	public void onModuleLoad();
}
