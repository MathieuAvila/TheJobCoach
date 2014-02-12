package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.userpage.client.Coach.PanelCoach;
import com.TheJobCoach.webapp.userpage.client.Library.ContentSiteLibrary;
import com.TheJobCoach.webapp.userpage.client.Library.SiteLibraryData;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.client.TestSecurity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestContentSimple implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		{
			final RootPanel root = RootPanel.get("contentnews");
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
						ContentNews cud = new ContentNews(hp, TestSecurity.defaultUser);
						cud.onModuleLoad();
					}
				});
			}
		}
		{
			final RootPanel root = RootPanel.get("contentlibrary");
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
						ContentSiteLibrary cud = new ContentSiteLibrary(hp, TestSecurity.defaultUser);
						hp.add(cud);
					}
				});
			}
		}
		{
			final RootPanel root = RootPanel.get("contentcoach");
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
						PanelCoach cud = new PanelCoach(hp, TestSecurity.defaultUser);
						hp.add(cud);
					}
				});
			}
		}
	}

}
