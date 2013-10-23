package com.TheJobCoach.webapp.util.server;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;

@SuppressWarnings("deprecation")
public class TestCoachSecurityCheck {

	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static UserId id2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);
	
	class HttpSessionTest implements HttpSession
	{

		Object var = null;
		
		@Override
		public Object getAttribute(String arg0) {
			System.out.println("getAttribute " + arg0 + " " + ( var == null ? "null": ((UserId)var).userName));
			if (arg0.equals(CoachSecurityCheck.SESSION_VARIABLE))
				return var;
			return null;
		}

		@Override
		public Enumeration getAttributeNames() {
			return null;
		}

		@Override
		public long getCreationTime() {
			return 0;
		}

		@Override
		public String getId() {
			return null;
		}

		@Override
		public long getLastAccessedTime() {
			return 0;
		}

		@Override
		public int getMaxInactiveInterval() {
			return 0;
		}

		@Override
		public ServletContext getServletContext() {
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		public HttpSessionContext getSessionContext() {
			return null;
		}

		@Override
		public Object getValue(String arg0) {
			return null;
		}

		@Override
		public String[] getValueNames() {
			return null;
		}

		@Override
		public void invalidate() {
		}

		@Override
		public boolean isNew() {
			return false;
		}

		@Override
		public void putValue(String arg0, Object arg1) {
		}

		@Override
		public void removeAttribute(String arg0) {
		}

		@Override
		public void removeValue(String arg0) {
		}

		@Override
		public void setAttribute(String arg0, Object arg1) {
			System.out.println("setAttribute " + arg0
					+ " " 
					+ ((arg1 == null ) ? "null" : ((UserId)arg1).userName));
		
			if (arg0.equals(CoachSecurityCheck.SESSION_VARIABLE))
				var = arg1;
		}

		@Override
		public void setMaxInactiveInterval(int arg0) {
		}
		
	}
	
	public void runCheck(HttpSessionTest session, UserId user, boolean t)
	{
		boolean check = false;
		
		try{
		CoachSecurityCheck.checkUser(user, session);
		}
		catch (CoachSecurityException e)
		{
			check = true;
		}
		assertEquals(!t, check);
	}
	
	@Test
	public void testGetStringDate()
	{
		HttpSessionTest session = new HttpSessionTest();
		runCheck(session, id, false);
		CoachSecurityCheck.loginUser(id, session);	
		CoachSecurityCheck.loginUser(id2, session);	
		runCheck(session, id, false);
		runCheck(session, id2, true);
		CoachSecurityCheck.disconnectUser(session);
		runCheck(session, id, false);
		runCheck(session, id2, false);
		CoachSecurityCheck.loginUser(id, session);	
		runCheck(session, id, true);
		runCheck(session, id2, false);
		
	}
	
}
