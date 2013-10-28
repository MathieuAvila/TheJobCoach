package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.ExternalContact.EditExternalContact.EditExternalContactResult;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentExternalContact implements EntryPoint {

	UserId user;

	final ExtendedCellTable<ExternalContact> cellTable = new ExtendedCellTable<ExternalContact>();

	final static Lang lang = GWT.create(Lang.class);
	final static LangExternalContact langExternalContact = GWT.create(LangExternalContact.class);

	Panel rootPanel;

	public ContentExternalContact(Panel panel, UserId _user)
	{
		user = _user;
		rootPanel = panel;
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	// The list of data to display.
	private Vector<ExternalContact> ExternalContactList = new Vector<ExternalContact>();

	// Create a data provider.
	AsyncDataProvider<ExternalContact> dataProvider = new AsyncDataProvider<ExternalContact>() {
		@Override
		protected void onRangeChanged(HasData<ExternalContact> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= ExternalContactList.size() ) end = ExternalContactList.size();
			if (ExternalContactList.size() != 0)
			{
				List<ExternalContact> dataInRange = ExternalContactList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
			}
		}
	};

	void getAllContent()
	{	
		ServerCallHelper<Vector<ExternalContact>> callback = new ServerCallHelper<Vector<ExternalContact>>(rootPanel) {
			@Override
			public void onSuccess(Vector<ExternalContact> result) {
				ExternalContactList.clear();
				ExternalContactList.addAll(result);
				dataProvider.updateRowCount(ExternalContactList.size(), true);
				dataProvider.updateRowData(0, ExternalContactList.subList(0, ExternalContactList.size()));
				cellTable.redraw();				
			}
		};
		userService.getExternalContactList(user, callback);
	}

	private void delete(final ExternalContact contact)
	{
		MessageBox mb = new MessageBox(
				rootPanel, true, true, MessageBox.TYPE.QUESTION, langExternalContact._TextDeleteTitle(), 
				langExternalContact._TextDeleteText() + contact.lastName + " " + contact.firstName, new MessageBox.ICallback() {
					@Override
					public void complete(boolean ok) {
						if (ok == true)
						{
							userService.deleteExternalContact(user, contact.ID, new ServerCallHelper<String>(rootPanel) {
								public void onSuccess(String result)
								{
									getAllContent();
								}
							});
						}}});
		mb.onModuleLoad();
	}

	class NewExternalContactHandler implements ClickHandler
	{		
		public void onClick(ClickEvent event)
		{
			EditExternalContact eus = new EditExternalContact(rootPanel, null, user, new EditExternalContactResult() {

				@Override
				public void setResult(ExternalContact result) {

					if (result != null)
					{
						result.ID = SiteUUID.getDateUuid();
						userService.setExternalContact(user, result, new ServerCallHelper<String>(rootPanel) {								
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

	private void updateExternalContact(ExternalContact contact)
	{		
		EditExternalContact eus = new EditExternalContact(rootPanel, contact, user, new EditExternalContactResult() {
			@Override
			public void setResult(ExternalContact result) {
				if (result != null)
				{
					userService.setExternalContact(user, result, new ServerCallHelper<String>(rootPanel) {
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

		ContentHelper.insertTitlePanel(simplePanelCenter, langExternalContact._TextExternalContactTitle(), ClientImageBundle.INSTANCE.userExternalContactContent());


		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<ExternalContact, String>() {
			@Override
			public void update(int index, ExternalContact object, String value) {
				delete(object);
			}});

		cellTable.addColumnWithIcon(IconCellSingle.IconType.UPDATE, new FieldUpdater<ExternalContact, String>() {
			@Override
			public void update(int index, ExternalContact object, String value) {
				updateExternalContact(object);
			}});

		// Create first name column.
		TextColumn<ExternalContact> firstNameColumn = new TextColumn<ExternalContact>() 	{
			@Override
			public String getValue(ExternalContact ExternalContact) 
			{
				return ExternalContact.firstName;
			}
		};

		// Create last name column.
		TextColumn<ExternalContact> lastColumn = new TextColumn<ExternalContact>() {
			@Override
			public String getValue(ExternalContact ExternalContact) 
			{
				return ExternalContact.lastName;
			}
		};

		// Create organization column.
		TextColumn<ExternalContact> organizationColumn = new TextColumn<ExternalContact>() {
			@Override
			public String getValue(ExternalContact ExternalContact) 
			{
				return ExternalContact.organization;
			}
		};

		// Create phone column.
		TextColumn<ExternalContact> phoneColumn = new TextColumn<ExternalContact>() {
			@Override
			public String getValue(ExternalContact ExternalContact) 
			{
				return ExternalContact.phone;
			}
		};

		firstNameColumn.setSortable(true);
		lastColumn.setSortable(true);
		organizationColumn.setSortable(true);

		cellTable.addColumn(firstNameColumn, langExternalContact._TextFirstName());
		cellTable.addColumn(lastColumn, langExternalContact._TextLastName());
		cellTable.addColumn(organizationColumn, langExternalContact._Text_Organization());
		cellTable.addColumnEmail(new GetValue<String, ExternalContact>() {
			@Override
			public String getValue(ExternalContact contact)
			{				
				return contact.email;
			}			
		});
		cellTable.addColumn(phoneColumn, langExternalContact._Text_Phone());
		//cellTable.getColumnSortList().push(firstNameColumn);	
		cellTable.setStyleName("filecelltable");

		dataProvider.addDataDisplay(cellTable);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		cellTable.setRowData(0, ExternalContactList);
		cellTable.setRowCount(ExternalContactList.size(), true);
		cellTable.setVisibleRange(0, 20);
		cellTable.addColumnSortHandler(columnSortHandler);

		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");		

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

		ButtonImageText buttonNewExternalContact = new ButtonImageText(ButtonImageText.Type.NEW, langExternalContact._TextNewExternalContact());
		horizontalPanel.add(buttonNewExternalContact);

		// Add a handler to the new button.
		NewExternalContactHandler newHandler = new NewExternalContactHandler();
		buttonNewExternalContact.addClickHandler(newHandler);

		getAllContent();		
	}
}
