package com.TheJobCoach.admindata;


import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;


public class News {

	static ColumnFamilyDefinition cfDef = null;

	static ColumnFamilyDefinition cfDefToken = null;

	final static String COLUMN_FAMILY_NAME_NEWS_DATE = "newslist";
	final static String COLUMN_FAMILY_NAME_NEWS_DATA = "newsdata";

	public News()
	{
		if (cfDef == null)
		{
			cfDef = HFactory.createColumnFamilyDefinition(
					CassandraAccessor.KEYSPACENAME,                              
					COLUMN_FAMILY_NAME_NEWS_DATE, 
					ComparatorType.ASCIITYPE);
			try{
				CassandraAccessor.getCluster().addColumnFamily(cfDef);
			}
			catch(Exception e) {} // Assume it already exists.
		}
		if (cfDefToken == null)
		{
			cfDefToken = HFactory.createColumnFamilyDefinition(
					CassandraAccessor.KEYSPACENAME,                              
					COLUMN_FAMILY_NAME_NEWS_DATA, 
					ComparatorType.ASCIITYPE);
			try{
				CassandraAccessor.getCluster().addColumnFamily(cfDefToken);
			}
			catch(Exception e) {} // Assume it already exists.
		}
	}

	public String createNews(NewsInformation info) throws CassandraException
	{
		System.out.println(info);
		System.out.println("date "+ info.created);
		
		@SuppressWarnings("deprecation")
		String colDate = info.created.getYear() + "-" + info.created.getMonth();
		System.out.println("COLDATE CREATE: " + colDate);
		boolean result = CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_NEWS_DATE, colDate, 
				(new ShortMap())
				.add(info.ID, info.ID)
				.get());
		if (!result) throw new CassandraException();
		result = CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_NEWS_DATA, info.ID, 
				(new ShortMap())
				.add("title", info.title)
				.add("text", info.text)
				.add("date", info.created)
				.get());
		if (!result) throw new CassandraException();
		return info.ID;
	}

	@SuppressWarnings("deprecation")
	public String deleteNews(NewsInformation info) throws CassandraException
	{
		String colDate = info.created.getYear() + "-" + info.created.getMonth();
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_NEWS_DATE, colDate, info.ID);
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_NEWS_DATA, info.ID);				
		return info.ID;		
	}

	@SuppressWarnings("deprecation")
	public Vector<NewsInformation> getNews(Date start, Date end) throws CassandraException
	{
		int startYear = start.getYear();
		int startMonth = start.getMonth();
		int endYear = end.getYear();
		int endMonth = end.getMonth();
		Vector<NewsInformation> returnResult = new Vector<NewsInformation>();
		
		int nextYear = startYear;
		int nextMonth = startMonth;
		do
		{
			startMonth = nextMonth; 
			startYear = nextYear;			
			System.out.println("YEAR " + startYear + " MONTH " + startMonth + " EYEAR " + endYear + " EMONTH " + endMonth);
			String colDate = startYear + "-" + startMonth;
			System.out.println("COLDATE: " + colDate);
			
			Map<String, String> result = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_NEWS_DATE, colDate);
			if (result != null)
			{				
				Set<String> keySet = result.keySet();
				for (String key: keySet)
				{				
					Map<String, String> resultInfo = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_NEWS_DATA, key);
					if (resultInfo == null) continue;
					Date d = Convertor.toDate(resultInfo.get("date"));
					System.out.println("FOUND ... " + d + " compare : " + start + " _____ " + end);
					if (d.after(start) && d.before(end))
					{
						System.out.println("Insert !");
						returnResult.add(new NewsInformation(
								key,
								Convertor.toDate(resultInfo.get("date")),
								resultInfo.get("title"),
								resultInfo.get("text")
								));
					}
				}
			}
			nextMonth++;
			if (nextMonth == 12)
			{
				nextMonth = 0;
				nextYear++;
			}			
			System.out.println("CONDITION " + ((startYear != endYear) || (startMonth != endMonth)));
		}
		while ((startYear != endYear) || (startMonth != endMonth));
		return returnResult;		
	}
	
	public Vector<NewsInformation> getLatestNews() throws CassandraException
	{
		Date first = new Date();
		Calendar date = Calendar.getInstance();		
		date.add(Calendar.MONTH, -3);
		first = date.getTime();
		
		date = Calendar.getInstance();		
		date.add(Calendar.MONTH, 12);
		date.add(Calendar.DAY_OF_MONTH, 30);
		Date end = date.getTime();
		
		Vector<NewsInformation> result = getNews(first, end);
		while (result.size() > 10)
			result.remove(10);
		return result;
	}
}
