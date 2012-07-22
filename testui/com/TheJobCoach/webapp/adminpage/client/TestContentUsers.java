package com.TheJobCoach.webapp.adminpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
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
			System.out.println("Main Page");
			root.setStyleName("mainpage-content");		
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("mainpage-content");
			root.add(hp);
			hp.setSize("100%", "100%");
			ContentUsers ele = new ContentUsers(hp, new UserId("mathieu", "token", UserId.UserType.USER_TYPE_ADMIN));
			ele.onModuleLoad();
		}
	}
}