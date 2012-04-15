package com.TheJobCoach.webapp.util.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;

public class TestMessageBox extends GWTTestCase {

	@Override
	public String getModuleName() {		
		return "com.TheJobCoach.webapp.util.Util";
	}

	public void testMessageBox()
	{
		final boolean end = false;
		MessageBox mb = new MessageBox(
			RootPanel.get("content"), 
			true, true, MessageBox.TYPE.WARNING, "title", "message", new MessageBox.ICallback() {

			@Override
			public void complete(boolean ok) {
				// TODO Auto-generated method stub
				//end = true;
			}
		});
		mb.onModuleLoad();
		
	}
	
}
