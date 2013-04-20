package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.userpage.client.UserSite.EditUserSite;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.client.IChooseResult;
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
		final RootPanel root = RootPanel.get("editusersite");
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
					EditUserSite ele = new EditUserSite(hp, null, new UserId("", "", UserType.USER_TYPE_SEEKER), new IChooseResult<UserJobSite>() {			

						@Override
						public void setResult(UserJobSite result) {
						}});
					ele.onModuleLoad();
				}
			});
		}
	}

}
