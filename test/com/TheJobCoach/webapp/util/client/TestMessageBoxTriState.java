package com.TheJobCoach.webapp.util.client;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class TestMessageBoxTriState extends GwtTest {

	private MessageBoxTriState mb;

	@Test
	public void testMessageBox()
	{

	}

	int accept;
	
	@Test
	public void allTest()
	{
		HorizontalPanel p = new HorizontalPanel();
		Button b1 = new Button();
		Button b2 = new Button();
		mb = new MessageBoxTriState(
				p, MessageBoxTriState.TYPE.QUESTION, "title", "message", 
				b1, b2, new MessageBoxTriState.ICallback() {

					@Override
					public void complete(int accepted) 
					{
						accept = accepted;
					}
				});
		assertTrue(mb.dBox.isShowing() == true);

		b2.click();
		assertTrue(1 == accept);
		assertTrue(mb.dBox.isShowing() == false);
		
		mb.buttonCancel.click();
		assertTrue(-1 == accept);
		assertTrue(mb.dBox.isShowing() == false);
		
		b1.click();
		assertTrue(0 == accept);
		assertTrue(mb.dBox.isShowing() == false);
		
	}

}
