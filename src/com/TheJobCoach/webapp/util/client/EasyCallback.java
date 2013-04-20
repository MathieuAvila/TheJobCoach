package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;

public class EasyCallback<I> implements AsyncCallback<I>
{
	Panel rootPanel;
	SuccessRun success;
	
	public interface SuccessRun
	{
		public void onSuccess();
	}

	public EasyCallback(Panel rootPanel, SuccessRun success)
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
		success.onSuccess();
	}
}
