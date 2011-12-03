package com.TheJobCoach.webapp.thejobcoach.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	
	String greetServer(String name) throws IllegalArgumentException;
	
	Boolean createAccount(String userName, String userPassword, String email);
	
	Boolean connect(String userName, String userPassword);
}
