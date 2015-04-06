package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class HowtoWindow
{	
	final static Lang lang = GWT.create(Lang.class);

	public static void popUp()
	{
		EasyAsync.Check(RootPanel.get(), new ToRun() {
			@Override
			public void Open()
			{
				MessageBox mb = new MessageBox(RootPanel.get(), true, false, MessageBox.TYPE.NONE, lang.content_help(),
						"<iframe style=\"width:500px; height:500px;\" src=\"static/howto/howto.html\"></iframe>", null);
				mb.onModuleLoad();
			}	
		});
	}
}
