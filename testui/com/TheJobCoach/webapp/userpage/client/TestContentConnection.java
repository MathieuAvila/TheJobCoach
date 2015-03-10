package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Connection.ConnectionToDetail;
import com.TheJobCoach.webapp.userpage.client.Connection.Chat.ChatContainer;
import com.TheJobCoach.webapp.userpage.client.Connection.Chat.ChatServiceImpl;
import com.TheJobCoach.webapp.userpage.client.Connection.Chat.IChatService;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.UtilService;
import com.TheJobCoach.webapp.util.client.UtilServiceAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ServerCallRun;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.client.TestSecurity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.ChatInfo;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestContentConnection implements EntryPoint {
	
	class AsyncCallbackVoid  implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught)
		{
		}

		@Override
		public void onSuccess(Void result)
		{
		}
	};
	
	public class ChatServiceFake implements IChatService
	{
		UserId user;
		final TestServiceAsync testService =  GWT.create(TestService.class);

		public ChatServiceFake(UserId user)
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
					testService.isTypingTo(user.userName, toUser, new ServerCallHelper<Void>(RootPanel.get()) {
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
					testService.addChatMsg(user.userName, toUser, message, new ServerCallHelper<Void>(RootPanel.get()) {
						@Override
						public void onSuccess(Void v)
						{
							System.out.println("test message sent successfully");
						}});
				}});
		}

		@Override
		public void getChatHistory(final String from, final Date d,
				final GetChatHistoryResult onResult)
		{
			EasyAsync.serverCall(new ServerCallRun(){
				@Override
				public void Run() throws CassandraException,
						CoachSecurityException, SystemException
				{
					testService.getLastMsgFromUser(user.userName, from, 10, d, new ServerCallHelper<Vector<ChatInfo>>(RootPanel.get()) {
						@Override
						public void onSuccess(Vector<ChatInfo> v)
						{
							onResult.Run(v);
						}});
				}});
			
		}

	}
	
	static Date lastTime = new Date();
	
	public Date getLastTime()
	{
		Date ret = lastTime;
		lastTime = new Date();
		return ret;
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		{
			final RootPanel root = RootPanel.get("contentconnection");
			if (root != null)
			{
				EasyAsync.Check(root, new ToRun() {
					@Override
					public void Open()
					{
						ChatContainer chat = new ChatContainer(new ChatServiceImpl(TestSecurity.defaultUser));
						root.setStyleName("mainpage-content");
						root.setSize("100%", "100%");
						root.add(chat);
						root.add(new ConnectionToDetail(TestSecurity.defaultUser, chat));
					}
				});
			}
		}
		
		{
			final RootPanel root = RootPanel.get("contentconnectiondetail");
			if (root != null)
			{
				EasyAsync.Check(root, new ToRun() {
					@Override
					public void Open()
					{
						ChatContainer chat = new ChatContainer(new ChatServiceImpl(TestSecurity.defaultUser));
						root.setStyleName("mainpage-content");
						root.setSize("100%", "100%");
						ConnectionToDetail c2d = new ConnectionToDetail(TestSecurity.defaultUser, chat);
						root.add(chat);
						root.add(c2d);
						c2d.toDetail(
								new ContactInformation(
										ContactStatus.CONTACT_OK, 
										TestSecurity.defaultUserConnection.userName,
										"", "", 
										new Visibility(),
										new Visibility(true,true,true,true))); // may be wrong.
					}
				});
			}
		}

		{
			final RootPanel root = RootPanel.get("contentconnectionchatremote");
			if (root != null)
			{
				EasyAsync.Check(root, new ToRun() {
					@Override
					public void Open()
					{	
						final UtilServiceAsync utilService =  GWT.create(UtilService.class);

						root.setStyleName("mainpage-content");
						root.setSize("100%", "100%");
						
						final TestServiceAsync testService = GWT.create(TestService.class);

						final ChatContainer chat = new ChatContainer(new ChatServiceImpl(TestSecurity.defaultUser));
						chat.getChatFromUser(new ContactInformation(TestSecurity.defaultUserConnection.userName, "first2", "last2"));
						
						final ChatContainer remoteChat = new ChatContainer(new ChatServiceFake(TestSecurity.defaultUserConnection));
						remoteChat.getChatFromUser(new ContactInformation(TestSecurity.defaultUser.userName, "first", "last"));

						VerticalPanel vp = new VerticalPanel();
						Button logIn = new Button("LogIn");
						Button logOut = new Button("LogOut");
						
						vp.add(logIn);
						logIn.addClickHandler(new ClickHandler(){
							@Override
							public void onClick(ClickEvent event)
							{
								try	{
									testService.logInOut(TestSecurity.defaultUserConnection.userName, "password", true, new AsyncCallbackVoid());
								} catch (Exception e) {e.printStackTrace();}
							}						
						});
						vp.add(logOut);
						logOut.addClickHandler(new ClickHandler(){
							@Override
							public void onClick(ClickEvent event)
							{
								try	{
									testService.logInOut(TestSecurity.defaultUserConnection.userName, "password", false, new AsyncCallbackVoid());
								} catch (Exception e) {e.printStackTrace();}
							}						
						});
						
			
						final Timer t = new Timer()
						{
							
							@Override
							public void run()
							{
								Date lastTime = getLastTime();

								//System.out.println("update on alam");
								schedule(1000);
								UpdateRequest request = new UpdateRequest(lastTime, 1, false);
								ServerCallHelper<UpdateResponse> callback =  new ServerCallHelper<UpdateResponse>(root){
									@Override
									public void onSuccess(UpdateResponse result)
									{
										chat.updateInfo(result.chatInfo);
									}
								};
								utilService.sendUpdateList(TestSecurity.defaultUser, request, callback);
								ServerCallHelper<UpdateResponse> callbackLocal =  new ServerCallHelper<UpdateResponse>(root){
									@Override
									public void onSuccess(UpdateResponse result)
									{
										remoteChat.updateInfo(result.chatInfo);
									}
								};
								try
								{
									testService.sendUpdateList(TestSecurity.defaultUserConnection, request, callbackLocal);
								}
								catch (Exception e)	{e.printStackTrace();}
							}				
						};
						t.schedule(1000);
						
						vp.add(remoteChat);
						vp.add(chat);
						
						root.add(vp);
					}
				});
			}
		}
		
	}

}
