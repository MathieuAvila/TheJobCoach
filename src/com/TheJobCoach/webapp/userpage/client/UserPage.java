package com.TheJobCoach.webapp.userpage.client;


import com.TheJobCoach.webapp.footer.client.Footer;
import com.TheJobCoach.webapp.mainpage.client.MainPage;
import com.TheJobCoach.webapp.userpage.client.Account.ContentAccount;
import com.TheJobCoach.webapp.userpage.client.CoachSettings.ContentCoachSettings;
import com.TheJobCoach.webapp.userpage.client.Document.ContentUserDocument;
import com.TheJobCoach.webapp.userpage.client.ExternalContact.ContentExternalContact;
import com.TheJobCoach.webapp.userpage.client.MyGoals.ContentMyGoals;
import com.TheJobCoach.webapp.userpage.client.MyReports.ContentMyReports;
import com.TheJobCoach.webapp.userpage.client.Opportunity.ContentUserOpportunity;
import com.TheJobCoach.webapp.userpage.client.Todo.ContentTodo;
import com.TheJobCoach.webapp.userpage.client.UserSite.ContentUserSite;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.TheJobCoach.webapp.util.client.HorizontalSpacer;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class UserPage implements EntryPoint {

	final static Lang lang = GWT.create(Lang.class);

	final VerticalPanel simplePanelContent = new VerticalPanel();
	Label selectedMenu = null;

	UserId userId = null;

	public void setUser(UserId _userId)
	{
		userId = _userId;
	}

	public void changeMenu(String menu)
	{
		if (menu.equals("myjobboards"))
		{
			EasyAsync.Check(simplePanelContent, new ToRun() {
				@Override
				public void Open()
				{
					ContentUserSite contentUserSite = new ContentUserSite(simplePanelContent, userId);
					contentUserSite.onModuleLoad();
				}
			});
		};
		if (menu.equals("news"))
		{			
			EasyAsync.Check(simplePanelContent, new ToRun() {
				@Override
				public void Open()
				{
					ContentNews contentNews = new ContentNews(simplePanelContent, userId);				
					contentNews.onModuleLoad();
				}
			});
		};
		if (menu.equals("todo"))
		{
			EasyAsync.Check(simplePanelContent, new ToRun() {
				@Override
				public void Open()
				{
					ContentTodo contentTodo = new ContentTodo(simplePanelContent, userId);
					contentTodo.onModuleLoad();
				}			
			});

		}
		if (menu.equals("mydocuments"))
		{
			EasyAsync.Check(simplePanelContent, new ToRun() {
				@Override
				public void Open()
				{
					ContentUserDocument contentMyDocuments = new ContentUserDocument(simplePanelContent, userId);
					contentMyDocuments.onModuleLoad();
				}			
			});

		}
		if (menu.equals("applications"))
		{
			EasyAsync.Check(simplePanelContent, new ToRun() {
				@Override
				public void Open()
				{
					ContentUserOpportunity contentUserOpportunity = new ContentUserOpportunity(simplePanelContent, userId);
					contentUserOpportunity.onModuleLoad();
				}			
			});
		}
		if (menu.equals("report"))
		{
			EasyAsync.Check(simplePanelContent, new ToRun() {
				@Override
				public void Open()
				{
					ContentReport contentReport = new ContentReport(simplePanelContent, userId);
					contentReport.onModuleLoad();
				}			
			});
		}
		if (menu.equals("account"))
		{
			EasyAsync.Check(simplePanelContent, new ToRun() {
				@Override
				public void Open()
				{
					ContentAccount contentAccount = new ContentAccount(simplePanelContent, userId);
					contentAccount.onModuleLoad();
				}			
			});
		}
		if (menu.equals("goals"))
		{
			EasyAsync.Check(simplePanelContent, new ToRun() {
				@Override
				public void Open()
				{
					ContentMyGoals contentGoals = new ContentMyGoals(simplePanelContent, userId);
					contentGoals.onModuleLoad();
				}			
			});
		}
		if (menu.equals("coachsettings"))
		{
			EasyAsync.Check(simplePanelContent, new ToRun() {
				@Override
				public void Open()
				{
					ContentCoachSettings contentCoachSettings = new ContentCoachSettings(simplePanelContent, userId);
					contentCoachSettings.onModuleLoad();
				}			
			});
		}
		if (menu.equals("myreports"))
		{
			EasyAsync.Check(simplePanelContent, new ToRun() {
				@Override
				public void Open()
				{
					ContentMyReports contentMyReports = new ContentMyReports(simplePanelContent, userId);
					contentMyReports.onModuleLoad();
				}			
			});
		}
		if (menu.equals("addressbook"))
		{
			EasyAsync.Check(simplePanelContent, new ToRun() {
				@Override
				public void Open()
				{
					ContentExternalContact contentExternalContact = new ContentExternalContact(simplePanelContent, userId);
					contentExternalContact.onModuleLoad();
				}	
			});
		}
	}

	private void setLabelMenu(final Label label, final String menu)
	{
		label.setStyleName("userpage-label-normal");
		label.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (label == selectedMenu)
				{
					label.setStyleName("userpage-label-clicked");
				}
				else
				{
					label.setStyleName("userpage-label-normal");					
				}
			}
		});
		label.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event)
			{
				label.setStyleName("userpage-label-clickable");
			}
		});
		label.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				label.setStyleName("userpage-label-clicked");
				if (selectedMenu != null)
				{
					selectedMenu.setStyleName("userpage-label-normal");
				}
				selectedMenu = label;
				changeMenu(menu);
			}
		});
		label.setStyleName("userpage-label-normal");
	}
	
	void addLabelWithImage(Panel p, Label l, ImageResource imageResource)
	{
		HorizontalPanel hp = new HorizontalPanel();
		Image image = new Image(imageResource);
		hp.add(image);
		hp.setCellWidth(image, "40px");
		hp.add(l);
		hp.setCellVerticalAlignment(l, HasVerticalAlignment.ALIGN_MIDDLE);
		p.add(hp);
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		final RootPanel rootPanel = RootPanel.get("content");
		rootPanel.clear();

		rootPanel.setStyleName("mainpage-content");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.setSize("100%", "100%");

		FlexTable flexTable = new FlexTable();
		rootPanel.add(flexTable, 0, 0);
		flexTable.setSize("100%", "100%");

		HorizontalPanel upperGlobalPanel = new HorizontalPanel();
		
		flexTable.setWidget(0, 0, upperGlobalPanel);		
		PanelCoach pc = new PanelCoach(upperGlobalPanel, userId);
		upperGlobalPanel.add(pc);
		upperGlobalPanel.setCellWidth(pc,  "100%");
		pc.onModuleLoad();
		
		HorizontalPanel panelConnectionInfo = new HorizontalPanel();
		
		VerticalPanel connectionTextPanel = new VerticalPanel();
		
		HorizontalPanel panelConnectedAs = new HorizontalPanel();
		panelConnectedAs.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		Label labelConnectedAs = new Label(lang._Text_ConnectedAs());
		panelConnectedAs.add(labelConnectedAs);
		labelConnectedAs.setWidth("12em");
		panelConnectedAs.add(new HorizontalSpacer("1em"));
		Label labelUserName = new Label(userId.userName);
		panelConnectedAs.add(labelUserName);
		panelConnectedAs.add(new HorizontalSpacer("0.5em"));
		panelConnectedAs.setCellVerticalAlignment(labelUserName, HasVerticalAlignment.ALIGN_MIDDLE);
		panelConnectedAs.setCellVerticalAlignment(labelConnectedAs, HasVerticalAlignment.ALIGN_MIDDLE);
		labelUserName.setStyleName("label-username");

		HorizontalPanel panelConnectionTime = new HorizontalPanel();
		panelConnectionTime.add(new Label(lang._Text_ConnectionTimeToday()));
		Label connectionTime = new Label();
		panelConnectionTime.add(connectionTime);
		
		connectionTextPanel.add(panelConnectedAs);
		connectionTextPanel.add(panelConnectionTime);
		
		PanelUpdate panelUpdate = new PanelUpdate(panelConnectedAs, userId, connectionTime);
		panelConnectedAs.add(panelUpdate);
		
		panelConnectionInfo.add(panelUpdate);
		panelConnectionInfo.add(connectionTextPanel);
		
		upperGlobalPanel.add(panelConnectionInfo);
		upperGlobalPanel.setCellHorizontalAlignment(panelConnectionInfo, HasHorizontalAlignment.ALIGN_RIGHT);
		
		panelUpdate.setWidth("30px");
		panelUpdate.onModuleLoad();

		Image imageLogout = new Image(ClientImageBundle.INSTANCE.urlLogout());
		panelConnectionInfo.add(imageLogout);
		panelConnectionInfo.setCellVerticalAlignment(labelConnectedAs, HasVerticalAlignment.ALIGN_MIDDLE);
		
		imageLogout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				EasyAsync.serverCall(rootPanel, new EasyAsync.ServerCallRun()
				{					
					@Override
					public void Run() throws CassandraException, CoachSecurityException
					{					
						EasyAsync.Check(rootPanel, new EasyAsync.ToRun()
						{							
							@Override
							public void Open()
							{
								MainPage main = new MainPage();
								main.onModuleLoad();
							}
						});
					}
				 });
				}
		});
		imageLogout.setStyleName("mainpage-label-clickable");

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setWidth("100%");
		flexTable.setWidget(1, 0, horizontalPanel_1);

		VerticalPanel verticalPanel_2 = new VerticalPanel();
		horizontalPanel_1.add(verticalPanel_2);
		horizontalPanel_1.setCellWidth(verticalPanel_2, "300px");
		verticalPanel_2.setWidth("300px");

		StackPanel stackPanelAccount = new StackPanel();
		verticalPanel_2.add(stackPanelAccount);
		stackPanelAccount.setWidth("100%");

		VerticalPanel verticalPanelAccount = new VerticalPanel();
		stackPanelAccount.add(verticalPanelAccount, lang._TextAccount(), false);
		verticalPanelAccount.setSize("100%", "100%");

		final Label label_Parameters = new Label(lang._TextMyAccount());
		setLabelMenu(label_Parameters, "account");
		addLabelWithImage(verticalPanelAccount, label_Parameters, ClientImageBundle.INSTANCE.parametersContent_menu());

		SimplePanel simplePanel_4 = new SimplePanel();
		verticalPanel_2.add(simplePanel_4);
		simplePanel_4.setHeight("10px");

		StackPanel stackPanelMyApplication = new StackPanel();
		verticalPanel_2.add(stackPanelMyApplication);
		stackPanelMyApplication.setWidth("100%");

		VerticalPanel verticalPanelMySearch = new VerticalPanel();
		stackPanelMyApplication.add(verticalPanelMySearch, lang._TextMySearch(), false);
		verticalPanelMySearch.setSize("100%", "100%");

		final Label label_ActionsTodo = new Label(lang._TextTodo());
		setLabelMenu(label_ActionsTodo, "todo");
		addLabelWithImage(verticalPanelMySearch, label_ActionsTodo, ClientImageBundle.INSTANCE.todoContent_menu());
		//verticalPanelMySearch.add(label_ActionsTodo);

		final Label lblNewLabel_AddressBook = new Label(lang._TextMyAddressBook());
		setLabelMenu(lblNewLabel_AddressBook, "addressbook");
		addLabelWithImage(verticalPanelMySearch, lblNewLabel_AddressBook, ClientImageBundle.INSTANCE.userExternalContactContent_menu());
		//verticalPanelMySearch.add(lblNewLabel_AddressBook);
		
		final Label lblNewLabel_JobBoards = new Label(lang._TextMyJobBoards());
		setLabelMenu(lblNewLabel_JobBoards, "myjobboards");
		addLabelWithImage(verticalPanelMySearch, lblNewLabel_JobBoards, ClientImageBundle.INSTANCE.userJobSiteContent_menu());
		//verticalPanelMySearch.add(lblNewLabel_JobBoards);
		
		final Label lblNewLabel_Documents = new Label(lang._TextMyDocuments());
		setLabelMenu(lblNewLabel_Documents, "mydocuments");
		addLabelWithImage(verticalPanelMySearch, lblNewLabel_Documents, ClientImageBundle.INSTANCE.userDocumentContent_menu());
		//verticalPanelMySearch.add(lblNewLabel_Documents);

		final Label label_MyApplications = new Label(lang._TextApplicationFollowUp());
		setLabelMenu(label_MyApplications, "applications");
		addLabelWithImage(verticalPanelMySearch, label_MyApplications, ClientImageBundle.INSTANCE.opportunityContent_menu());
		//verticalPanelMySearch.add(label_MyApplications);
