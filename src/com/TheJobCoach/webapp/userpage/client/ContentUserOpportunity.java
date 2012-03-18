package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.EditOpportunity.EditOpportunityResult;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.cellview.client.CellTable;
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

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserOpportunity implements EntryPoint {

	UserId user;

	final CellTable<UserOpportunity> cellTable = new CellTable<UserOpportunity>();
	UserOpportunity currentOpportunity = null;
	
	final Lang lang = GWT.create(Lang.class);
	
	private void setUserOpportunity(UserOpportunity opp)
	{
		currentOpportunity = opp;
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

	void getOneOpportunity(String opportunityId)
	{
		AsyncCallback<UserOpportunity> callback = new AsyncCallback<UserOpportunity>()	{
			@Override
			public void onFailure(Throwable caught)
			{
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(UserOpportunity result)
			{
				System.out.println(result);
				// Find position.
				for (int count=0; count != userOpportunityList.size(); count++) {
					if (userOpportunityList.get(count).ID.equals(result.ID))
					{
						userOpportunityList.set(count, result);
					}
				}
				dataProvider.updateRowData(0, userOpportunityList);
			}
		};
	}

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


	class DeleteHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			try {
				userService.deleteUserOpportunity(user, currentOpportunity.ID, new AsyncCallback<String>() {
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
		}
	}

	class NewOpportunityHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			EditOpportunity eus = new EditOpportunity();
			UserOpportunity ujb = new UserOpportunity();
			eus.setRootPanel(rootPanel, ujb, new EditOpportunityResult() {

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
			}, lang._TextNewOpportunity());
			eus.onModuleLoad();
		}
	}


	class UpdateOpportunity implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			EditOpportunity eus = new EditOpportunity();
			eus.setRootPanel(rootPanel, currentOpportunity, new EditOpportunityResult() {
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
			}, lang._TextUpdateOpportunity());
			eus.onModuleLoad();
		}
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
		
		InlineHTML lblOpportunities = new InlineHTML();
		lblOpportunities.setHTML("<h2>" + lang.lblOpportunities_text() + "</h2>" );
		simplePanelCenter.add(lblOpportunities);
		
		// Create title column.
		TextColumn<UserOpportunity> titleColumn = new TextColumn<UserOpportunity>() 	{
			@Override
			public String getValue(UserOpportunity site) 
			{
				return site.title;
			}
		};

		// Create description column.
		TextColumn<UserOpportunity> companyColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity site) 
			{
				return site.companyId;
			}
		};

		// Create status column.
		TextColumn<UserOpportunity> statusColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity site) 
			{
				return UserOpportunity.applicationStatusToString(site.status);
			}
		};

		// Create location column.
		TextColumn<UserOpportunity> locationColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity site) 
			{
				return site.location;
			}
		};

		// Create salary column.
		TextColumn<UserOpportunity> salaryColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity site) 
			{
				return Integer.toString(site.salary);
			}
		};

		// Create contract type column.
		TextColumn<UserOpportunity> contractTypeColumn = new TextColumn<UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity site) 
			{
				return site.contractType;
			}
		};

		// Create last visit column.
		TextColumn<UserOpportunity> firstSeenColumn = new TextColumn<UserOpportunity>() {
			@SuppressWarnings("deprecation")
			@Override
			public String getValue(UserOpportunity site) 
			{
				return site.firstSeen.toLocaleString();
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
		
		Button buttonNewSite = new Button();
		horizontalPanel.add(buttonNewSite);
		buttonNewSite.setText(lang._TextNewOpportunity());
		
		Button buttonUpdateSite = new Button();
		horizontalPanel.add(buttonUpdateSite);
		buttonUpdateSite.setText(lang._TextUpdateOpportunity());
		
		Button buttonDeleteSite = new Button();
		buttonDeleteSite.setText(lang._TextDeleteOpportunity());
		horizontalPanel.add(buttonDeleteSite);
		
		Button buttonEditlogs = new Button(lang._TextEditLogs());
		horizontalPanel.add(buttonEditlogs);
				
		HTML htmlDescriptionhtml = new HTML(lang.htmlDescriptionhtml_html(), true);
		simplePanelCenter.add(htmlDescriptionhtml);
		htmlDescriptionhtml.setSize("100%", "100%");
				
		Grid grid = new Grid(2, 1);
		simplePanelCenter.add(grid);
		grid.setWidth("100%");
		
		Label lblSource = new Label(lang._TextSource());
		grid.setWidget(0, 0, lblSource);
		
		
		// Add a handler to the delete button.
		DeleteHandler deleteHandler = new DeleteHandler();
		buttonDeleteSite.addClickHandler(deleteHandler);
		
		// Add a handler to the update button.
		UpdateOpportunity updateHandler = new UpdateOpportunity();
		buttonUpdateSite.addClickHandler(updateHandler);
		
		// Add a handler to the new button.
		NewOpportunityHandler newHandler = new NewOpportunityHandler();
		buttonNewSite.addClickHandler(newHandler);
		
		getAllContent();		
	}
}
