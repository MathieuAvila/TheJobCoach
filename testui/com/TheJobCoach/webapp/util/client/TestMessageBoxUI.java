package com.TheJobCoach.webapp.util.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestMessageBoxUI implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		System.out.println("Loading...");
		RootPanel root = RootPanel.get("testmessagebox");
		if (root != null)
		{
			root.setStyleName("mainpage-content");		
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("mainpage-content");
			root.add(hp);
			hp.setSize("100%", "100%");
			MessageBox mb = new MessageBox(hp, true, true, MessageBox.TYPE.WARNING, "title", "message", new MessageBox.ICallback() {

				@Override
				public void complete(boolean ok) {
					// TODO Auto-generated method stub
					//end = true;
				}
			});
			mb.onModuleLoad();
		}
	}

}
