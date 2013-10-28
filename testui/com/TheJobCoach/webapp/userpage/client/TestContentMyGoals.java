package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.userpage.client.MyGoals.ContentMyGoals;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.client.TestSecurity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestContentMyGoals implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		final RootPanel root = RootPanel.get("contentmygoals");
		if (root != null)
		{
			EasyAsync.Check(root, new ToRun() {
				@Override
				public void Open()
				{
					root.setStyleName("mainpage-content");		
					HorizontalPanel hp = new HorizontalPanel();
					hp.setStyleName("mainpage-content");
					root.add(hp);
					hp.setSize("100%", "100%");
					ContentMyGoals cud = new ContentMyGoals(hp, TestSecurity.defaultUser);
					cud.onModuleLoad();
				}
			});
		}
	}

}
