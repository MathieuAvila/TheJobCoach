package com.TheJobCoach.webapp.userpage.client.Opportunity;

import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.CassandraException;
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
public class ContentUserLog implements EntryPoint, IContentUserLog {

	UserId user;

	final ExtendedCellTable<UserLogEntry> cellTable = new ExtendedCellTable<UserLogEntry>();

	final Lang lang = GWT.create(Lang.class);
	final LangLogEntry langLogEntry = GWT.create(LangLogEntry.class);
	
	UserOpportunity editedOpportunity;
	UserLogEntry currentLogEntry;

	public ContentUserLog(Panel panel, UserId _user, UserOpportunity opp)
	{
		rootPanel = panel;
		editedOpportunity = opp;
		user = _user;
	}

	public ContentUserLog()
	{
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
				rootPanel, true, true, MessageBox.TYPE.QUESTION, langLogEntry._Text_DeleteEditLogTitle(), 
				langLogEntry._Text_DeleteEditLog() + currentLogEntry.title + " ? ", new MessageBox.ICallback() {
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
			EditLogEntry eus = new EditLogEntry(rootPanel, null, editedOpportunity.ID, user, new EditLogEntry.EditLogEntryResult() 
			{
				@Override
				public void setResult(UserLogEntry result) {
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
		EditLogEntry eus = new EditLogEntry(rootPanel, currentLogEntry, editedOpportunity.ID, user, new EditLogEntry.EditLogEntryResult() {
			@Override
			public void setResult(UserLogEntry result) {
				if (result != null)
				{					
					getAllContent();
				}
			}
		});
		eus.onModuleLoad();
	}	

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{			
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "");
		rootPanel.add(simplePanelCenter);
		
		ContentHelper.insertTitlePanel(simplePanelCenter, langLogEntry._Text_EditLog(), ClientImageBundle.INSTANCE.userLogContent());
		
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
				return langLogEntry.logEntryStatusMap().get("logEntryStatus_" + UserLogEntry.entryTypeToString(userLog.type));
			}
		};

		// Create event column.
		TextColumn<UserLogEntry> createdColumn = new TextColumn<UserLogEntry>() {
			@Override
			public String getValue(UserLogEntry userLog) 
			{
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(userLog.eventDate);
			}
		};

		// Create attached files column.
		TextColumn<UserLogEntry> filesColumn = new TextColumn<UserLogEntry>() {
			@Override
			public String getValue(UserLogEntry userLog) 
			{
				return (userLog.attachedDocumentId.size()!=0) ? Integer.toString(userLog.attachedDocumentId.size()): "";
			}
		};

		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<UserLogEntry, String>() {
			@Override
			public void update(int index, UserLogEntry object, String value) {
				deleteLogEntry(object);
			}});

		cellTable.addColumnWithIcon(IconCellSingle.IconType.UPDATE, new FieldUpdater<UserLogEntry, String>() {
			@Override
			public void update(int index, UserLogEntry object, String value) {
				updateLogEntry(object);
			}});
		
		titleColumn.setSortable(true);
		statusColumn.setSortable(true);
		createdColumn.setSortable(true);
		cellTable.setStyleName("filecelltable");
		cellTable.addColumn(titleColumn, lang._TextName());
		cellTable.addColumn(statusColumn, lang._TextStatus());
		cellTable.addColumn(createdColumn, langLogEntry._TextCreated());
		cellTable.addColumn(filesColumn, langLogEntry._TextFiles());

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
		
		ButtonImageText buttonBack = new ButtonImageText(ButtonImageText.Type.BACK, langLogEntry._Text_BackToOpportunityList());
		buttonBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ContentUserOpportunity contentUserOpportunity = new ContentUserOpportunity(rootPanel, user);
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

		// Add a handler to the new button.
		NewLogEntryHandler newHandler = new NewLogEntryHandler();
		buttonNewLogEntry.addClickHandler(newHandler);

		getAllContent();		
	}


	@Override
	public IContentUserLog clone(Panel panel, UserId _user, UserOpportunity opp)
	{
		return new ContentUserLog(panel, _user, opp);
	}
}
