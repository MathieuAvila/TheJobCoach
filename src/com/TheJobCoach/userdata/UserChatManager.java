package com.TheJobCoach.userdata;


import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.ChatInfo;
import com.TheJobCoach.webapp.util.shared.UserId;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;


public class UserChatManager implements IUserDataManager {

	static ColumnFamilyDefinition cfDefChatLog = null;
	static ColumnFamilyDefinition cfDefChatDetail = null;

	final static String COLUMN_FAMILY_NAME_CHAT_LOG = "userchatlog";
	final static String COLUMN_FAMILY_NAME_CHAT_DETAIL = "userchatdetail";

	public UserChatManager()
	{
		cfDefChatLog = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CHAT_LOG, cfDefChatLog);
		cfDefChatDetail = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CHAT_DETAIL, cfDefChatDetail);
	}

	static UserChatManager instance = new UserChatManager();

	private static Map<ChatInfo.MsgType, String> typeToString = new HashMap<ChatInfo.MsgType, String>();
	private static Map<String, ChatInfo.MsgType> stringToType = new HashMap<String, ChatInfo.MsgType>();
	private static Map<ChatInfo.UserStatus, String> statusToString = new HashMap<ChatInfo.UserStatus, String>();
	private static Map<String, ChatInfo.UserStatus> stringToStatus = new HashMap<String, ChatInfo.UserStatus>();
	static
	{
		typeToString.put(ChatInfo.MsgType.MSG_FROM, "f");
		typeToString.put(ChatInfo.MsgType.MSG_TO, "t");
		typeToString.put(ChatInfo.MsgType.STATUS_CHANGE, "c");
		typeToString.put(ChatInfo.MsgType.IS_TYPING, "i");

		stringToType.put("f", ChatInfo.MsgType.MSG_FROM);
		stringToType.put("t", ChatInfo.MsgType.MSG_TO);
		stringToType.put("c", ChatInfo.MsgType.STATUS_CHANGE);
		stringToType.put("i", ChatInfo.MsgType.IS_TYPING);
		
		stringToStatus.put("i", ChatInfo.UserStatus.ONLINE);
		stringToStatus.put("o", ChatInfo.UserStatus.OFFLINE);

		statusToString.put(ChatInfo.UserStatus.ONLINE, "i");
		statusToString.put(ChatInfo.UserStatus.OFFLINE, "o");
	}

	UserId current;
	
	public UserChatManager(UserId current)
	{
		this.current = current;
	}
		
	void innerAddChatMsg(String log, String com, Date d, String msg, boolean from) throws CassandraException
	{
		HashMap<String, String> map = new HashMap<String, String>();
		String key = com + "#" + Convertor.toString(d);
		String value = (from ? typeToString.get(ChatInfo.MsgType.MSG_FROM) : typeToString.get(ChatInfo.MsgType.MSG_TO)) + "#" + msg;
		map.put(key, value);
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CHAT_DETAIL, log, map);
		
		System.out.println("Add msg to table: "+log+ " key:" + key + " value:" + value);
	}

	public void addChatMsg(String dst, Date d, String msg) throws CassandraException
	{
		innerAddChatMsg(current.userName, dst, d, msg, false);
		innerAddChatMsg(dst, current.userName, d, msg, true);
		
		// add to other's log
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Convertor.toString(d) + "#" + typeToString.get(ChatInfo.MsgType.MSG_FROM) + "#" + current.userName, msg);	
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CHAT_LOG, dst, map);
	}

	public void isTypingTo(String dst, Date d) throws CassandraException
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Convertor.toString(d) + "#" + typeToString.get(ChatInfo.MsgType.IS_TYPING) + "#" + current.userName, "");	
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CHAT_LOG, dst, map);
	}
	
	/**  Tell user "dst" that my new status is status */
	public void hasChangedStatus(String dst, Date d, ChatInfo.UserStatus status) throws CassandraException
	{
		HashMap<String, String> map = new HashMap<String, String>();
		String key = Convertor.toString(d) + "#" + typeToString.get(ChatInfo.MsgType.STATUS_CHANGE) + "#" + current.userName;
		String value = statusToString.get(status);
		map.put(key, value);
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CHAT_LOG, dst, map);
	}
	
	public Vector<ChatInfo> getLastInfos(Date from) throws CassandraException
	{
		Vector<ChatInfo> result = new Vector<ChatInfo>();
		Map<String, String> events = CassandraAccessor.getColumnRange(COLUMN_FAMILY_NAME_CHAT_LOG, current.userName, Convertor.toString(from), "", 100);
		Vector<String> evList = new Vector<String>(events.keySet());
		Collections.sort(evList);
		for (String event: evList)
		{
			String[] rest = event.split("#");
			ChatInfo.MsgType type = stringToType.get(rest[1]);
			switch(type)
			{
			case MSG_TO:
			case MSG_FROM:	
				result.add(new ChatInfo(Convertor.toDate(rest[0]), rest[2], type, events.get(event)));
				break;
			case IS_TYPING:
				result.add(new ChatInfo(Convertor.toDate(rest[0]), rest[2]));
				break;
			case STATUS_CHANGE:
				result.add(new ChatInfo(Convertor.toDate(rest[0]), rest[2], stringToStatus.get(events.get(event))));
				break;
			default:
				break;
			}
		}
		return result;
	}

	public Vector<ChatInfo> getLastMsgFromUser(String fromUser, Date from, int maxCount) throws CassandraException
	{
		Vector<ChatInfo> result = new Vector<ChatInfo>();
		
		System.out.println(fromUser + "#" + Convertor.toString(from));
		System.out.println(fromUser + "#0000000000000");
		
		Map<String, String> events = CassandraAccessor.getColumnRangeReversed(COLUMN_FAMILY_NAME_CHAT_DETAIL, 
				current.userName, 
				fromUser + "#" + Convertor.toString(from), 
				fromUser + "#000000000000",
				maxCount);
		Vector<String> evList = new Vector<String>(events.keySet());
		Collections.sort(evList);
		for (String event: evList)
		{
			String[] rest = event.split("#");
			String[] restRes = events.get(event).split("#");
			String message = "";
			if (restRes.length == 2) message = restRes[1];
			System.out.println("event: " + event + " value:" + events.get(event));
			result.add(new ChatInfo(Convertor.toDate(rest[1]), rest[0], stringToType.get(restRes[0]), message));
			System.out.println("get msg from : " + rest[0] + " ");
		}
		return result;
	}

	@Override
	public void deleteUser(UserId user) throws CassandraException
	{
		if (user != null)
		{
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CHAT_LOG, user.userName);
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CHAT_DETAIL, user.userName);
		}
	}

	@Override
	public void createTestUser(UserId user, String lang)
	{

	}

	@Override
	public void createUserDefaults(UserId user, String lang)
	{
		/* nothing to do */
	}
}
