package com.TheJobCoach.webapp.util.server;

import java.util.Map;

import com.TheJobCoach.userdata.UserValues;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.client.UtilService;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UtilServiceImpl extends RemoteServiceServlet implements UtilService
{
	UserValues userValues = new UserValues();
	
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
}
