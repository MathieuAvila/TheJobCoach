package com.TheJobCoach.webapp.util.shared;

import java.io.Serializable;
import java.util.Date;

public class UpdateRequest implements Serializable {
	
	private static final long serialVersionUID = 5013350776113022480L;

	public Date from;
	public int seconds;
	public boolean getFirstTime;
	public Date lastRequest;
	
	public UpdateRequest(Date from, int seconds, boolean getFirstTime, Date lastRequest) {
		super();
		this.from = from;
		this.seconds = seconds;
		this.getFirstTime = getFirstTime;
		this.lastRequest = lastRequest;
	}

	public UpdateRequest()
	{		
	}
	
}
