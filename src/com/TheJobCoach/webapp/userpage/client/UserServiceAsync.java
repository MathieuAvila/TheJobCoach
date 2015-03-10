package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.adminpage.shared.UserSearchResult;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.GoalReportInformation;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.ChatInfo;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface UserServiceAsync {
	
	void getUserSiteList(UserId id, AsyncCallback<Vector<UserJobSite>> callback);
	void getUserSite(UserId id, String siteId, AsyncCallback<UserJobSite> callback);
	void deleteUserSite(UserId id, String siteId, AsyncCallback<Integer> callback);
	void setUserSite(UserId id, UserJobSite data, AsyncCallback<Integer> callback);
	
	public void getUserDocumentList(UserId id, AsyncCallback<Vector<UserDocument>> callback);
	public void getUserDocumentIdList(UserId userId, AsyncCallback<Vector<UserDocumentId>> callback);
	public void deleteUserDocument(UserId id, String documentId, AsyncCallback<String> callback);
	public void setUserDocument(UserId id, UserDocument document, AsyncCallback<String> callback);

	public void getNews(UserId id, AsyncCallback<Vector<NewsInformation>> callback);
	
	public void getUserOpportunityList(UserId id, String list, AsyncCallback<Vector<UserOpportunity>> callback);
	public void getUserOpportunity(UserId id, String oppId, AsyncCallback<UserOpportunity> callback);
	public void setUserOpportunity(UserId id, String list, UserOpportunity opp, AsyncCallback<String> callback);
	public void deleteUserOpportunity(UserId id, String oppId, AsyncCallback<String> callback);
	public void fetchUserOpportunity(UserId user, String ref, String site, AsyncCallback<UserOpportunity> callback);
	
	public void getExternalContactList(UserId id, AsyncCallback<Vector<ExternalContact>> callback);
	public void setExternalContact(UserId id, ExternalContact contact, AsyncCallback<String> callback);
	public void deleteExternalContact(UserId id, String contact, AsyncCallback<String> callback);
		
	public void getUserLogEntryList(UserId id, String oppId, AsyncCallback<Vector<UserLogEntry>> callback);
	public void getUserLogEntry(UserId id, String logId, AsyncCallback<UserLogEntry> callback);
	public void setUserLogEntry(UserId id, UserLogEntry opp, AsyncCallback<String> callback);
	public void deleteUserLogEntry(UserId id, String logId, AsyncCallback<String> callback);
	
	public void sendComment(UserId user, String value, AsyncCallback<String> callback);
	
	public void getTodoEventList(UserId id, String lang, AsyncCallback<Vector<TodoEvent>> callback);
	public void setTodoEvent(UserId id, TodoEvent todo, AsyncCallback<Boolean> callback);
	public void deleteTodoEvent(UserId id, String todoId, boolean done, AsyncCallback<Boolean> callback);
	
	public void getUserGoalReport(UserId id, Date start, Date end, AsyncCallback<GoalReportInformation> callback);
	
	public void setPassword(UserId id, String newPassword, AsyncCallback<String> callback);
	public void toggleDeleteAccount(boolean delete, AsyncCallback<Boolean> callback) throws CassandraException, CoachSecurityException, SystemException;

	public void searchUsers(UserId id, String firstName, String lastName, int sizeRange, int startRange, AsyncCallback<UserSearchResult> callback);	

	public void updateContactRequest(UserId userContact, boolean ok, AsyncCallback<ContactInformation.ContactStatus> callback);
	public void updateShares(String userName, ContactInformation.Visibility contact, AsyncCallback<Void> callback);
	public void getContactList(AsyncCallback<Vector<ContactInformation>> callback);
	public void sendJobMail(UserId userContact, String message, AsyncCallback<Boolean> callback);
	
	public void getLastMsgFromUser(String fromUser, int maxCount, Date d, AsyncCallback<Vector<ChatInfo>> callback) throws CassandraException, SystemException, CoachSecurityException;
	public void addChatMsg(String dst, String msg, AsyncCallback<Void> callback) throws CassandraException, SystemException, CoachSecurityException;
	public void isTypingTo(String dst, AsyncCallback<Void> callback) throws CassandraException, SystemException, CoachSecurityException;

}
