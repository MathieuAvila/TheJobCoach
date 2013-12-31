package com.TheJobCoach.webapp.userpage.client.Opportunity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.ErrorCatcherMessageBox;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
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
public class AutoTestContentUserOpportunity extends GwtTest {


	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	static String opp1 = "opp1";
	static String opp2 = "opp2";
	static String opp3 = "opp3";
	static UserOpportunity opportunity1 = new UserOpportunity(opp1, getDate(2000, 1, 1), getDate(2000, 2, 1),
			"title1", "description1", "companyId1",
			"contractType1",  "1000.10",  
			getDate(2000, 1, 1), getDate(2000, 1, 1),
			false, "source1", "url1", "location1",
			UserOpportunity.ApplicationStatus.APPLIED, "note1");
	
	static UserOpportunity opportunity2 = new UserOpportunity(opp2, getDate(2000, 1, 2), getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  "2",  
			getDate(2000, 1, 2), getDate(2000, 1, 2),
			false, "source2", "url2", "location2",
			UserOpportunity.ApplicationStatus.NEW, "note2");

	static UserOpportunity opportunity3 = new UserOpportunity(opp3, getDate(2000, 1, 2), getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  "2",  
			getDate(2000, 1, 2), getDate(2000, 1, 2),
			false, "source2", "url2", "location2",
			UserOpportunity.ApplicationStatus.CLOSED, "note3");
	
	Vector<UserOpportunity> contactList =  new Vector<UserOpportunity>(Arrays.asList(opportunity1, opportunity2, opportunity3));
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsSet, callsDelete, callsGetSingle;

		String lastId;
		
		public void reset()
		{
			callsGet = callsSet=  callsDelete = callsGetSingle = 0;
		}
		
		
		@Override
		public void getUserOpportunityList(UserId id, String list,
				AsyncCallback<Vector<UserOpportunity>> callback)
				{
			callsGet++;
			callback.onSuccess(contactList);
		}
		@Override
		public void getUserOpportunity(UserId id, String oppId,
				AsyncCallback<UserOpportunity> callback)
				{
			callsGetSingle++;
			callback.onSuccess(opportunity2);	
		}
		@Override
		public void setUserOpportunity(UserId id, String list,
				UserOpportunity opp, AsyncCallback<String> callback)
				{
			callsSet++;
			callback.onSuccess("");
		}
		
		@Override
		public void deleteUserOpportunity(UserId id, String oppId,
				AsyncCallback<String> callback)  {
			callsDelete++;
			lastId = oppId;
			callback.onSuccess("");
		}
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();
	
	HorizontalPanel p;
	
	@Before
	public void beforeContentUserOpportunity()
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
	static final int COLUMN_URL          = 2;
	static final int COLUMN_EDIT_LOGS    = 10;

	final Vector<TestEditDialog> creationStack =  new Vector<TestEditDialog>();
	final Vector<TestContentUserLog> userLogStack =  new Vector<TestContentUserLog>();

	class TestEditDialog implements IEditDialogModel<UserOpportunity>
	{
		public int loaded;
		public Panel rootPanel;
		public UserId userId;
		public UserOpportunity edition;
		public IChooseResult<UserOpportunity> result;
		
		public TestEditDialog(Panel rootPanel,	UserId userId, UserOpportunity edition,	IChooseResult<UserOpportunity> result2)
		{
			this.rootPanel = rootPanel;
			this.userId = userId;
			this.edition = edition;
			this.result = result2;
		}
		
		public TestEditDialog()
		{
		}

		@Override
		public IEditDialogModel<UserOpportunity> clone(Panel rootPanel,
				UserId userId, UserOpportunity edition,
				IChooseResult<UserOpportunity> result)
		{
			TestEditDialog fnResult = new TestEditDialog(rootPanel, userId, edition, result);
			creationStack.add(fnResult);
			return fnResult;
		}

		@Override
		public void onModuleLoad()
		{
			loaded++;
		}
	}

	class TestContentUserLog implements IContentUserLog
	{
		public int loaded;
		public Panel rootPanel;
		public UserId userId;
		public UserOpportunity edition;
		
		public TestContentUserLog(Panel rootPanel,	UserId userId, UserOpportunity edition)
		{
			this.rootPanel = rootPanel;
			this.userId = userId;
			this.edition = edition;
		}
		
		public TestContentUserLog()
		{
		}

