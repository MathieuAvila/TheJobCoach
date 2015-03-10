package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.adminpage.shared.UserSearchResult;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
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

public class DefaultUserServiceAsync implements UserServiceAsync
{	
	static public int calls = 0;

	Logger logger = LoggerFactory.getLogger(DefaultUserServiceAsync.class);
   		
	@Override
	public void getUserSiteList(UserId id,
			AsyncCallback<Vector<UserJobSite>> callback) {
		logger.info("getUserSiteList " + id.userName);
	}
	@Override
	public void getUserSite(UserId id, String siteId,
			AsyncCallback<UserJobSite> callback) {
		logger.info("getUserSite " + id.userName);
	}
	@Override
	public void deleteUserSite(UserId id, String siteId,
			AsyncCallback<Integer> callback) {
		logger.info("deleteUserSite " + id.userName);
	}
	@Override
	public void setUserSite(UserId id, UserJobSite data,
			AsyncCallback<Integer> callback) {
		logger.info("setUserSite " + id.userName);
	}
	@Override
	public void getUserDocumentList(UserId id,
			AsyncCallback<Vector<UserDocument>> callback)
			 {
		logger.info("getUserDocumentList " + id.userName);
	}
	@Override
	public void getUserDocumentIdList(UserId id,
			AsyncCallback<Vector<UserDocumentId>> callback) {
		logger.info("getUserDocumentIdList " + id.userName);
	}
	@Override
	public void deleteUserDocument(UserId id, String documentId,
			AsyncCallback<String> callback) {
		logger.info("deleteUserDocument " + id.userName);
	}
	@Override
	public void setUserDocument(UserId id, UserDocument document,
			AsyncCallback<String> callback) {
		logger.info("setUserDocument " + id.userName);
	}
	@Override
	public void getNews(UserId id,
			AsyncCallback<Vector<NewsInformation>> callback)
			 {
		logger.info("getNews " + id.userName);
	}
	@Override
	public void getUserOpportunityList(UserId id, String list,
			AsyncCallback<Vector<UserOpportunity>> callback)
			 {
		logger.info("getUserOpportunityList " + id.userName);
	}
	@Override
	public void getUserOpportunity(UserId id, String oppId,
			AsyncCallback<UserOpportunity> callback)
			 {
		logger.info("getUserOpportunity " + id.userName);
	}
	@Override
	public void setUserOpportunity(UserId id, String list,
			UserOpportunity opp, AsyncCallback<String> callback)
			 {
		logger.info("setUserOpportunity " + id.userName);
	}
	@Override
	public void deleteUserOpportunity(UserId id, String oppId,
			AsyncCallback<String> callback) {
		logger.info("deleteUserOpportunity " + id.userName);
	}
	@Override
	public void getUserLogEntryList(UserId id, String oppId,
			AsyncCallback<Vector<UserLogEntry>> callback)
			 {
		logger.info("getUserLogEntryList " + id.userName);
	}
	@Override
	public void getUserLogEntry(UserId id, String logId,
			AsyncCallback<UserLogEntry> callback) {
		logger.info("getUserLogEntry " + id.userName);
	}
	@Override
	public void setUserLogEntry(UserId id, UserLogEntry opp,
			AsyncCallback<String> callback) {
		logger.info("getUserSiteList " + id.userName);
	}
	@Override
	public void deleteUserLogEntry(UserId id, String logId,
			AsyncCallback<String> callback) {
		logger.info("deleteUserLogEntry " + id.userName);
	}
	@Override
	public void sendComment(UserId id, String value,
			AsyncCallback<String> callback) {
		logger.info("sendComment " + id.userName);
	}
	@Override
	public void getTodoEventList(UserId id, String lang,
			AsyncCallback<Vector<TodoEvent>> callback)
	{
		logger.info("getTodoEventList " + id.userName);
	}
	@Override
	public void setTodoEvent(UserId id, TodoEvent todo,
			AsyncCallback<Boolean> callback)
	{
		logger.info("setTodoEvent " + id.userName);
	}
	@Override
	public void deleteTodoEvent(UserId id, String todo, boolean done,
			AsyncCallback<Boolean> callback)
	{
		logger.info("deleteTodoEvent " + id.userName);
	}
	@Override
	public void getExternalContactList(UserId id,
			AsyncCallback<Vector<ExternalContact>> callback)
			
	{
		logger.info("getExternalContactList " + id.userName);
	}
	@Override
	public void setExternalContact(UserId id, ExternalContact contact,
			AsyncCallback<String> callback)
	{
		logger.info("setExternalContact " + id.userName);
	}
	@Override
	public void deleteExternalContact(UserId id, String contact,
			AsyncCallback<String> callback)
	{
		logger.info("deleteExternalContact " + id.userName);
	}
	@Override
	public void getUserGoalReport(UserId id, Date start, Date end,
			AsyncCallback<GoalReportInformation> callback)
	{
		logger.info("getUserGoalReport " + id.userName);
	}
	@Override
	public void setPassword(UserId id, String newPassword,
			AsyncCallback<String> callback)
	{
		logger.info("setPassword " + id.userName);
	}
	@Override
	public void fetchUserOpportunity(UserId id, String ref, String site,
			AsyncCallback<UserOpportunity> callback)
	{
		logger.info("fetchUserOpportunity " + id.userName + " ref:" + ref + " site:" + site);
	}
	@Override
	public void searchUsers(UserId id, String firstName, String lastName,
			int sizeRange, int startRange,
			AsyncCallback<UserSearchResult> callback)
	{
		logger.info("searchUsers " + id.userName + " firstName " + firstName + " lastName " + lastName + " sizeRange " + sizeRange + " startRange " + startRange);
	}
	@Override
	public void updateContactRequest(UserId userContact, boolean ok,
			AsyncCallback<ContactStatus> callback)
	{
		logger.info("updateContactRequest " + userContact.userName + " OK:" + ok);
	}
	@Override
	public void getContactList(
			AsyncCallback<Vector<ContactInformation>> callback)
	{
		logger.info("getContactList");
	}
	@Override
	public void sendJobMail(UserId userContact, String message,
			AsyncCallback<Boolean> callback)
	{
		logger.info("sendJobMail " + userContact.userName + " message:" + message);
	}
	@Override
	public void updateShares(String userName, Visibility contact,
			AsyncCallback<Void> callback)
	{
		logger.info("updateShares " + userName);
	}
	@Override
	public void toggleDeleteAccount(boolean delete,
			AsyncCallback<Boolean> callback) throws CassandraException,
			CoachSecurityException, SystemException
	{
		logger.info("toggleDeleteAccount " + delete);
	}

	@Override
	public void addChatMsg(String dst, String msg, AsyncCallback<Void> callback)
			throws CassandraException, SystemException, CoachSecurityException
	{
		logger.info("addChatMsg " + dst);
	}

	@Override
	public void isTypingTo(String dst, AsyncCallback<Void> callback)
			throws CassandraException, SystemException, CoachSecurityException
	{
		logger.info("isTypingTo " + dst);
	}
	@Override
	public void getLastMsgFromUser(String fromUser, int maxCount, Date d,
			AsyncCallback<Vector<ChatInfo>> callback)
			throws CassandraException, SystemException, CoachSecurityException
	{
		logger.info("getLastMsgFromUser " + fromUser);
	}
};