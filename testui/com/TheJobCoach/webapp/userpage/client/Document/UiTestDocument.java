package com.TheJobCoach.webapp.userpage.client.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.TestSecurity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class UiTestDocument implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		{
			final RootPanel root = RootPanel.get("componentchoosedocument");
			if (root != null)
			{
				EasyAsync.Check(root, new ToRun() {
					@Override
					public void Open()
					{
						ArrayList<UserDocumentId> userDocumentList = new ArrayList<UserDocumentId>();
						userDocumentList.add(new UserDocumentId("ID1", "updateID1", "name1", "fileName1", new Date(), new Date()));
						userDocumentList.add(new UserDocumentId("ID2", "updateID2", "name2", "fileName2", new Date(), new Date()));
						userDocumentList.add(new UserDocumentId("ID3", "updateID3", "name3", "fileName3", new Date(), new Date()));
						ComponentChooseDocument cud = new ComponentChooseDocument(root, TestSecurity.defaultUser, new IChooseResult<UserDocumentId>()
								{
							@Override
							public void setResult(UserDocumentId result) {

							}						
								});
						cud.onModuleLoad();					
					}
				});
			}
		}
		{
			final RootPanel root = RootPanel.get("componentdocumentlist");
			if (root != null)
			{
				EasyAsync.Check(root, new ToRun() {
					@Override
					public void Open()
					{					
						Vector<UserDocumentId> userDocumentList = new Vector<UserDocumentId>();
						userDocumentList.add(new UserDocumentId("ID1", "updateID1", "name1", "fileName1", new Date(), new Date()));
						userDocumentList.add(new UserDocumentId("ID2", "updateID2", "name2", "fileName2", new Date(), new Date()));
						userDocumentList.add(new UserDocumentId("ID3", "updateID3", "name3", "fileName3", new Date(), new Date()));
						ComponentDocumentList cud = new ComponentDocumentList(userDocumentList, root, TestSecurity.defaultUser);
						cud.onModuleLoad();
					}
				});
			}
		}
		{
			final RootPanel root = RootPanel.get("contentuserdocument");
			if (root != null)
			{
				EasyAsync.Check(root, new ToRun() {
					@Override
					public void Open()
					{	
						ContentUserDocument cud = new ContentUserDocument(root, TestSecurity.defaultUser);
						cud.onModuleLoad();
					}
				});
			}
		}
	}

}
