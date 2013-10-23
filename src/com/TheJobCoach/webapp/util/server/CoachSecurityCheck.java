package com.TheJobCoach.webapp.util.server;

import javax.servlet.http.HttpSession;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;

/**
 * The server side implementation of the RPC service.
 */
public class CoachSecurityCheck
{
	static final String SESSION_VARIABLE = "coachuser";
	
	public static void checkUser(UserId id, HttpSession session) throws CoachSecurityException
	{
		UserId loggedin = (UserId)session.getAttribute(SESSION_VARIABLE);
		System.out.println(" 1 " + id.userName);
		if (loggedin == null) throw new CoachSecurityException();
		System.out.println(" 2 " + loggedin.userName);
		System.out.println(" 3 " + !loggedin.equals(id));
		if (!loggedin.equals(id)) throw new CoachSecurityException();
	}

	public static void loginUser(UserId id, HttpSession session)
	{
		session.setAttribute(SESSION_VARIABLE, id);
	}
	
	public static void disconnectUser(HttpSession session)
	{
		session.setAttribute(SESSION_VARIABLE, null);
	}
}
