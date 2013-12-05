package com.TheJobCoach.webapp.userpage.client.UserSite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestEditUserSite extends GwtTest 
{
	
	static Logger logger = LoggerFactory.getLogger(AutoTestEditUserSite.class);

	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	String site1 = "site1";
	UserJobSite ujs1 = 
			new UserJobSite(site1, "site1", "URL1", "description1", "login1", "password1", 
			new UpdatePeriod(CoachTestUtils.getDate(2013, 12, 1), 10, PeriodType.DAY, true));
	
	UserJobSite ujs1_result =
			new UserJobSite(site1, "site12", "URL12", "description12", "login12", "password12", 
			new UpdatePeriod(CoachTestUtils.getDate(2013, 12, 1), 11, PeriodType.DAY, true));

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int calls;
		
		public boolean testNew = false;
		
		@Override
		public void setUserSite(UserId id, UserJobSite data,
				AsyncCallback<Integer> callback) {
			calls++;
			logger.info("setUserSite " + id.userName);
			assertEquals(userId.userName, id.userName);
			
			if (!testNew) 
			{
				assertEquals(data.ID, ujs1_result.ID);
			}
			else
			{
				assertNotNull(data.ID);
				assertFalse("".equals(data.ID));
			}
			assertEquals(data.description, ujs1_result.description);
			assertEquals(data.name, ujs1_result.name);
			assertEquals(data.login, ujs1_result.login);
			assertEquals(data.password, ujs1_result.password);
			assertEquals(data.URL, ujs1_result.URL);
			assertEquals(data.update.length, ujs1_result.update.length);

			callback.onSuccess(0);
		}
	}
	
	static SpecialUserServiceAsync userService;
	
	HorizontalPanel p;
 
	EditUserSite eus;

	@Before
	public void beforeEditUserSite()
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
	
	class ResultCallbackTest implements IChooseResult<UserJobSite>
	{
		int calls;
		@Override
		public void setResult(UserJobSite result)
		{
			calls++;
		}
	}
	
	void setUserSite(EditUserSite eus, UserJobSite result)
	{
		eus.textBoxName.setValue(result.name);
		eus.textBoxLogin.setValue(result.login);
		eus.textBoxPassword.setValue(result.password);
		eus.textAreaDescription.setText(result.description);
		eus.textBoxUrl.setValue(result.URL);
		eus.compUpdatePeriod.setTimeCount(result.update.length);
	}
	
	@Test
	public void testValid() throws InterruptedException
	{
		ResultCallbackTest resultCb = new ResultCallbackTest();
		userService.calls = 0;
		userService.testNew = false;
		eus = new EditUserSite(p, ujs1, userId, resultCb);
		eus.onModuleLoad();	
		
		setUserSite(eus, ujs1_result);
		
		eus.okCancel.getOk().click();
		assertEquals(1, userService.calls);
		assertEquals(1, resultCb.calls);
	}
	
	@Test
	public void testValidVoid() throws InterruptedException
	{
		ResultCallbackTest resultCb = new ResultCallbackTest();
		userService.testNew = true;
		userService.calls = 0;
		eus = new EditUserSite(p, null, userId, resultCb);
		eus.onModuleLoad();	
		
		setUserSite(eus, ujs1_result);
		
		eus.okCancel.getOk().click();
		assertEquals(1, userService.calls);
		assertEquals(1, resultCb.calls);
	}

	@Test
	public void testCancel() throws InterruptedException
	{
		userService.calls = 0;
		ResultCallbackTest resultCb = new ResultCallbackTest();
		eus = new EditUserSite(p, ujs1, userId, resultCb);
		eus.onModuleLoad();	
		eus.okCancel.getCancel().click();
		assertEquals(0, userService.calls);
		assertEquals(0, resultCb.calls);
	}

	
}
