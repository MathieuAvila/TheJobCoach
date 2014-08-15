package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;

public class ServerCallHelper<T> implements AsyncCallback<T>
{
	private Panel rootPanel;
	
	public ServerCallHelper(Panel rootPanel)
	{
		this.rootPanel = rootPanel;
	}

	@Override
	public void onFailure(Throwable caught)
	{
		/* In case we need degugging those stacks on the client side 
		 */
		   System.out.println("ServerCallHelper stack trace .. ");
           for (StackTraceElement element: caught.getStackTrace())
               {
                       System.out.println(".. " + element.toString());
               }
           System.out.println("ServerCallHelper caught : " + caught.toString());
           
		if (caught instanceof CassandraException)
		{
			MessageBox.messageBoxException(rootPanel, caught.toString());
		}
		if (caught instanceof CoachSecurityException)
		{
			Window.Location.replace("TheJobCoach.html");
		}
	}
	@Override
	public void onSuccess(T result)
	{
		// Do nothing by default
	}
}