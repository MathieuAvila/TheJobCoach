package com.TheJobCoach.webapp.userpage.client.Connection;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DetailPanel extends VerticalPanel {

	protected UserId user;
	protected ContactInformation connectionUser;
	protected UserId contactId;
	
	protected final static Lang lang = GWT.create(Lang.class);
	protected final static LangConnection langConnection = GWT.create(LangConnection.class);

	protected static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);
	protected static com.TheJobCoach.webapp.util.client.ClientImageBundle wpUtilImageBundle = (com.TheJobCoach.webapp.util.client.ClientImageBundle) GWT.create(com.TheJobCoach.webapp.util.client.ClientImageBundle.class);
	
	public DetailPanel(final UserId user, final ContactInformation connectionUser)
	{
		this.user = user;
		this.connectionUser = connectionUser;
		contactId = new UserId(connectionUser.userName);
		setSize("100%", "100%");
	}

	protected final UserServiceAsync userService = GWT.create(UserService.class);

	public void showPanelDetail()
	{
		
	}
}
