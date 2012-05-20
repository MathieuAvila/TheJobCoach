package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
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
		RootPanel root = RootPanel.get("contentmygoals");
		if (root != null)
		{
			System.out.println("Content My Goals");
			root.setStyleName("mainpage-content");		
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("mainpage-content");
			root.add(hp);
			hp.setSize("100%", "100%");
			ContentMyGoals cud = new ContentMyGoals(hp, new UserId("mathieu", "token", UserId.UserType.USER_TYPE_SEEKER));
			cud.onModuleLoad();
		}
	}

}
