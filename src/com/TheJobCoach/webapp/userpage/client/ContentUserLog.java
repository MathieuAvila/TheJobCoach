package com.TheJobCoach.webapp.userpage.client;

import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.EditLogEntry.EditLogEntryResult;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserLog implements EntryPoint {

	UserId user;

	final CellTable<UserLogEntry> cellTable = new CellTable<UserLogEntry>();

	final Lang lang = GWT.create(Lang.class);
	UserOpportunity editedOpportunity;
	UserLogEntry currentLogEntry;

	public ContentUserLog(Panel panel, UserId _user, UserOpportunity opp)
	{
		rootPanel = panel;
		editedOpportunity = opp;
		user = _user;
	}

	private final static UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	private void setUserLogEntry(UserLogEntry selected)
	{
		currentLogEntry = selected;
	}

	// The list of data to display.
	private Vector<UserLogEntry> UserLogEntryList = new Vector<UserLogEntry>();

	// Create a data provider.
	AsyncDataProvider<UserLogEntry> dataProvider = new AsyncDataProvider<UserLogEntry>() {
		@Override
		protected void onRangeChanged(HasData<UserLogEntry> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= UserLogEntryList.size() ) end = UserLogEntryList.size();
			if (UserLogEntryList.size() != 0)
			{
				List<UserLogEntry> dataInRange = UserLogEntryList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
			}
		}
	};

	void getAllContent()
	{		
		AsyncCallback<Vector<UserLogEntry>> callback = new AsyncCallback<Vector<UserLogEntry>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Vector<UserLogEntry> result) {
				UserLogEntryList.clear();
				UserLogEntryList.addAll(result);
				dataProvider.updateRowCount(UserLogEntryList.size(), true);
				dataProvider.updateRowData(0, UserLogEntryList.subList(0, UserLogEntryList.size()));
				cellTable.redraw();				
			}
		};
		try {
			userService.getUserLogEntryList(user, editedOpportunity.ID, callback);
		}
		catch (CassandraException e) 
		{
			e.printStackTrace();
		}
	}


	void deleteLogEntry(final UserLogEntry currentLogEntry)
	{
		MessageBox mb = new MessageBox(
				rootPanel, true, true, MessageBox.TYPE.QUESTION, "Delete user log entry ?", 
				"Are you sure you wish to delete user log entry " + currentLogEntry.title, new MessageBox.ICallback() {
					@Override
					public void complete(boolean ok) {
						if (ok == true)
						{
							try {
								userService.deleteUserLogEntry(user, currentLogEntry.ID, new AsyncCallback<String>() {
									public void onFailure(Throwable caught) {
										MessageBox.messageBoxException(rootPanel, caught.toString());
									}
									public void onSuccess(String result)
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
					}});
		mb.onModuleLoad();
	}


	class NewLogEntryHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			EditLogEntry eus = new EditLogEntry(rootPanel, null, editedOpportunity.ID, user, new EditLogEntryResult() 
			{
				@Override
				public void setResult(UserLogEntry result) {
					System.out.println("Commited new log entry " + result);
					if (result != null)
					{
						getAllContent();
					}
				}
			});
			eus.onModuleLoad();
		}
	}


	void updateLogEntry(UserLogEntry currentLogEntry)
	{
		EditLogEntry eus = new EditLogEntry(rootPanel, currentLogEntry, editedOpportunity.ID, user, new EditLogEntryResult() {
			@Override
			public void setResult(UserLogEntry result) {
				System.out.println("Updated new log entry " + result);
				if (result != null)
				{					
					getAllContent();
				}
			}
		});
		eus.onModuleLoad();
	}	

	private <C> Column<UserLogEntry, C> addColumn(Cell<C> cell,final GetValue<C> getter, FieldUpdater<UserLogEntry, C> fieldUpdater) 
	{
		Column<UserLogEntry, C> column = new Column<UserLogEntry, C>(cell) 
				{

			@Override
			public C getValue(UserLogEntry object) 
			{
				return getter.getValue(object);
			}
				};
				column.setFieldUpdater(fieldUpdater);

				return column;
	}


	private static interface GetValue<C> {
		C getValue(UserLogEntry contact);
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
		simplePanelCenter.setSize("100%", "");
		rootPanel.add(simplePanelCenter);
		
		ContentHelper.insertTitlePanel(simplePanelCenter, lang._Text_EditLog(), ClientImageBundle.INSTANCE.userLogContent());
		
		// Create title column.
		TextColumn<UserLogEntry> titleColumn = new TextColumn<UserLogEntry>() 	{
			@Override
			public String getValue(UserLogEntry userLog) 
			{
				return userLog.title;
			}
		};

		// Create status column.
		TextColumn<UserLogEntry> statusColumn = new TextColumn<UserLogEntry>() {
			@Override
			public String getValue(UserLogEntry userLog) 
			{
				System.out.println("USER LOG TYPE " + userLog.type);
				return lang.logEntryStatusMap().get("logEntryStatus_" + UserLogEntry.entryTypeToString(userLog.type));
			}
		};

		// Create created column.
		TextColumn<UserLogEntry> createdColumn = new TextColumn<UserLogEntry>() {
			@Override
			public String getValue(UserLogEntry userLog) 
			{
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(userLog.creation);
			}
		};

		// Create expectedFollowUp column.
		TextColumn<UserLogEntry> expectedFollowUpColumn = new TextColumn<UserLogEntry>() {
			@Override
			public String getValue(UserLogEntry userLog) 
			{
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(userLog.expectedFollowUp);
			}
		};

		IconCellSingle deleteCell =	new IconCellSingle(IconCellSingle.IconType.DELETE);		
		cellTable.addColumn(addColumn(deleteCell, new GetValue<String>() {
			public String getValue(UserLogEntry contact) {
				return "&nbsp;";//contact.fileName;
			}
		},
		new FieldUpdater<UserLogEntry, String>() {
			public void update(int index, UserLogEntry object, String value) {				
				deleteLogEntry(object);
			}
		}), lang._TextDeleteUserDocument());

		IconCellSingle updateCell =	new IconCellSingle(IconCellSingle.IconType.UPDATE);		
		cellTable.addColumn(addColumn(updateCell, new GetValue<String>() {
			public String getValue(UserLogEntry contact) {
				return "&nbsp;";//contact.fileName;
			}
		},
		new FieldUpdater<UserLogEntry, String>() {
			public void update(int index, UserLogEntry object, String value) {
				updateLogEntry(object);
			}
		}), lang._TextUpdateUserDocument());


		titleColumn.setSortable(true);
		statusColumn.setSortable(true);
		createdColumn.setSortable(true);
		expectedFollowUpColumn.setSortable(true);
		cellTable.setStyleName("filecelltable");
		cellTable.addColumn(titleColumn, lang._TextName());
		cellTable.addColumn(statusColumn, lang._TextStatus());
		cellTable.addColumn(createdColumn, lang._TextCreated());
		cellTable.addColumn(expectedFollowUpColumn, lang._TextExpectedFollowUp());

		// Add a selection model to handle user selection.
		final SingleSelectionModel<UserLogEntry> selectionModel = new SingleSelectionModel<UserLogEntry>();
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{
			public void onSelectionChange(SelectionChangeEvent event) 
			{
				UserLogEntry selected = selectionModel.getSelectedObject();
				if (selected != null) 
				{
					setUserLogEntry(selected);					
				}
			}
		});

		dataProvider.addDataDisplay(cellTable);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		cellTable.setRowData(0, UserLogEntryList);
		cellTable.setRowCount(UserLogEntryList.size(), true);
		cellTable.setVisibleRange(0, 20);
		cellTable.addColumnSortHandler(columnSortHandler);

		SimplePanel simplePanel_1 = new SimplePanel();
		simplePanel_1.setStyleName("#closeButton");
		simplePanelCenter.add(simplePanel_1);
		simplePanel_1.setHeight("10px");

		Grid grid_1 = new Grid(3, 2);
		simplePanelCenter.add(grid_1);

		Label lblTitle = new Label(lang._TextName());
		lblTitle.setStyleName("summary-title");
		grid_1.setWidget(0, 0, lblTitle);

		Label lblTitleContent = new Label(editedOpportunity.title);
		lblTitleContent.setStyleName("summary-text");
		grid_1.setWidget(0, 1, lblTitleContent);

		Label labelStatus = new Label(lang._TextStatus());
		labelStatus.setStyleName("summary-title");
		grid_1.setWidget(1, 0, labelStatus);

		Label label_1 = new Label(lang.applicationStatusMap().get("ApplicationStatus_" + UserOpportunity.applicationStatusToString(editedOpportunity.status)));
		label_1.setStyleName("summary-text");
		grid_1.setWidget(1, 1, label_1);

		Label labelCompany = new Label(lang._TextCompany());
		labelCompany.setStyleName("summary-title");
		grid_1.setWidget(2, 0, labelCompany);

		Label labelCompanyContent = new Label(editedOpportunity.companyId);
		labelCompanyContent.setStyleName("summary-text");
		grid_1.setWidget(2, 1, labelCompanyContent);

		SimplePanel simplePanel = new SimplePanel();
		simplePanelCenter.add(simplePanel);
		simplePanel.setHeight("20px");
		
		ButtonImageText buttonBack = new ButtonImageText(ButtonImageText.Type.BACK, lang._Text_BackToOpportunityList());
		buttonBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ContentUserOpportunity contentUserOpportunity = new ContentUserOpportunity();
				contentUserOpportunity.setRootPanel(rootPanel);
				contentUserOpportunity.setUserParameters(user);
				contentUserOpportunity.onModuleLoad();		
			}
			
		});
		
		simplePanelCenter.add(buttonBack);

		SimplePanel simplePanel_2 = new SimplePanel();
		simplePanelCenter.add(simplePanel_2);
		simplePanel_2.setHeight("20px");

		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");		

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

		Button buttonNewLogEntry = new ButtonImageText(ButtonImageText.Type.NEW, lang._TextNewLogEntry());	
		horizontalPanel.add(buttonNewLogEntry);

		HTML htmlDescriptionhtml = new HTML("", true);
		simplePanelCenter.add(htmlDescriptionhtml);
		htmlDescriptionhtml.setSize("100%", "100%");

		Grid grid = new Grid(2, 1);
		simplePanelCenter.add(grid);
		grid.setWidth("100%");

		Label lblSource = new Label(lang._TextSource());
		grid.setWidget(0, 0, lblSource);

		// Add a handler to the new button.
		NewLogEntryHandler newHandler = new NewLogEntryHandler();
		buttonNewLogEntry.addClickHandler(newHandler);

		getAllContent();		
	}
}
