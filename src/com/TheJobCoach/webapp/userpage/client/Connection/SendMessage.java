package com.TheJobCoach.webapp.userpage.client.Connection;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.VerticalSpacer;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SendMessage extends DialogBox  implements ISendMessage  {

	Panel rootPanel;
	final Lang lang = GWT.create(Lang.class);
	final LangConnection langConnection = GWT.create(LangConnection.class);

	private final DialogBlockOkCancel okCancel = new DialogBlockOkCancel(langConnection.sendMessage(), this);
	
	private final static UserServiceAsync userService = GWT.create(UserService.class);
	
	private Button btnSendMessage = null;
	RichTextArea textAreaMessage = new RichTextArea();
	
	UserId contact;
	String firstName;
	String lastName;
	
	public SendMessage()
	{
	}

	@Override
	public void sendMessage(Panel panel, UserId contact, String firstName, String lastName)
	{
		rootPanel = panel;
		this.contact = contact;
		this.firstName = firstName;
		this.lastName = lastName;
		this.setStylePrimaryName("common-form-dialog");
		this.setText(langConnection.sendMessage());
		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);
		
		VerticalPanel vp = new VerticalPanel();
		this.setWidget(vp);

		vp.add(new VerticalSpacer("1em"));
		vp.add(new Label(langConnection.sendMessageTo().replace("%f", firstName).replace("%l", lastName)));
		vp.add(new VerticalSpacer("1em"));
		vp.add(textAreaMessage);

		btnSendMessage = okCancel.getOk();
		btnSendMessage.addClickHandler(new SendMessageHandler(this));

		vp.add(okCancel);

		this.center();	
	}

	// Create a handler for the send message button
	class SendMessageHandler implements ClickHandler {
		DialogBox dBox;
		public void onClick(ClickEvent event) 
		{
			okCancel.setEnabled(false);
			userService.sendJobMail(contact, "", 
					new ServerCallHelper<Boolean>(rootPanel) {
		
						public void onSuccess(Boolean result) 
						{							
							dBox.hide();				
						}
					});
		}
		public SendMessageHandler(DialogBox dBox)
		{
			this.dBox = dBox;
		}
	}

}
