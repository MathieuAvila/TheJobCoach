package com.TheJobCoach.admindata;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;

public class TestNews
{

	static News news = new News();

	static Date start = new Date();
	static Date end = new Date();

	@SuppressWarnings("deprecation")
	Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}

	@Test
	public void testPurge()
	{
		start = getDate(1960, 1, 1);
		end = getDate(1960, 10, 1);

		Vector<NewsInformation> result = null;
		try {
			result = news.getNews(start, end);
		} catch (CassandraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (NewsInformation localNews: result)
		{
			try {
				news.deleteNews(localNews);
			} catch (CassandraException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testCreateNews()
	{	
		try {
			news.createNews(new NewsInformation("ID1", getDate(1960, 1, 10), "MyTitle1", "MyText1"));
			news.createNews(new NewsInformation("ID2", getDate(1960, 1, 12), "MyTitle2", "MyText2"));
			news.createNews(new NewsInformation("ID3", getDate(1960, 2, 5), "MyTitle3", "MyText3"));
			news.createNews(new NewsInformation("ID4", getDate(1960, 3, 5), "MyTitle4", "MyText4"));
		} catch (CassandraException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void checkDate(Date d1, Date d2)
	{
		assertEquals(d1.getYear(), d2.getYear());
		assertEquals(d1.getMonth(), d2.getMonth());
		assertEquals(d1.getDay(), d2.getDay());
	}
	
	@Test
	public void testGetNews()
	{	
		Vector<NewsInformation> result = null;
		try 
		{
			result = news.getNews(getDate(1960, 1, 11), getDate(1960, 2, 6));
		} 
		catch (CassandraException e) 
		{
			e.printStackTrace();
		}			
		assertEquals(2, result.size());
		NewsInformation n0 = result.get(0);
		NewsInformation n1 = result.get(1);
		
		assertEquals("ID2", n0.ID);
		assertEquals("MyTitle2", n0.title);
		assertEquals("MyText2", n0.text);
		checkDate(getDate(1960, 1, 12), n0.created);

		assertEquals("ID3", n1.ID);
		assertEquals("MyTitle3", n1.title);
		assertEquals("MyText3", n1.text);		
		checkDate(getDate(1960, 2, 5), n1.created);
	}
		
	@Test
	public void testGetNewsYears()
	{	
		Vector<NewsInformation> result = null;
		try 
		{
			result = news.getNews(getDate(1958, 10, 11), getDate(1962, 1, 6));
		} 
		catch (CassandraException e) 
		{
			e.printStackTrace();
		}			
		assertEquals(4, result.size());
	}

	@Test
	public void testDeleteNews()
	{	
		try {
			news.deleteNews(new NewsInformation("ID3", getDate(1960, 2, 5), "", ""));
		} catch (CassandraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Vector<NewsInformation> result = null;
		try 
		{
			result = news.getNews(getDate(1960, 1, 9), getDate(1960, 3, 6));
		}
		catch (CassandraException e) 
		{
			e.printStackTrace();
		}
		for (NewsInformation ni: result)
		{
			System.out.println(ni.ID + "  " + ni.created);			
		}
		assertEquals(3, result.size());
	}
	
}
