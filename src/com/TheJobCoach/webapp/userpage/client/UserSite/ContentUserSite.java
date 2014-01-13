package com.TheJobCoach.webapp.userpage.client.UserSite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserSite implements EntryPoint {

	final Lang lang = GWT.create(Lang.class);

	UserId user;

	// The list of data to display.
	private List<UserJobSite> jobSiteList = new ArrayList<UserJobSite>();

	final ExtendedCellTable<UserJobSite> cellTable = new ExtendedCellTable<UserJobSite>(jobSiteList);
	UserJobSite currentSite = null;

	public ContentUserSite(Panel rootPanel, UserId user) {
		this.user = user;
		this.rootPanel = rootPanel;
	}

	private void setUserJobSite(UserJobSite site)
	{
		currentSite = site;
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	public void setRootPanel(Panel panel)
	{
		rootPanel = panel;
	}

	void getOneSite(String siteId)
	{
		ServerCallHelper<UserJobSite> callback = new ServerCallHelper<UserJobSite>(rootPanel)	{
			@Override
			public void onSuccess(UserJobSite result)
			{
				for (int count=0; count != jobSiteList.size(); count++) {
					if (jobSiteList.get(count).ID.equals(result.ID))
					{
						jobSiteList.set(count, result);
					}
				}
				cellTable.updateData();
			}
		};
		userService.getUserSite(user, siteId, callback);
	}

	void getAllContent()
	{		
		ServerCallHelper<List<String>> callback = new ServerCallHelper<List<String>>(rootPanel) {
			@Override
			public void onSuccess(List<String> result) {
				jobSiteList.clear();
				for (String idRes: result)
				{
					jobSiteList.add(new UserJobSite(idRes,"", "", "", "", ""));
					getOneSite(idRes);
				}
				cellTable.updateData();
			}
		};
		userService.getUserSiteList(user, callback);
		
	}


	void deleteSite(final UserJobSite currentSite)
	{
		MessageBox mb = new MessageBox(rootPanel, true, true, MessageBox.TYPE.QUESTION, 
				lang._TextConfirmDeleteSiteTitle(), lang._TextConfirmDeleteSite(), new MessageBox.ICallback() {

			public void complete(boolean ok) {
				if(ok)
				{
					EasyAsync.serverCall(rootPanel, new EasyAsync.ServerCallRun() {
						public void Run() throws CassandraException
						{
							userService.deleteUserSite(user, currentSite.ID, new ServerCallHelper<Integer>(rootPanel)
							{	
								@Override
								public void onSuccess(Integer r)
								{
									getAllContent();
								}
							});
						}});
				}
			}
		});
		mb.onModuleLoad();
	}

	public void newSite()
	{
		EditUserSite eus = new EditUserSite(rootPanel, null, user, new IChooseResult<UserJobSite>() {
			@Override
			public void setResult(UserJobSite result) {				
				getAllContent();				
			}
		});
		eus.onModuleLoad();
	}

	void updateSite(UserJobSite currentSite)
	{
		EditUserSite eus = new EditUserSite(rootPanel, currentSite, user, new IChooseResult<UserJobSite>() {
			@Override
			public void setResult(UserJobSite result) {				
				getAllContent();				
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

		// Create URL column.
		cellTable.addColumnUrl(new ExtendedCellTable.GetValue<String, UserJobSite>() {
			public String getValue(UserJobSite contact) {
				return contact.URL;
			}
		});

		// Create name column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserJobSite>() {
			@Override
			public String getValue(UserJobSite site)
			{
				return site.name;
			}			
		},  lang._TextName());

		// Create login column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserJobSite>() {
			@Override
			public String getValue(UserJobSite site)
			{
				return site.login;
			}			
		},  lang._TextLogin());

		// Create password column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserJobSite>() {
			@Override
			public String getValue(UserJobSite site)
			{
				return site.password;
			}			
		},  lang._TextPassword());

		// Create lastVisit column.
		cellTable.specialAddColumnSortableDate(new GetValue<Date, UserJobSite>() {
			@Override
			public Date getValue(UserJobSite site)
			{
				return site.update.last;
			}			
		},  lang._TextLastVisit());
		
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
		getAllContent();
		cellTable.setRowData(0, jobSiteList);
		cellTable.setRowCount(jobSiteList.size(), true);
		
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
