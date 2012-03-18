package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class UserOpportunity implements Serializable {

	private static final long serialVersionUID = 1115255124501443736L;

	public enum ApplicationStatus 
	{
		NONE,
		NEW,
		APPLIED,
		NEED_CALL_BACK,
		WAIT_INTERVIEW,
		WAIT_INTERVIEW_RESULT,
		REQUEST_PENDING,
		DISMISSED,
		SUCCESS,
		FAILURE
	};

	/** UUID. Same as @UserApplication. */
	public String ID;

	/** First time spotted */	
	public Date firstSeen;

	/** Last time spotted */
	public Date lastUpdate;

	/** Title for the job */
	public String title;

	/** Full description for the opportunity */
	public String description;

	/** Refers to the list of companies */
	public String companyId;

	/** Type of contract */
	public String contractType;

	/** How much you expect to earn */
	public int salary;

	/** When the job starts. Set to 1/1/1900 if not set */	
	public Date startDate;

	/** When it ends. Set to 1/1/1900 if not set */
	public Date endDate;

	/** TRUE if it was created by the system */
	public boolean systemSource;

	/** Name of the source. User Text if not system, otherwise system interpreted. */
	public String source;

	/** reference URL */
	public String url;

	/** Where to work */
	public String location;

	/** Last Time user updated info */
	public Date lastUserUpdate;

	/** Has user applied and when it is ?" */
	public ApplicationStatus status;

	static public String applicationStatusToString(ApplicationStatus t)
	{
		switch (t)
		{
		case NONE: return "NONE";
		case NEW: return "NEW";
		case APPLIED: return "APPLIED";
		case NEED_CALL_BACK: return "NEED_CALL_BACK";
		case WAIT_INTERVIEW: return "WAIT_INTERVIEW";
		case WAIT_INTERVIEW_RESULT: return "WAIT_INTERVIEW_RESULT";
		case REQUEST_PENDING: return "REQUEST_PENDING";
		case DISMISSED: return "DISMISSED";
		case SUCCESS: return "SUCCESS";
		case FAILURE: return "FAILURE";
		}
		return "NONE";
	}
	
	static public ApplicationStatus applicationStatusToString(String t)
	{
		if ("NONE".equals(t)) return ApplicationStatus.NONE;
		if ("NEW".equals(t)) return ApplicationStatus.NEW;
		if ("APPLIED".equals(t)) return ApplicationStatus.APPLIED;
		if ("NEED_CALL_BACK".equals(t)) return ApplicationStatus.NEED_CALL_BACK;
		if ("WAIT_INTERVIEW".equals(t))return ApplicationStatus.WAIT_INTERVIEW;
		if ("WAIT_INTERVIEW_RESULT".equals(t))return ApplicationStatus.WAIT_INTERVIEW_RESULT;
		if ("REQUEST_PENDING".equals(t)) return ApplicationStatus.REQUEST_PENDING;
		if ("DISMISSED".equals(t)) return ApplicationStatus.DISMISSED;
		if ("SUCCESS".equals(t)) return ApplicationStatus.SUCCESS;
		if ("FAILURE".equals(t)) return ApplicationStatus.FAILURE;
		return ApplicationStatus.NONE;
	}


	public UserOpportunity(String iD, Date firstSeen, Date lastUpdate,
			String title, String description, String companyId,
			String contractType, int salary, Date startDate, Date endDate,
			boolean systemSource, String source, String url, String location,
			ApplicationStatus status) 
	{
		super();
		ID = iD;
		this.firstSeen = firstSeen;
		this.lastUpdate = lastUpdate;
		this.title = title;
		this.description = description;
		this.companyId = companyId;
		this.contractType = contractType;
		this.salary = salary;
		this.startDate = startDate;
		this.endDate = endDate;
		this.systemSource = systemSource;
		this.source = source;
		this.url = url;
		this.location = location;
		this.status = status;
	}

	public UserOpportunity()
	{
	}

	static Vector<ApplicationStatus> applicationStatusTable = null;

	public static final Vector<ApplicationStatus> getApplicationStatusTable()
	{
		if (applicationStatusTable == null)
		{
			applicationStatusTable = new Vector<ApplicationStatus>();
			ApplicationStatus[] table = ApplicationStatus.values();
			for (ApplicationStatus e : table)
			{
				applicationStatusTable.add(e);
			}
		}
		return applicationStatusTable;
	}
}
