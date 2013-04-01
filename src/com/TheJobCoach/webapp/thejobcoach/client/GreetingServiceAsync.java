package com.TheJobCoach.webapp.thejobcoach.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void createAccount(String userName, String userPassword, String email, AsyncCallback<Boolean> callback);
	void connect(String userName, String userPassword, AsyncCallback<Boolean> callback);
}
