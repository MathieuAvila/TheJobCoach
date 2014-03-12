package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.ErrorCatcherMessageBox;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.EventBuilder;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestContentExternalContact extends GwtTest {


	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	private ContentExternalContact cud;
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	static String contact1 = "contact1";
	static String contact2 = "contact2";
	static String contact3 = "contact3";
		
	static ExternalContact ec1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	static ExternalContact ec2 = new ExternalContact(contact2, "firstName2", "lastName2", "email2", "phone2", "personalNote2", "organization2", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, false));
	static ExternalContact ec3 = new ExternalContact(contact3, "firstName3", "lastName3", "email3", "phone3", "personalNote3", "organization3", new UpdatePeriod(CoachTestUtils.getDate(2000, 1, 1), 2, PeriodType.DAY, true));
	
	Vector<ExternalContact> contactList =  new Vector<ExternalContact>(Arrays.asList(ec1, ec2, ec3));
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsSet, callsDelete;

		public String lastId;
		
		@Override
		public void getExternalContactList(UserId id,
				AsyncCallback<Vector<ExternalContact>> callback)
				
		{
			callsGet++;
			callback.onSuccess(contactList);
		}

		@Override
		public void setExternalContact(UserId id, ExternalContact contact,
				AsyncCallback<String> callback) 
		{
			callsSet++;
			callback.onSuccess("");
		}

		@Override
		public void deleteExternalContact(UserId id, String contact,
				AsyncCallback<String> callback) 
		{
			callsDelete++;
			lastId = contact;
			callback.onSuccess("");			
		}

		public void reset()
		{
			callsGet = callsSet = callsDelete = 0;
		}
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();
	
	HorizontalPanel p;
	
	@Before
	public void beforeContentExternalContact()
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

	static final int COLUMN_DELETE       = 0;
	static final int COLUMN_UPDATE       = 1;

	final Vector<TestEditContact> creationStack =  new Vector<TestEditContact>();
	
	class TestEditContact implements IEditDialogModel<ExternalContact>
	{
		public int loaded;
		public Panel rootPanel;
		public UserId userId;
		public ExternalContact edition;
		public IChooseResult<ExternalContact> result;
		
		public TestEditContact(Panel rootPanel,	UserId userId, ExternalContact edition,	IChooseResult<ExternalContact> result2)
		{
			this.rootPanel = rootPanel;
			this.userId = userId;
			this.edition = edition;
			this.result = result2;
		}
		
		public TestEditContact()
		{
		}

		@Override
		public IEditDialogModel<ExternalContact> clone(Panel rootPanel,
				UserId userId, ExternalContact edition,
				IChooseResult<ExternalContact> result)
		{
			TestEditContact fnResult = new TestEditContact(rootPanel, userId, edition, result);
			creationStack.add(fnResult);
			return fnResult;
		}

		@Override
		public void onModuleLoad()
		{
			loaded++;
		}
	}
	@Test
	public void testGetAll() throws InterruptedException
	{
		ErrorCatcherMessageBox mbCatcher = new ErrorCatcherMessageBox();
		
		userService.callsGet = 0;
		cud = new ContentExternalContact(
				p, userId, new TestEditContact());
		cud.onModuleLoad();
		assertEquals(1, userService.callsGet);
		assertEquals(3, cud.cellTable.getRowCount());
		
		assertEquals(contact1, cud.cellTable.getVisibleItem(0).ID);
		assertEquals(contact2, cud.cellTable.getVisibleItem(1).ID);
		assertEquals(contact3, cud.cellTable.getVisibleItem(2).ID);
		

		// Check columns values
		
		assertEquals(7, cud.cellTable.getColumnCount());
		assertEquals(ec1.firstName,                             cud.cellTable.getColumn(2).getValue(ec1));
		assertEquals(ec1.lastName,                              cud.cellTable.getColumn(3).getValue(ec1));
		assertEquals(ec1.organization,                          cud.cellTable.getColumn(4).getValue(ec1));
		assertEquals(ec1.phone,									cud.cellTable.getColumn(5).getValue(ec1));
		assertEquals(ec1.email,                                 cud.cellTable.getColumn(6).getValue(ec1));
		
		// Click on 2nd element		
		Browser.click(cud.cellTable, ec2);

		// Click on delete element
		userService.reset();
		mbCatcher.clearError();
		Event event = EventBuilder.create(Event.ONCLICK).build();		
		cud.cellTable.getColumn(COLUMN_DELETE).onBrowserEvent(new Cell.Context(1, COLUMN_DELETE, ec2), cud.cellTable.getElement(), ec2, event);
		assertEquals(0, userService.callsDelete);
		assertNotNull(mbCatcher.currentBox);
		assertEquals(mbCatcher.type, MessageBox.TYPE.QUESTION);
		assertTrue(mbCatcher.message.contains("supprimer le contact"));
		assertTrue(mbCatcher.title.contains("Supprimer le contact"));
		//assertEquals(mbCatcher, MessageBox.TYPE.QUESTION);
		mbCatcher.currentBox.clickOk();
		
		assertEquals(1, userService.callsDelete);
		assertEquals(1, userService.callsGet);
		assertEquals(ec2.ID, userService.lastId);
				
		// Click on edit element
		userService.reset();
		mbCatcher.clearError();
		event = EventBuilder.create(Event.ONCLICK).build();		
		cud.cellTable.getColumn(COLUMN_UPDATE).onBrowserEvent(new Cell.Context(1, COLUMN_UPDATE, ec2), cud.cellTable.getElement(), ec2, event);
		assertEquals(1, creationStack.size());
		TestEditContact editDialog = creationStack.get(0);
		assertEquals(1, editDialog.loaded);
		assertEquals(ec2.ID, editDialog.edition.ID);
		creationStack.clear();
		// If edition is validated, ... it triggers an update, otherwise nothing.
		editDialog.result.setResult(null); // try nothing
		assertEquals(0, userService.callsGet);
		editDialog.result.setResult(ec2); // try something now
		assertEquals(1, userService.callsGet);

		// Click on new
		creationStack.clear();
		userService.reset();
		mbCatcher.clearError();
		event = EventBuilder.create(Event.ONCLICK).build();		
		cud.buttonNewExternalContact.click();
		assertEquals(1, creationStack.size());
		editDialog = creationStack.get(0);
		assertEquals(1, editDialog.loaded);
		assertNull(editDialog.edition);
		// If edition is validated, ... it triggers an update, otherwise nothing.
		editDialog.result.setResult(null); // try nothing
		assertEquals(0, userService.callsGet);
		editDialog.result.setResult(ec2); // try something now
		assertEquals(1, userService.callsGet);
		
	}	
}
