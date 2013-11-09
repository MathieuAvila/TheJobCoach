package com.TheJobCoach.webapp.userpage.client;

import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService 
{	
	public List<String> getUserSiteList(UserId id) throws CassandraException, CoachSecurityException;
	public Integer deleteUserSite(UserId id, String siteId) throws CassandraException, CoachSecurityException;
	public Integer setUserSite(UserId id, UserJobSite site) throws CassandraException, CoachSecurityException;
	public UserJobSite getUserSite(UserId id, String siteId) throws CassandraException, CoachSecurityException;
	
	public Vector<UserDocument> getUserDocumentList(UserId id) throws CassandraException, CoachSecurityException;
	public Vector<UserDocumentId> getUserDocumentIdList(UserId userId) throws CassandraException, CoachSecurityException;
	public String deleteUserDocument(UserId id, String documentId) throws CassandraException, CoachSecurityException;
	public String setUserDocument(UserId id, UserDocument document) throws CassandraException, CoachSecurityException;

	public Vector<NewsInformation> getNews(UserId id)  throws CassandraException, CoachSecurityException;
	
	public Vector<UserOpportunity> getUserOpportunityList(UserId id, String list) throws CassandraException, CoachSecurityException;
	public UserOpportunity getUserOpportunity(UserId id, String oppId) throws CassandraException, CoachSecurityException;
	public String setUserOpportunity(UserId id, String list, UserOpportunity opp) throws CassandraException, CoachSecurityException;
	public String deleteUserOpportunity(UserId id, String oppId) throws CassandraException, CoachSecurityException;
	
	public Vector<ExternalContact> getExternalContactList(UserId id) throws CassandraException, CoachSecurityException;
	public String setExternalContact(UserId id, ExternalContact contact) throws CassandraException, CoachSecurityException;
	public String deleteExternalContact(UserId id, String contact) throws CassandraException, CoachSecurityException;

	public Vector<UserLogEntry> getUserLogEntryList(UserId id, String oppId) throws CassandraException, CoachSecurityException;
	public UserLogEntry getUserLogEntry(UserId id, String logId) throws CassandraException, CoachSecurityException;
	public String setUserLogEntry(UserId id, UserLogEntry opp) throws CassandraException, CoachSecurityException;
	public String deleteUserLogEntry(UserId id, String logId) throws CassandraException, CoachSecurityException;
	
	public String sendComment(UserId user, String value) throws CassandraException, CoachSecurityException;
	
	public Vector<TodoEvent> getTodoEventList(UserId id, String lang) throws CassandraException, CoachSecurityException;
	public Boolean setTodoEvent(UserId id, TodoEvent todo) throws CassandraException, CoachSecurityException;
	public Boolean deleteTodoEvent(UserId id, TodoEvent todo) throws CassandraException, CoachSecurityException;
}
