package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentReport implements EntryPoint {

	UserId user;
	private VerticalPanel simplePanelCenter = new VerticalPanel();
	private TextArea textArea = new TextArea();
	private Lang lang = GWT.create(Lang.class);
	
	public void setUserParameters(UserId _user)
	{
		user = _user;
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	public void setRootPanel(Panel panel)
	{
		rootPanel = panel;
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{			
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel rootPanel = RootPanel.get("centercontent");
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();
				
		simplePanelCenter.setSize("100%", "100%");
		VerticalPanel uberPanel = new VerticalPanel();
		rootPanel.add(uberPanel);
		
		ContentHelper.insertTitlePanel(uberPanel, lang._TextReport(), ClientImageBundle.INSTANCE.sendComment());
		uberPanel.add(simplePanelCenter);
	}
}
