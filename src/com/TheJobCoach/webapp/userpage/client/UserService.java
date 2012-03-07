package com.TheJobCoach.webapp.userpage.client;

import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService 
{	
	public List<String> getUserSiteList(UserId id) throws CassandraException;
	public Integer deleteUserSite(UserId id, String siteId) throws CassandraException;
	public Integer setUserSite(UserId id, UserJobSite site) throws CassandraException;
	public UserJobSite getUserSite(UserId id, String siteId) throws CassandraException;
	public String addUserSite(UserId id) throws CassandraException;

	public List<String> getUserDocumentList(UserId id) throws CassandraException;
	public Integer deleteUserDocument(UserId id, String documentId) throws CassandraException;
	public Integer setUserDocument(UserId id, UserDocument document) throws CassandraException;
	public UserDocument getUserDocument(UserId id, String documentId) throws CassandraException;
	public String addUserDocument(UserId id) throws CassandraException;

	public Vector<NewsInformation> getNews(UserId id)  throws CassandraException;	
}
