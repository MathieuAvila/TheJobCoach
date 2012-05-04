package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestUserPage implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		final RootPanel root = RootPanel.get("userpage");
		if (root != null)
		{
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onFailure(Throwable reason) {
				System.out.println("Error on userpage");
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				root.setStyleName("mainpage-content");		
				HorizontalPanel hp = new HorizontalPanel();
				hp.setStyleName("mainpage-content");
				root.add(hp);
				hp.setSize("100%", "100%");
				UserPage cud = new UserPage();
				cud.setUser(new UserId("mathieu", "token", UserId.UserType.USER_TYPE_SEEKER));
				cud.onModuleLoad();
			}});		
		}
	}

}
