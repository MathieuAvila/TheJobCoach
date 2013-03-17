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
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;

public class ComponentChooseExternalContact implements EntryPoint 
{

	private final static UserServiceAsync userService = GWT.create(UserService.class);
	private final static LangExternalContact langExternalContact = GWT.create(LangExternalContact.class);
	final ExtendedCellTable<ExternalContact> cellTable = new ExtendedCellTable<ExternalContact>();
	DialogBlockOkCancel okCancel;
	ComponentChooseDocumentResult result;
	
	public interface ComponentChooseDocumentResult
	{
		public void setResult(ExternalContact result);
	}

	List<ExternalContact> docList = new ArrayList<ExternalContact>();
	Panel rootPanel;
	UserId userId;
	
	public ComponentChooseExternalContact(Panel rootPanel, UserId userId, ComponentChooseDocumentResult result)
	{
		this.rootPanel = rootPanel;
		this.userId = userId;
		this.result = result;
	}

	// Create a data provider.
	AsyncDataProvider<ExternalContact> dataProvider = new AsyncDataProvider<ExternalContact>() {
		@Override
		protected void onRangeChanged(HasData<ExternalContact> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= docList.size() ) end = docList.size();
			if (docList.size() != 0)
			{
				List<ExternalContact> dataInRange = docList.subList(start, end);
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
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Vector<ExternalContact> result) {
				docList.clear();
				docList.addAll(result);				
				dataProvider.updateRowCount(docList.size(), true);
				dataProvider.updateRowData(0, docList.subList(0, docList.size()));
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
		dataProvider.updateRowCount(docList.size(), true);
		cellTable.redraw();
	}
	
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
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
			public String getValue(ExternalContact document) 
			{
				return document.firstName;
			}
		};
		cellTable.addColumn(firstNameColumn, langExternalContact._TextFirstName());

		// Create first name column.
		TextColumn<ExternalContact> lastNameColumn = new TextColumn<ExternalContact>() 	{
			@Override
			public String getValue(ExternalContact document) 
			{
				return document.lastName;
			}
		};
		cellTable.addColumn(lastNameColumn, langExternalContact._TextLastName());
				
		// Create organization column.
		TextColumn<ExternalContact> organizationColumn = new TextColumn<ExternalContact>() 	{
			@Override
			public String getValue(ExternalContact document) 
			{
				return document.lastName;
			}
		};
		cellTable.addColumn(organizationColumn, langExternalContact._Text_Organization());

		hp.add(cellTable);		
		
		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(docList.size(), true);
		getAllContent();
		
		// Add a selection model to handle user selection.
		final SingleSelectionModel<ExternalContact> selectionModel = new SingleSelectionModel<ExternalContact>();
		cellTable.setSelectionModel(selectionModel);

		okCancel = new DialogBlockOkCancel(null, dBox);
		okCancel.getOk().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				okCancel.setEnabled(false);
				ExternalContact doc = selectionModel.getSelectedObject();
				//System.out.println("Selected " + doc);
				result.setResult(doc);
				dBox.hide();
			}
		});
		hp.add(okCancel);
		
		dBox.setWidget(hp);
		dBox.center();
	}
}
