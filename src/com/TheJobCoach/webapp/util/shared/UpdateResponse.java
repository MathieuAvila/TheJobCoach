package com.TheJobCoach.webapp.util.shared;

import java.io.Serializable;
import java.util.Map;
import java.util.Vector;

public class UpdateResponse implements Serializable {
	
	private static final long serialVersionUID = 5013350776113021480L;

	public int totalDayTime;
	
	public Map<String, String> updatedValues;
	public Vector<ChatInfo> chatInfo;
	
	public UpdateResponse(int totalDayTime) {
		super();
		this.totalDayTime = totalDayTime;
	}

	public UpdateResponse(Map<String, String> updatedValues, Vector<ChatInfo> chatInfo)
	{
		this.updatedValues = updatedValues;
		this.chatInfo = chatInfo;
	}
	
	public UpdateResponse()
	{		
	}
	
}
