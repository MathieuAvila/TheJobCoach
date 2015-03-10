package com.TheJobCoach.webapp.userpage.client.Connection.Chat;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.EasyAsync.ServerCallRun;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.ChatInfo;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class ChatServiceImpl implements IChatService
{
	UserId user;
	final UserServiceAsync userService =  GWT.create(UserService.class);

	public ChatServiceImpl(UserId user)
	{
		this.user = user;
	}
	
	@Override
	public void sendIsTyping(final String toUser)
	{
		EasyAsync.serverCall(new ServerCallRun(){
			@Override
			public void Run() throws CassandraException,
					CoachSecurityException, SystemException
			{
				userService.isTypingTo(toUser, new ServerCallHelper<Void>(RootPanel.get()) {
					@Override
					public void onSuccess(Void v)
					{
						System.out.println("isTyping sent successfully");
					}});
			}});
	}

	@Override
	public void sendMsg(final String toUser, final String message)
	{
		EasyAsync.serverCall(new ServerCallRun(){
			@Override
			public void Run() throws CassandraException,
					CoachSecurityException, SystemException
			{
				userService.addChatMsg(toUser, message, new ServerCallHelper<Void>(RootPanel.get()) {
					@Override
					public void onSuccess(Void v)
					{
						System.out.println("message sent successfully");
					}});
			}});
	}

	@Override
	public void getChatHistory(final String fromUser, final Date d, final GetChatHistoryResult onResult)
	{
		EasyAsync.serverCall(new ServerCallRun(){
			@Override
			public void Run() throws CassandraException,
					CoachSecurityException, SystemException
			{
				userService.getLastMsgFromUser(fromUser, 10, d, new ServerCallHelper< Vector<ChatInfo>>(RootPanel.get()) {
					@Override
					public void onSuccess(Vector<ChatInfo> v)
					{
						onResult.Run(v);
					}});
			}});
	}

}
