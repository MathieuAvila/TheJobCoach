package com.TheJobCoach.webapp.util.server;

import java.util.Map;

import com.TheJobCoach.userdata.ConnectionLog;
import com.TheJobCoach.userdata.UserValues;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.client.UtilService;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
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
	public Map<String,String> getValues(UserId user, String rootValue) throws CassandraException, SystemException 
	{
		return userValues.getValues(user, rootValue);		
	}
	
	@Override
	public String setValues(UserId user, Map<String,String> map) throws CassandraException, SystemException 
	{
		userValues.setValues(user, map, true);	
		return "";
	}

	@Override
	public UpdateResponse sendUpdateList(UserId user, UpdateRequest request) 	throws CassandraException, SystemException
	{
		// TODO: Case with multiple windows at the same time.
		logManager.addLogTimeDay(
				user.userName, 
				request.from, 
				request.seconds); 
		UpdateResponse response = new UpdateResponse();
		if (request.getFirstTime)
			response.totalDayTime = logManager.getLogTimeDay(user.userName, request.from);
		return response;
	}
}
