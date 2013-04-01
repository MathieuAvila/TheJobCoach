package com.TheJobCoach.webapp.thejobcoach.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.thejobcoach.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {
	
	private final static Logger logger = LoggerFactory.getLogger(GreetingServiceImpl.class);

	public Boolean createAccount(String userName, String userPassword, String email) throws IllegalArgumentException {
		logger.info("Create account for: '" + userName + "' with password: '" + userPassword+ "' with EMail:'" + email + "'");
		return true;
	}

	@Override
	public Boolean connect(String userName, String userPassword) {
		logger.info("Connection from: '" + userName + "' with password: '" + userPassword+ "'");
		return true;
	}
}
