package com.TheJobCoach.webapp.userpage.client.Account;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestChangePassword extends GwtTest {
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int calls;
		
		@Override
		public void setPassword(UserId id, String newPassword,
				AsyncCallback<String> callback)
		{
			assertEquals(userId.userName, id.userName);
			assertEquals("12345", newPassword);
			calls++;
			callback.onSuccess("");
		}
	}
	
	static SpecialUserServiceAsync userService;
	@Before
	public void beforeContentMyReports()
	{
		if ( userService == null) userService = new SpecialUserServiceAsync();
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
	
	HorizontalPanel p;
    
	@Test
	public void testValid() 
	{
		userService.calls = 0;
		EditPassword ep = new EditPassword(userId);
		assertEquals(0, userService.calls);
		assertTrue(ep.dBox.isShowing());
		
		assertFalse(ep.okCancel.getOk().isEnabled());
		
		// pwd different
		ep.textBoxPassword.setValue("12345");
		ep.textBoxRetypePassword.setValue("1234");
		assertFalse(ep.okCancel.getOk().isEnabled());
		
		// pwd same, too small
		ep.textBoxPassword.setValue("1234");
		ep.textBoxRetypePassword.setValue("1234");
		assertFalse(ep.okCancel.getOk().isEnabled());
		
		// pwd different, good size
		ep.textBoxPassword.setValue("123456");
		ep.textBoxRetypePassword.setValue("12345");
		assertFalse(ep.okCancel.getOk().isEnabled());
		
		// pwd same, good size => OK
		ep.textBoxPassword.setValue("12345");
		ep.textBoxRetypePassword.setValue("12345");
		assertTrue(ep.okCancel.getOk().isEnabled());
		
		// Click OK
		ep.okCancel.getOk().click();
		assertEquals(1, userService.calls);
		assertFalse(ep.dBox.isShowing());
		
		// Check if CANCEL
		userService.calls = 0;
		ep = new EditPassword(userId);
		assertEquals(0, userService.calls);
		assertTrue(ep.dBox.isShowing());
		ep.okCancel.getCancel().click();
		assertFalse(ep.dBox.isShowing());	
	}

}
