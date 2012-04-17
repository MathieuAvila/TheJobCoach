package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestContentUserSite implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		System.out.println("Loading...");
		RootPanel root = RootPanel.get("contentusersite");
		if (root != null)
		{
			root.setStyleName("mainpage-content");		
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("mainpage-content");
			root.add(hp);
			hp.setSize("100%", "100%");
			ContentUserSite cud = new ContentUserSite();
			cud.setRootPanel(hp);
			cud.setUserParameters(new UserId("mathieu", "token", UserId.UserType.USER_TYPE_SEEKER));
			cud.onModuleLoad();
		}
	}

}
