package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.ui.Panel;

public interface IEditDialogModel<T>
{	
	public IEditDialogModel<T> clone(Panel rootPanel, UserId userId, T edition, IChooseResult<T> result);
	public void onModuleLoad();
}
