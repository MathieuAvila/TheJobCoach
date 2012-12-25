package com.TheJobCoach.webapp.util.shared;

import java.io.Serializable;

public class UpdateResponse implements Serializable {
	
	private static final long serialVersionUID = 5013350776113021480L;

	public int totalDayTime;
	
	public UpdateResponse(int totalDayTime) {
		super();
		this.totalDayTime = totalDayTime;
	}

	public UpdateResponse()
	{		
	}
	
}
