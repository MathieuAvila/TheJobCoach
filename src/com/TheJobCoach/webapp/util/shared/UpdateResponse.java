package com.TheJobCoach.webapp.util.shared;

import java.io.Serializable;
import java.util.Map;

public class UpdateResponse implements Serializable {
	
	private static final long serialVersionUID = 5013350776113021480L;

	public int totalDayTime;
	
	public Map<String, String> updatedValues;
	
	public UpdateResponse(int totalDayTime) {
		super();
		this.totalDayTime = totalDayTime;
	}

	public UpdateResponse(Map<String, String> updatedValues)
	{
		this.updatedValues = updatedValues;
	}
	
	public UpdateResponse()
	{		
	}
	
}
