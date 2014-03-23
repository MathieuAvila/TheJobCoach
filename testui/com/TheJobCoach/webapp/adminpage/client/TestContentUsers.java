package com.TheJobCoach.webapp.adminpage.client;

import com.TheJobCoach.webapp.util.client.TestSecurity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestContentUsers implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		RootPanel root = RootPanel.get("admincontentusers");
		if (root != null)
		{
			root.setStyleName("mainpage-content");		
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("mainpage-content");
			root.add(hp);
			hp.setSize("100%", "100%");
			ContentUsers ele = new ContentUsers(hp, TestSecurity.defaultUser);
			ele.onModuleLoad();
		}
	}
}
