package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
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
public class ContentUserSite implements EntryPoint {

	UserId user;

	final CellTable<UserJobSite> cellTable = new CellTable<UserJobSite>();
	TextBox textBoxName = new TextBox();
	TextArea textAreaDescription = new TextArea();
	TextBox textBoxUrl = new TextBox();
	TextBox textBoxLogin = new TextBox();
	TextBox textBoxPassword = new TextBox();
	DatePicker datePickerLastVisit = new DatePicker();
	UserJobSite currentSite = null;
	
	private void setUserJobSite(UserJobSite site)
	{
		textBoxName.setValue(site.name);
		textAreaDescription.setValue(site.description);
		textBoxUrl.setValue(site.URL);
		textBoxLogin.setValue(site.login);
		textBoxPassword.setValue(site.password);
		datePickerLastVisit.setValue(site.lastVisit);
		currentSite = site;
	}
	
	UserJobSite getUserJobSite()
	{
		if (currentSite == null) return null;
		UserJobSite result = new UserJobSite();
		result.ID = currentSite.ID;
		result.name = textBoxName.getValue();
		result.description = textAreaDescription.getValue();
		result.URL = textBoxUrl.getValue();
		result.login = textBoxLogin.getValue();
		result.password = textBoxPassword.getValue();
		result.lastVisit = datePickerLastVisit.getValue();
		return result;
	}

	
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

	// The list of data to display.
	private List<UserJobSite> jobSiteList = new ArrayList<UserJobSite>();

