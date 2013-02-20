package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;

public class UpdatePeriod implements Serializable {

	private static final long serialVersionUID = 1116255124512443730L;

	public enum PeriodType { DAY, WEEK, MONTH };
	public Date last;
	public int length;
	public PeriodType periodType;
	
	public UpdatePeriod()
	{
		periodType = PeriodType.DAY;
		last = new Date();
	}

	public UpdatePeriod(Date last, int length, PeriodType periodType)
	{
		super();
		this.last = last;
		this.length = length;
		this.periodType = periodType;
	}

}

