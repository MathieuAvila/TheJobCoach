package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.google.gwt.user.client.ui.Panel;

public interface IChooseExternalContact
{
	public interface ChooseExternalContactResult
	{
		public void setResult(ExternalContact result);
	}
	
	public IChooseExternalContact clone(Panel rootPanel, UserId userId, ChooseExternalContactResult result);
	public void onModuleLoad();

}
