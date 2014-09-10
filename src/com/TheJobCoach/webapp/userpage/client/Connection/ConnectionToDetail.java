package com.TheJobCoach.webapp.userpage.client.Connection;

import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.ui.SimplePanel;

public class ConnectionToDetail extends SimplePanel implements IConnectionToDetail
{
	UserId user;
	
	public void toDetail(ContactInformation connectionUser)
	{
		clear();
		add(new ContentConnectionDetail(user, connectionUser, this));
	}
	
	public void toConnections()
	{
		clear();
		add(new ContentConnection(user, new SendMessage(), this, new EditShares()));
	}
	
	public ConnectionToDetail(UserId user)
	{
		this.user = user;
		toConnections();
	}
}
