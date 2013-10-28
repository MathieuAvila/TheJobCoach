package com.TheJobCoach.webapp.adminpage.client;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.adminpage.shared.UserReport;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>AdminService</code>.
 */
public interface AdminServiceAsync {
	
	void getUserReportList(UserId id, AsyncCallback<List<UserReport>> callback);
	
	public void getNewsInformationList(UserId id, Date start, Date end, AsyncCallback<Vector<NewsInformation>> callback);
	public void createNewsInformation(UserId id, NewsInformation news, AsyncCallback<String> callback);
	public void deleteNewsInformation(UserId id, NewsInformation newsId, AsyncCallback<String> callback);

	public void deleteUser(UserId user, String userName, AsyncCallback<String> callback);
	
}
