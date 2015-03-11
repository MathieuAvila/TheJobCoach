package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.ChatInfo;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("TestServlet")
public interface TestService extends RemoteService 
{	
	
	public Vector<ChatInfo> getLastMsgFromUser(String myUser, String fromUser, int count, Date d) throws CassandraException, SystemException, CoachSecurityException;
	public void addChatMsg(String myUser, String dst, String msg) throws CassandraException, SystemException, CoachSecurityException;
	public void isTypingTo(String myUser, String dst) throws CassandraException, SystemException, CoachSecurityException;
	
	public void logInOut(String myUser, String myPassword, boolean in) throws CassandraException, SystemException, CoachSecurityException;
	
	public UpdateResponse sendUpdateList(UserId defaultUser, UpdateRequest request) throws CassandraException, CoachSecurityException;

	public Vector<ContactInformation> getContactList(String userName) throws CassandraException, CoachSecurityException;
}
