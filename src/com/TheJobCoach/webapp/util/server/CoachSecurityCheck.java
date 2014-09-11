package com.TheJobCoach.webapp.util.server;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.UserId;

/**
 * The server side implementation of the RPC service.
 */
public class CoachSecurityCheck
{
	static final String SESSION_VARIABLE = "coachuser";
	
	static Logger logger = LoggerFactory.getLogger(CoachSecurityCheck.class);
	
	static void printStackTrace()
	{
		System.out.println("Security stack trace:");
		for (StackTraceElement element: Thread.currentThread().getStackTrace())
		{
			System.out.println(".. " + element.toString());
		}
	}
	
	public static void throwSecurityException() throws CoachSecurityException
	{
		printStackTrace();
		throw new CoachSecurityException();
	}
	
	public static void checkUser(UserId id, HttpSession session) throws CoachSecurityException
	{
		UserId loggedin = (UserId)session.getAttribute(SESSION_VARIABLE);
		if (loggedin == null) 
			{
			logger.warn("unknown user: " + id.userName);
			throwSecurityException();
			}
		
		if (!loggedin.token.equals(id.token))
			logger.warn("bad token - : " + loggedin.token + " versus " + id.token);
		if (!loggedin.userName.equals(id.userName))
				logger.warn("bad id - : " + loggedin.userName + " versus " + id.userName);
		if (!loggedin.type.equals(id.type))
			logger.warn("bad type - : " + loggedin.type + " versus " + id.type);
		if (loggedin.testAccount != id.testAccount)
			logger.warn("bad test account type - : " + loggedin.testAccount + " versus " + id.testAccount);

		if (!loggedin.equals(id))
			{
			logger.warn("bad user - SECURITY WARNING - : " + id.userName + " " + loggedin.userName);
			throwSecurityException();
			}
	}

	static void invalidate(HttpSession session)
	{
		for (@SuppressWarnings("unchecked")
		Enumeration<String> e = (Enumeration<String>)session.getAttributeNames(); e.hasMoreElements();)
			session.setAttribute((String)e.nextElement(), null);
	}
	
	public static void loginUser(UserId id, HttpSession session)
	{
		invalidate(session);
		logger.info("log user: " + id.userName);
		session.setAttribute(SESSION_VARIABLE, id);
	}
	
	public static void disconnectUser(HttpSession session)
	{
		String user ="unknown";
		invalidate(session);
		UserId loggedin = (UserId)session.getAttribute(SESSION_VARIABLE);
		if (loggedin != null) user = loggedin.userName;
		logger.info("disconnect user: " + user);
		session.setAttribute(SESSION_VARIABLE, null);
	}
}
