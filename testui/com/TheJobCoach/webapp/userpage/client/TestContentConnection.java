package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.userpage.client.Connection.ConnectionToDetail;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.client.TestSecurity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestContentConnection implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		{
			final RootPanel root = RootPanel.get("contentconnection");
			if (root != null)
			{
				EasyAsync.Check(root, new ToRun() {
					@Override
					public void Open()
					{
						root.setStyleName("mainpage-content");
						root.setSize("100%", "100%");
						root.add(new ConnectionToDetail(TestSecurity.defaultUser));
					}
				});
			}
		}
		/*
		{
			final RootPanel root = RootPanel.get("contentconnectiondetail");
			if (root != null)
			{
				EasyAsync.Check(root, new ToRun() {
					@Override
					public void Open()
					{
						root.setStyleName("mainpage-content");
						//root.setSize("100%", "100%");
						RootLayoutPanel lp = RootLayoutPanel.get();
						ContentConnectionDetail cud = new ContentConnectionDetail(TestSecurity.defaultUser, 
								new ContactInformation(ContactStatus.CONTACT_OK, TestSecurity.defaultUserConnection.userName,
										"firstName", "lastName", 
										new Visibility(true, true, true, true),
										new Visibility(true, true, true, true)), null);
						lp.add(cud);
						
					}
				});
			}
		}
		*/
	}

}
