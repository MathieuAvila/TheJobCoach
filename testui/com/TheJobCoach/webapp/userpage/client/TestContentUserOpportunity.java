package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.userpage.client.EditLogEntry.EditLogEntryResult;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestContentUserOpportunity implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		System.out.println("Loading...");
		RootPanel root = RootPanel.get("contentuseropportunity");
		if (root != null)
		{
			root.setStyleName("mainpage-content");		
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("mainpage-content");
			root.add(hp);
			hp.setSize("100%", "100%");
			ContentUserOpportunity cuo = new ContentUserOpportunity();
			cuo.setRootPanel(hp);
			cuo.setUserParameters(new UserId("mathieu", "token", UserId.UserType.USER_TYPE_SEEKER));
			
			cuo.onModuleLoad();
		}
	}

}
