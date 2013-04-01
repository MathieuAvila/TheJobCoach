package com.TheJobCoach.admindata;


import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;

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
		@SuppressWarnings("deprecation")
		String colDate = info.created.getYear() + "-" + info.created.getMonth();
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
			String colDate = startYear + "-" + startMonth;			
			Map<String, String> result = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_NEWS_DATE, colDate);
			if (result != null)
			{				
				Set<String> keySet = result.keySet();
				for (String key: keySet)
				{				
					Map<String, String> resultInfo = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_NEWS_DATA, key);
					if (resultInfo == null) continue;
					Date d = Convertor.toDate(resultInfo.get("date"));
					if (d.after(start) && d.before(end))
					{
						returnResult.insertElementAt(new NewsInformation(
								key,
								Convertor.toDate(resultInfo.get("date")),
								resultInfo.get("title"),
								resultInfo.get("text")
								), 0);
					}
				}
			}
			nextMonth++;
			if (nextMonth == 12)
			{
				nextMonth = 0;
				nextYear++;
			}						
		}
		while ((startYear != endYear) || (startMonth != endMonth));
		return returnResult;		
	}
	
	public Vector<NewsInformation> getLatestNews() throws CassandraException
	{
		Calendar first = Calendar.getInstance();		
		Vector<NewsInformation> result = new Vector<NewsInformation>();
		Set<String> foundList = new HashSet<String>();
		int loop = 0;
		while ((result.size() < 10)&&(loop < 24))
		{
			Calendar end = (Calendar) first.clone();
			end.add(Calendar.MONTH, +1);
			Vector<NewsInformation> resultLocal = getNews(first.getTime(), end.getTime());
			for (NewsInformation ni: resultLocal)
			{
				if (!foundList.contains(ni.ID))
				{
					foundList.add(ni.ID);
					result.add(ni);
				}
			}
			first.add(Calendar.MONTH, -1);
			loop++;
		}
		return result;
	}
}
