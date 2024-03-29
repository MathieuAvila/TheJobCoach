package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.UserId;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface LoginServiceAsync {
	void createAccount(UserId id, UserInformation info,	String locale, AsyncCallback<MainPageReturnCode.CreateAccountStatus> callback);
	void connect(String userName, String userPassword, AsyncCallback<MainPageReturnLogin> callback);
	void validateAccount(String userName, String token,AsyncCallback<MainPageReturnCode.ValidateAccountStatus> callback);
	void lostCredentials(String email, String lang , AsyncCallback<Boolean> callback);
	void createTestUser(String lang, UserId.UserType type, AsyncCallback<UserId> callback);
	void disconnect(AsyncCallback<String> callback);	

}
