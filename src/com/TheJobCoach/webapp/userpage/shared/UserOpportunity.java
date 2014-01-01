package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class UserOpportunity implements Serializable {

	private static final long serialVersionUID = 1115255124501443736L;

	public enum ApplicationStatus 
	{
		NEW,
		DISCOVERED,
		APPLIED,		
		INTERVIEWS,		
		SUCCESS,
		CLOSED
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
	
	/** User private notes */
	public String note;
	
	/** Refers to the list of companies */
	public String companyId;

	/** Type of contract */
	public String contractType;

	/** How much you expect to earn 
	 * Not an integer, but can be converted to if necessary and appropriate */
	public String salary;

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
		case NEW: return "NEW";
		case APPLIED: return "APPLIED";
		case DISCOVERED: return "DISCOVERED";
		case INTERVIEWS: return "INTERVIEWS";
		case SUCCESS: return "SUCCESS";
		case CLOSED: return "CLOSED";
		}
		return "NONE";
	}

	static public ApplicationStatus applicationStatusToString(String t)
	{
		if ("NEW".equals(t)) return ApplicationStatus.NEW;
		if ("DISCOVERED".equals(t)) return ApplicationStatus.DISCOVERED;
		if ("APPLIED".equals(t)) return ApplicationStatus.APPLIED;
		if ("INTERVIEWS".equals(t)) return ApplicationStatus.INTERVIEWS;
		if ("SUCCESS".equals(t)) return ApplicationStatus.SUCCESS;
		if ("CLOSED".equals(t)) return ApplicationStatus.CLOSED;
		return ApplicationStatus.NEW;
	}


	public UserOpportunity(String iD, Date firstSeen, Date lastUpdate,
			String title, String description, String companyId,
			String contractType, String salary, Date startDate, Date endDate,
			boolean systemSource, String source, String url, String location,
			ApplicationStatus status, String note) 
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
		this.note = note;
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
