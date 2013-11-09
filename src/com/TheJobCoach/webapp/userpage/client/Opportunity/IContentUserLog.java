package com.TheJobCoach.webapp.userpage.client.Opportunity;

import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.ui.Panel;

public interface IContentUserLog
{
	public IContentUserLog clone(Panel panel, UserId _user, UserOpportunity opp);
	public void onModuleLoad();
}
