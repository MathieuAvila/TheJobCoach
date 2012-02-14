package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.mainpage.client.Lang;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Validate implements EntryPoint {
	
	Panel rootPanel;

	public void setRootPanel(Panel panel)
	{
		rootPanel = panel;
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		
		//UserPage uP = new UserPage();
		//uP.onModuleLoad();
		//return;
		
		Lang lang = GWT.create(Lang.class);
		System.out.println("Footer Panel Locale is: " + LocaleInfo.getCurrentLocale().getLocaleName());				
		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		rootPanel.clear();
		rootPanel.setStyleName("footer-content");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.setSize("100%", "100%");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(verticalPanel);
		verticalPanel.setSize("100%", "100%");
		
		Image image = new Image("jobcoach.gif");
		verticalPanel.add(image);
		verticalPanel.setCellHorizontalAlignment(image, HasHorizontalAlignment.ALIGN_CENTER);
		
		Label label = new Label("The Job Coach");
		label.setStyleName("mainpage-title");
		verticalPanel.add(label);
		verticalPanel.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);
		
		HTMLPanel panel = new HTMLPanel("New HTML");
		verticalPanel.add(panel);
		
		Button btnNewButton = new Button("New button");
		verticalPanel.add(btnNewButton);
		        

	}
	
}
