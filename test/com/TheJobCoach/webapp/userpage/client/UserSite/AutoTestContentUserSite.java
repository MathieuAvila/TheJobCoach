package com.TheJobCoach.webapp.userpage.client.UserSite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.ErrorCatcherMessageBox;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
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
import com.googlecode.gwt.test.utils.events.EventBuilder;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestContentUserSite extends GwtTest {
	
	Logger logger = LoggerFactory.getLogger(DefaultUserServiceAsync.class);

	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	static String site1 = "site1";
	static String site2 = "site2";
	static String site3 = "site3";

	static UserJobSite ujs1 = new UserJobSite(site1, "site1", "URL1", "description1", "login1", "password1", 
			new UpdatePeriod(CoachTestUtils.getDate(2013, 12, 1), 10, PeriodType.DAY, true));
	static UserJobSite ujs2 = new UserJobSite(site2, "site2", "URL2", "description2", "login2", "password2", 
			new UpdatePeriod(CoachTestUtils.getDate(2013, 12, 3), 10, PeriodType.DAY, false));
	static UserJobSite ujs3 = new UserJobSite(site3, "site3", "URL3", "description3", "login3", "password3", 
			new UpdatePeriod(CoachTestUtils.getDate(2013, 12, 10), 10, PeriodType.DAY, true));
	Vector<UserJobSite> siteList =  new Vector<UserJobSite>(Arrays.asList(ujs1, ujs2, ujs3));
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsSet, callsDelete;

		String lastId;
		
		public void reset()
		{
			callsGet = callsSet =  callsDelete = 0;
			lastId = "";
		}
		
		@Override
		public void getUserSiteList(UserId id,
				AsyncCallback<Vector<UserJobSite>> callback) {
			logger.info("getUserSiteList " + id.userName);
			callsGet++;
			callback.onSuccess(siteList);
		}
		
		@Override
		public void deleteUserSite(UserId id, String siteId,
				AsyncCallback<Integer> callback) {
			logger.info("deleteUserSite " + id.userName+ " " + siteId);
			callsDelete++;
			lastId = siteId;
			callback.onSuccess(0);
		}
		
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();
	
	HorizontalPanel p;
	
	@Before
	public void beforeContentUserJobSite()
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
	
	final Vector<TestEditDialog> creationStack =  new Vector<TestEditDialog>();

	class TestEditDialog implements IEditDialogModel<UserJobSite>
	{
		public int loaded;
		public Panel rootPanel;
		public UserId userId;
		public UserJobSite edition;
		public IChooseResult<UserJobSite> result;
		
		public TestEditDialog(Panel rootPanel,	UserId userId, UserJobSite edition,	IChooseResult<UserJobSite> result2)
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
		public IEditDialogModel<UserJobSite> clone(Panel rootPanel,
				UserId userId, UserJobSite edition,
				IChooseResult<UserJobSite> result)
		{
			logger.info("cloned");
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

	@Test
	public void testGetAll() throws InterruptedException
	{
		ContentUserSite cuo;
		TestEditDialog testEdit = new TestEditDialog();
		
		ErrorCatcherMessageBox mbCatcher = new ErrorCatcherMessageBox();
		userService.reset();
		cuo = new ContentUserSite(p, userId, testEdit);
		cuo.onModuleLoad();
		assertEquals(1, userService.callsGet);

		assertEquals(3, cuo.cellTable.getRowCount());
		
		assertEquals(site1, cuo.cellTable.getVisibleItem(0).ID);
		assertEquals(site2, cuo.cellTable.getVisibleItem(1).ID);
		assertEquals(site3, cuo.cellTable.getVisibleItem(2).ID);
		
		// Check columns values
		
		assertEquals(7, cuo.cellTable.getColumnCount());
		
		assertEquals(ujs1.name,                                         cuo.cellTable.getColumn(3).getValue(ujs1));
		assertEquals(ujs1.login,                                        cuo.cellTable.getColumn(4).getValue(ujs1));
		assertEquals(ujs1.password,                                     cuo.cellTable.getColumn(5).getValue(ujs1));
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(ujs1.update.last),
					cuo.cellTable.getColumn(6).getValue(ujs1));

		
		// Click on delete element
		userService.reset();
		mbCatcher.clearError();
		Event event = EventBuilder.create(Event.ONCLICK).build();		
		cuo.cellTable.getColumn(COLUMN_DELETE).onBrowserEvent(new Cell.Context(1, COLUMN_DELETE, site2), cuo.cellTable.getElement(), ujs2, event);
		assertEquals(0, userService.callsDelete);
		assertNotNull(mbCatcher.currentBox);
		assertEquals(mbCatcher.type, MessageBox.TYPE.QUESTION);
		assertTrue(mbCatcher.message.contains("Etes-vous sûr de vouloir supprimer le site suivant: "));
		assertTrue(mbCatcher.title.contains("Confirmer la suppression du site"));
		mbCatcher.currentBox.clickOk();
		
		assertEquals(1, userService.callsDelete); // deletion
		assertEquals(1, userService.callsGet); // update after delete
		assertEquals(ujs2.ID, userService.lastId); // check deleted Id
				
		// Click on edit element
		userService.reset();
		mbCatcher.clearError();
		event = EventBuilder.create(Event.ONCLICK).build();		
		cuo.cellTable.getColumn(COLUMN_UPDATE).onBrowserEvent(new Cell.Context(1, COLUMN_UPDATE, site2), cuo.cellTable.getElement(), ujs2, event);
		assertEquals(1, creationStack.size());
		TestEditDialog editDialog = creationStack.get(0);
		assertEquals(1, editDialog.loaded);
		assertEquals(ujs2.ID, editDialog.edition.ID);
		creationStack.clear();
		// If edition is validated, ... it triggers an update, otherwise nothing.
		editDialog.result.setResult(null); // try nothing
		assertEquals(0, userService.callsGet);
		editDialog.result.setResult(ujs2); // try something now
		assertEquals(1, userService.callsGet);

		// Click on new
		creationStack.clear();
		userService.reset();
		mbCatcher.clearError();
		event = EventBuilder.create(Event.ONCLICK).build();		
		cuo.buttonNewSite.click();
		assertEquals(1, creationStack.size());
		editDialog = creationStack.get(0);
		assertEquals(1, editDialog.loaded);
		assertNull(editDialog.edition);
		// If edition is validated, ... it triggers an update, otherwise nothing.
		editDialog.result.setResult(null); // try nothing
		assertEquals(0, userService.callsGet);
		editDialog.result.setResult(ujs2); // try something now
		assertEquals(1, userService.callsGet);

		// TODO check URL column
	}	
}
