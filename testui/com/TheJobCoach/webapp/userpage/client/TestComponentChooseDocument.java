package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.Date;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestComponentChooseDocument implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		final RootPanel root = RootPanel.get("componentchoosedocument");
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
					System.out.println("Content Component Choose Document");
					root.setStyleName("mainpage-content");		
					HorizontalPanel hp = new HorizontalPanel();
					hp.setStyleName("mainpage-content");
					root.add(hp);
					hp.setSize("100%", "100%");
					ArrayList<UserDocumentId> userDocumentList = new ArrayList<UserDocumentId>();
					userDocumentList.add(new UserDocumentId("ID1", "updateID1", "name1", "fileName1", new Date(), new Date()));
					userDocumentList.add(new UserDocumentId("ID2", "updateID2", "name2", "fileName2", new Date(), new Date()));
					userDocumentList.add(new UserDocumentId("ID3", "updateID3", "name3", "fileName3", new Date(), new Date()));
					ComponentChooseDocument cud = new ComponentChooseDocument(hp, new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER), new ComponentChooseDocument.ComponentChooseDocumentResult()
					{
						@Override
						public void setResult(UserDocumentId result) {
							System.out.println("Selected + " + result);
						}						
					});
					cud.onModuleLoad();					
				}
			});
		}
	}

}
