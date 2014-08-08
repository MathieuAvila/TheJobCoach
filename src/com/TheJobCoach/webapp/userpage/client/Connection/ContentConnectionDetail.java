package com.TheJobCoach.webapp.userpage.client.Connection;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.ExternalContact.LangExternalContact;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.VerticalSpacer;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContentConnectionDetail extends VerticalPanel {

	UserId user;
	ContactInformation connectionUser;

	final static Lang lang = GWT.create(Lang.class);
	final static LangConnection langConnection = GWT.create(LangConnection.class);
	final static LangExternalContact langExternalContact = GWT.create(LangExternalContact.class);

	static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);
	static com.TheJobCoach.webapp.util.client.ClientImageBundle wpUtilImageBundle = (com.TheJobCoach.webapp.util.client.ClientImageBundle) GWT.create(com.TheJobCoach.webapp.util.client.ClientImageBundle.class);
	
	TabPanel tp = new TabPanel();
	DetailPanel dpUser = null;
	DetailPanel dpOpportunity = null;
	DetailPanel dpDocument = null;
	DetailPanel dpContact = null;
	
	IConnectionToDetail connectionToDetail;
	
	public ContentConnectionDetail(UserId user, ContactInformation connectionUser, final IConnectionToDetail connectionToDetail)
	{
		super();
		this.user = user;
		this.connectionUser = connectionUser;
		this.connectionToDetail = connectionToDetail;

		ButtonImageText buttonBack = new ButtonImageText(ButtonImageText.Type.BACK, langConnection.backToConnections());
		buttonBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				connectionToDetail.toConnections();
			}

		});

		add(buttonBack);
		add(new VerticalSpacer("1em"));
		add(tp);

		dpUser = new DetailUser(user, connectionUser);
		tp.add(dpUser, new Image(ClientImageBundle.INSTANCE.userConnectionContent_menu()));

		//if (connectionUser.hisVisibility.opportunity)
		{
			dpOpportunity = new DetailOpportunity(user, connectionUser);
			tp.add(dpOpportunity, new Image(ClientImageBundle.INSTANCE.opportunityContent_menu()));
		}
		
		//if (connectionUser.hisVisibility.document)
		{
			dpDocument = new DetailDocument(user, connectionUser);
			tp.add(dpDocument, new Image(ClientImageBundle.INSTANCE.userDocumentContent_menu()));
		}
		
		//if (connectionUser.hisVisibility.contact)
		{
			dpContact = new DetailContact(user, connectionUser);
			tp.add(dpContact, new Image(ClientImageBundle.INSTANCE.userExternalContactContent_menu()));
		}

		tp.setAnimationEnabled(true);
		
	
		this.setSize("100%", "100%");
		
		//this.selectTab(0);
		dpUser.showPanelDetail();
		
		tp.addSelectionHandler(new SelectionHandler<Integer>(){

			@Override
			public void onSelection(SelectionEvent<Integer> event)
			{
				DetailPanel dp = (DetailPanel)(tp.getWidget(event.getSelectedItem()) );
				dp.showPanelDetail();
				System.out.println("ggggg");
			}});
	}
}
