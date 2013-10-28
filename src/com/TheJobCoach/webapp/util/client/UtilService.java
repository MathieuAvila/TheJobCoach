package com.TheJobCoach.webapp.util.client;

import java.util.Map;

import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("util")
public interface UtilService extends RemoteService 
{		
	public Map<String,String> getValues(UserId user, String rootValue) throws CassandraException, SystemException, CoachSecurityException;
	public String setValues(UserId user, Map<String,String> map) throws CassandraException, SystemException, CoachSecurityException;
	
	public UpdateResponse sendUpdateList(UserId userId, UpdateRequest request) throws CassandraException, SystemException, CoachSecurityException;
}
