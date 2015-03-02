package com.TheJobCoach.userdata;

import static org.junit.Assert.assertEquals;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MockMailer;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.ChatInfo;

public class TestUserChatManager
{
	Logger logger = LoggerFactory.getLogger(TestUserChatManager.class);

	UserId contact_id_1 = new UserId("user1", "token1", UserId.UserType.USER_TYPE_SEEKER);
	UserId contact_id_2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);

	UserChatManager userChatManager = new UserChatManager();
	UserChatManager userChatManager1 = new UserChatManager(contact_id_1);
	UserChatManager userChatManager2 = new UserChatManager(contact_id_2);
	
	MockMailer mockMail = new MockMailer();
	
	AccountManager account = new AccountManager();
	UserValues userValues = new UserValues();
	
	@Before
	public void setTest() throws CassandraException, SystemException
	{
		userChatManager.deleteUser(contact_id_1);
		userChatManager.deleteUser(contact_id_2);
		
		MailerFactory.setMailer(mockMail);
		
		// create user accounts
		CoachTestUtils.createOneAccount(contact_id_1);
		CoachTestUtils.createOneAccount(contact_id_2);
	}

	void checkIsMsgLog(ChatInfo info, int s, int ms, ChatInfo.MsgType type, String from, String msg)
	{
		System.out.println(info.dst + " " + from);
		System.out.println(info.type + " " + type);
		System.out.println(String.valueOf(info.eventTime.getTime() - CoachTestUtils.getDate(2010, 1, 1, 1, 2, s, 0).getTime())
				+ " " + String.valueOf(info.eventTime.getTime() - CoachTestUtils.getDate(2010, 1, 1, 1, 2, s, ms).getTime()));
		assertEquals(CoachTestUtils.getDate(2010, 1, 1, 1, 2, s, ms), info.eventTime);
		assertEquals(from, info.dst);
		assertEquals(type, info.type);
	}
	
	void checkIsMsg(ChatInfo info, int s, int ms, String from, String msg)
	{
		checkIsMsgLog( info,  s,  ms, ChatInfo.MsgType.MSG_FROM,  from,  msg);
	}

	void checkIsTyping(ChatInfo info, int s, int ms, String from)
	{
		assertEquals(ChatInfo.MsgType.IS_TYPING, info.type);
		//System.out.println(info.eventTime.getTime() - CoachTestUtils.getDate(2010, 1, 1, 1, 2, s, 0).getTime());
		//System.out.println(CoachTestUtils.getDate(2010, 1, 1, 1, 2, s, ms));
		//System.out.println(info.eventTime);
		assertEquals(CoachTestUtils.getDate(2010, 1, 1, 1, 2, s, ms), info.eventTime);
		assertEquals(from, info.dst);
	}
	
	void checkIsStatus(ChatInfo info, int s, int ms, String from, ChatInfo.UserStatus status)
	{
		assertEquals(ChatInfo.MsgType.STATUS_CHANGE, info.type);
		//System.out.println(info.eventTime.getTime() - CoachTestUtils.getDate(2010, 1, 1, 1, 2, s, 0).getTime());
		//System.out.println(CoachTestUtils.getDate(2010, 1, 1, 1, 2, s, ms));
		//System.out.println(info.eventTime);
		assertEquals(CoachTestUtils.getDate(2010, 1, 1, 1, 2, s, ms), info.eventTime);
		assertEquals(from, info.dst);
		assertEquals(status, info.status);
	}
	
	@Test
	public void testEvents() throws CassandraException, SystemException
	{
		setTest();
		
		// no msg at first
		assertEquals(0, userChatManager1.getLastInfos(CoachTestUtils.getDate(2000, 1, 1)).size());
		assertEquals(0, userChatManager2.getLastInfos(CoachTestUtils.getDate(2000, 1, 1)).size());
		assertEquals(0, userChatManager1.getLastMsgFromUser(contact_id_2.userName, CoachTestUtils.getDate(2000, 1, 1), 100).size());
		assertEquals(0, userChatManager2.getLastMsgFromUser(contact_id_1.userName, CoachTestUtils.getDate(2000, 1, 1), 100).size());
		
		// insert events from 1 to 2: status, msg, msg, typing, msg
		userChatManager1.hasChangedStatus(contact_id_2.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 3, 100), ChatInfo.UserStatus.ONLINE);		
		userChatManager1.addChatMsg(contact_id_2.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 4, 200), "msg_1_2__1");
		userChatManager1.addChatMsg(contact_id_2.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 5, 201), "msg_1_2__2");
		userChatManager1.isTypingTo(contact_id_2.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 6, 300));
		userChatManager1.addChatMsg(contact_id_2.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 7, 301), "msg_1_2__3");
		
		// insert events from 2 to 1: status, msg, msg, typing, msg
		userChatManager2.hasChangedStatus(contact_id_1.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 4, 150), ChatInfo.UserStatus.OFFLINE);		
		userChatManager2.addChatMsg(contact_id_1.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 4, 250), "msg_2_1__1");
		userChatManager2.addChatMsg(contact_id_1.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 5, 251), "msg_2_1__2");
		userChatManager2.isTypingTo(contact_id_1.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 6, 350));
		userChatManager2.addChatMsg(contact_id_1.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 7, 351), "msg_2_1__3");
	
		// check changes for 1
		Vector<ChatInfo> changes1_all = userChatManager1.getLastInfos(CoachTestUtils.getDate(2010, 1, 1, 1, 2, 3, 0));
		assertEquals(5, changes1_all.size());
		checkIsStatus(changes1_all.get(0), 4, 150, contact_id_2.userName, ChatInfo.UserStatus.OFFLINE);
		checkIsMsg(changes1_all.get(1), 4, 250, contact_id_2.userName, "msg_2_1__1");
		checkIsMsg(changes1_all.get(2), 5, 251, contact_id_2.userName, "msg_2_1__2");
		checkIsTyping(changes1_all.get(3), 6, 350, contact_id_2.userName);
		checkIsMsg(changes1_all.get(4), 7, 351, contact_id_2.userName, "msg_2_1__3");
		
		// check changes for 2
		Vector<ChatInfo> changes2_all = userChatManager2.getLastInfos(CoachTestUtils.getDate(2010, 1, 1, 1, 2, 3, 0));
		assertEquals(5, changes2_all.size());
		checkIsStatus(changes2_all.get(0), 3, 100, contact_id_1.userName, ChatInfo.UserStatus.ONLINE);
		checkIsMsg(changes2_all.get(1), 4, 200, contact_id_1.userName, "msg_1_2__1");
		checkIsMsg(changes2_all.get(2), 5, 201, contact_id_1.userName, "msg_1_2__2");
		checkIsTyping(changes2_all.get(3), 6, 300, contact_id_1.userName);
		checkIsMsg(changes2_all.get(4), 7, 301, contact_id_1.userName, "msg_1_2__3");
		
		// check messages for 1
		Vector<ChatInfo> messages1_all = userChatManager1.getLastMsgFromUser(contact_id_2.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 3, 0), 100);
		assertEquals(6, messages1_all.size());
		checkIsMsgLog(messages1_all.get(0), 4, 200, ChatInfo.MsgType.MSG_TO,     contact_id_2.userName, "msg_1_2__1");
		checkIsMsgLog(messages1_all.get(1), 4, 250, ChatInfo.MsgType.MSG_FROM,   contact_id_2.userName, "msg_2_1__1");
		checkIsMsgLog(messages1_all.get(2), 5, 201, ChatInfo.MsgType.MSG_TO,     contact_id_2.userName, "msg_1_2__2");
		checkIsMsgLog(messages1_all.get(3), 5, 251, ChatInfo.MsgType.MSG_FROM,   contact_id_2.userName,   "msg_2_2__2");
		checkIsMsgLog(messages1_all.get(4), 7, 301, ChatInfo.MsgType.MSG_TO,     contact_id_2.userName,   "msg_1_2__3");
		checkIsMsgLog(messages1_all.get(5), 7, 351, ChatInfo.MsgType.MSG_FROM,   contact_id_2.userName,   "msg_2_2__3");

		// check messages for 2
		Vector<ChatInfo> messages2_all = userChatManager2.getLastMsgFromUser(contact_id_1.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 3, 0), 100);
		assertEquals(6, messages2_all.size());
		checkIsMsgLog(messages2_all.get(0), 4, 200, ChatInfo.MsgType.MSG_FROM,     contact_id_1.userName, "msg_1_2__1");
		checkIsMsgLog(messages2_all.get(1), 4, 250, ChatInfo.MsgType.MSG_TO,       contact_id_1.userName, "msg_2_1__1");
		checkIsMsgLog(messages2_all.get(2), 5, 201, ChatInfo.MsgType.MSG_FROM,     contact_id_1.userName, "msg_1_2__2");
		checkIsMsgLog(messages2_all.get(3), 5, 251, ChatInfo.MsgType.MSG_TO,       contact_id_1.userName,   "msg_2_2__2");
		checkIsMsgLog(messages2_all.get(4), 7, 301, ChatInfo.MsgType.MSG_FROM,     contact_id_1.userName,   "msg_1_2__3");
		checkIsMsgLog(messages2_all.get(5), 7, 351, ChatInfo.MsgType.MSG_TO,       contact_id_1.userName,   "msg_2_2__3");
		
		// check sub_list of messages for 1
		Vector<ChatInfo> messages1_sub = userChatManager1.getLastMsgFromUser(contact_id_2.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 5, 250), 100);
		assertEquals(3, messages1_sub.size());
		checkIsMsgLog(messages1_sub.get(0), 5, 251, ChatInfo.MsgType.MSG_FROM,   contact_id_2.userName,   "msg_2_2__2");
		checkIsMsgLog(messages1_sub.get(1), 7, 301, ChatInfo.MsgType.MSG_TO,     contact_id_2.userName,   "msg_1_2__3");
		checkIsMsgLog(messages1_sub.get(2), 7, 351, ChatInfo.MsgType.MSG_FROM,   contact_id_2.userName,   "msg_2_2__3");
			
		// check sub_list of messages for 1, before end
		Vector<ChatInfo> messages1_cut = userChatManager1.getLastMsgFromUser(contact_id_2.userName, CoachTestUtils.getDate(2010, 1, 1, 1, 2, 3, 0), 2);
		assertEquals(2, messages1_cut.size());
		checkIsMsgLog(messages1_cut.get(0), 4, 200, ChatInfo.MsgType.MSG_TO,     contact_id_2.userName, "msg_1_2__1");
		checkIsMsgLog(messages1_cut.get(1), 4, 250, ChatInfo.MsgType.MSG_FROM,   contact_id_2.userName, "msg_2_1__1");

		// check sub list of changes starting from date
		Vector<ChatInfo> changes1_cut = userChatManager1.getLastInfos(CoachTestUtils.getDate(2010, 1, 1, 1, 2, 5, 300));
		assertEquals(2, changes1_cut.size());
		checkIsTyping(changes1_cut.get(0), 6, 350, contact_id_2.userName);
		checkIsMsg(changes1_cut.get(1), 7, 351, contact_id_2.userName, "msg_2_1__3");		
	}
}
