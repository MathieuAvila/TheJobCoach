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
public class TestEditLogEntry implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		RootPanel root = RootPanel.get("edituserlog");
		if (root != null)
		{
			root.setStyleName("mainpage-content");		
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("mainpage-content");
			root.add(hp);
			hp.setSize("100%", "100%");
			EditLogEntry ele = new EditLogEntry(hp, null, "toto1", new UserId("avila", "avila", UserType.USER_TYPE_SEEKER), new EditLogEntryResult() {

				@Override
				public void setResult(UserLogEntry result) {
					System.out.println("done: " + result);
				}});
			ele.onModuleLoad();
		}
	}

}
