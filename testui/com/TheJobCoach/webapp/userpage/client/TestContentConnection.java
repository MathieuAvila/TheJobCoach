package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.userpage.client.Connection.ContentConnection;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.client.TestSecurity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestContentConnection implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		{
			final RootPanel root = RootPanel.get("contentconnection");
			if (root != null)
			{
				EasyAsync.Check(root, new ToRun() {
					@Override
					public void Open()
					{
						root.setStyleName("mainpage-content");
						root.setSize("100%", "100%");
						ContentConnection cud = new ContentConnection(TestSecurity.defaultUser);
						root.add(cud);
					}
				});
			}
		}
	}

}
