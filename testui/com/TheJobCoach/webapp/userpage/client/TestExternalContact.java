package com.TheJobCoach.webapp.userpage.client;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.ExternalContact.ComponentChooseExternalContact;
import com.TheJobCoach.webapp.userpage.client.ExternalContact.ComponentExternalContactList;
import com.TheJobCoach.webapp.userpage.client.ExternalContact.ContentExternalContact;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.TestSecurity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestExternalContact implements EntryPoint {
		
	@SuppressWarnings("deprecation")
	public static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		{
			final RootPanel root = RootPanel.get("contentexternalcontact");
			if (root != null)
			{
				EasyAsync.Check(root, new ToRun() {
					@Override
					public void Open()
					{		
						root.setStyleName("mainpage-content");		
						HorizontalPanel hp = new HorizontalPanel();
						hp.setStyleName("mainpage-content");
						root.add(hp);
						hp.setSize("100%", "100%");
						ContentExternalContact cud = new ContentExternalContact(hp,TestSecurity.defaultUser);
						cud.onModuleLoad();
					}
				});
			}
		}
		
		{
			final RootPanel rootComponentExternalContactList = RootPanel.get("componentexternalcontactlist");
			if (rootComponentExternalContactList != null)
			{
				GWT.runAsync(new RunAsyncCallback() 
				{
					@Override
					public void onFailure(Throwable reason) 
					{
						MessageBox.messageBoxException(rootComponentExternalContactList, reason.toString());
					}

					@Override
					public void onSuccess() 
					{
						rootComponentExternalContactList.setStyleName("mainpage-content");		
						HorizontalPanel hp = new HorizontalPanel();
						hp.setStyleName("mainpage-content");
						rootComponentExternalContactList.add(hp);
						hp.setSize("100%", "100%");
						
						String contact1 = "contact1";
						String contact2 = "contact2";
						String contact3 = "contact3";							
						ExternalContact ujs1 = new ExternalContact(contact1, "firstName1", "lastName1", "email1", "phone1", "personalNote1", "organization1", new UpdatePeriod(getDate(2000, 1, 1), 2, PeriodType.DAY, true));
						ExternalContact ujs2 = new ExternalContact(contact2, "firstName2", "lastName2", "email2", "phone2", "personalNote2", "organization2", new UpdatePeriod(getDate(2000, 1, 1), 2, PeriodType.DAY, false));
						ExternalContact ujs3 = new ExternalContact(contact3, "firstName3", "lastName3", "email3", "phone3", "personalNote3", "organization3", new UpdatePeriod(getDate(2000, 1, 1), 2, PeriodType.DAY, true));
						Vector<ExternalContact> external_contact_list = new Vector<ExternalContact>(Arrays.asList(ujs1, ujs2, ujs3));

						ComponentExternalContactList cud = new ComponentExternalContactList(
								external_contact_list, 
								hp, 
								TestSecurity.defaultUser);
						cud.onModuleLoad();
					}
				});
			}
		}
		
		{
			final RootPanel rootComponentChooseExternalContact = RootPanel.get("componentchooseexternalcontact");
			if (rootComponentChooseExternalContact != null)
			{
				GWT.runAsync(new RunAsyncCallback() 
				{
					@Override
					public void onFailure(Throwable reason) 
					{
						MessageBox.messageBoxException(rootComponentChooseExternalContact, reason.toString());
					}

					@Override
					public void onSuccess() 
					{
						rootComponentChooseExternalContact.setStyleName("mainpage-content");		
						HorizontalPanel hp = new HorizontalPanel();
						hp.setStyleName("mainpage-content");
						rootComponentChooseExternalContact.add(hp);
						hp.setSize("100%", "100%");

						ComponentChooseExternalContact cud = new ComponentChooseExternalContact(
								hp, 
								TestSecurity.defaultUser, new IChooseResult<ExternalContact>()
								{									
									@Override
									public void setResult(ExternalContact result)
									{
										System.out.println("ID=" + result.ID);
									}
								});
						cud.onModuleLoad();
					}
				});
			}
		}
	}

}