	// Create a data provider.
	AsyncDataProvider<UserJobSite> dataProvider = new AsyncDataProvider<UserJobSite>() {
		@Override
		protected void onRangeChanged(HasData<UserJobSite> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= jobSiteList.size() ) end = jobSiteList.size();
			if (jobSiteList.size() != 0)
			{
				List<UserJobSite> dataInRange = jobSiteList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
			}
		}
	};

	void getOneSite(String siteId)
	{
		AsyncCallback<UserJobSite> callback = new AsyncCallback<UserJobSite>()	{
			@Override
			public void onFailure(Throwable caught)
			{
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(UserJobSite result)
			{
				System.out.println(result);
				// Find position.
				for (int count=0; count != jobSiteList.size(); count++) {
					if (jobSiteList.get(count).ID.equals(result.ID))
					{
						jobSiteList.set(count, result);
					}
				}
				dataProvider.updateRowData(0, jobSiteList);
			}
		};
		try {
			userService.getUserSite(user, siteId, callback);
		} catch (CassandraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void getAllContent()
	{		
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(List<String> result) {
				System.out.println(result);
				jobSiteList.clear();
				for (String idRes: result)
				{
					jobSiteList.add(new UserJobSite(idRes,"", "", "", "", "" , new Date()));
					getOneSite(idRes);
				}
				dataProvider.updateRowCount(jobSiteList.size(), true);
				cellTable.redraw();
			}
		};
		try {
			userService.getUserSiteList(user, callback);
		} catch (CassandraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	class DeleteHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			try {
				userService.deleteUserSite(user, currentSite.ID, new AsyncCallback<Integer>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						Window.alert(caught.toString());
						//connectButton.setEnabled(true);
					}
					public void onSuccess(Integer result)
					{
						getAllContent();
					}
				});
			} 
			catch (CassandraException e) 
			{
				Window.alert(e.toString());
			}
		}
	}
	
	class SaveHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			try {
				userService.setUserSite(user, getUserJobSite(), new AsyncCallback<Integer>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						Window.alert(caught.toString());
						//connectButton.setEnabled(true);
					}
					public void onSuccess(Integer result)
					{
						getAllContent();
					}
				});
			} 
			catch (CassandraException e) 
			{
				Window.alert(e.toString());
			}
		}
	}

	class NewSiteHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			try {
				userService.addUserSite(user, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						Window.alert(caught.toString());
						//connectButton.setEnabled(true);
					}
					public void onSuccess(String result)
					{
						System.out.println("Created site: " + result);
						getAllContent();
					}
				});
			} 
			catch (CassandraException e) 
			{
				Window.alert(e.toString());
			}
		}
	}


	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{		
		Lang lang = GWT.create(Lang.class);
		System.out.println("Load Content User Site, locale is: " + LocaleInfo.getCurrentLocale().getLocaleName());				

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel rootPanel = RootPanel.get("centercontent");
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "100%");
		rootPanel.add(simplePanelCenter);
		//cellTable
		//cellTable.setRowCount(5);
		//cellTable.setVisibleRange(0, 3);

		// Create name column.
		TextColumn<UserJobSite> nameColumn = new TextColumn<UserJobSite>() 	{
			@Override
			public String getValue(UserJobSite site) 
			{
				return site.name;
			}
		};

		// Create description column.
		TextColumn<UserJobSite> descriptionColumn = new TextColumn<UserJobSite>() {
			@Override
			public String getValue(UserJobSite site) 
			{
				return site.description;
			}
		};

		// Create URL column.
		TextColumn<UserJobSite> URLColumn = new TextColumn<UserJobSite>() {
			@Override
			public String getValue(UserJobSite site) 
			{
				return site.URL;
			}
		};

		// Create login column.
		TextColumn<UserJobSite> loginColumn = new TextColumn<UserJobSite>() {
			@Override
			public String getValue(UserJobSite site) 
			{
				return site.login;
			}
		};

		// Create password column.
		TextColumn<UserJobSite> passwordColumn = new TextColumn<UserJobSite>() {
			@Override
			public String getValue(UserJobSite site) 
			{
				return site.password;
			}
		};

		// Create lastVisit column.
		TextColumn<UserJobSite> lastVisitColumn = new TextColumn<UserJobSite>() {
			@SuppressWarnings("deprecation")
			@Override
			public String getValue(UserJobSite site) 
			{
				return site.lastVisit.toLocaleString();
			}
		};

		nameColumn.setSortable(true);
		descriptionColumn.setSortable(true);
		URLColumn.setSortable(true);
		loginColumn.setSortable(true);
		passwordColumn.setSortable(true);
		lastVisitColumn.setSortable(true);
		cellTable.addColumn(nameColumn, lang._TextName());
		cellTable.addColumn(descriptionColumn, lang._TextDescription());
		cellTable.addColumn(URLColumn, lang._TextURL());
		cellTable.addColumn(loginColumn, lang._TextLogin());
		cellTable.addColumn(passwordColumn, lang._TextPassword());
		cellTable.addColumn(lastVisitColumn, lang._TextLastVisit());
		cellTable.getColumnSortList().push(nameColumn);	

		// Add a selection model to handle user selection.
		final SingleSelectionModel<UserJobSite> selectionModel = new SingleSelectionModel<UserJobSite>();
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{
			public void onSelectionChange(SelectionChangeEvent event) 
			{
				UserJobSite selected = selectionModel.getSelectedObject();
				if (selected != null) 
				{
					setUserJobSite(selected);
					//Window.alert("You selected: " + selected.name);
				}
			}
		});
		
		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(jobSiteList.size(), true);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		getAllContent();
		cellTable.setRowData(0, jobSiteList);
		cellTable.setRowCount(jobSiteList.size(), true);
		cellTable.setVisibleRange(0, 5);
		cellTable.addColumnSortHandler(columnSortHandler);
		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");		
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");
		
		Button buttonNewSite = new Button(lang._TextSave());
		horizontalPanel.add(buttonNewSite);
		buttonNewSite.setText(lang._TextNewSite());
		
		Button buttonDeleteSite = new Button("");
		buttonDeleteSite.setText(lang._TextDeleteSite());
		horizontalPanel.add(buttonDeleteSite);
		
		SimplePanel simplePanel = new SimplePanel();
		simplePanelCenter.add(simplePanel);
		simplePanel.setHeight("40px");
		
		Grid grid = new Grid(7, 2);
		simplePanelCenter.add(grid);
		simplePanelCenter.setCellHeight(grid, "100%");
		simplePanelCenter.setCellWidth(grid, "100%");
		grid.setSize("100%", "");
		
		Label lblName = new Label(lang._TextName());
		grid.setWidget(0, 0, lblName);
		
		grid.setWidget(0, 1, textBoxName);
		textBoxName.setWidth("100%");
		
		Label lblDescription = new Label(lang._TextDescription());
		grid.setWidget(1, 0, lblDescription);
		
		grid.setWidget(1, 1, textAreaDescription);
		textAreaDescription.setSize("100%", "50px");
		
		Label lblUrl = new Label(lang._TextURL());
		grid.setWidget(2, 0, lblUrl);
		
		grid.setWidget(2, 1, textBoxUrl);
		
		Label lblLogin = new Label(lang._TextLogin());
		grid.setWidget(3, 0, lblLogin);
		
		grid.setWidget(3, 1, textBoxLogin);
		
		Label lblPassword = new Label(lang._TextPassword());
		grid.setWidget(4, 0, lblPassword);
		
		grid.setWidget(4, 1, textBoxPassword);
		
		Label lblLastvisit = new Label(lang._TextLastVisit());
		grid.setWidget(5, 0, lblLastvisit);
		
		grid.setWidget(5, 1, datePickerLastVisit);
		
		Button buttonSave = new Button(lang._TextSave());
		grid.setWidget(6, 0, buttonSave);
		buttonSave.setWidth("150px");
		grid.getCellFormatter().setHorizontalAlignment(6, 0, HasHorizontalAlignment.ALIGN_LEFT);
		
		// Add a handler to the delete button.
		DeleteHandler deleteHandler = new DeleteHandler();
		buttonDeleteSite.addClickHandler(deleteHandler);
		
		// Add a handler to the save button.
		SaveHandler saveHandler = new SaveHandler();
		buttonSave.addClickHandler(saveHandler);
		
		NewSiteHandler newHandler = new NewSiteHandler();
		buttonNewSite.addClickHandler(newHandler);
	}
}
