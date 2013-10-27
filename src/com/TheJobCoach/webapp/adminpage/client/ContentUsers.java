package com.TheJobCoach.webapp.adminpage.client;

import java.util.ArrayList;
import java.util.List;

import com.TheJobCoach.webapp.adminpage.shared.UserReport;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUsers implements EntryPoint {

	UserId user;
	
	private final AdminServiceAsync adminService = GWT.create(AdminService.class);

	Panel rootPanel;

	public ContentUsers(Panel panel, UserId _user)
	{
		rootPanel = panel;
		user = _user;
	}

	// The list of data to display.
	private List<UserReport> userList = new ArrayList<UserReport>();
	
	final CellTable<UserReport> cellTable = new CellTable<UserReport>();
	
	// Create a data provider.
	AsyncDataProvider<UserReport> dataProvider = new AsyncDataProvider<UserReport>() {
		
		@Override
		protected void onRangeChanged(HasData<UserReport> display) {
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= userList.size() ) end = userList.size();
			if (userList.size() != 0)
			{
				List<UserReport> dataInRange = userList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
			}
		}
	};
	
	void getAllContent()
	{		
		ServerCallHelper<List<UserReport>> callback = new ServerCallHelper<List<UserReport>>(rootPanel) {
			@Override
			public void onSuccess(List<UserReport> result) {
				userList = result;
				cellTable.setVisibleRange(0, userList.size());
				dataProvider.updateRowData(0, userList);
				dataProvider.updateRowCount(userList.size(), true);
				cellTable.redraw();
			}
		};
		adminService.getUserReportList(user, callback);
	}


	private <C> Column<UserReport, C> addColumn(Cell<C> cell,final GetValue<C> getter, FieldUpdater<UserReport, C> fieldUpdater) 
	{
		Column<UserReport, C> column = new Column<UserReport, C>(cell) 
				{

			@Override
			public C getValue(UserReport object) 
			{
				return getter.getValue(object);
			}
				};
				column.setFieldUpdater(fieldUpdater);

				return column;
	}


	private static interface GetValue<C> {
		C getValue(UserReport contact);
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
		
		// Create password column.
		TextColumn<UserReport> passwordColumn = new TextColumn<UserReport>() 	{
			@Override
			public String getValue(UserReport report) 
			{
				return report.password;
			}
		};
		passwordColumn.setSortable(true);
		cellTable.addColumn(passwordColumn, "Password");
		
		// Create mail column.
				TextColumn<UserReport> mailColumn = new TextColumn<UserReport>() 	{
					@Override
					public String getValue(UserReport report) 
					{
						return report.mail;
					}
				};
				mailColumn.setSortable(true);
				cellTable.addColumn(mailColumn, "email");
				
		// Create type column.
		TextColumn<UserReport> typeColumn = new TextColumn<UserReport>() 	{
			@Override
			public String getValue(UserReport report) 
			{
				return report.type.toString();
			}
		};
		typeColumn.setSortable(true);
		cellTable.addColumn(typeColumn, "Type");

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
			@SuppressWarnings("deprecation")
			@Override
			public String getValue(UserReport report) 
			{
				return report.creationDate.toGMTString();
			}
		};
		createdColumn.setSortable(true);
		cellTable.addColumn(createdColumn, "Created on");
		

		IconCellSingle deleteCell =	new IconCellSingle(IconCellSingle.IconType.DELETE);		
		cellTable.addColumn(addColumn(deleteCell, new GetValue<String>() {
			public String getValue(UserReport contact) {
				return "&nbsp;";//contact.fileName;
			}
		},
		new FieldUpdater<UserReport, String>() {
			public void update(int index, UserReport object, String value) {				
				deleteUser(object);
			}
		}), "Delete user");

		
		// Add a selection model to handle user selection.
		final SingleSelectionModel<UserReport> selectionModel = new SingleSelectionModel<UserReport>();
		cellTable.setSelectionModel(selectionModel);		
		
		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(userList.size(), true);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		getAllContent();
		cellTable.setRowData(0, userList);
		cellTable.setRowCount(userList.size(), true);
		cellTable.setVisibleRange(0, userList.size());
		cellTable.addColumnSortHandler(columnSortHandler);
		cellTable.setStyleName("filecelltable");		
		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");		
		
	}


	protected void deleteUser(final UserReport object) {
		MessageBox mb = new MessageBox(
				rootPanel, true, true, MessageBox.TYPE.QUESTION, "delete", 
				"Confirm delete user: " + object.userName, new MessageBox.ICallback() {
					
					@Override
					public void complete(boolean ok) {
						if (ok == true)
						{
							ServerCallHelper<String> callback = new ServerCallHelper<String>(rootPanel) {
								@Override
								public void onSuccess(String result) {
									getAllContent();
								}
							};
							adminService.deleteUser(user,  object.userName, callback);		
							dataProvider.updateRowCount(userList.size(), true);
							cellTable.redraw();							
						}
					}
				});
		mb.onModuleLoad();
	}
}
