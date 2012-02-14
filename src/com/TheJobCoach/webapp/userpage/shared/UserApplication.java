package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class UserApplication implements Serializable {

	
	private static final long serialVersionUID = 1115255124501443730L;
	
	public enum UserApplicationStatus 
	{ 
		NEW,
		APPLIED,
		DISMISSED,
		CALLED_BACK,
		NEED_PHONE_CALL,
		WAIT_INTERVIEW,
		WAIT_INTERVIEW_RESULT,
		SUCCESS
	};
	
	public String ID;
	public Date lastUpdate;
	public Vector<String> logEntryList;
	public UserApplicationStatus status;
	
	public UserApplication(String _ID, Date _lastUpdate, Vector<String> _logEntryList, UserApplicationStatus _status)
	{
		ID = _ID;
		lastUpdate = _lastUpdate;
		logEntryList = _logEntryList;
		status = _status;
	}
	
	public UserApplication()
	{
	}
}