/*
		final Label label_ActionsAgenda = new Label(lang._TextAgenda());
		setLabelMenu(label_ActionsAgenda, "agenda");
		verticalPanelMySearch.add(label_ActionsAgenda);
	*/
		SimplePanel simplePanel_2 = new SimplePanel();
		verticalPanel_2.add(simplePanel_2);
		simplePanel_2.setHeight("10px");

		StackPanel stackPanelStats = new StackPanel();
		verticalPanel_2.add(stackPanelStats);
		stackPanelStats.setWidth("100%");

		VerticalPanel verticalPanelStats = new VerticalPanel();
		stackPanelStats.add(verticalPanelStats, lang._TextEvaluations(), false);
		verticalPanelStats.setSize("100%", "100%");

		final Label label_CoachSettings = new Label(lang._TextCoachSettings());
		setLabelMenu(label_CoachSettings, "coachsettings");
		addLabelWithImage(verticalPanelStats, label_CoachSettings, ClientImageBundle.INSTANCE.coachSettingsContent_menu());
		
		final Label label_Bilans = new Label(lang._TextBilans());
		setLabelMenu(label_Bilans, "myreports");
		addLabelWithImage(verticalPanelStats, label_Bilans, ClientImageBundle.INSTANCE.userMyReportsContent_menu());
		
		final Label lblNewLabel_MyGoals = new Label(lang._TextMyGoals());
		setLabelMenu(lblNewLabel_MyGoals, "goals");
		addLabelWithImage(verticalPanelStats, lblNewLabel_MyGoals, ClientImageBundle.INSTANCE.userVirtualCoachGoalsContent_menu());
		//verticalPanelStats.add(lblNewLabel_MyGoals);
		

		SimplePanel simplePanel_5 = new SimplePanel();
		verticalPanel_2.add(simplePanel_5);
		simplePanel_5.setHeight("10px");

		StackPanel stackPanelShares = new StackPanel();
		verticalPanel_2.add(stackPanelShares);
		stackPanelShares.setWidth("100%");

		VerticalPanel verticalPanelShares = new VerticalPanel();
		stackPanelShares.add(verticalPanelShares, lang._TextCommunity(), false);
		verticalPanelShares.setSize("100%", "100%");

		final Label label_Report = new Label(lang._TextReport());
		setLabelMenu(label_Report, "report");
		addLabelWithImage(verticalPanelShares, label_Report, ClientImageBundle.INSTANCE.sendCommentContent_menu());
