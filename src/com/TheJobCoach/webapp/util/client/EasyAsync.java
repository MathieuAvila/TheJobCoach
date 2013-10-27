package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;

public class EasyAsync
{
	public interface ToRun
	{
		public void Open();
	}
	
	public interface ServerCallRun
	{
		public void Run() throws CassandraException, CoachSecurityException;
	}
	
	public static void Check(final Panel root, final ToRun run)
	{
		GWT.runAsync(new RunAsyncCallback() 
		{
			@Override
			public void onFailure(Throwable reason) 
			{
				MessageBox.messageBoxException(root, reason.toString());
			}
			@Override
			public void onSuccess() 
			{
				run.Open();
			}
		});
	}
	
	public static void serverCall(Panel rootPanel, ServerCallRun toRun)
	{
		try 
		{
			toRun.Run();
		}
		catch (CassandraException e)
		{
			MessageBox.messageBoxException(rootPanel, e.toString());
		}
		catch (CoachSecurityException e)
		{
			Window.Location.replace("/index.html");
			Window.Location.reload();
		}
	}

}
