package com.TheJobCoach.webapp.userpage.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.userdata.ContactManager;
import com.TheJobCoach.userdata.UserChatManager;
import com.TheJobCoach.webapp.userpage.client.TestService;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.ChatInfo;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TestServiceImpl extends RemoteServiceServlet implements TestService
{
	static private AccountManager account = new AccountManager();
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	UserChatManager getUserChatManager(String myUser) throws CoachSecurityException
	{
		UserChatManager result = new UserChatManager(new UserId(myUser));
		return result;
	}

	@Override
	public Vector<ChatInfo> getLastMsgFromUser(String myUser, String fromUser,
			int maxCount, Date d) throws CassandraException, SystemException,
			CoachSecurityException
	{
		UserChatManager userChatManager = getUserChatManager(myUser);
		return userChatManager.getLastMsgFromUser(fromUser, d, maxCount);
	}

	@Override
	public void addChatMsg(String myUser, String dst, String msg)
			throws CassandraException, SystemException, CoachSecurityException
	{
		UserChatManager userChatManager = getUserChatManager(myUser);
		userChatManager.addChatMsg(dst, new Date(), msg);
	}

	@Override
	public void isTypingTo(String myUser, String dst) throws CassandraException,
			SystemException, CoachSecurityException
	{
		UserChatManager userChatManager = getUserChatManager(myUser);
		userChatManager.isTypingTo(dst, new Date());
	}

	@Override
	public void logInOut(String myUser, String myPassword, boolean in) throws CassandraException, SystemException, CoachSecurityException
	{
		logger.info("Logging: " + myUser + " in/out: " + in);
		if (in)
			account.loginAccount(myUser, myPassword);
		else 
			account.disconnect(new UserId(myUser));
	}

	@Override
	public UpdateResponse sendUpdateList(UserId defaultUser,
			UpdateRequest request) throws CassandraException, CoachSecurityException
	{
		UpdateResponse response = new UpdateResponse(
				new HashMap<String, String>(), 
				getUserChatManager(defaultUser.userName).getLastInfos(request.from));
		return response;
	}

	@Override
	public Vector<ContactInformation> getContactList(String userName)
			throws CassandraException, CoachSecurityException
	{
		ContactManager contact =  new ContactManager(new UserId(userName));
		Vector<ContactInformation> result = contact.getContactList();
		return result;
	}

	
}
