package com.TheJobCoach.webapp.util.client;

import java.util.Map;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface UtilServiceAsync {
	
	public void getValues(UserId user, String rootValue, AsyncCallback<Map<String,String>> callback);
	public void setValues(UserId user, Map<String,String> map, AsyncCallback<String> callback);
}
