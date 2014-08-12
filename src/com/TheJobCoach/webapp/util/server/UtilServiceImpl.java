package com.TheJobCoach.webapp.util.server;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.userdata.ConnectionLog;
import com.TheJobCoach.userdata.ContactManager;
import com.TheJobCoach.userdata.UserValues;
import com.TheJobCoach.webapp.userpage.server.UserServiceImpl;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
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

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	UserId getUserId() throws CoachSecurityException
	{
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		UserId result = (UserId)session.getAttribute("userid");
		if (result == null) throw new CoachSecurityException();
		return result;
	}
	
	ContactManager getContactManager() throws CoachSecurityException
	{
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();

		ContactManager result = (ContactManager)session.getAttribute("contactmanager");
		if (result != null) return result;
		result = new ContactManager(getUserId());
		session.setAttribute("contactmanager", result);
		return result;
	}
	
	@Override
	public Map<String,String> getValues(UserId id, String rootValue) throws CassandraException, SystemException , CoachSecurityException
	{
		UserId currentId = getUserId();
		if (!id.userName.equals(currentId.userName))
		{
			// check credentials
			ContactManager cm = getContactManager();
			ContactInformation ci = cm.getUserClearance(id);
			if (ci.status != ContactStatus.CONTACT_OK)
			{
				// security error.
				logger.warn("user " + currentId.userName + " trying to illegally access: " + id.userName);
				throw new CoachSecurityException();
			}
		}
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
