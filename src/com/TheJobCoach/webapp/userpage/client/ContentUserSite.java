package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.EditUserSite.EditUserSiteResult;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
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

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserSite implements EntryPoint {

	final Lang lang = GWT.create(Lang.class);
	
	UserId user;

	final ExtendedCellTable<UserJobSite> cellTable = new ExtendedCellTable<UserJobSite>();
	UserJobSite currentSite = null;

	public ContentUserSite(Panel rootPanel, UserId user) {
		this.user = user;
		this.rootPanel = rootPanel;
	}

	private void setUserJobSite(UserJobSite site)
	{
		currentSite = site;
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
			MessageBox.messageBoxException(rootPanel, e.toString());
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
			MessageBox.messageBoxException(rootPanel, e.toString());
		}
	}


	void deleteSite(final UserJobSite currentSite)
	{
		MessageBox mb = new MessageBox(rootPanel, true, true, MessageBox.TYPE.QUESTION, 
				lang._TextConfirmDeleteSiteTitle(), lang._TextConfirmDeleteSite(), new MessageBox.ICallback() {
					
					public void complete(boolean ok) {
						if(ok)
						{
							try {
								userService.deleteUserSite(user, currentSite.ID, new AsyncCallback<Integer>() {
									public void onFailure(Throwable caught) {
										// Show the RPC error message to the user
										MessageBox.messageBoxException(rootPanel, caught.toString());										
									}
									public void onSuccess(Integer result)
									{
										getAllContent();
									}
								});
							} 
							catch (CassandraException e) 
							{
								MessageBox.messageBoxException(rootPanel, e.toString());
							}
						}
					}
				});
	mb.onModuleLoad();
	}

	public void newSite()
	{
		EditUserSite eus = new EditUserSite(rootPanel, null, user, new EditUserSiteResult() {
			@Override
			public void setResult(UserJobSite result) {
				try 
				{
					if (result != null)
						userService.setUserSite(user, result, new AsyncCallback<Integer>() {
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
					MessageBox.messageBoxException(rootPanel, e.toString());
				}
			}
		});
		eus.onModuleLoad();
	}

	void updateSite(UserJobSite currentSite)
	{

		EditUserSite eus = new EditUserSite(rootPanel, currentSite, user, new EditUserSiteResult() {

			@Override
			public void setResult(UserJobSite result) {
				try 
				{
					if (result != null)
						userService.setUserSite(user, result, new AsyncCallback<Integer>() {
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
					MessageBox.messageBoxException(rootPanel, e.toString());
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

		ContentHelper.insertTitlePanel(simplePanelCenter, lang.lblJobSites_text(), ClientImageBundle.INSTANCE.userJobSiteContent());
		
		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<UserJobSite, String>() {
			@Override
			public void update(int index, UserJobSite object, String value) {
				deleteSite(object);
			}}	
				);

		cellTable.addColumnWithIcon(IconCellSingle.IconType.UPDATE, new FieldUpdater<UserJobSite, String>() {
			@Override
			public void update(int index, UserJobSite object, String value) {
				updateSite(object);
			}}	
				);
		
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
		cellTable.addColumnUrl(new ExtendedCellTable.GetValue<String, UserJobSite>() {
			public String getValue(UserJobSite contact) {
				return contact.URL;
			}
		});
		
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
			@Override
			public String getValue(UserJobSite site) 
			{
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(site.lastVisit);
			}
		};

		nameColumn.setSortable(true);
		descriptionColumn.setSortable(true);
		loginColumn.setSortable(true);
		passwordColumn.setSortable(true);
		lastVisitColumn.setSortable(true);
		cellTable.addColumn(nameColumn, lang._TextName());
		cellTable.addColumn(descriptionColumn, lang._TextDescription());
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
				}
			}
		});

		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(jobSiteList.size(), true);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		getAllContent();
		cellTable.setRowData(0, jobSiteList);
		cellTable.setRowCount(jobSiteList.size(), true);
		cellTable.addColumnSortHandler(columnSortHandler);
		
		simplePanelCenter.add(cellTable);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

		ButtonImageText button = new ButtonImageText(ButtonImageText.Type.NEW, lang._TextNewSite());
		button.addClickHandler(new ClickHandler()
		{			
			public void onClick(ClickEvent event) {
				newSite();
			}
		});
		simplePanelCenter.add(button);
	}
}
