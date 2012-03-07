package com.TheJobCoach.webapp.adminpage.client;

import java.util.ArrayList;
import java.util.List;

import com.TheJobCoach.webapp.adminpage.shared.UserReport;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUsers implements EntryPoint {

	UserId user;
	
	public void setUserParameters(UserId _user)
	{
		user = _user;
	}

	private final AdminServiceAsync adminService = GWT.create(AdminService.class);

	Panel rootPanel;

	public void setRootPanel(Panel panel)
	{
		rootPanel = panel;
	}

	// The list of data to display.
	private List<UserReport> jobSiteList = new ArrayList<UserReport>();
	
	final CellTable<UserReport> cellTable = new CellTable<UserReport>();
	
	// Create a data provider.
	AsyncDataProvider<UserReport> dataProvider = new AsyncDataProvider<UserReport>() {
		
		@Override
		protected void onRangeChanged(HasData<UserReport> display) {
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= jobSiteList.size() ) end = jobSiteList.size();
			if (jobSiteList.size() != 0)
			{
				List<UserReport> dataInRange = jobSiteList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
			}
		}
	};
	
	void getAllContent()
	{		
		AsyncCallback<List<UserReport>> callback = new AsyncCallback<List<UserReport>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(List<UserReport> result) {
				System.out.println(result);
				jobSiteList = result;
				cellTable.setVisibleRange(0, jobSiteList.size());
				dataProvider.updateRowData(0, jobSiteList);
				dataProvider.updateRowCount(jobSiteList.size(), true);
				cellTable.redraw();
			}
		};
		adminService.getUserReportList(user, callback);
	}


	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel rootPanel = RootPanel.get("centercontent");
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "100%");
		rootPanel.add(simplePanelCenter);
		
		// Create name column.
		TextColumn<UserReport> nameColumn = new TextColumn<UserReport>() 	{
			@Override
			public String getValue(UserReport report) 
			{
				return report.userName;
			}
		};
		nameColumn.setSortable(true);
		cellTable.addColumn(nameColumn, "Name");
		
		// Create token column.
		TextColumn<UserReport> tokenColumn = new TextColumn<UserReport>() 	{
			@Override
			public String getValue(UserReport report) 
			{
				return report.token;
			}
		};
		tokenColumn.setSortable(true);
		cellTable.addColumn(tokenColumn, "Token");
		
		// Create validated column.
		TextColumn<UserReport> validatedColumn = new TextColumn<UserReport>() 	{
			@Override
			public String getValue(UserReport report) 
			{
				return (report.validated == true) ? "ok" : "nok";
			}
		};
		validatedColumn.setSortable(true);
		cellTable.addColumn(validatedColumn, "Validated");
		
		// Create created date column.
		TextColumn<UserReport> createdColumn = new TextColumn<UserReport>() 	{
			@Override
			public String getValue(UserReport report) 
			{
				return report.creationDate.toGMTString();
			}
		};
		createdColumn.setSortable(true);
		cellTable.addColumn(createdColumn, "Created on");
		
		
		// Add a selection model to handle user selection.
		final SingleSelectionModel<UserReport> selectionModel = new SingleSelectionModel<UserReport>();
		cellTable.setSelectionModel(selectionModel);		
		
		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(jobSiteList.size(), true);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		getAllContent();
		cellTable.setRowData(0, jobSiteList);
		cellTable.setRowCount(jobSiteList.size(), true);
		cellTable.setVisibleRange(0, jobSiteList.size());
		cellTable.addColumnSortHandler(columnSortHandler);
		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");		
		
	}
}
