package com.TheJobCoach.webapp.userpage.client.Connection.Chat;

import com.TheJobCoach.webapp.util.shared.ChatInfo;

public interface IChatInfoHandler
{
	public void receiveChatInfo(ChatInfo event, boolean history);
}
