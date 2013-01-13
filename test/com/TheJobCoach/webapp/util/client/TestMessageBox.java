package com.TheJobCoach.webapp.util.client;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtTest;

public class TestMessageBox extends GwtTest {

	private MessageBox mb;

	@Override
	public String getModuleName() {		
		return "com.TheJobCoach.webapp.util.Util";
	}
	
	@Test
	public void testMessageBox()
	{

	}

	@Before
	public void beforeMessageBox()
	{
		HorizontalPanel p = new HorizontalPanel();
		mb = new MessageBox(
				p, 
				true, true, MessageBox.TYPE.WARNING, "title", "message", new MessageBox.ICallback() {

					@Override
					public void complete(boolean ok) {
						// TODO Auto-generated method stub
						//end = true;
					}
				});
		mb.onModuleLoad();		

		// Some pre-assertions

	}

}
