package com.TheJobCoach.webapp.userpage.server;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.TheJobCoach.userdata.Account;
import com.TheJobCoach.userdata.AccountInterface;
import com.TheJobCoach.userdata.UserDocumentManager;
import com.TheJobCoach.userdata.UserJobSiteManager;
import com.TheJobCoach.userdata.UserLogManager;
import com.TheJobCoach.userdata.UserOpportunityManager;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements UserService
{
	static private AccountInterface account = new Account();
	static private UserJobSiteManager jobSiteManager = new UserJobSiteManager();
	static private com.TheJobCoach.admindata.News news = new com.TheJobCoach.admindata.News();
	static private UserOpportunityManager userOpportunityManager = new UserOpportunityManager();
	static private UserLogManager userLogManager = new UserLogManager();
	static private UserDocumentManager userDocumentManager = new UserDocumentManager();

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
	public Vector<UserDocument> getUserDocumentList(UserId id)
			throws CassandraException {
		return userDocumentManager.getUserDocumentList(id);
	}

	@Override
	public String deleteUserDocument(UserId id, String documentId)
			throws CassandraException {
		userDocumentManager.deleteUserDocument(id, documentId);
		return documentId;
	}

	@Override
	public String setUserDocument(UserId id, UserDocument document)
			throws CassandraException {
		userDocumentManager.setUserDocument(id, document);
		return document.ID;
	}

	@Override
	public Vector<NewsInformation> getNews(UserId id)  throws CassandraException
	{
		return news.getLatestNews();
	}

	@Override
	public Vector<UserOpportunity> getUserOpportunityShortList(UserId id,
			String list) throws CassandraException {
		return userOpportunityManager.getOpportunitiesShortList(id, list);
	}

	@Override
	public UserOpportunity getUserOpportunity(UserId id, String oppId)
			throws CassandraException {
		return userOpportunityManager.getOpportunityLong(id, oppId);
	}

	@Override
	public String setUserOpportunity(UserId id, String list, UserOpportunity opp) throws CassandraException {
		userOpportunityManager.setUserOpportunity(id, opp, list);
		return opp.ID;
	}

	@Override
	public String deleteUserOpportunity(UserId id, String oppId) throws CassandraException {
		userOpportunityManager.deleteUserOpportunity(id, oppId);
		return oppId;
	}

	@Override
	public Vector<UserLogEntry> getUserLogEntryList(UserId id, String oppId)
			throws CassandraException {
		return userLogManager.getLogList(id, oppId);
	}

	@Override
	public UserLogEntry getUserLogEntry(UserId id, String logId)
			throws CassandraException {
		return userLogManager.getLogEntryLong(id, logId);
	}

	@Override
	public String setUserLogEntry(UserId id, UserLogEntry log)
			throws CassandraException {
		userLogManager.setUserLogEntry(id, log);
		return log.ID;
	}

	@Override
	public String deleteUserLogEntry(UserId id, String logId)
			throws CassandraException {
		userLogManager.deleteUserLogEntry(id, logId);
		return logId;
	}

	@Override
	public String sendComment(UserId user, String value) throws CassandraException 
	{
		System.out.println("send comment '" + value + "' to trash");
		account.sendComment(user, value);
		return null;
	}
	
	@Override
	public Map<String,String> getValues(UserId user, String rootValue) throws CassandraException 
	{
		return null;
	}
	
	@Override
	public String setValue(UserId user, String id, String value) throws CassandraException 
	{
		return null;
	}
}
