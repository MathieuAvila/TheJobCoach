package com.TheJobCoach.webapp.userpage.server;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.admindata.News;
import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.userdata.AccountInterface;
import com.TheJobCoach.userdata.ContactManager;
import com.TheJobCoach.userdata.TodoList;
import com.TheJobCoach.userdata.UserDocumentManager;
import com.TheJobCoach.userdata.UserExternalContactManager;
import com.TheJobCoach.userdata.UserJobSiteManager;
import com.TheJobCoach.userdata.UserLogManager;
import com.TheJobCoach.userdata.UserOpportunityManager;
import com.TheJobCoach.userdata.UserValues;
import com.TheJobCoach.userdata.fetch.JobBoard;
import com.TheJobCoach.userdata.report.GoalReport;
import com.TheJobCoach.webapp.adminpage.shared.UserSearchResult;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.GoalReportInformation;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.server.ServletSecurityCheck;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements UserService
{
	static private AccountInterface account = new AccountManager();
	static private UserJobSiteManager jobSiteManager = new UserJobSiteManager();
	static private News news = new News();
	static private UserOpportunityManager userOpportunityManager = new UserOpportunityManager();
	static private UserLogManager userLogManager = new UserLogManager();
	static private UserDocumentManager userDocumentManager = UserDocumentManager.getInstance();
	static private UserExternalContactManager userExternalContactManager = new UserExternalContactManager();
	static private TodoList todoList = new TodoList();
	static private GoalReport goalReport = new GoalReport();
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	UserValues userValues = new UserValues();
	
	@Override
	public Vector<UserJobSite> getUserSiteList(UserId id) throws CassandraException, CoachSecurityException 
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		List<String> idList = jobSiteManager.getUserSiteList(id);
		Vector<UserJobSite> result = new Vector<UserJobSite>(); 
		for (String siteId: idList)
		{
			UserJobSite site = jobSiteManager.getUserSite(id, siteId);
			if (site != null)
				result.add(jobSiteManager.getUserSite(id, siteId));
		}
		return result;
	}

	@Override
	public Integer deleteUserSite(UserId id, String siteId)	throws CassandraException , CoachSecurityException 
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		jobSiteManager.deleteUserSite(id, siteId);
		return 0;
	}

	@Override
	public Integer setUserSite(UserId id, UserJobSite site)	throws CassandraException, CoachSecurityException 
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		jobSiteManager.setUserSite(id, site);
		return 0;
	}

	@Override
	public UserJobSite getUserSite(UserId id, String siteId) throws CassandraException, CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return jobSiteManager.getUserSite(id, siteId);
	}

	@Override
	public Vector<UserDocument> getUserDocumentList(UserId id)
			throws CassandraException, CoachSecurityException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		logger.info("Get document list");
		return userDocumentManager.getUserDocumentList(id);
	}

	@Override
	public String deleteUserDocument(UserId id, String documentId)
			throws CassandraException, CoachSecurityException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		userDocumentManager.deleteUserDocument(id, documentId);
		return documentId;
	}

	@Override
	public String setUserDocument(UserId id, UserDocument document)	throws CassandraException, CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		logger.info("Set document: " + document.ID + " " + document.name);
		userDocumentManager.setUserDocument(id, document);
		return document.ID;
	}

	@Override
	public Vector<NewsInformation> getNews(UserId id)  throws CassandraException, CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		Vector<NewsInformation> result = news.getLatestNews();
		return result;
	}

	@Override
	public Vector<UserOpportunity> getUserOpportunityList(UserId id,
			String list) throws CassandraException, CoachSecurityException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return userOpportunityManager.getOpportunitiesList(id, list);
	}

	@Override
	public UserOpportunity getUserOpportunity(UserId id, String oppId)
			throws CassandraException, CoachSecurityException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return userOpportunityManager.getOpportunityLong(id, oppId);
	}

	@Override
	public String setUserOpportunity(UserId id, String list, UserOpportunity opp) throws CassandraException , CoachSecurityException, SystemException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		userOpportunityManager.setUserOpportunity(id, opp, list);
		return opp.ID;
	}

	@Override
	public String deleteUserOpportunity(UserId id, String oppId) throws CassandraException, CoachSecurityException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		userOpportunityManager.deleteUserOpportunity(id, oppId);
		return oppId;
	}

	@Override
	public UserOpportunity fetchUserOpportunity(UserId id, String ref, String site) throws CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		long time = userValues.getForcedWaitTimeMs(id, 5000);
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)	{	}
		return JobBoard.getOpportunity(site, ref);
	}

	@Override
	public Vector<UserLogEntry> getUserLogEntryList(UserId id, String oppId)
			throws CassandraException, CoachSecurityException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return userLogManager.getLogList(id, oppId);
	}

	@Override
	public UserLogEntry getUserLogEntry(UserId id, String logId)
			throws CassandraException, CoachSecurityException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return userLogManager.getLogEntryLong(id, logId);
	}

	@Override
	public String setUserLogEntry(UserId id, UserLogEntry log)
			throws CassandraException, CoachSecurityException, SystemException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		userLogManager.setUserLogEntry(id, log);
		return log.ID;
	}

	@Override
	public String deleteUserLogEntry(UserId id, String logId)
			throws CassandraException, CoachSecurityException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		userLogManager.deleteUserLogEntry(id, logId);
		return logId;
	}

	@Override
	public String sendComment(UserId id, String value) throws CassandraException , CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		account.sendComment(id, value);
		return null;
	}

	@Override
	public Vector<UserDocumentId> getUserDocumentIdList(UserId id)
			throws CassandraException, CoachSecurityException {
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return userDocumentManager.getUserDocumentIdList(id);
	}

	@Override
	public Vector<TodoEvent> getTodoEventList(UserId id, String lang)
			throws CassandraException, CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return todoList.getTodoEventList(id, lang);
	}

	@Override
	public Boolean setTodoEvent(UserId id, TodoEvent todo)
			throws CassandraException, CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		todoList.setTodoEvent(id, todo);
		return new Boolean(true);
	}

	@Override
	public Boolean deleteTodoEvent(UserId id, String todoId, boolean done)
			throws CassandraException, CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		todoList.deleteTodoEvent(id, todoId, done);
		return new Boolean(true);
	}

	@Override
	public Vector<ExternalContact> getExternalContactList(UserId id)
			throws CassandraException, CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return userExternalContactManager.getExternalContactList(id);
	}

	@Override
	public String setExternalContact(UserId id, ExternalContact contact)
			throws CassandraException, CoachSecurityException, SystemException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		userExternalContactManager.setExternalContact(id, contact);
		return "";
	}

	@Override
	public String deleteExternalContact(UserId id, String contact)
			throws CassandraException, CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		userExternalContactManager.deleteExternalContact(id, contact);
		return "";
	}
	
	public GoalReportInformation getUserGoalReport(UserId id, Date start, Date end) throws CassandraException, CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return goalReport.getReport(id, start, end);
	}

	@Override
	public String setPassword(UserId id, String newPassword)
			throws CassandraException, CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		account.setPassword(id, newPassword);
		return "";
	}
	
	@Override
	public UserSearchResult searchUsers(UserId id, String firstName, String lastName, int sizeRange, int startRange) throws CassandraException, SystemException, CoachSecurityException
	{
		ServletSecurityCheck.check(this.getThreadLocalRequest(), id);
		return account.searchUsers(id, firstName, lastName, sizeRange, startRange);
	}

	UserId getUserId() throws SystemException
	{
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		
		UserId result = (UserId)session.getAttribute("userid");
		if (result == null) throw new SystemException();
		return result;
	}
	
	ContactManager getContactManager() throws SystemException
	{
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		
		ContactManager result = (ContactManager)session.getAttribute("contactmanager");
		if (result != null) return result;
		result = new ContactManager(getUserId());
		session.setAttribute("contactmanager", result);
		return result;
		
	}
	
	@Override
	public ContactStatus updateContactRequest(UserId userContact) throws SystemException, CassandraException
	{
		ContactManager contact = getContactManager();
		return contact.updateContactRequest(userContact);
	}

	@Override
	public Vector<ContactInformation> getContactList() throws SystemException, CassandraException
	{
		ContactManager contact = getContactManager();
		return contact.getContactList();
	}

	@Override
	public Boolean sendJobMail(UserId userContact, String message) throws SystemException, CassandraException
	{
		ContactManager contact = getContactManager();
		return contact.sendJobMail(userContact, message);
	}

	
	
}
