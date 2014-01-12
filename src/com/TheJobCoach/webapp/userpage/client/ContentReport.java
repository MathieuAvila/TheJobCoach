package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;

public class ContentReport implements EntryPoint {

	UserId user;
	private VerticalPanel simplePanelCenter = new VerticalPanel();
	private TextArea textArea = new TextArea();
	private Lang lang = GWT.create(Lang.class);

	private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	public ContentReport(Panel panel, UserId _user)
	{
		rootPanel = panel;
		user = _user;
	}

	private void sendComment()
	{
		ServerCallHelper<String> callback = new ServerCallHelper<String>(rootPanel) {			
			@Override
			public void onSuccess(String result)
			{
				simplePanelCenter.clear();
				Label thanks = new Label(lang._TextReplyComment());
				thanks.setStyleName("standard-text");
				simplePanelCenter.add(thanks);
			}
		};
		userService.sendComment(user, textArea.getValue(), callback);
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

		ContentHelper.insertTitlePanel(uberPanel, lang._TextMakeComment(), ClientImageBundle.INSTANCE.sendCommentContent());
		uberPanel.add(simplePanelCenter);

		Label labelExplanation = new Label(lang._TextAboutComment());
		labelExplanation.setStyleName("standard-text");
		simplePanelCenter.add(labelExplanation);

		simplePanelCenter.add(textArea);
		textArea.setSize("100%", "200px");

		final ButtonImageText buttonSendReport = new ButtonImageText(ButtonImageText.Type.MAIL, lang._TextSendComment());
		buttonSendReport.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {
				buttonSendReport.setEnabled(false);
				sendComment();
			}
		});
		simplePanelCenter.add(buttonSendReport);
	}
}