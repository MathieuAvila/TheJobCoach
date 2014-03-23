package com.TheJobCoach.webapp.util.server;

import java.util.Map;

import com.TheJobCoach.userdata.ConnectionLog;
import com.TheJobCoach.userdata.UserValues;
import com.TheJobCoach.webapp.util.client.UtilService;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UtilServiceImpl extends RemoteServiceServlet implements UtilService
{
	UserValues userValues = new UserValues();
	ConnectionLog logManager = new ConnectionLog();
	
	@Override
	public Map<String,String> getValues(UserId id, String rootValue) throws CassandraException, SystemException , CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return userValues.getValues(id, rootValue);		
	}
	
	@Override
	public String setValues(UserId id, Map<String,String> map) throws CassandraException, SystemException , CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		userValues.setValues(id, map, true);	
		return "";
	}

	@Override
	public UpdateResponse sendUpdateList(UserId id, UpdateRequest request) 	throws CassandraException, SystemException, CoachSecurityException
	{
		// TODO: Case with multiple windows at the same time.
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		logManager.addLogTimeDay(
				id.userName, 
				request.from, 
				request.seconds);
		UpdateResponse response = new UpdateResponse(userValues.getUpdatedValues(id));
		if (request.getFirstTime)
			response.totalDayTime = logManager.getLogTimeDay(id.userName, request.from);
		return response;
	}
}
