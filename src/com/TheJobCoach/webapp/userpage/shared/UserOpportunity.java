package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;

public class UserOpportunity implements Serializable {

	private static final long serialVersionUID = 1115255124501443736L;
	
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
	
	public UserOpportunity(String iD, Date firstSeen, Date lastUpdate,
			String title, String description, String companyId,
			String contractType, int salary, Date startDate, Date endDate,
			boolean systemSource, String source, String url, String location) 
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
	}
	
	public UserOpportunity()
	{
	}
}
