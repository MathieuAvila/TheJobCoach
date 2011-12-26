package com.TheJobCoach.webapp.userpage.client;

import java.util.List;

import com.TheJobCoach.webapp.userpage.client.UserService.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
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
}
