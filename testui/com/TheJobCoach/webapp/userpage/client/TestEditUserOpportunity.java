package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.userpage.client.Opportunity.EditOpportunity;
import com.TheJobCoach.webapp.userpage.client.Opportunity.EditOpportunity.EditOpportunityResult;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestEditUserOpportunity implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		final RootPanel root = RootPanel.get("edituseropportunity");
		if (root != null)
		{

			GWT.runAsync(new RunAsyncCallback() {

				@Override
				public void onFailure(Throwable reason) {
					System.out.println("Error on userpage");
				}

				@Override
				public void onSuccess() {
					System.out.println("Edit User Opportunity");
					root.setStyleName("mainpage-content");		
					HorizontalPanel hp = new HorizontalPanel();
					hp.setStyleName("mainpage-content");
					root.add(hp);
					hp.setSize("100%", "100%");
					EditOpportunity ele = new EditOpportunity(hp, new UserId("user", "tokenuser", UserType.USER_TYPE_SEEKER), new UserOpportunity(), new EditOpportunityResult() {
						@Override
						public void setResult(UserOpportunity result) {
							
						}});
					ele.onModuleLoad();
				}});
		}
	}
}
