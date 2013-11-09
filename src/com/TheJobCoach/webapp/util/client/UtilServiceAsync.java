package com.TheJobCoach.webapp.util.client;

import java.util.Map;

import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface UtilServiceAsync {
	
	public void getValues(UserId user, String rootValue, AsyncCallback<Map<String,String>> callback) throws CassandraException, SystemException, CoachSecurityException;
	public void setValues(UserId user, Map<String,String> map, AsyncCallback<String> callback) throws CassandraException, SystemException, CoachSecurityException;
	public void sendUpdateList(UserId user, UpdateRequest request, AsyncCallback<UpdateResponse> callback) throws CassandraException, SystemException, CoachSecurityException;

}
