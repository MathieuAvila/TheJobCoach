package com.TheJobCoach.webapp.mainpage.server;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.webapp.util.shared.UserId;

public class DeconnectionListener implements HttpSessionListener
{
	private static Logger logger = LoggerFactory.getLogger(DeconnectionListener.class);
	static AccountManager account = new AccountManager();

	@Override
	public void sessionCreated(HttpSessionEvent arg0)
	{
		logger.info("Create session: " + arg0.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0)
	{
		logger.info("Destroy session: " + arg0.getSession().getId());
		HttpSession session = arg0.getSession();
		UserId result = (UserId)session.getAttribute("userid");
		if (result == null) return;
		account.disconnect(result);
	}

}
