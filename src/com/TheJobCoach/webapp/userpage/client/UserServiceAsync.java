package com.TheJobCoach.webapp.userpage.client;

import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface UserServiceAsync {
	
	void getUserSiteList(UserId id, AsyncCallback<List<String>> callback) throws CassandraException;
	void getUserSite(UserId id, String siteId, AsyncCallback<UserJobSite> callback) throws CassandraException;
	void deleteUserSite(UserId id, String siteId, AsyncCallback<Integer> callback) throws CassandraException;
	void setUserSite(UserId id, UserJobSite data, AsyncCallback<Integer> callback) throws CassandraException;
	void addUserSite(UserId id, AsyncCallback<String> callback) throws CassandraException;

	public void getUserDocumentList(UserId id, AsyncCallback<List<String>> callback) throws CassandraException;
	public void deleteUserDocument(UserId id, String documentId, AsyncCallback<Integer> callback) throws CassandraException;
	public void setUserDocument(UserId id, UserDocument document, AsyncCallback<Integer> callback) throws CassandraException;
	public void getUserDocument(UserId id, String documentId, AsyncCallback<UserDocument> callback) throws CassandraException;
	public void addUserDocument(UserId id, AsyncCallback<String> callback) throws CassandraException;
	
	public void getNews(UserId id, AsyncCallback<Vector<NewsInformation>> callback) throws CassandraException;	
}
