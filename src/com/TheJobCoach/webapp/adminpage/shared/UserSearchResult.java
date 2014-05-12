package com.TheJobCoach.webapp.adminpage.shared;

import java.io.Serializable;
import java.util.Vector;

public class UserSearchResult implements Serializable {
	
	private static final long serialVersionUID = 5023720505251872968L;
	
	public Vector<UserSearchEntry> entries;
	public int totalCount;
	
	public UserSearchResult()
	{
		super();
	}

	public UserSearchResult(Vector<UserSearchEntry> entries, int totalCount)
	{
		super();
		this.entries = entries;
		this.totalCount = totalCount;
	}
	

};