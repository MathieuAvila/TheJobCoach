package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.userpage.client.Opportunity.AutoFeed;
import com.TheJobCoach.webapp.userpage.client.Opportunity.ContentUserOpportunity;
import com.TheJobCoach.webapp.userpage.client.Opportunity.EditOpportunity;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.TestSecurity;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
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
		{
			final RootPanel root = RootPanel.get("contentuseropportunity");
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
						ContentUserOpportunity cuo = new ContentUserOpportunity(hp, TestSecurity.defaultUser);

						cuo.onModuleLoad();
					}});
			}}}
	{
		final RootPanel root = RootPanel.get("autofeed");
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
					AutoFeed cuo = new AutoFeed(hp, TestSecurity.defaultUser, new IChooseResult<UserOpportunity>() {
						@Override
						public void setResult(UserOpportunity result) {	
						}}, 
						new EditOpportunity());

					cuo.onModuleLoad();
				}});
		}
	}

}
