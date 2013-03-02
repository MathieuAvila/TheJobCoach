package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;

public class UpdatePeriod implements Serializable {

	private static final long serialVersionUID = 1116255124512443730L;

	public enum PeriodType { DAY, WEEK, MONTH };
	public Date last;
	public int length;
	public PeriodType periodType;
	
	static public String periodType2String(PeriodType period)
	{
		switch(period)
		{
		case DAY: return "DAY";
		case WEEK:return "WEEK";
		case MONTH:return "MONTH";
		}
		return "DAY";
	}
	
	static public PeriodType string2PeriodType(String period)
	{
		if (period.equals("DAY")) return PeriodType.DAY;
		if (period.equals("WEEK")) return PeriodType.WEEK;
		if (period.equals("MONTH")) return PeriodType.MONTH;
		return PeriodType.DAY;
	}
	
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

