package com.TheJobCoach.webapp.adminpage.server;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.adminpage.client.AdminService;
import com.TheJobCoach.webapp.adminpage.shared.UserReport;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.util.server.ServletSecurityCheck;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class AdminServiceImpl extends RemoteServiceServlet implements AdminService 
{
	static com.TheJobCoach.userdata.AccountManager account = new com.TheJobCoach.userdata.AccountManager();
	static com.TheJobCoach.admindata.News news = new com.TheJobCoach.admindata.News();
	
	@Override
	public List<UserReport> getUserReportList(UserId id)
			throws CassandraException, CoachSecurityException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return account.getUserReportList();
	}

	@Override
	public Vector<NewsInformation> getNewsInformationList(UserId id, Date start, Date end) throws CassandraException, CoachSecurityException 
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return news.getNews(start, end);		
	}

	@Override
	public String createNewsInformation(UserId id, NewsInformation newsVal)	throws CassandraException, CoachSecurityException 
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return news.createNews(newsVal);
	}

	@Override
	public String deleteNewsInformation(UserId id, NewsInformation newsId) throws CassandraException, CoachSecurityException 
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return news.deleteNews(newsId);
	}

	@Override
	public String deleteUser(UserId id, String userName)
			throws CassandraException, CoachSecurityException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		account.deleteAccount(userName);
		return "";
	}
}
