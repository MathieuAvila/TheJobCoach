package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;

import com.TheJobCoach.webapp.util.shared.FormatUtil;

public class UpdatePeriod implements Serializable  {

	private static final long serialVersionUID = 1116255124512443730L;

	
	public enum PeriodType { DAY, WEEK, MONTH };
	public boolean needRecall;
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
		periodType = PeriodType.MONTH;
		length = 1;
		last = new Date();
		needRecall = true;
	}

	public UpdatePeriod(Date last, int length, PeriodType periodType, boolean needRecall)
	{
		super();
		init(last, length, periodType, needRecall);
	}

	public UpdatePeriod(UpdatePeriod update) 
	{
		init(update.last, update.length, update.periodType, update.needRecall);
	}

	void init(Date last, int length, PeriodType periodType, boolean needRecall) 
	{
		this.last = (Date) last.clone();
		this.length = length;
		this.periodType = periodType;
		this.needRecall = needRecall;
	}

	
	@SuppressWarnings("deprecation")
	public Date getNextCall()
	{
		Date result = (Date) this.last.clone();
		switch(periodType)
		{
		case DAY:  result.setDate(last.getDate() + length); break;
		case WEEK: result.setDate(last.getDate() + length * 7); break;
		case MONTH:result.setMonth(last.getMonth() + length); break;
		}
		return result;
	}

	public boolean equals(Object o)
	{
		UpdatePeriod up2 = (UpdatePeriod)o;
		return  periodType.equals(up2.periodType) && 
				FormatUtil.getDateString(last).equals(FormatUtil.getDateString(up2.last)) && 
				(length == up2.length) && 
				(needRecall == up2.needRecall)
				;
	}
}

