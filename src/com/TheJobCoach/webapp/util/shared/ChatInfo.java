package com.TheJobCoach.webapp.util.shared;

import java.io.Serializable;
import java.util.Date;

public class ChatInfo implements Serializable {
	
	private static final long serialVersionUID = 5013350776113022480L;

	public enum UserStatus { ONLINE, OFFLINE };
	public enum MsgType { MSG_FROM, MSG_TO, STATUS_CHANGE, IS_TYPING };
	
	public Date eventTime;
	public MsgType type;
	public UserStatus status;
	public String dst;
	public String msg;
	
	/** msg */
	public ChatInfo(Date eventTime, String dst, MsgType from_to, String msg) {
		super();
		this.eventTime = eventTime;
		this.type = from_to;
		this.status = UserStatus.ONLINE;
		this.dst = dst;
		this.msg = msg;
	}
	
	/** typing */
	public ChatInfo(Date eventTime, String dst) {
		super();
		this.eventTime = eventTime;
		this.type = MsgType.IS_TYPING;
		this.status = UserStatus.ONLINE;
		this.dst = dst;
	}

	/** status */
	public ChatInfo(Date eventTime, String dst, UserStatus status)
	{
		super();
		this.eventTime = eventTime;
		this.dst = dst;
		this.type = MsgType.STATUS_CHANGE;
		this.status = status;
	}
	
	public ChatInfo()
	{
		super();		
	}
	
}
