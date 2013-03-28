package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.IChooseDialogModel;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ComponentChooseExternalContact implements EntryPoint, IChooseDialogModel<ExternalContact>
{

	private final static UserServiceAsync userService = GWT.create(UserService.class);
	private final static LangExternalContact langExternalContact = GWT.create(LangExternalContact.class);
	final ExtendedCellTable<ExternalContact> cellTable = new ExtendedCellTable<ExternalContact>();
	DialogBlockOkCancel okCancel;
	IChooseResult<ExternalContact> result;

	List<ExternalContact> externalContactList = new ArrayList<ExternalContact>();
	Panel rootPanel;
	UserId userId;
	
	public ComponentChooseExternalContact(Panel rootPanel, UserId userId, IChooseResult<ExternalContact> result)
	{
		this.rootPanel = rootPanel;
		this.userId = userId;
		this.result = result;
	}
	
	public ComponentChooseExternalContact()
	{		
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

	void getAllContent()
	{		
		AsyncCallback<Vector<ExternalContact>> callback = new AsyncCallback<Vector<ExternalContact>>() {
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.messageBoxException(rootPanel, caught);
			}
			@Override
			public void onSuccess(Vector<ExternalContact> result) {
				externalContactList.clear();
				externalContactList.addAll(result);				
				dataProvider.updateRowCount(externalContactList.size(), true);
				dataProvider.updateRowData(0, externalContactList.subList(0, externalContactList.size()));
				cellTable.redraw();				
			}
		};
		try {
			userService.getExternalContactList(userId, callback);	
		}
		catch (CassandraException e)
		{
			MessageBox.messageBoxException(rootPanel, e);
		}
		dataProvider.updateRowCount(externalContactList.size(), true);
		cellTable.redraw();
	}
	
	/* (non-Javadoc)
	 * @see com.TheJobCoach.webapp.userpage.client.ExternalContact.IChooseExternalContact#onModuleLoad()
	 */
	@Override
	public void onModuleLoad()
	{	
		final DialogBox dBox = new DialogBox();
		dBox.setText(langExternalContact._Text_ChooseExternalContact());
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		VerticalPanel hp = new VerticalPanel();		
					
		// Create first name column.
		TextColumn<ExternalContact> firstNameColumn = new TextColumn<ExternalContact>() 	{
			@Override
			public String getValue(ExternalContact externalContact) 
			{
				return externalContact.firstName;
			}
		};
		cellTable.addColumn(firstNameColumn, langExternalContact._TextFirstName());

		// Create first name column.
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

		hp.add(cellTable);		
		
		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(externalContactList.size(), true);
		getAllContent();
		
		// Add a selection model to handle user selection.
		final SingleSelectionModel<ExternalContact> selectionModel = new SingleSelectionModel<ExternalContact>();
		cellTable.setSelectionModel(selectionModel);

		okCancel = new DialogBlockOkCancel(null, dBox);
		// Can't click on OK when no selection is made.
		okCancel.getOk().setEnabled(false);
		// When selecting one, ok is allowed
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{
			public void onSelectionChange(SelectionChangeEvent event) 
			{
				okCancel.getOk().setEnabled(true);
			}
		});
		
		okCancel.getOk().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				okCancel.setEnabled(false);
				ExternalContact externalContact = selectionModel.getSelectedObject();
				result.setResult(externalContact);
				dBox.hide();
			}
		});
		hp.add(okCancel);
		
		dBox.setWidget(hp);
		dBox.center();
	}

	@Override
	public IChooseDialogModel<ExternalContact> clone(Panel rootPanel, UserId userId,
			IChooseResult<ExternalContact> result)
	{
		return new ComponentChooseExternalContact(rootPanel, userId, result);
	}
}
