package com.TheJobCoach.webapp.userpage.client;


import com.TheJobCoach.webapp.footer.client.Footer;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class UserPage implements EntryPoint {
		
	final VerticalPanel simplePanelCenter = new VerticalPanel();
	Label selectedMenu = null;
	
	UserId userId = new UserId("user", "user", UserId.UserType.USER_TYPE_SEEKER);
	
	public void setUser(UserId _userId)
	{
		userId = _userId;
	}
	
	public void changeMenu(String menu)
	{
		System.out.println(menu);
		if (menu.equals("myjobboards"))
		{
			ContentUserSite contentUserSite = new ContentUserSite();
			contentUserSite.setRootPanel(simplePanelCenter);
			contentUserSite.setUserParameters(userId);
			contentUserSite.onModuleLoad();
			
		}
		if (menu.equals("news"))
		{
			ContentNews contentNews = new ContentNews();
			contentNews.setRootPanel(simplePanelCenter);
			contentNews.setUserParameters(userId);
			contentNews.onModuleLoad();
		}
		if (menu.equals("mydocuments"))
		{
			ContentMyDocuments contentMyDocuments = new ContentMyDocuments();
			contentMyDocuments.setRootPanel(simplePanelCenter);
			contentMyDocuments.setUserParameters(userId);
			contentMyDocuments.onModuleLoad();
		}
	}
	
	public void setLabelMenu(final Label label, final String menu)
	{
		label.setStyleName("userpage-label-normal");
		if (
				!menu.equals("news") &&
				!menu.equals("myjobboards") &&
				!menu.equals("mydocuments")
				)
		{
			label.setStyleName("userpage-label-blocked");
			return;
		}
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
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		Lang lang = GWT.create(Lang.class);
		System.out.println("Locale is: " + LocaleInfo.getCurrentLocale().getLocaleName());				
		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel rootPanel = RootPanel.get("content");
		rootPanel.clear();
		
		rootPanel.setStyleName("mainpage-content");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.setSize("100%", "100%");
				
		FlexTable flexTable = new FlexTable();
		rootPanel.add(flexTable, 0, 10);
		flexTable.setSize("100%", "100%");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		flexTable.setWidget(0, 0, horizontalPanel);
		
		Image image = new Image("jobcoach.gif");
		horizontalPanel.add(image);
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		flexTable.setWidget(1, 0, horizontalPanel_1);
		horizontalPanel_1.setSize("100%", "100%");
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		horizontalPanel_1.add(verticalPanel_2);
		horizontalPanel_1.setCellWidth(verticalPanel_2, lang.verticalPanel_2_width());
		verticalPanel_2.setSize("300px", "100%");
		
		StackPanel stackPanelAccount = new StackPanel();
		verticalPanel_2.add(stackPanelAccount);
		stackPanelAccount.setWidth("100%");
		
		VerticalPanel verticalPanelAccount = new VerticalPanel();
		stackPanelAccount.add(verticalPanelAccount, lang._TextAccount(), false);
		verticalPanelAccount.setSize("100%", "100%");
		
		final Label label_Profile = new Label(lang._TextMyProfile());
		setLabelMenu(label_Profile, "profile");
		verticalPanelAccount.add(label_Profile);
		
		final Label label_Parameters = new Label(lang._TextMyParameters());
		label_Parameters.setStyleName("userpage-label-clicked");
		setLabelMenu(label_Parameters, "parameters");
		verticalPanelAccount.add(label_Parameters);
		
		SimplePanel simplePanel_4 = new SimplePanel();
		verticalPanel_2.add(simplePanel_4);
		simplePanel_4.setHeight("10px");
		
		StackPanel stackPanelGoals = new StackPanel();
		verticalPanel_2.add(stackPanelGoals);
		stackPanelGoals.setWidth("100%");
		
		VerticalPanel verticalPanelGoals = new VerticalPanel();
		stackPanelGoals.add(verticalPanelGoals, lang._TextGoals(), false);
		verticalPanelGoals.setSize("100%", "100%");
		
		final Label lblNewLabel_MyGoals = new Label(lang._TextMyGoals());
		setLabelMenu(lblNewLabel_MyGoals, "goals");
		verticalPanelGoals.add(lblNewLabel_MyGoals);
		
		SimplePanel simplePanel = new SimplePanel();
		verticalPanel_2.add(simplePanel);
		simplePanel.setHeight("10px");
		
		StackPanel stackPanelMyTools = new StackPanel();
		verticalPanel_2.add(stackPanelMyTools);
		stackPanelMyTools.setWidth("100%");
		
		VerticalPanel verticalPanelMyTools = new VerticalPanel();
		stackPanelMyTools.add(verticalPanelMyTools, lang._TextMyTools(), false);
		verticalPanelMyTools.setSize("100%", "100%");
				
		final Label lblNewLabel_AddressBook = new Label(lang._TextMyAddressBook());
		setLabelMenu(lblNewLabel_AddressBook, "addressbook");
		verticalPanelMyTools.add(lblNewLabel_AddressBook);
		final Label lblNewLabel_JobBoards = new Label(lang._TextMyJobBoards());
		setLabelMenu(lblNewLabel_JobBoards, "myjobboards");
		verticalPanelMyTools.add(lblNewLabel_JobBoards);
		final Label lblNewLabel_Documents = new Label(lang._TextMyDocuments());
		setLabelMenu(lblNewLabel_Documents, "mydocuments");
		verticalPanelMyTools.add(lblNewLabel_Documents);
		
		SimplePanel simplePanel_1 = new SimplePanel();
		verticalPanel_2.add(simplePanel_1);
		simplePanel_1.setHeight("10px");
		
		StackPanel stackPanelMyApplication = new StackPanel();
		verticalPanel_2.add(stackPanelMyApplication);
		stackPanelMyApplication.setWidth("100%");
		
		VerticalPanel verticalPanelMyApplication = new VerticalPanel();
		stackPanelMyApplication.add(verticalPanelMyApplication, lang._TextMyApplications(), false);
		verticalPanelMyApplication.setSize("100%", "100%");
		
		final Label label_Researches = new Label(lang._TextMyOpportunities());
		setLabelMenu(label_Researches, "research");
		verticalPanelMyApplication.add(label_Researches);
		
		final Label label_SearchResults = new Label(lang._TextSearchResults());
		setLabelMenu(label_SearchResults, "searchresult");
		verticalPanelMyApplication.add(label_SearchResults);
		
		final Label label_MyApplications = new Label(lang._TextApplicationFollowUp());
		setLabelMenu(label_MyApplications, "applications");
		verticalPanelMyApplication.add(label_MyApplications);
		
		final Label label_ActionsAgenda = new Label(lang._TextAgenda());
		setLabelMenu(label_ActionsAgenda, "agenda");
		verticalPanelMyApplication.add(label_ActionsAgenda);
		
		final Label label_ArchivedApplications = new Label(lang._TextArchivedApplications());
		setLabelMenu(label_ArchivedApplications, "applications");
		verticalPanelMyApplication.add(label_ArchivedApplications);
		
		SimplePanel simplePanel_2 = new SimplePanel();
		verticalPanel_2.add(simplePanel_2);
		simplePanel_2.setHeight("10px");
		
		StackPanel stackPanelStats = new StackPanel();
		verticalPanel_2.add(stackPanelStats);
		stackPanelStats.setWidth("100%");
		
		VerticalPanel verticalPanelStats = new VerticalPanel();
		stackPanelStats.add(verticalPanelStats, lang._TextEvaluations(), false);
		verticalPanelStats.setSize("100%", "100%");
		
		final Label label_Bilans = new Label(lang._TextBilans());
		setLabelMenu(label_Bilans, "bilans");
		verticalPanelStats.add(label_Bilans);
		
		final Label label_Statistics = new Label(lang._TextStatistiques());
		setLabelMenu(label_Statistics, "statistiques");
		verticalPanelStats.add(label_Statistics);
		
		SimplePanel simplePanel_3 = new SimplePanel();
		verticalPanel_2.add(simplePanel_3);
		simplePanel_3.setHeight("10px");
		
		StackPanel stackPanelAdvices = new StackPanel();
		verticalPanel_2.add(stackPanelAdvices);
		stackPanelAdvices.setWidth("100%");
		
		VerticalPanel verticalPanelAdvices = new VerticalPanel();
		stackPanelAdvices.add(verticalPanelAdvices, lang._TextLibrary(), false);
		verticalPanelAdvices.setSize("100%", "100%");
		
		final Label label_JobBoards = new Label(lang._TextSites());
		setLabelMenu(label_JobBoards, "jobboards");
		verticalPanelAdvices.add(label_JobBoards);
		
		final Label label_Advices = new Label(lang._TextAdvices());
		setLabelMenu(label_Advices, "advices");
		verticalPanelAdvices.add(label_Advices);
		
		SimplePanel simplePanel_5 = new SimplePanel();
		verticalPanel_2.add(simplePanel_5);
		simplePanel_5.setHeight("10px");
		
		StackPanel stackPanelShares = new StackPanel();
		verticalPanel_2.add(stackPanelShares);
		stackPanelShares.setWidth("100%");
		
		VerticalPanel verticalPanelShares = new VerticalPanel();
		stackPanelShares.add(verticalPanelShares, lang._TextCommunity(), false);
		verticalPanelShares.setSize("100%", "100%");
		
		final Label label_Forum = new Label(lang._TextForum());
		setLabelMenu(label_Forum, "forum");
		verticalPanelShares.add(label_Forum);
		
		final Label label_News = new Label(lang._TextNews());
		setLabelMenu(label_News, "news");
		verticalPanelShares.add(label_News);
		
		SimplePanel simplePanel_6 = new SimplePanel();
		horizontalPanel_1.add(simplePanel_6);
		simplePanel_6.setWidth("50px");
		horizontalPanel_1.setCellWidth(simplePanel_6, lang.simplePanel_6_width());
		
		horizontalPanel_1.add(simplePanelCenter);
		horizontalPanel_1.setCellHeight(simplePanelCenter, lang.simplePanelCenter_height());
		horizontalPanel_1.setCellWidth(simplePanelCenter, lang.simplePanelCenter_width());
		simplePanelCenter.setSize("100%", "100%");
		
		HTMLPanel panelAds = new HTMLPanel("<div id=\"adframe\">");
		horizontalPanel_1.add(panelAds);
		
		ContentUserSite contentUserSite = new ContentUserSite();
		contentUserSite.setRootPanel(simplePanelCenter);
		contentUserSite.setUserParameters(userId);
		contentUserSite.onModuleLoad();
		
		VerticalPanel verticalPanel = new VerticalPanel();
		flexTable.setWidget(2, 0, verticalPanel);
		flexTable.getCellFormatter().setHeight(2, 0, lang.verticalPanel_height_1());
		verticalPanel.setStyleName("mainpage-content");
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setSize("", "30px");
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_MIDDLE);

		Footer footerPanel = new Footer();
		footerPanel.setRootPanel(verticalPanel);	
		footerPanel.onModuleLoad();
	}
}
