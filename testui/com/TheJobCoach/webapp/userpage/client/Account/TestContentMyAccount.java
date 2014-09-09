package com.TheJobCoach.webapp.userpage.client.Account;

import com.TheJobCoach.webapp.userpage.client.Account.ContentAccount;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.client.TestSecurity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestContentMyAccount implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		final RootPanel root = RootPanel.get("contentmyaccount");
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
					hp.add(new ContentAccount(TestSecurity.defaultUser));
				}
			});
		}
		
		final RootPanel rootPw = RootPanel.get("changepassword");
		if (rootPw != null)
		{
			EasyAsync.Check(rootPw, new ToRun() {
				@Override
				public void Open()
				{
					rootPw.setStyleName("mainpage-content");		
					HorizontalPanel hp = new HorizontalPanel();
					hp.setStyleName("mainpage-content");
					rootPw.add(hp);
					hp.setSize("100%", "100%");
					new EditPassword(TestSecurity.defaultUser);
				}
			});
		}
	}

}
