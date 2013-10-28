package com.TheJobCoach.webapp.userpage.client.Opportunity;

import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.google.gwt.user.client.ui.Panel;

public interface IContentUserOpportunity
{
	public IContentUserOpportunity clone(Panel panel, UserId _user);
	public void onModuleLoad();
}
