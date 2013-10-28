package com.TheJobCoach.webapp.util.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;

/**
 * The server side implementation of the RPC service.
 */
public class ServletSecurityCheck
{
	public static void check(HttpServletRequest request, UserId id) throws CoachSecurityException
	{
		HttpSession session = request.getSession();
		CoachSecurityCheck.checkUser(id, session);
	}

}
