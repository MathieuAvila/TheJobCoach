package com.TheJobCoach.webapp.userpage.client;

import java.io.Serializable;
import java.util.List;

import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {
	
	class UserId implements Serializable {
		private static final long serialVersionUID = 5023720505251872867L;
		public String userName;
		public String token;
		public UserId(String _userName, String _token)
		{
			userName = _userName;
			token = _token;
		}
		public UserId() {}
	};
	
	public List<String> getUserSiteList(UserId id) throws CassandraException;
	public Integer deleteUserSite(UserId id, String siteId) throws CassandraException;
	public Integer setUserSite(UserId id, UserJobSite site) throws CassandraException;
	public UserJobSite getUserSite(UserId id, String siteId) throws CassandraException;
	public String addUserSite(UserId id) throws CassandraException;
}
