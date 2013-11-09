package com.TheJobCoach.webapp.adminpage.client;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.TheJobCoach.webapp.adminpage.shared.UserReport;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("admin")
public interface AdminService extends RemoteService {
	
	public List<UserReport> getUserReportList(UserId id) throws CassandraException, CoachSecurityException;

	public Vector<NewsInformation> getNewsInformationList(UserId id, Date start, Date end) throws CassandraException, CoachSecurityException;
	public String createNewsInformation(UserId id, NewsInformation news) throws CassandraException, CoachSecurityException;
	public String deleteNewsInformation(UserId id, NewsInformation newsId) throws CassandraException, CoachSecurityException;
	
	public String deleteUser(UserId user, String userName) throws CassandraException, CoachSecurityException;
	
}
