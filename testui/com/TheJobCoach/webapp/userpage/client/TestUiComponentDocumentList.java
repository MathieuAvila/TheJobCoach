package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.Document.ComponentDocumentList;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestUiComponentDocumentList implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		final RootPanel root = RootPanel.get("componentdocumentlist");
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
					Vector<UserDocumentId> userDocumentList = new Vector<UserDocumentId>();
					userDocumentList.add(new UserDocumentId("ID1", "updateID1", "name1", "fileName1", new Date(), new Date()));
					userDocumentList.add(new UserDocumentId("ID2", "updateID2", "name2", "fileName2", new Date(), new Date()));
					userDocumentList.add(new UserDocumentId("ID3", "updateID3", "name3", "fileName3", new Date(), new Date()));
					ComponentDocumentList cud = new ComponentDocumentList(userDocumentList, hp, new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER));
					cud.onModuleLoad();
					hp.add(cud);
				}
			});
		}
	}

}
