package com.TheJobCoach.webapp.adminpage.server;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.adminpage.client.AdminService;
import com.TheJobCoach.webapp.adminpage.shared.UserReport;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class AdminServiceImpl extends RemoteServiceServlet implements AdminService 
{
	static com.TheJobCoach.userdata.Account account = new com.TheJobCoach.userdata.Account();
	static com.TheJobCoach.admindata.News news = new com.TheJobCoach.admindata.News();

	@Override
	public List<UserReport> getUserReportList(UserId id)
			throws CassandraException {
		return account.getUserReportList();
	}

	@Override
	public Vector<NewsInformation> getNewsInformationList(UserId id, Date start, Date end) throws CassandraException 
	{
		return news.getNews(start, end);		
	}

	@Override
	public String createNewsInformation(UserId id, NewsInformation newsVal)	throws CassandraException 
	{
		return news.createNews(newsVal);
	}

	@Override
	public String deleteNewsInformation(UserId id, NewsInformation newsId) throws CassandraException 
	{
		return news.deleteNews(newsId);
	}

	@Override
	public String deleteUser(UserId user, String userName)
			throws CassandraException {
		account.deleteAccount(userName);
		return "";
	}
}
