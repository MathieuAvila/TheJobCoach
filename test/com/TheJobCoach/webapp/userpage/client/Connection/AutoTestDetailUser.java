package com.TheJobCoach.webapp.userpage.client.Connection;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
import com.TheJobCoach.webapp.util.client.DefaultUtilServiceAsync;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestDetailUser extends GwtTest {

	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
	}

	class SpecialUtilServiceAsync extends DefaultUtilServiceAsync
	{
	}

	static SpecialUtilServiceAsync utilService = null;
	static SpecialUserServiceAsync userService = null;

	@Before
	public void beforeDetailUser()
	{
		if ( utilService == null) utilService = new SpecialUtilServiceAsync();
		if ( userService == null) userService = new SpecialUserServiceAsync();
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.util.client.UtilService"))
				{
					return utilService;
				}
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					return userService;
				}
				return null;
			}}
		);
	}
	
	@Test
	public void testGetAll() throws InterruptedException
	{
		DetailUser cuo;		
		utilService.calls = 0;

		ContactInformation ci = new ContactInformation(ContactStatus.CONTACT_OK, userId.userName, "firstName1", "Name1", 
				new Visibility(), 
				new Visibility(false, false, false, false));
		
		utilService.addValue(UserValuesConstantsAccount.ACCOUNT_TITLE, "title");
		utilService.addValue(UserValuesConstantsAccount.ACCOUNT_KEYWORDS, "keys");
		
		cuo = new DetailUser(userId, ci);
		assertEquals(1, utilService.calls);
		
		cuo.showPanelDetail(); // won't do anything more...
		assertEquals(1, utilService.calls);

		assertEquals("title", cuo.labelTitle.getText());
		assertEquals("keys", cuo.labelKeywords.getText());
		assertEquals("En recherche active", cuo.labelStatus.getText());
		assertEquals(ci.firstName, ((Label)cuo.grid.getWidget(DetailUser.ROW_FIRSTNAME, 2)).getText());
		assertEquals(ci.lastName, ((Label)cuo.grid.getWidget(DetailUser.ROW_LASTNAME, 2)).getText());
	}

}
