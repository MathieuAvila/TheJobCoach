package com.TheJobCoach.webapp.util.server;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.TheJobCoach.userdata.ConnectionLog;
import com.TheJobCoach.userdata.UserValues;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.client.UtilService;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
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
	
	private void check(UserId id) throws CoachSecurityException
	{
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		CoachSecurityCheck.checkUser(id, session);
	}
	@Override
	public Map<String,String> getValues(UserId id, String rootValue) throws CassandraException, SystemException , CoachSecurityException
	{
		check(id);
		return userValues.getValues(id, rootValue);		
	}
	
	@Override
	public String setValues(UserId id, Map<String,String> map) throws CassandraException, SystemException , CoachSecurityException
	{
		check(id);
		userValues.setValues(id, map, true);	
		return "";
	}

	@Override
	public UpdateResponse sendUpdateList(UserId id, UpdateRequest request) 	throws CassandraException, SystemException, CoachSecurityException
	{
		// TODO: Case with multiple windows at the same time.
		check(id);
		logManager.addLogTimeDay(
				id.userName, 
				request.from, 
				request.seconds); 
		UpdateResponse response = new UpdateResponse();
		if (request.getFirstTime)
			response.totalDayTime = logManager.getLogTimeDay(id.userName, request.from);
		return response;
	}
}
