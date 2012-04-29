package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.userpage.client.EditUserSite.EditUserSiteResult;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestEditUserSite implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		RootPanel root = RootPanel.get("editusersite");
		if (root != null)
		{
			System.out.println("Edit User Site");
			root.setStyleName("mainpage-content");		
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("mainpage-content");
			root.add(hp);
			hp.setSize("100%", "100%");
			EditUserSite ele = new EditUserSite(hp, null, new UserId("", "", UserType.USER_TYPE_SEEKER), new EditUserSiteResult() {			

				@Override
				public void setResult(UserJobSite result) {
					// TODO Auto-generated method stub
					
				}});
			ele.onModuleLoad();
		}
	}

}
