package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.ui.Panel;

public interface IChooseDialogModel<T>
{	
	public IChooseDialogModel<T> clone(Panel rootPanel, UserId userId, IChooseResult<T> result);
	public void onModuleLoad();
}
