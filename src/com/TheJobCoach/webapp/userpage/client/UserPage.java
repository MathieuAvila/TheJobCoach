package com.TheJobCoach.webapp.userpage.client;


import com.TheJobCoach.webapp.userpage.client.UserService.UserId;
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

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class UserPage implements EntryPoint {
		
	
	UserId userId = new UserId("user", "user");
	
	public void setUser(UserId _userId)
	{
		userId = _userId;
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
		rootPanel.add(flexTable, 0, 0);
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
		verticalPanel_2.setHeight("100%");
		
		StackPanel stackPanelAccount = new StackPanel();
		verticalPanel_2.add(stackPanelAccount);
		stackPanelAccount.setWidth("100%");
		
		VerticalPanel verticalPanelAccount = new VerticalPanel();
		stackPanelAccount.add(verticalPanelAccount, lang._TextAccount(), false);
		verticalPanelAccount.setSize("100%", "100%");
		
		Label label_Profile = new Label(lang._TextMyProfile());
		verticalPanelAccount.add(label_Profile);
		
		Label label_Parameters = new Label(lang._TextMyParameters());
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
		
		Label lblNewLabel_MyGoals = new Label(lang._TextMyGoals());
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
		
		Label lblNewLabel_AddressBook = new Label(lang._TextMyAddressBook());
		verticalPanelMyTools.add(lblNewLabel_AddressBook);
		Label lblNewLabel_JobBoards = new Label(lang._TextMyJobBoards());
		verticalPanelMyTools.add(lblNewLabel_JobBoards);
		Label lblNewLabel_Documents = new Label(lang._TextMyDocuments());
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
		
		Label label_Researches = new Label(lang._TextMyResearches());
		verticalPanelMyApplication.add(label_Researches);
		
		Label label_SearchResults = new Label(lang._TextSearchResults());
		verticalPanelMyApplication.add(label_SearchResults);
		
		Label label_MyApplications = new Label(lang._TextApplicationFollowUp());
		verticalPanelMyApplication.add(label_MyApplications);
		
		Label label_ActionsAgenda = new Label(lang._TextAgenda());
		verticalPanelMyApplication.add(label_ActionsAgenda);
		
		Label label_ArchivedApplications = new Label(lang._TextArchivedApplications());
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
		
		Label label_Bilans = new Label(lang._TextBilans());
		verticalPanelStats.add(label_Bilans);
		
		Label label_Statistics = new Label(lang._TextStatistiques());
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
		
		Label label_JobBoards = new Label(lang._TextSites());
		verticalPanelAdvices.add(label_JobBoards);
		
		Label label_Advices = new Label(lang._TextAdvices());
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
		
		Label label_Forum = new Label(lang._TextForum());
		verticalPanelShares.add(label_Forum);
		
		Label label_News = new Label(lang._TextNews());
		verticalPanelShares.add(label_News);
		
		HTMLPanel panelCenter = new HTMLPanel("<div id=\"centercontent\">");
		horizontalPanel_1.add(panelCenter);
		panelCenter.setSize("100%", "100%");
		
		VerticalPanel simplePanelCenter = new VerticalPanel();
		horizontalPanel_1.add(simplePanelCenter);
		
		HTMLPanel panelAds = new HTMLPanel("<div id=\"adframe\">");
		horizontalPanel_1.add(panelAds);
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		flexTable.setWidget(2, 0, horizontalPanel_2);
		flexTable.getCellFormatter().setHeight(2, 0, "10px");
		
		Label label_15 = new Label(lang._TextAbout());
		horizontalPanel_2.add(label_15);
		
		Label label_16 = new Label("  -  ");
		horizontalPanel_2.add(label_16);
		horizontalPanel_2.setCellHorizontalAlignment(label_16, HasHorizontalAlignment.ALIGN_CENTER);
		label_16.setWidth("20px");
		
		Label label_17 = new Label(lang._TextWhoWeAre());
		horizontalPanel_2.add(label_17);
		
		Label label_18 = new Label("  -  ");
		horizontalPanel_2.add(label_18);
		horizontalPanel_2.setCellHorizontalAlignment(label_18, HasHorizontalAlignment.ALIGN_CENTER);
		label_18.setWidth("20px");
		
		Label label_19 = new Label(lang._TextConfidentiality());
		horizontalPanel_2.add(label_19);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		
		ContentUserSite contentUserSite = new ContentUserSite();
		contentUserSite.setRootPanel(simplePanelCenter);
		contentUserSite.setUserParameters(userId);
		contentUserSite.onModuleLoad();
	}
}
