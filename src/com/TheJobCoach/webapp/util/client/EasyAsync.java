package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

public class EasyAsync
{
	public interface ToRun
	{
		public void Open();
	}
	
	public interface ServerCallRun
	{
		public void Run() throws CassandraException, CoachSecurityException, SystemException;
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
	
	public static void serverCall(ServerCallRun toRun)
	{
		serverCall(RootPanel.get(), toRun);
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
		catch (SystemException e)
		{
			MessageBox.messageBoxException(rootPanel, e.toString());
		}
	}

}
