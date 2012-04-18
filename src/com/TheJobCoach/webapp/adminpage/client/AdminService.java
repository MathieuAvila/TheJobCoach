package com.TheJobCoach.webapp.adminpage.client;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.TheJobCoach.webapp.adminpage.shared.UserReport;
import com.TheJobCoach.webapp.mainpage.shared.UserId;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("admin")
public interface AdminService extends RemoteService {
	
	public List<UserReport> getUserReportList(UserId id) throws CassandraException;

	public Vector<NewsInformation> getNewsInformationList(UserId id, Date start, Date end) throws CassandraException;
	public String createNewsInformation(UserId id, NewsInformation news) throws CassandraException;
	public String deleteNewsInformation(UserId id, NewsInformation newsId) throws CassandraException;
	
	public String deleteUser(UserId user, String userName) throws CassandraException;
	
}
