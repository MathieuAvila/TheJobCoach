package com.TheJobCoach.webapp.userpage.client;

import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.octo.gwt.test.GwtCreateHandler;
import com.octo.gwt.test.GwtTest;

public class TestContentUserDocument extends GwtTest {

	
	public class Toto implements UserServiceAsync
	{	
		@Override
		public void getUserSiteList(UserId id,
				AsyncCallback<List<String>> callback) throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void getUserSite(UserId id, String siteId,
				AsyncCallback<UserJobSite> callback) throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void deleteUserSite(UserId id, String siteId,
				AsyncCallback<Integer> callback) throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setUserSite(UserId id, UserJobSite data,
				AsyncCallback<Integer> callback) throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void getUserDocumentList(UserId id,
				AsyncCallback<Vector<UserDocument>> callback)
				throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void getUserDocumentIdList(UserId userId,
				AsyncCallback<Vector<UserDocumentId>> callback) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void deleteUserDocument(UserId id, String documentId,
				AsyncCallback<String> callback) throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setUserDocument(UserId id, UserDocument document,
				AsyncCallback<String> callback) throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void getNews(UserId id,
				AsyncCallback<Vector<NewsInformation>> callback)
				throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void getUserOpportunityShortList(UserId id, String list,
				AsyncCallback<Vector<UserOpportunity>> callback)
				throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void getUserOpportunity(UserId id, String oppId,
				AsyncCallback<UserOpportunity> callback)
				throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setUserOpportunity(UserId id, String list,
				UserOpportunity opp, AsyncCallback<String> callback)
				throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void deleteUserOpportunity(UserId id, String oppId,
				AsyncCallback<String> callback) throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void getUserLogEntryList(UserId id, String oppId,
				AsyncCallback<Vector<UserLogEntry>> callback)
				throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void getUserLogEntry(UserId id, String logId,
				AsyncCallback<UserLogEntry> callback) throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setUserLogEntry(UserId id, UserLogEntry opp,
				AsyncCallback<String> callback) throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void deleteUserLogEntry(UserId id, String logId,
				AsyncCallback<String> callback) throws CassandraException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void sendComment(UserId user, String value,
				AsyncCallback<String> callback) {
			// TODO Auto-generated method stub
			
		}
	};

	
	private ContentUserDocument cud;

	@Override
	public String getModuleName() {		
		return "com.TheJobCoach.webapp.userpage.UserPage";
	}

	@Test
	public void testToto()
	{

	}

	@Before
	public void beforeContentUserDocument()
	{
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("TOTO " + arg0);
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					System.out.println("TAT\n");
					return new Toto();
				}
				return null;
			}}
		);
		HorizontalPanel p = new HorizontalPanel();
		cud = new ContentUserDocument(
				p, 
				new UserId()
				);
		cud.onModuleLoad();		

		// Some pre-assertions

	}
	
}
