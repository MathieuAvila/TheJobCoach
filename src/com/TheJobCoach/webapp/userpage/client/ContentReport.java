package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

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

	private void sendComment()
	{		
		AsyncCallback<String> callback = new AsyncCallback<String>()	{
			@Override
			public void onFailure(Throwable caught)
			{
				Window.alert(caught.getMessage());
			}
			@SuppressWarnings("deprecation")
			@Override
			public void onSuccess(String result)
			{
				simplePanelCenter.clear();
				simplePanelCenter.add(new Label(lang._TextReplyComment()));
			}
		};
		System.out.println("comment is :" + textArea.getValue());
		userService.sendComment(user, textArea.getValue(), callback);
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{	
		System.out.println("Load Report, locale is: " + LocaleInfo.getCurrentLocale().getLocaleName());				

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel rootPanel = RootPanel.get("centercontent");
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();
				
		simplePanelCenter.setSize("100%", "100%");
		rootPanel.add(simplePanelCenter);
		
		HorizontalPanel panelTitle = new HorizontalPanel();
		simplePanelCenter.add(panelTitle);
		Image imageLogout = new Image(ClientImageBundle.INSTANCE.sendComment());
		imageLogout.setWidth(String.valueOf(imageLogout.getWidth()) + "px");
		panelTitle.add(imageLogout);
		
		HorizontalPanel panelSpace = new HorizontalPanel();
		panelSpace.setWidth("20px");
		panelTitle.add(panelSpace);
		
		InlineHTML labelTitle = new InlineHTML();
		labelTitle.setHTML("<h2>" + lang._TextMakeComment() + "</h2>");
		
		panelTitle.add(labelTitle);
		panelTitle.setCellVerticalAlignment(labelTitle, HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label labelExplanation = new Label(lang._TextAboutComment());
		labelExplanation.setStyleName("standard-text");
		simplePanelCenter.add(labelExplanation);
		
		simplePanelCenter.add(textArea);
		textArea.setSize("100%", "100%");
		textArea.setHeight("200px");
		
		ButtonImageText buttonSendReport = new ButtonImageText(ButtonImageText.Type.MAIL, lang._TextSendComment());
		buttonSendReport.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {
				sendComment();				
			}			
		});
		simplePanelCenter.add(buttonSendReport);
	}
}
