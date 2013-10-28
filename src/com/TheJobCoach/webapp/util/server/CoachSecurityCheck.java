package com.TheJobCoach.webapp.util.server;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;

/**
 * The server side implementation of the RPC service.
 */
public class CoachSecurityCheck
{
	static final String SESSION_VARIABLE = "coachuser";
	
	static Logger logger = LoggerFactory.getLogger(CoachSecurityCheck.class);
	
	public static void checkUser(UserId id, HttpSession session) throws CoachSecurityException
	{
		UserId loggedin = (UserId)session.getAttribute(SESSION_VARIABLE);
		if (loggedin == null) 
			{
			logger.warn("unknown user: " + id.userName);
			throw new CoachSecurityException();
			}
		if (!loggedin.equals(id))
			{
			logger.warn("bad user - SECURITY WARNING - : " + id.userName + " " + loggedin.userName);
			throw new CoachSecurityException();
			}
	}

	public static void loginUser(UserId id, HttpSession session)
	{
		logger.info("log user: " + id.userName);
		session.setAttribute(SESSION_VARIABLE, id);
	}
	
	public static void disconnectUser(HttpSession session)
	{
		String user ="unknown";
		UserId loggedin = (UserId)session.getAttribute(SESSION_VARIABLE);
		if (loggedin != null) user = loggedin.userName;
		logger.info("disconnect user: " + user);
		session.setAttribute(SESSION_VARIABLE, null);
	}
}
