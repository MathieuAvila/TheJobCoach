package com.TheJobCoach.webapp.userpage.client.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.webapp.adminpage.shared.UserSearchEntry;
import com.TheJobCoach.webapp.adminpage.shared.UserSearchResult;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserId.UserType;
import com.google.gwt.user.client.ui.CheckBox;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestEditShares extends GwtTest {

	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	UserId userIdTest = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER, true);

	ContactInformation ci1 = new ContactInformation(ContactStatus.CONTACT_OK, 
			"u1", "firstName1", "lastName1", new Visibility(), new Visibility());
	ContactInformation ci2 = new ContactInformation(ContactStatus.CONTACT_REQUESTED, 
			"u2", "firstName2", "lastName2", new Visibility(), new Visibility());
	ContactInformation ci3 = new ContactInformation(ContactStatus.CONTACT_AWAITING, 
			"u3", "firstName3", "lastName3", new Visibility(), new Visibility());

	// added after request
	ContactInformation ci_new = new ContactInformation(ContactStatus.CONTACT_REQUESTED, 
			"new_user", "firstName_new", "lastName_new", new Visibility(), new Visibility());

	Vector<ContactInformation> contactList =  new Vector<ContactInformation>(Arrays.asList(ci1, ci2, ci3));

	UserSearchEntry s_userId = new UserSearchEntry(userId.userName, "firstName", "lastName", "job", UserType.USER_TYPE_SEEKER);
	UserSearchEntry s_ci1 = new UserSearchEntry(ci1.userName, "firstName1", "lastName1", "job1", UserType.USER_TYPE_SEEKER);
	UserSearchEntry s_new = new UserSearchEntry("new_user", "firstName_new", "lastName_new", "job_new", UserType.USER_TYPE_SEEKER);

	Vector<UserSearchEntry> searchResultEntries = new Vector<UserSearchEntry>(Arrays.asList(s_userId, s_ci1, s_new));
	UserSearchResult searchResult = new UserSearchResult(searchResultEntries, 15);

	@Test
	public void testAll() throws InterruptedException
	{
		List<Boolean> bArray = Arrays.asList(true, false);
		for (boolean mo : bArray)
		{
			for (boolean ml : bArray)
			{
				for (boolean mc : bArray)
				{
					for (boolean md : bArray)
					{

						for (boolean ho : bArray)
						{
							for (boolean hl : bArray)
							{
								for (boolean hc : bArray)
								{
									for (boolean hd : bArray)
									{
										ContactInformation ci = new ContactInformation(
												ContactStatus.CONTACT_OK
												, "",
												"", "", 
												new Visibility(md, mc, mo, ml),
												new Visibility(hd, hc, ho, hl));
										IChooseResult<ContactInformation> result = new IChooseResult<ContactInformation>()
												{
											@Override
											public void setResult(
													ContactInformation result)
											{
											}};
											EditShares es = new EditShares(ci, result);

											assertEquals(mc, es.checkBoxContact.getItem().getValue());
											assertEquals(md, es.checkBoxDocument.getItem().getValue());
											assertEquals(ml, es.checkBoxLog.getItem().getValue());
											assertEquals(mo, es.checkBoxOpportunity.getItem().getValue());

											assertEquals(ho, ((CheckBox)es.gridHisHerShares.getWidget(0, 2)).getValue());
											assertEquals(hl, ((CheckBox)es.gridHisHerShares.getWidget(1, 2)).getValue());
											assertEquals(hd, ((CheckBox)es.gridHisHerShares.getWidget(2, 2)).getValue());
											assertEquals(hc, ((CheckBox)es.gridHisHerShares.getWidget(3, 2)).getValue());
											es.setAnimationEnabled(false);
											es.hide();											
									}
								}
							}
						}
					}
				}
			}
		}
		for (int checkBoxNr = 0 ; checkBoxNr != 4; checkBoxNr++)
		{
			ContactInformation ci = new ContactInformation(
					ContactStatus.CONTACT_OK
					, "",
					"", "", 
					new Visibility(false, false, false, false),
					new Visibility());
			IChooseResult<ContactInformation> result = new IChooseResult<ContactInformation>()	{
				@Override
				public void setResult(ContactInformation result)		{
				}
			};
			EditShares es = new EditShares(ci, result);
			es.setAnimationEnabled(false);
			assertFalse(es.okCancel.getOk().isEnabled());
			CheckBox cb = null;
			switch (checkBoxNr)
			{
			case 0: cb = es.checkBoxContact.getItem(); break;
			case 1: cb = es.checkBoxDocument.getItem(); break;
			case 2: cb = es.checkBoxLog.getItem(); break;
			case 3: cb = es.checkBoxOpportunity.getItem(); break;
			}
			cb.setValue(true);
			// XXX GWT-Test does not transmit all signals ???
			es.changed(true, true, false);
			assertTrue(es.okCancel.getOk().isEnabled());
			es.okCancel.getOk().click();
			switch (checkBoxNr)
			{
			case 0: assertTrue(ci.myVisibility.contact) ; break;
			case 1: assertTrue(ci.myVisibility.document) ; break;
			case 2: assertTrue(ci.myVisibility.log) ; break;
			case 3: assertTrue(ci.myVisibility.opportunity) ; break;
			}
			assertFalse(es.isShowing());
		}

	}
}
