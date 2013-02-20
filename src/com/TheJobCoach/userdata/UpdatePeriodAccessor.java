package com.TheJobCoach.userdata;

import java.util.Map;

import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;

public class UpdatePeriodAccessor
{
	public static String periodTypeToString(UpdatePeriod.PeriodType period)
	{
		System.out.println("TOTO" + period);
		switch(period)
		{
		case DAY: return "d";
		case WEEK: return "w";
		case MONTH: return "m";
		}
		return "";
	}

	public static UpdatePeriod.PeriodType periodTypeFromString(String period)
	{
		if (period.equals("d")) return UpdatePeriod.PeriodType.DAY;
		if (period.equals("w")) return UpdatePeriod.PeriodType.WEEK;
		if (period.equals("m")) return UpdatePeriod.PeriodType.MONTH;
		return UpdatePeriod.PeriodType.DAY;
	}

	public static UpdatePeriod fromCassandra(Map<String, String> resultReq)
	{
		return new UpdatePeriod(
				Convertor.toDate(resultReq.get("period_date")), 
				Convertor.toInt(resultReq.get("period_length")), 
				periodTypeFromString(resultReq.get("period_type"))
				);
	}
	
	public static ShortMap toCassandra(ShortMap map, UpdatePeriod period)
	{
		return map
				.add("period_date", period.last)
				.add("period_length", period.length)
				.add("period_type", periodTypeToString(period.periodType));
	}
}
