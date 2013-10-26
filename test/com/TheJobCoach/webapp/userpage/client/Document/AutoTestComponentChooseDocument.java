package com.TheJobCoach.webapp.userpage.client.Document;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.GwtTestUtilsWrapper;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentRevision;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.Browser;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestComponentChooseDocument extends GwtTest {


	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	private ComponentChooseDocument ccd;
	

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

	UserDocumentId ud1_docid = new UserDocumentId(ud1.ID, rev1.ID, ud1.name, ud1.fileName, rev1.date, rev1.date);
	UserDocumentId ud2_docid = new UserDocumentId(ud2.ID, rev2.ID, ud2.name, ud2.fileName, rev2.date, rev2.date);
	UserDocumentId ud3_docid = new UserDocumentId(ud3.ID, rev3.ID, ud3.name, ud3.fileName, rev3.date, rev3.date);
	UserDocumentId ud4_docid = new UserDocumentId(ud4.ID, rev4.ID, ud4.name, ud4.fileName, rev4.date, rev4.date);
	
	final Vector<UserDocument> docList = new Vector<UserDocument>(Arrays.asList(ud1, ud2, ud3, ud4));
	final Vector<UserDocumentId> docIdList = new Vector<UserDocumentId>(Arrays.asList(ud1_docid, ud2_docid, ud3_docid, ud4_docid));

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsSet, callsDelete;
		
		@Override
		public void getUserDocumentIdList(UserId userId,
				AsyncCallback<Vector<UserDocumentId>> callback) {
			Vector<UserDocumentId> result = new Vector<UserDocumentId>();
			for (UserDocumentId doc: docIdList) result.add(doc);
			callsGet++;
			callback.onSuccess(result);
		}
	}

	static SpecialUserServiceAsync userService = null;
	
	HorizontalPanel p;
	
	@Before
	public void beforeContentExternalContact()
	{
		if (userService == null) userService = new SpecialUserServiceAsync();
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
	
	public class ChooseResult implements IChooseResult<UserDocumentId>
	{
		public int count = 0;
		public UserDocumentId lastValue = null;
		@Override
		public void setResult(UserDocumentId result)
		{
			lastValue = result;
			count++;
		}
	}
	
	@Test
	public void testGetAll() throws InterruptedException
	{
		userService.callsGet = 0;
		
		ChooseResult result = new ChooseResult();
		
		ccd = new ComponentChooseDocument(
				p, userId, result);
		
		ccd.onModuleLoad();
		getBrowserSimulator().fireLoopEnd();
		GwtTestUtilsWrapper.waitCallProcessor(this, getBrowserSimulator());	
		assertEquals(1, userService.callsGet);
		assertEquals(4, ccd.cellTable.getRowCount());
		
		// Check document list
		assertEquals(ud1_id, ccd.cellTable.getVisibleItem(0).ID);
		assertEquals(ud2_id, ccd.cellTable.getVisibleItem(1).ID);
		assertEquals(ud3_id, ccd.cellTable.getVisibleItem(2).ID);
		assertEquals(ud4_id, ccd.cellTable.getVisibleItem(3).ID);
		
		// Check columns values
		assertEquals(3,                        ccd.cellTable.getColumnCount());
		assertEquals(ud2_docid.name,           ccd.cellTable.getColumn(0).getValue(ud2_docid));
		assertEquals(ud2_docid.fileName,       ccd.cellTable.getColumn(1).getValue(ud2_docid));
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(ud2_docid.lastUpdate),     ccd.cellTable.getColumn(2).getValue(ud2_docid));

		// ok is disabled
		assertEquals(false, ccd.okCancel.getOk().isEnabled());
		
		// Select 2nd element
		Browser.click(ccd.cellTable, ccd.cellTable.getVisibleItem(1));
		assertEquals(true, ccd.okCancel.getOk().isEnabled());

		// Click on OK.
		ccd.okCancel.getOk().click();
		getBrowserSimulator().fireLoopEnd();
		assertEquals(1, result.count);
		assertEquals(ud2_docid.ID, result.lastValue.ID);		
	}
	
	@Test
	public void testCancel() throws InterruptedException
	{	// Click on cancel
		ChooseResult result2 = new ChooseResult();
		ccd = new ComponentChooseDocument(
				p, userId, result2);
		ccd.onModuleLoad();
		Browser.click(ccd.cellTable, ccd.cellTable.getVisibleItem(1));
		assertEquals(true, ccd.okCancel.getOk().isEnabled());
		ccd.okCancel.getCancel().click();
		getBrowserSimulator().fireLoopEnd();
		assertEquals(0, result2.count);
	}
}
