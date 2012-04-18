package com.TheJobCoach.webapp.adminpage.client;


import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Position;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AdminPage implements EntryPoint {
		
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
		
		if (menu.equals("users"))
		{
			ContentUsers contentUsers = new ContentUsers(simplePanelCenter, userId);
			contentUsers.onModuleLoad();
		}
		if (menu.equals("news"))
		{
			ContentNews contentNews = new ContentNews();
			contentNews.setRootPanel(simplePanelCenter);
			contentNews.setUserParameters(userId);
			contentNews.onModuleLoad();
		}
	}
	
	public void setLabelMenu(final Label label, final String menu)
	{
		label.setStyleName("userpage-label-normal");
		if (
				!menu.equals("news") &&
				!menu.equals("users") &&
				!menu.equals("account")
				)
		{
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
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		flexTable.setWidget(1, 0, horizontalPanel_1);
		horizontalPanel_1.setSize("100%", "100%");
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		horizontalPanel_1.add(verticalPanel_2);
		verticalPanel_2.setHeight("100%");
		
		horizontalPanel_1.add(simplePanelCenter);
		
		StackPanel stackPanelAccount = new StackPanel();
		verticalPanel_2.add(stackPanelAccount);
		stackPanelAccount.setWidth("100%");
		
		VerticalPanel verticalPanelAccount = new VerticalPanel();
		stackPanelAccount.add(verticalPanelAccount, "Account", false);
		verticalPanelAccount.setSize("100%", "100%");
		
		final Label label_Profile = new Label("Users");
		setLabelMenu(label_Profile, "users");
		verticalPanelAccount.add(label_Profile);
		
		final Label label_News = new Label("News");
		setLabelMenu(label_News, "news");
		verticalPanelAccount.add(label_News);
		
		changeMenu("news");
	}
}
