package com.TheJobCoach.webapp.userpage.client;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentRevision;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtTest;

public class AutoTestContentUserDocument extends GwtTest {


	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	private ContentUserDocument cud;
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	static String ud1_id = "doc1";
	static String ud2_id = "doc2";
	static String ud3_id = "doc3";
	static String ud4_id = "doc4";
	
	static UserDocumentRevision rev1 = new UserDocumentRevision(getDate(2000, 12, 1), ud1_id, "file1");
	static UserDocument ud1 = new UserDocument(
			ud1_id, "ndoc1", "description1", getDate(2000, 12, 1), "file1", 
			UserDocument.DocumentStatus.NEW, UserDocument.DocumentType.RESUME, new Vector<UserDocumentRevision>(Arrays.asList(rev1)));
	
	static UserDocumentRevision rev2 = new UserDocumentRevision(getDate(2000, 12, 2), ud2_id, "file2");
	static UserDocument ud2 = new UserDocument(
			ud2_id, "ndoc2", "description2", getDate(2000, 12, 2), "file2", 
			UserDocument.DocumentStatus.OUTDATED, UserDocument.DocumentType.MOTIVATION, new Vector<UserDocumentRevision>(Arrays.asList(rev2)));
	
	static UserDocumentRevision rev3 = new UserDocumentRevision(getDate(2000, 12, 3), ud3_id, "file3");
	static UserDocument ud3 = new UserDocument(
			ud3_id, "ndoc1", "description1", getDate(2000, 12, 3), "file3", 
			UserDocument.DocumentStatus.SECONDARY, UserDocument.DocumentType.OTHER, new Vector<UserDocumentRevision>(Arrays.asList(rev3)));
	
	static UserDocumentRevision rev4 = new UserDocumentRevision(getDate(2000, 12, 1), ud1_id, "file4");
	static UserDocument ud4 = new UserDocument(
			ud4_id, "ndoc4", "description4", getDate(2000, 12, 1), "file4", 
			UserDocument.DocumentStatus.NEW, UserDocument.DocumentType.RESUME, new Vector<UserDocumentRevision>(Arrays.asList(rev4)));
	
	Vector<UserDocument> docIdList = new Vector<UserDocument>(Arrays.asList(ud1, ud2, ud3, ud4));

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int calls;
			
		@Override
		public void getUserDocumentList(UserId id,
				AsyncCallback<Vector<UserDocument>> callback)
				throws CassandraException {
			System.out.println("getUserDocumentList");
			callback.onSuccess(docIdList);
			calls++;			
		}

	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();
	
	@Override
	public String getModuleName() {		
		return "com.TheJobCoach.webapp.userpage.UserPage";
	}
	
	HorizontalPanel p;
	
	@Before
	public void beforeContentUserDocument()
	{
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					return userService;
				}
				return null;
			}}
		);
		p = new HorizontalPanel();		
	}
	
	@Test
	public void testGetAll() throws InterruptedException
	{
		userService.calls = 0;
		cud = new ContentUserDocument(
				p, userId);
		cud.onModuleLoad();
		assertEquals(1, userService.calls);
		assertEquals(4, cud.cellTable.getRowCount());
	}	
}
