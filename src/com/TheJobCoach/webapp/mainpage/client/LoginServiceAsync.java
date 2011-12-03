package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface LoginServiceAsync {
	void createAccount(String userName, String name, String firstName, String userPassword, String email, AsyncCallback<MainPageReturnCode.CreateAccountStatus> callback);
	void connect(String userName, String userPassword, AsyncCallback<MainPageReturnCode.ConnectStatus> callback);
	void validateAccount(String userName, AsyncCallback<MainPageReturnCode.ValidateAccountStatus> callback);
}
