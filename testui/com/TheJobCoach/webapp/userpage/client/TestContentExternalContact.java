package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.ExternalContact.ContentExternalContact;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestContentExternalContact implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		final RootPanel root = RootPanel.get("contentexternalcontact");
		if (root != null)
		{
			GWT.runAsync(new RunAsyncCallback() 
			{
				@Override
				public void onFailure(Throwable reason) 
				{
					MessageBox.messageBoxException(root, reason.toString());
				}

				@Override
				public void onSuccess() 
				{
					System.out.println("Content External Contact");
					root.setStyleName("mainpage-content");		
					HorizontalPanel hp = new HorizontalPanel();
					hp.setStyleName("mainpage-content");
					root.add(hp);
					hp.setSize("100%", "100%");
					ContentExternalContact cud = new ContentExternalContact(hp, new UserId("mathieu", "token", UserId.UserType.USER_TYPE_SEEKER));
					cud.onModuleLoad();
				}
			});
		}
	}

}