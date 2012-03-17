package com.TheJobCoach.webapp.userpage.server;

import java.util.List;
import java.util.Vector;

import com.TheJobCoach.userdata.UserJobSiteManager;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements UserService
{
	static private UserJobSiteManager jobSiteManager = new UserJobSiteManager();
	static com.TheJobCoach.admindata.News news = new com.TheJobCoach.admindata.News();

	@Override
	public List<String> getUserSiteList(UserId id) throws CassandraException 
	{
		List<String> result = jobSiteManager.getUserSiteList(id);
		return result;
	}

	@Override
	public Integer deleteUserSite(UserId id, String siteId)	throws CassandraException 
	{
		jobSiteManager.deleteUserSite(id, siteId);
		return 0;
	}

	@Override
	public Integer setUserSite(UserId id, UserJobSite site)	throws CassandraException
	{
		System.out.println("SET: " + site.ID + "  " + site.name);
		jobSiteManager.setUserSite(id, site);
		return 0;
	}

	@Override
	public UserJobSite getUserSite(UserId id, String siteId) throws CassandraException
	{
		return jobSiteManager.getUserSite(id, siteId);
	}

	@Override
	public String addUserSite(UserId id) throws CassandraException {
		return jobSiteManager.addUserSite(id);
	}

	@Override
	public List<String> getUserDocumentList(UserId id)
			throws CassandraException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer deleteUserDocument(UserId id, String documentId)
			throws CassandraException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer setUserDocument(UserId id, UserDocument document)
			throws CassandraException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDocument getUserDocument(UserId id, String documentId)
			throws CassandraException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addUserDocument(UserId id) throws CassandraException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Vector<NewsInformation> getNews(UserId id)  throws CassandraException
	{
		return news.getLatestNews();
	}

}
