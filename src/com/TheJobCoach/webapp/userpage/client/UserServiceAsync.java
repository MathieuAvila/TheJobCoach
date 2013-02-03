package com.TheJobCoach.webapp.userpage.client;

import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface UserServiceAsync {
	
	void getUserSiteList(UserId id, AsyncCallback<List<String>> callback) throws CassandraException;
	void getUserSite(UserId id, String siteId, AsyncCallback<UserJobSite> callback) throws CassandraException;
	void deleteUserSite(UserId id, String siteId, AsyncCallback<Integer> callback) throws CassandraException;
	void setUserSite(UserId id, UserJobSite data, AsyncCallback<Integer> callback) throws CassandraException;
	
	public void getUserDocumentList(UserId id, AsyncCallback<Vector<UserDocument>> callback) throws CassandraException;
	public void getUserDocumentIdList(UserId userId, AsyncCallback<Vector<UserDocumentId>> callback);
	public void deleteUserDocument(UserId id, String documentId, AsyncCallback<String> callback) throws CassandraException;
	public void setUserDocument(UserId id, UserDocument document, AsyncCallback<String> callback) throws CassandraException;

	public void getNews(UserId id, AsyncCallback<Vector<NewsInformation>> callback) throws CassandraException;
	
	public void getUserOpportunityShortList(UserId id, String list, AsyncCallback<Vector<UserOpportunity>> callback) throws CassandraException;
	public void getUserOpportunity(UserId id, String oppId, AsyncCallback<UserOpportunity> callback) throws CassandraException;
	public void setUserOpportunity(UserId id, String list, UserOpportunity opp, AsyncCallback<String> callback) throws CassandraException;
	public void deleteUserOpportunity(UserId id, String oppId, AsyncCallback<String> callback) throws CassandraException;
		
	public void getUserLogEntryList(UserId id, String oppId, AsyncCallback<Vector<UserLogEntry>> callback) throws CassandraException;
	public void getUserLogEntry(UserId id, String logId, AsyncCallback<UserLogEntry> callback) throws CassandraException;
	public void setUserLogEntry(UserId id, UserLogEntry opp, AsyncCallback<String> callback) throws CassandraException;
	public void deleteUserLogEntry(UserId id, String logId, AsyncCallback<String> callback) throws CassandraException;
	
	public void sendComment(UserId user, String value, AsyncCallback<String> callback);
	
	public void getTodoEventList(UserId id, String lang, AsyncCallback<Vector<TodoEvent>> callback);
	public void setTodoEvent(UserId id, TodoEvent todo, AsyncCallback<Boolean> callback);
	public void deleteTodoEvent(UserId id, TodoEvent todo, AsyncCallback<Boolean> callback);
}