		@Override
		public IContentUserLog clone(Panel panel, UserId _user, UserOpportunity opp)
		{
			IContentUserLog fnResult = new TestContentUserLog(rootPanel, userId, opp);
			userLogStack.add((TestContentUserLog) fnResult);
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
		ContentUserOpportunity cuo;
		
		ErrorCatcherMessageBox mbCatcher = new ErrorCatcherMessageBox();
		userService.callsGet = 0;
		cuo = new ContentUserOpportunity(
				p, userId, new TestEditDialog(), new TestContentUserLog());
		cuo.onModuleLoad();
		assertEquals(1, userService.callsGet);
		assertEquals(0, userService.callsGetSingle);
		
		assertEquals(3, cuo.cellTable.getRowCount());
		
		assertEquals(opp1, cuo.cellTable.getVisibleItem(0).ID);
		assertEquals(opp2, cuo.cellTable.getVisibleItem(1).ID);
		assertEquals(opp3, cuo.cellTable.getVisibleItem(2).ID);
		
		// Check columns values
		
		assertEquals(11, cuo.cellTable.getColumnCount());
		assertEquals(opportunity1.title,                                         cuo.cellTable.getColumn(3).getValue(opportunity1));
		assertEquals(opportunity1.companyId,                                     cuo.cellTable.getColumn(4).getValue(opportunity1));
		assertEquals("Candidaté",                                                cuo.cellTable.getColumn(5).getValue(opportunity1));
		assertEquals(opportunity1.location,                                      cuo.cellTable.getColumn(6).getValue(opportunity1));
		assertEquals(opportunity1.salary,										 cuo.cellTable.getColumn(7).getValue(opportunity1));
		assertEquals(opportunity1.contractType,                                  cuo.cellTable.getColumn(8).getValue(opportunity1));
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(opportunity1.firstSeen),
																				 cuo.cellTable.getColumn(9).getValue(opportunity1));
		
		// Click on 2nd element		
		Browser.click(cuo.cellTable, opportunity2);
		assertEquals(1, userService.callsGet);
		assertEquals(1, userService.callsGetSingle);
		
		assertEquals(opportunity2.source,       cuo.labelTextSource.getText());
		assertEquals(opportunity2.description,  cuo.panelDescriptionContent.getHTML());
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(opportunity2.lastUpdate),
				cuo.labelCreationDate.getText());
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(opportunity2.startDate),
				cuo.labelStartDate.getText());
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(opportunity2.endDate),
				cuo.labelEndDate.getText());
		
		// Click on delete element
		userService.reset();
		mbCatcher.clearError();
		Event event = EventBuilder.create(Event.ONCLICK).build();		
		cuo.cellTable.getColumn(COLUMN_DELETE).onBrowserEvent(new Cell.Context(1, COLUMN_DELETE, opportunity2), cuo.cellTable.getElement(), opportunity2, event);
		assertEquals(0, userService.callsDelete);
		assertNotNull(mbCatcher.currentBox);
		assertEquals(mbCatcher.type, MessageBox.TYPE.QUESTION);
		assertTrue(mbCatcher.message.contains("supprimer l'opportuni"));
		assertTrue(mbCatcher.title.contains("Supprimer l'opportunit"));
		//assertEquals(mbCatcher, MessageBox.TYPE.QUESTION);
		mbCatcher.currentBox.clickOk();
		
		assertEquals(1, userService.callsDelete);
		assertEquals(1, userService.callsGet);
		assertEquals(opportunity2.ID, userService.lastId);
				
		// Click on edit element
		userService.reset();
		mbCatcher.clearError();
		event = EventBuilder.create(Event.ONCLICK).build();		
		cuo.cellTable.getColumn(COLUMN_UPDATE).onBrowserEvent(new Cell.Context(1, COLUMN_UPDATE, opportunity2), cuo.cellTable.getElement(), opportunity2, event);
		assertEquals(1, creationStack.size());
		TestEditDialog editDialog = creationStack.get(0);
		assertEquals(1, editDialog.loaded);
		assertEquals(opportunity2.ID, editDialog.edition.ID);
		creationStack.clear();
		// If edition is validated, ... it triggers an update, otherwise nothing.
		editDialog.result.setResult(null); // try nothing
		assertEquals(0, userService.callsGet);
		editDialog.result.setResult(opportunity2); // try something now
		assertEquals(1, userService.callsGet);

		// Click on new
		creationStack.clear();
		userService.reset();
		mbCatcher.clearError();
		event = EventBuilder.create(Event.ONCLICK).build();		
		cuo.buttonNewOpportunity.click();
		assertEquals(1, creationStack.size());
		editDialog = creationStack.get(0);
		assertEquals(1, editDialog.loaded);
		assertNull(editDialog.edition);
		// If edition is validated, ... it triggers an update, otherwise nothing.
		editDialog.result.setResult(null); // try nothing
		assertEquals(0, userService.callsGet);
		editDialog.result.setResult(opportunity2); // try something now
		assertEquals(1, userService.callsGet);
		
		// Click on edit logs
		userService.reset();
		mbCatcher.clearError();
		event = EventBuilder.create(Event.ONCLICK).build();		
		cuo.cellTable.getColumn(COLUMN_EDIT_LOGS).onBrowserEvent(new Cell.Context(1, COLUMN_EDIT_LOGS, opportunity2), cuo.cellTable.getElement(), opportunity2, event);
		assertEquals(1, creationStack.size());
		TestContentUserLog userLog = userLogStack.get(0);
		assertEquals(1, userLog.loaded);
		assertNotNull(userLog.edition);
		assertEquals(opportunity2.ID, userLog.edition.ID);
		
		// TODO check what is loaded/freed
		
		// TODO check URL column
	}	
}
