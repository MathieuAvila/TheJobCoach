package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.IChooseDialogModel;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class ComponentExternalContactList extends CaptionPanel 
{	
	private final static LangExternalContact langExternalContact = GWT.create(LangExternalContact.class);
	final ExtendedCellTable<ExternalContact> cellTable = new ExtendedCellTable<ExternalContact>();
	
	Vector<ExternalContact> externalContactList;
	Panel rootPanel;
	UserId userId;
	ButtonImageText buttonAdd;
	IChooseDialogModel<ExternalContact> chooseExternalContact;

	public ComponentExternalContactList(Vector<ExternalContact> contactList, Panel rootPanel, UserId userId, IChooseDialogModel<ExternalContact> chooseExternalContact)
	{
		this.externalContactList = contactList;
		this.rootPanel = rootPanel;
		this.userId = userId;
		this.chooseExternalContact = chooseExternalContact;
	}

	// Create a data provider.
	AsyncDataProvider<ExternalContact> dataProvider = new AsyncDataProvider<ExternalContact>() {
		@Override
		protected void onRangeChanged(HasData<ExternalContact> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= externalContactList.size() ) end = externalContactList.size();
			if (externalContactList.size() != 0)
			{
				List<ExternalContact> dataInRange = externalContactList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
				cellTable.redraw();				
			}
		}
	};

	void printAllContent()
	{	
		dataProvider.updateRowCount(externalContactList.size(), true);
		dataProvider.updateRowData(0, externalContactList.subList(0, externalContactList.size()));
		cellTable.redraw();	
	}

	private void removeContact(ExternalContact object) 
	{
		externalContactList.remove(object);
		printAllContent();
	}
	
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{
		rootPanel.add(this);
		
		VerticalPanel vp = new VerticalPanel();
		this.add(vp);
		vp.setWidth("100%");
		vp.setSpacing(5);
		this.setCaptionText(langExternalContact._TextExternalContactTitle());
		this.setStyleName("SelectorList-CaptionPanel");
		
		// Create first name column.
		TextColumn<ExternalContact> firstNameColumn = new TextColumn<ExternalContact>() 	{
			@Override
			public String getValue(ExternalContact externalContact) 
			{
				return externalContact.firstName;
			}
		};
		cellTable.addColumn(firstNameColumn, langExternalContact._TextFirstName());

		// Create last name column.
		TextColumn<ExternalContact> lastNameColumn = new TextColumn<ExternalContact>() 	{
			@Override
			public String getValue(ExternalContact externalContact) 
			{
				return externalContact.lastName;
			}
		};
		cellTable.addColumn(lastNameColumn, langExternalContact._TextLastName());

		// Create organization column.
		TextColumn<ExternalContact> organizationColumn = new TextColumn<ExternalContact>() 	{
			@Override
			public String getValue(ExternalContact externalContact) 
			{
				return externalContact.organization;
			}
		};
		cellTable.addColumn(organizationColumn, langExternalContact._Text_Organization());

		// Create remove column.
		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<ExternalContact, String>() {
			@Override
			public void update(int index, ExternalContact object, String value) {
				removeContact(object);
			}
			});
		
		vp.add(cellTable);
		cellTable.setWidth("100%");
		vp.setCellWidth(cellTable, "100%");

		buttonAdd = new ButtonImageText(ButtonImageText.Type.ADD_16, langExternalContact._Text_AddExternalContact());		
		vp.add(buttonAdd);
		
		buttonAdd.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) 
			{
				IChooseDialogModel<ExternalContact> cud = chooseExternalContact.clone(rootPanel, userId, new IChooseResult<ExternalContact>() 
				{
					@Override
					public void setResult(ExternalContact result) 
					{
						boolean found = false;
						for (ExternalContact contact: externalContactList) if (contact.ID.equals(result.ID)) found=true;
						if (!found)
							externalContactList.add(result);
						printAllContent();
					}
				});
				cud.onModuleLoad();							
			}});
		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(externalContactList.size(), true);

		printAllContent();
	}
}
