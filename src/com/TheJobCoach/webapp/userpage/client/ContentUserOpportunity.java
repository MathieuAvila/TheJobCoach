package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.EditOpportunity.EditOpportunityResult;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserOpportunity implements EntryPoint {

	UserId user;

	final ExtendedCellTable<UserOpportunity> cellTable = new ExtendedCellTable<UserOpportunity>();
	UserOpportunity currentOpportunity = null;
	final HTML panelDescriptionContent = new HTML("");

	final Lang lang = GWT.create(Lang.class);

	private void setUserOpportunity(UserOpportunity opp)
	{
		AsyncCallback<UserOpportunity> callback = new AsyncCallback<UserOpportunity>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(UserOpportunity result) {
				currentOpportunity = result;
				panelDescriptionContent.setHTML(currentOpportunity.description);
				cellTable.redraw();				
			}
		};
		try {
			userService.getUserOpportunity(user, opp.ID, callback);
		}
		catch (CassandraException e) 
		{
			e.printStackTrace();
		}		
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
	private Vector<UserOpportunity> userOpportunityList = new Vector<UserOpportunity>();

	// Create a data provider.
	AsyncDataProvider<UserOpportunity> dataProvider = new AsyncDataProvider<UserOpportunity>() {
		@Override
		protected void onRangeChanged(HasData<UserOpportunity> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= userOpportunityList.size() ) end = userOpportunityList.size();
			if (userOpportunityList.size() != 0)
			{
				List<UserOpportunity> dataInRange = userOpportunityList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
			}
		}
	};

	void getAllContent()
	{		
		AsyncCallback<Vector<UserOpportunity>> callback = new AsyncCallback<Vector<UserOpportunity>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Vector<UserOpportunity> result) {
				System.out.println(result);
				userOpportunityList.clear();
				userOpportunityList.addAll(result);
				dataProvider.updateRowCount(userOpportunityList.size(), true);
				dataProvider.updateRowData(0, userOpportunityList.subList(0, userOpportunityList.size()));
				cellTable.redraw();				
			}
		};
		try {
			userService.getUserOpportunityShortList(user, "managed", callback);
		}
		catch (CassandraException e) 
		{
			e.printStackTrace();
		}
	}

	private void delete(final UserOpportunity opp)
	{
		MessageBox mb = new MessageBox(
				rootPanel, true, true, MessageBox.TYPE.QUESTION, "Delete opportunity ?", 
				"Are you sure you wish to delete opportunity " + opp.title, new MessageBox.ICallback() {
					@Override
					public void complete(boolean ok) {
						if (ok == true)
						{
							try {
								userService.deleteUserOpportunity(user, opp.ID, new AsyncCallback<String>() {
									public void onFailure(Throwable caught) {
										// Show the RPC error message to the user
										Window.alert(caught.toString());
										//connectButton.setEnabled(true);
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
						}}});
		mb.onModuleLoad();
	}

	class NewOpportunityHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			EditOpportunity eus = new EditOpportunity(rootPanel, user, null, new EditOpportunityResult() {

				@Override
				public void setResult(UserOpportunity result) {
					try 
					{
						if (result != null)
						{
							result.ID = new Date().toString();
							userService.setUserOpportunity(user, "managed", result, new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									// Show the RPC error message to the user
									Window.alert(caught.toString());
									//connectButton.setEnabled(true);
								}
								public void onSuccess(String result)
								{
									System.out.println("Created opp: " + result);
									getAllContent();
								}
							});
						}
					}
					catch (CassandraException e)
					{
						System.out.println(e);
					}
				}
			});
			eus.onModuleLoad();
		}
	}
	
	private void updateUserOpportunity(UserOpportunity opp)
	{
		EditOpportunity eus = new EditOpportunity(rootPanel, user, opp, new EditOpportunityResult() {
			@Override
			public void setResult(UserOpportunity result) {
				try 
				{
					if (result != null)
					{
						userService.setUserOpportunity(user, "managed", result, new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								Window.alert(caught.toString());
								//connectButton.setEnabled(true);
							}
							public void onSuccess(String result)
							{
								System.out.println("Updated opp: " + result);
								getAllContent();
							}
						});
					}
				}
				catch (CassandraException e)
				{
					System.out.println(e);
				}
			}
		});
		eus.onModuleLoad();
	}

	void editLog(UserOpportunity opp)
	{
		ContentUserLog cul = new ContentUserLog(rootPanel, user, opp);		
		cul.onModuleLoad();		
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

		ContentHelper.insertTitlePanel(simplePanelCenter, lang.lblOpportunities_text(), ClientImageBundle.INSTANCE.opportunityContent());


		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<UserOpportunity, String>() {
			@Override
			public void update(int index, UserOpportunity object, String value) {
				delete(object);
			}});

		cellTable.addColumnWithIcon(IconCellSingle.IconType.UPDATE, new FieldUpdater<UserOpportunity, String>() {
			@Override
			public void update(int index, UserOpportunity object, String value) {
				updateUserOpportunity(object);
			}});
		
		cellTable.addColumnUrl(new ExtendedCellTable.GetValue<String, UserOpportunity>() {
			public String getValue(UserOpportunity contact) {
				return contact.url;
			}
		});

		// Create title column.
		TextColumn<UserOpportunity> titleColumn = new TextColumn<UserOpportunity>() 	{
			@Override
			public String getValue(UserOpportunity userOpportunity) 
			{
				return userOpportunity.title;
			}
		};

		// Create description column.
		TextColumn<UserOpportunity> companyColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity userOpportunity) 
			{
				return userOpportunity.companyId;
			}
		};

		// Create status column.
		TextColumn<UserOpportunity> statusColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity userOpportunity) 
			{
				return lang.applicationStatusMap().get("ApplicationStatus_" + UserOpportunity.applicationStatusToString(userOpportunity.status));
			}
		};

		// Create location column.
		TextColumn<UserOpportunity> locationColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity userOpportunity) 
			{
				return userOpportunity.location;
			}
		};

		// Create salary column.
		TextColumn<UserOpportunity> salaryColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity userOpportunity) 
			{
				return Integer.toString(userOpportunity.salary);
			}
		};

		// Create contract type column.
		TextColumn<UserOpportunity> contractTypeColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity userOpportunity) 
			{
				return userOpportunity.contractType;
			}
		};

		// Create last visit column.
		TextColumn<UserOpportunity> firstSeenColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity userOpportunity) 
			{
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(userOpportunity.firstSeen);				
			}
		};

		titleColumn.setSortable(true);
		companyColumn.setSortable(true);
		locationColumn.setSortable(true);
		salaryColumn.setSortable(true);
		contractTypeColumn.setSortable(true);
		firstSeenColumn.setSortable(true);

		cellTable.addColumn(titleColumn, lang._TextName());
		cellTable.addColumn(companyColumn, lang._TextCompany());
		cellTable.addColumn(statusColumn, lang._TextStatus());
		cellTable.addColumn(locationColumn, lang._TextLocation());
		cellTable.addColumn(salaryColumn, lang._TextSalary());
		cellTable.addColumn(contractTypeColumn, lang._TextContractType());
		cellTable.addColumn(firstSeenColumn, lang._TextFirstSeen());
		cellTable.getColumnSortList().push(titleColumn);	
		cellTable.setStyleName("filecelltable");


		cellTable.addColumnWithIcon(IconCellSingle.IconType.RIGHT, new FieldUpdater<UserOpportunity, String>() {
			@Override
			public void update(int index, UserOpportunity object, String value) {
				editLog(object);
			}});

		// Add a selection model to handle user selection.
		final SingleSelectionModel<UserOpportunity> selectionModel = new SingleSelectionModel<UserOpportunity>();
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{
			public void onSelectionChange(SelectionChangeEvent event) 
			{
				UserOpportunity selected = selectionModel.getSelectedObject();
				if (selected != null) 
				{
					setUserOpportunity(selected);					
				}
			}
		});

		dataProvider.addDataDisplay(cellTable);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		cellTable.setRowData(0, userOpportunityList);
		cellTable.setRowCount(userOpportunityList.size(), true);
		cellTable.setVisibleRange(0, 20);
		cellTable.addColumnSortHandler(columnSortHandler);

		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");		

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

		ButtonImageText buttonNewOpportunity = new ButtonImageText(ButtonImageText.Type.NEW, lang._TextNewOpportunity());
		horizontalPanel.add(buttonNewOpportunity);

		HTML htmlDescriptionhtml = new HTML("", true);
		simplePanelCenter.add(htmlDescriptionhtml);
		htmlDescriptionhtml.setSize("100%", "100%");

		SimplePanel simplePanel = new SimplePanel();
		simplePanelCenter.add(simplePanel);
		simplePanel.setHeight("10px");

		Grid grid_1 = new Grid(2, 2);
		simplePanelCenter.add(grid_1);
		grid_1.setWidth("100%");

		Label lblSource = new Label(lang._TextSource());
		lblSource.setStyleName("summary-title");
		grid_1.setWidget(0, 0, lblSource);

		Label label_1 = new Label((String) null);
		label_1.setStyleName("summary-text");
		grid_1.setWidget(0, 1, label_1);
		label_1.setWidth("100%");

		Label labelCreated = new Label(lang._TextCreated());
		labelCreated.setStyleName("summary-title");
		grid_1.setWidget(1, 0, labelCreated);

		Label labelCreatedContent = new Label((String) null);
		labelCreatedContent.setStyleName("summary-text");
		grid_1.setWidget(1, 1, labelCreatedContent);

		Label labelDescription = new Label(lang._TextDescription());
		labelDescription.setStyleName("summary-title");
		simplePanelCenter.add(labelDescription);

		simplePanelCenter.add(panelDescriptionContent);	

		// Add a handler to the new button.
		NewOpportunityHandler newHandler = new NewOpportunityHandler();
		buttonNewOpportunity.addClickHandler(newHandler);

		getAllContent();		
	}
}
