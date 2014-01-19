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
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserSite implements EntryPoint {

	final Lang lang = GWT.create(Lang.class);
	
	VerticalPanel simplePanelCenter = new VerticalPanel();
	
	UserId user;

	// The list of data to display.
	private List<UserJobSite> jobSiteList = new ArrayList<UserJobSite>();
	
	ButtonImageText buttonNewSite = new ButtonImageText(ButtonImageText.Type.NEW, lang._TextNewSite());
	
	final ExtendedCellTable<UserJobSite> cellTable = new ExtendedCellTable<UserJobSite>(jobSiteList);
	
	IEditDialogModel<UserJobSite> editSiteInterface;

	public ContentUserSite(Panel rootPanel, UserId user, IEditDialogModel<UserJobSite> editSiteInterface) {
		this.user = user;
		this.rootPanel = rootPanel;
		this.editSiteInterface = editSiteInterface;
	}

	public ContentUserSite(Panel rootPanel, UserId user) {
		this.user = user;
		this.rootPanel = rootPanel;
		this.editSiteInterface = new EditUserSite();
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

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

	void updateSite(UserJobSite currentSite)
	{
		IEditDialogModel<UserJobSite> eus = editSiteInterface.clone(rootPanel, user, currentSite, new IChooseResult<UserJobSite>() {
			@Override
			public void setResult(UserJobSite result) {				
				if (result != null) getAllContent();				
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

		simplePanelCenter.setSize("100%", "");
		rootPanel.add(simplePanelCenter);

		ContentHelper.insertTitlePanel(simplePanelCenter, lang.lblJobSites_text(), ClientImageBundle.INSTANCE.userJobSiteContent());

		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<UserJobSite, String>() {
			@Override
			public void update(int index, UserJobSite object, String value) {
				deleteSite(object);
			}});

		cellTable.addColumnWithIcon(IconCellSingle.IconType.UPDATE, new FieldUpdater<UserJobSite, String>() {
			@Override
			public void update(int index, UserJobSite object, String value) {
				updateSite(object);
			}});

		// Create URL column.
		cellTable.addColumnWithIcon(IconCellSingle.IconType.URL, new FieldUpdater<UserJobSite, String>() {
			@Override
			public void update(int index, final UserJobSite object, String value) {
				object.update.last = new Date();
				
				final Anchor link = new Anchor("", object.URL);
				simplePanelCenter.add(link);
				link.setTarget("_blank");
				clickElement(link.getElement());
				
				userService.setUserSite(user, object, new ServerCallHelper<Integer>(rootPanel)
						{	
					@Override
					public void onSuccess(Integer r)
					{
						cellTable.redraw();
					}
				});
			}});

		/*
		cellTable.addColumnUrl(new ExtendedCellTable.GetValue<String, UserJobSite>() {
			public String getValue(UserJobSite contact) {
				return contact.URL;
			}
		});*/

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
				updateSite(null);
			}
		});
		simplePanelCenter.add(buttonNewSite);
	}
	
	/* Helper to simulate JS click */
	public static native void clickElement(Element elem) /*-{
    elem.click();
	}-*/;

}
