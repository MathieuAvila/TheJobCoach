package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.TheJobCoach.webapp.userpage.client.UserService.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentNews implements EntryPoint {

	UserId user;
	
	public void setUserParameters(UserId _user)
	{
		user = _user;
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	public void setRootPanel(Panel panel)
	{
		rootPanel = panel;
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{		
		Lang lang = GWT.create(Lang.class);
		System.out.println("Load News, locale is: " + LocaleInfo.getCurrentLocale().getLocaleName());				

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel rootPanel = RootPanel.get("centercontent");
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "100%");
		rootPanel.add(simplePanelCenter);
		
		Label lblLatestNewsFrom = new Label("Latest News from The Job Coach");
		simplePanelCenter.add(lblLatestNewsFrom);
	}
}
