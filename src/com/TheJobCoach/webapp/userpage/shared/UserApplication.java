package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class UserApplication implements Serializable {

	
	private static final long serialVersionUID = 1115255124501443730L;
	
	public String ID;
	public Date lastUpdate;
	public Vector<String> logEntryList;
	
	public UserApplication()
	{
	}
}
