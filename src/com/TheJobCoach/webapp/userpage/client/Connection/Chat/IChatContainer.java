package com.TheJobCoach.webapp.userpage.client.Connection.Chat;

import java.util.Vector;

import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.shared.ChatInfo;

public interface IChatContainer
{
	IChatInfoHandler getChatFromUser(ContactInformation info, boolean create);
	void updateInfo(Vector<ChatInfo> chatInfo);
}
