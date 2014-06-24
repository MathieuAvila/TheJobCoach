package com.TheJobCoach.webapp.userpage.client;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestContentReport extends GwtTest {

	static Logger logger = LoggerFactory.getLogger(AutoTestContentReport.class);

	private ContentReport contentReport;

	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	UserId userIdTest = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER, true);

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public String m;
		
		public void reset()
		{
			calls = 0;
		}
		
		@Override
		public void sendComment(UserId id, String value,
				AsyncCallback<String> callback) {
			calls++;
			m = value;
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
	}

	@Test
	public void testReport()
	{
		userService.reset();		

		contentReport = new ContentReport(new HorizontalPanel(), userId);
		contentReport.onModuleLoad();
		contentReport.textArea.setText("God save the Queen");
		assertTrue(contentReport.buttonSendReport.isEnabled());
		contentReport.buttonSendReport.click();
		assertEquals(1, DefaultUserServiceAsync.calls);		
		assertEquals("God save the Queen", userService.m);
		assertFalse(contentReport.buttonSendReport.isEnabled());
	}
	
	@Test
	public void testReportTestUser()
	{
		userService.reset();		

		contentReport = new ContentReport(new HorizontalPanel(), userIdTest);
		contentReport.onModuleLoad();
		assertFalse(contentReport.buttonSendReport.isEnabled());
	}

}
