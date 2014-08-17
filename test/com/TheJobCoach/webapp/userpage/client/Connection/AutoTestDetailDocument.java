package com.TheJobCoach.webapp.userpage.client.Connection;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentRevision;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestDetailDocument extends GwtTest {

	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);


	static String ud1_id = "doc1";
	static String ud2_id = "doc2";
	static String ud3_id = "doc3";
	static String ud4_id = "doc4";
	
	static UserDocumentRevision rev1 = new UserDocumentRevision(CoachTestUtils.getDate(2000, 12, 1), ud1_id, "file1");
	static UserDocument ud1 = new UserDocument(
			ud1_id, "ndoc1", "description1", CoachTestUtils.getDate(2000, 12, 1), "file1", 
			UserDocument.DocumentStatus.NEW, UserDocument.DocumentType.RESUME, new Vector<UserDocumentRevision>(Arrays.asList(rev1)));
	
	static UserDocumentRevision rev2 = new UserDocumentRevision(CoachTestUtils.getDate(2000, 12, 2), ud2_id, "file2");
	static UserDocument ud2 = new UserDocument(
			ud2_id, "ndoc2", "description2", CoachTestUtils.getDate(2000, 12, 2), "file2", 
			UserDocument.DocumentStatus.OUTDATED, UserDocument.DocumentType.MOTIVATION, new Vector<UserDocumentRevision>(Arrays.asList(rev2)));
	
	static UserDocumentRevision rev3 = new UserDocumentRevision(CoachTestUtils.getDate(2000, 12, 3), ud3_id, "file3");
	static UserDocument ud3 = new UserDocument(
			ud3_id, "ndoc1", "description1", CoachTestUtils.getDate(2000, 12, 3), "file3", 
			UserDocument.DocumentStatus.SECONDARY, UserDocument.DocumentType.OTHER, new Vector<UserDocumentRevision>(Arrays.asList(rev3)));
	
	static UserDocumentRevision rev4 = new UserDocumentRevision(CoachTestUtils.getDate(2000, 12, 1), ud1_id, "file4");
	static UserDocument ud4 = new UserDocument(
			ud4_id, "ndoc4", "description4", CoachTestUtils.getDate(2000, 12, 1), "file4", 
			UserDocument.DocumentStatus.NEW, UserDocument.DocumentType.RESUME, new Vector<UserDocumentRevision>(Arrays.asList(rev4)));
	
	Vector<UserDocument> docIdList = new Vector<UserDocument>(Arrays.asList(ud1, ud2, ud3, ud4));

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet;

		String lastId;
		
		public void reset()
		{
			callsGet = 0;
		}
		
		@Override
		public void getUserDocumentList(UserId id,
				AsyncCallback<Vector<UserDocument>> callback)
				 {
			callsGet++;
			callback.onSuccess(docIdList);
		}
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();

	@Before
	public void beforeDetailOpportunity()
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
	}
	
	static final int COLUMN_NAME         = 0;
	static final int COLUMN_FILENAME     = 1;
	static final int COLUMN_TYPE         = 2;
	static final int COLUMN_STATUS       = 3;
	static final int COLUMN_MAX          = 4;

	@Test
	public void testGetAll() throws InterruptedException
	{
		DetailDocument cuo;		
		userService.reset();

		ContactInformation ci = new ContactInformation(ContactStatus.CONTACT_OK, userId.userName, "name1", "firstName1", 
				new Visibility(), 
				new Visibility(true, true, true, true));
		
		cuo = new DetailDocument(userId, ci);
		assertEquals(0, userService.callsGet);
		
		cuo.showPanelDetail();
		assertEquals(1, userService.callsGet);
	
		assertEquals(docIdList.size(), cuo.cellTable.getRowCount());
		
		assertEquals(ud1.ID, cuo.cellTable.getVisibleItem(0).ID);
		assertEquals(ud2.ID, cuo.cellTable.getVisibleItem(1).ID);
		assertEquals(ud3.ID, cuo.cellTable.getVisibleItem(2).ID);
		
		// Check columns values
		
		assertEquals(COLUMN_MAX,      cuo.cellTable.getColumnCount());
		assertEquals(ud1.name,        cuo.cellTable.getColumn(COLUMN_NAME).getValue(ud1));
		assertEquals(ud1.fileName,    cuo.cellTable.getColumn(COLUMN_FILENAME).getValue(ud1));
		assertEquals("CV",            cuo.cellTable.getColumn(COLUMN_TYPE).getValue(ud1));
		assertEquals("Nouveau",      cuo.cellTable.getColumn(COLUMN_STATUS).getValue(ud1));
	}

}
