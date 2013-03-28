package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.google.gwt.user.client.ui.Panel;

public interface IChooseExternalContact
{	
	public IChooseExternalContact clone(Panel rootPanel, UserId userId, IChooseResult<ExternalContact> result);
	public void onModuleLoad();
}
