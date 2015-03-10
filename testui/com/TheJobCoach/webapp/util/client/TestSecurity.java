package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.mainpage.client.LoginService;
import com.TheJobCoach.webapp.mainpage.client.LoginServiceAsync;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.shared.UserId;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestSecurity implements EntryPoint {

	//public static UserId defaultUser = new UserId("user2", "mytoken2", UserId.UserType.USER_TYPE_SEEKER);
	public static UserId defaultUser = new UserId("user1", "mytoken1", UserId.UserType.USER_TYPE_SEEKER);
	//public static UserId defaultUser = new UserId("test_user", "test_token", UserId.UserType.USER_TYPE_SEEKER, true);

	//public static UserId defaultUserConnection = new UserId("user1", "mytoken1", UserId.UserType.USER_TYPE_SEEKER);
	public static UserId defaultUserConnection = new UserId("user2", "mytoken2", UserId.UserType.USER_TYPE_SEEKER);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		// Setup default user connection
		final RootPanel root = RootPanel.get("security");
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    
	    loginService.connect(defaultUser.userName, "password", new ServerCallHelper<MainPageReturnLogin>(root) {
					public void onSuccess(final MainPageReturnLogin result)
			{
			System.out.println("succesfullly logged in for tests");
			}});
				
		if (root != null)
		{
			EasyAsync.Check(root, new ToRun() {
				@Override
				public void Open()
				{	
					
				}
			});
		}}

}
