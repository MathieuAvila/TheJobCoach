package com.TheJobCoach.webapp.util.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestMessageBoxError implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		System.out.println("Loading...");
		RootPanel root = RootPanel.get("testmessageboxerror");
		if (root != null)
		{
			root.setStyleName("mainpage-content");		
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("mainpage-content");
			root.add(hp);
			hp.setSize("100%", "100%");
			MessageBox.messageBox(hp, MessageBox.TYPE.ERROR, "my title", "Long message\n long message\nvery long message");
		}
	}
}