//		verticalPanelShares.add(label_Report);

		final Label label_News = new Label(lang._TextNews());
		setLabelMenu(label_News, "news");
		//verticalPanelShares.add(label_News);
		addLabelWithImage(verticalPanelShares, label_News, ClientImageBundle.INSTANCE.newsContent_menu());

		SimplePanel simplePanel_CentralInter = new SimplePanel();
		simplePanel_CentralInter.setWidth("30px");
		horizontalPanel_1.add(simplePanel_CentralInter);
		horizontalPanel_1.setCellWidth(simplePanel_CentralInter, "30px");

		horizontalPanel_1.add(simplePanelContent);
		horizontalPanel_1.setCellHeight(simplePanelContent, "100%");
		horizontalPanel_1.setCellWidth(simplePanelContent, "100%");
		simplePanelContent.setSize("100%", "100%");

		HTMLPanel panelAds = new HTMLPanel("<div id=\"adframe\">");
		horizontalPanel_1.add(panelAds);

		VerticalPanel verticalPanel = new VerticalPanel();
		flexTable.setWidget(3, 0, verticalPanel);
		flexTable.getCellFormatter().setHeight(3, 0, "100%");
		verticalPanel.setStyleName("mainpage-content");
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setSize("", "30px");
		flexTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getCellFormatter().setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_MIDDLE);

		Footer footerPanel = new Footer();
		footerPanel.setRootPanel(verticalPanel);	
		footerPanel.onModuleLoad();

		changeMenu("news");
	}
}
