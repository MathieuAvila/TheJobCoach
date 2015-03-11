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
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface TestServiceAsync {
	
	public void getLastMsgFromUser(String myUser, String fromUser, int maxCount, Date d, AsyncCallback<Vector<ChatInfo>> callback) throws CassandraException, SystemException, CoachSecurityException;
	public void addChatMsg(String myUser, String dst, String msg, AsyncCallback<Void> callback) throws CassandraException, SystemException, CoachSecurityException;
	public void isTypingTo(String myUser, String dst, AsyncCallback<Void> callback) throws CassandraException, SystemException, CoachSecurityException;

	public void logInOut(String myUser, String myPassword, boolean in, AsyncCallback<Void> callback) throws CassandraException, SystemException, CoachSecurityException;

	public void sendUpdateList(UserId defaultUser, UpdateRequest request, AsyncCallback<UpdateResponse> callback) throws CassandraException, CoachSecurityException;
	public void getContactList(String userName,	AsyncCallback<Vector<ContactInformation>> callback) throws CassandraException, CoachSecurityException;
}
