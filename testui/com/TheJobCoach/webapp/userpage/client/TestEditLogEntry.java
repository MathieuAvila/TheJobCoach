package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.userpage.client.Opportunity.EditLogEntry;
import com.TheJobCoach.webapp.userpage.client.Opportunity.EditLogEntry.EditLogEntryResult;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestEditLogEntry implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		final RootPanel root = RootPanel.get("edituserlog");
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
					EditLogEntry ele = new EditLogEntry(hp, null, "toto1", new UserId("user", "token", UserType.USER_TYPE_SEEKER), new EditLogEntryResult() {

						@Override
						public void setResult(UserLogEntry result) {
						}});
					ele.onModuleLoad();
				}
			});
		}
	}

}
