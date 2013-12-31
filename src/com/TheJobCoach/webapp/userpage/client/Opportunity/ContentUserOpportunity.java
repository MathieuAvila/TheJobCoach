package com.TheJobCoach.webapp.userpage.client.Opportunity;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.Range;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserOpportunity implements EntryPoint, IContentUserOpportunity {

	UserId user;

	final ExtendedCellTable<UserOpportunity> cellTable = new ExtendedCellTable<UserOpportunity>();
	UserOpportunity currentOpportunity = null;
	final HTML panelDescriptionContent = new HTML("");
	final Label labelTextSource = new Label();
	final Label labelCreationDate = new Label();
	final Label labelStartDate = new Label();
	final Label labelEndDate = new Label();

	final Lang lang = GWT.create(Lang.class);
	final LangLogEntry langLogEntry = GWT.create(LangLogEntry.class);

	ButtonImageText buttonNewOpportunity;
	ButtonImageText buttonFeedOpportunity;

	private IEditDialogModel<UserOpportunity> editModel;
	private IContentUserLog logContent;

	private void setUserOpportunity(UserOpportunity opp)
	{
		ServerCallHelper<UserOpportunity>callback = new ServerCallHelper<UserOpportunity>(rootPanel) {
			@Override
			public void onSuccess(UserOpportunity result) {
				currentOpportunity = result;
				panelDescriptionContent.setHTML(currentOpportunity.description);
				labelTextSource.setText(currentOpportunity.source);
				System.out.println("LONG " + currentOpportunity.title + " " +currentOpportunity.lastUpdate  + " " + currentOpportunity.firstSeen);
				labelCreationDate.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(currentOpportunity.lastUpdate));
				labelStartDate.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(currentOpportunity.startDate));
				labelEndDate.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(currentOpportunity.endDate));
				cellTable.redraw();				
			}
		};
		userService.getUserOpportunity(user, opp.ID, callback);	
	}

	public ContentUserOpportunity(Panel panel, UserId _user)
	{
		rootPanel = panel;
		user = _user;
		this.editModel = new EditOpportunity();
		this.logContent = new ContentUserLog(); 
	}

	public ContentUserOpportunity(Panel panel, UserId _user, IEditDialogModel<UserOpportunity> editModel, IContentUserLog logContent)
	{
		rootPanel = panel;
		user = _user;
		this.editModel = editModel; 
		this.logContent = logContent;
	}

	public ContentUserOpportunity()
	{
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

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
		ServerCallHelper<Vector<UserOpportunity>>callback = new ServerCallHelper<Vector<UserOpportunity>>(rootPanel) {
			@Override
			public void onSuccess(Vector<UserOpportunity> result) {
				userOpportunityList.clear();
				userOpportunityList.addAll(result);
				dataProvider.updateRowCount(userOpportunityList.size(), true);
				dataProvider.updateRowData(0, userOpportunityList.subList(0, userOpportunityList.size()));
				cellTable.redraw();				
			}
		};
		userService.getUserOpportunityList(user, "managed", callback);
	}

	private void delete(final UserOpportunity opp)
	{
		MessageBox mb = new MessageBox(
				rootPanel, true, true, MessageBox.TYPE.QUESTION, langLogEntry._Text_DeleteOpportunityTitle(), 
				langLogEntry._Text_DeleteOpportunity() + opp.title, new MessageBox.ICallback() {
					@Override
					public void complete(boolean ok) {
						if (ok == true)
						{
							userService.deleteUserOpportunity(user, opp.ID, new ServerCallHelper<String>(rootPanel) {
								public void onSuccess(String result)
								{
									getAllContent();
								}
							});
						}
					}});
		mb.onModuleLoad();
	}

	class NewOpportunityHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			IEditDialogModel<UserOpportunity> eus = editModel.clone(rootPanel, user, null, new IChooseResult<UserOpportunity>() {
				public void setResult(UserOpportunity result) {
					if (result != null)
					{
						result.ID = new Date().toString();
						userService.setUserOpportunity(user, "managed", result, new ServerCallHelper<String>(rootPanel) {
							public void onSuccess(String result)
							{
								getAllContent();
							}
						});
					}
				}
			});
			eus.onModuleLoad();
		}
	}
	
	class LocalIChooseResult implements IChooseResult<UserOpportunity> 
	{
		@Override
		public void setResult(UserOpportunity result) {
			if (result != null)
			{
				userService.setUserOpportunity(user, "managed", result, new ServerCallHelper<String>(rootPanel) {
					public void onSuccess(String result)
					{
						getAllContent();
					}
				});
			}
		}
	};
	
	class FeedOpportunityHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			AutoFeed eus = new AutoFeed(rootPanel, user, new LocalIChooseResult());
			eus.onModuleLoad();
		}
	}

	private void updateUserOpportunity(UserOpportunity opp)
	{
		IEditDialogModel<UserOpportunity> eus = editModel.clone(rootPanel, user, opp, new LocalIChooseResult());
		eus.onModuleLoad();
	}

	void editLog(UserOpportunity opp)
	{
		IContentUserLog cul = logContent.clone(rootPanel, user, opp);		
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

		// Create company column.
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
				return userOpportunity.salary;
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

		// Create first seen column.
		TextColumn<UserOpportunity> firstSeenColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity userOpportunity) 
			{
				System.out.println("SHORT " + userOpportunity.lastUpdate  + " " + userOpportunity.firstSeen + " " + userOpportunity.title);
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
		//cellTable.getColumnSortList().push(titleColumn);	
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

		buttonNewOpportunity = new ButtonImageText(ButtonImageText.Type.NEW, lang._TextNewOpportunity());
		horizontalPanel.add(buttonNewOpportunity);

		buttonFeedOpportunity = new ButtonImageText(ButtonImageText.Type.FEED, langLogEntry._Text_feed_from());
		horizontalPanel.add(buttonFeedOpportunity);

		HTML htmlDescriptionhtml = new HTML("", true);
		simplePanelCenter.add(htmlDescriptionhtml);
		htmlDescriptionhtml.setSize("100%", "100%");

		SimplePanel simplePanel = new SimplePanel();
		simplePanelCenter.add(simplePanel);
		simplePanel.setHeight("10px");

		Grid grid_1 = new Grid(4, 2);
		simplePanelCenter.add(grid_1);

		Label lblSource = new Label(lang._TextSource());
		lblSource.setStyleName("summary-title");
		grid_1.setWidget(0, 0, lblSource);

		labelTextSource.setStyleName("summary-text");
		grid_1.setWidget(0, 1, labelTextSource);
		labelTextSource.setWidth("100%");

		Label labelCreated = new Label(lang._TextFirstSeen());
		labelCreated.setStyleName("summary-title");
		grid_1.setWidget(1, 0, labelCreated);

		labelCreationDate.setStyleName("summary-text");
		grid_1.setWidget(1, 1, labelCreationDate);

		Label lblStartDate = new Label(lang._TextStartDate());
		lblStartDate.setStyleName("summary-title");
		grid_1.setWidget(2, 0, lblStartDate);

		labelStartDate.setStyleName("summary-text");
		grid_1.setWidget(2, 1, labelStartDate);

		Label lblEndDate = new Label(lang._TextEndDate());
		lblEndDate.setStyleName("summary-title");
		grid_1.setWidget(3, 0, lblEndDate);

		labelEndDate.setStyleName("summary-text");
		grid_1.setWidget(3, 1, labelEndDate);

		Label labelDescription = new Label(lang._TextDescription());
		labelDescription.setStyleName("summary-title");
		simplePanelCenter.add(labelDescription);

		simplePanelCenter.add(panelDescriptionContent);	

		// Add a handler to the new button.
		NewOpportunityHandler newHandler = new NewOpportunityHandler();
		buttonNewOpportunity.addClickHandler(newHandler);

		// Add a handler to the feed button.
		FeedOpportunityHandler feedHandler = new FeedOpportunityHandler();
		buttonFeedOpportunity.addClickHandler(feedHandler);

		getAllContent();		
	}

	@Override
	public IContentUserOpportunity clone(Panel panel, UserId _user)
	{
		return new ContentUserOpportunity(panel, _user);
	}
}
