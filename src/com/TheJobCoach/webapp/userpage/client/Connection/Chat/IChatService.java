package com.TheJobCoach.webapp.userpage.client.Connection.Chat;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.shared.ChatInfo;

public interface IChatService
{
	public interface GetChatHistoryResult
	{
		public void Run(Vector<ChatInfo> history);
	}
	
	public interface GetChatInformationResult
	{
		public void Run(ContactInformation result);
	}
	
	public void sendIsTyping(String toUser);
	public void sendMsg(String toUser, String message);
	public void getChatHistory(String from, Date d, GetChatHistoryResult onResult);
	
	public void getInformation(String from, GetChatInformationResult onResult);
}
