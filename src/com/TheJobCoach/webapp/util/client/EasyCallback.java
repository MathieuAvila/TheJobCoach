package com.TheJobCoach.webapp.util.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;

public class EasyCallback<I> implements AsyncCallback<I>
{
	Panel rootPanel;
	SuccessRun<I> success;
	
	public interface SuccessRun<I>
	{
		public void onSuccess(I result);
	}

	public EasyCallback(Panel rootPanel, SuccessRun<I> success)
	{
		this.rootPanel = rootPanel;
		this.success = success;
	}
	
	@Override
	public void onFailure(Throwable caught)
	{
		MessageBox.messageBoxException(rootPanel, caught.toString());
	}

	@Override
	public void onSuccess(I result)
	{
		success.onSuccess(result);
	}
}
