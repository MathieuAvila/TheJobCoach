package com.TheJobCoach.webapp.userpage.client.Connection;

import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.ui.Panel;

public interface ISendMessage  {

	public void sendMessage(Panel panel, UserId contact, String firstName, String lastName);
}
