package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface LoginServiceAsync {
	void createAccount(String userName, String name, String firstName, String email, String password, String locale, AsyncCallback<MainPageReturnCode.CreateAccountStatus> callback);
	void connect(String userName, String userPassword, AsyncCallback<MainPageReturnLogin> callback);
	void validateAccount(String userName, String token,AsyncCallback<MainPageReturnCode.ValidateAccountStatus> callback);
}
