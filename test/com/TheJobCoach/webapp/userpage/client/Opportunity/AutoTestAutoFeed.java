package com.TheJobCoach.webapp.userpage.client.Opportunity;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.ErrorCatcherMessageBox;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.Browser;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestAutoFeed extends GwtTest {
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	
	static String opp1 = "opp1";
	static UserOpportunity opportunity1 = new UserOpportunity(opp1, CoachTestUtils.getDate(2000, 1, 1), CoachTestUtils.getDate(2000, 2, 1),
			"title1", "description1", "companyId1",
			"contractType1",  "1000.10",  
			CoachTestUtils.getDate(2000, 1, 1), CoachTestUtils.getDate(2000, 1, 1),
			false, "source1", "url1", "location1",
			UserOpportunity.ApplicationStatus.DISCOVERED, "");
	
	final Vector<TestEditDialog> creationStack =  new Vector<TestEditDialog>();
	
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

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int calls;
		
		UserOpportunity opp = null;
		
		@Override
		public void fetchUserOpportunity(UserId id, String ref, String site,
				AsyncCallback<UserOpportunity> callback)
		{
			assertEquals(userId.userName, id.userName);
			calls++;
			callback.onSuccess(opp);
		}
	}
	
	static SpecialUserServiceAsync userService = null;
	
	class AutoFeedResultTest implements IChooseResult<UserOpportunity>
	{
		UserOpportunity result;
		
		public int calls = 0;
		
		@Override
		public void setResult(UserOpportunity result)
		{
			this.result = result;
			calls++;
		}
	}

	@Before
	public void beforeAutoFeed()
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
	}
	
	@Test
	public void testValid() throws InterruptedException
	{
		HorizontalPanel p = new HorizontalPanel();
		userService.calls = 0;
		ErrorCatcherMessageBox mbCatcher = new ErrorCatcherMessageBox();
		AutoFeedResultTest result = new AutoFeedResultTest();
		TestEditDialog editDialog = new TestEditDialog(p, userId, null, result);
		AutoFeed cud = new AutoFeed(p, userId, result, editDialog);
		cud.onModuleLoad();	
		assertFalse(cud.okCancel.getOk().isEnabled());
		cud.comboBoxSite.setSelectedIndex(1);
		assertFalse(cud.okCancel.getOk().isEnabled());
		
		Browser.fillText(cud.txtbxId, "123456");
		assertTrue(cud.okCancel.getOk().isEnabled());
		
		cud.okCancel.getOk().click();
		// service called
		assertEquals(1, userService.calls);
		assertEquals(0, result.calls);
		// null is returned: messagebox error and back to window
		assertTrue(mbCatcher.hasError());
		assertEquals(MessageBox.TYPE.ERROR, mbCatcher.type);
		assertEquals("Problème à l'import: veuillez vérifier que l'offre est encore accessible", mbCatcher.message);
		
		// click OK, and back to window
		userService.calls = 0;
		mbCatcher.clearError();
		assertTrue(cud.okCancel.getOk().isEnabled());
		
		// now click and return not-null
		userService.opp = opportunity1;
		cud.okCancel.getOk().click();
		// service called
		assertEquals(1, userService.calls);
		assertEquals(0, result.calls);
		assertEquals(1, creationStack.size());
		TestEditDialog createdEditor = creationStack.get(0);
		assertNotNull(createdEditor.edition);
		assertEquals(opportunity1, createdEditor.edition);
		assertEquals(1, createdEditor.loaded);
		assertEquals(false, cud.dBox.isShowing());
	}
	
	@Test
	public void testCancel() throws InterruptedException
	{
		userService.calls = 0;
		HorizontalPanel p = new HorizontalPanel();
		AutoFeedResultTest result = new AutoFeedResultTest();
		TestEditDialog editDialog = new TestEditDialog(p, userId, null, result);
		AutoFeed cud = new AutoFeed(p, userId, result, editDialog);
		cud.onModuleLoad();
		cud.okCancel.getCancel().click();
		assertEquals(0, userService.calls);
		assertEquals(0, result.calls);
	}

	
}
