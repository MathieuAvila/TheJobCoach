package com.TheJobCoach.webapp.userpage.client.UserSite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserSite implements EntryPoint {

	final Lang lang = GWT.create(Lang.class);

	UserId user;

	// The list of data to display.
	private List<UserJobSite> jobSiteList = new ArrayList<UserJobSite>();
	
	ButtonImageText buttonNewSite = new ButtonImageText(ButtonImageText.Type.NEW, lang._TextNewSite());
	
	final ExtendedCellTable<UserJobSite> cellTable = new ExtendedCellTable<UserJobSite>(jobSiteList);
	UserJobSite currentSite = null;

	public ContentUserSite(Panel rootPanel, UserId user) {
		this.user = user;
		this.rootPanel = rootPanel;
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	public void setRootPanel(Panel panel)
	{
		rootPanel = panel;
	}

	void getAllContent()
	{		
		ServerCallHelper<Vector<UserJobSite>> callback = new ServerCallHelper<Vector<UserJobSite>>(rootPanel) {
			@Override
			public void onSuccess(Vector<UserJobSite> result) {
				jobSiteList.clear();
				jobSiteList.addAll(result);
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
					userService.deleteUserSite(user, currentSite.ID, new ServerCallHelper<Integer>(rootPanel)
							{	
						@Override
						public void onSuccess(Integer r)
						{
							getAllContent();
						}
							});
				}}
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

		getAllContent();
		cellTable.setRowData(0, jobSiteList);
		cellTable.setRowCount(jobSiteList.size(), true);
		
		simplePanelCenter.add(cellTable);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

		buttonNewSite.addClickHandler(new ClickHandler()
		{			
			public void onClick(ClickEvent event) {
				newSite();
			}
		});
		simplePanelCenter.add(buttonNewSite);
	}
}
