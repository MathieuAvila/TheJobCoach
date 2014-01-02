package com.TheJobCoach.webapp.userpage.client.Opportunity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.GwtTestUtilsWrapper;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestAutoFeed extends GwtTest {
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int calls;
		
		@Override
		public void setUserLogEntry(UserId id, UserLogEntry opp,
				AsyncCallback<String> callback){
			assertEquals(userId.userName, id.userName);
			callback.onSuccess("");
			calls++;
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
		AutoFeedResultTest result = new AutoFeedResultTest();
		AutoFeed cud = new AutoFeed(p, userId, result);
		cud.onModuleLoad();	
		assertFalse(cud.okCancel.getOk().isEnabled());
		cud.comboBoxSite.setSelectedIndex(1);
		cud.onChange(null);
		assertFalse(cud.okCancel.getOk().isEnabled());
		
		cud.txtbxId.setValue("123456");
		cud.onChange(null);
		assertTrue(cud.okCancel.getOk().isEnabled());
		
		cud.okCancel.getOk().click();
		assertEquals(1, userService.calls);
		assertEquals(1, result.calls);
	}
	
	@Test
	public void testCancel() throws InterruptedException
	{
		userService.calls = 0;
		HorizontalPanel p = new HorizontalPanel();
		AutoFeedResultTest result2 = new AutoFeedResultTest();
		AutoFeed cud = new AutoFeed(p, userId, result2);
		cud.onModuleLoad();
		GwtTestUtilsWrapper.waitCallProcessor(this, getBrowserSimulator());	
		cud.okCancel.getCancel().click();
		GwtTestUtilsWrapper.waitCallProcessor(this, getBrowserSimulator());	
		assertEquals(0, userService.calls);
		assertEquals(0, result2.calls);
	}

	
}
