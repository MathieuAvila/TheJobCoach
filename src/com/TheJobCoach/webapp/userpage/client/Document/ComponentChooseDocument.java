package com.TheJobCoach.webapp.userpage.client.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.IChooseDialogModel;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ComponentChooseDocument implements EntryPoint, IChooseDialogModel<UserDocumentId>
{
	private final static UserServiceAsync userService = GWT.create(UserService.class);
	
	final Lang lang = GWT.create(Lang.class);
	final LangDocument langDocument = GWT.create(LangDocument.class);
	
	final ExtendedCellTable<UserDocumentId> cellTable = new ExtendedCellTable<UserDocumentId>();
	DialogBlockOkCancel okCancel;
	IChooseResult<UserDocumentId> result;
	
	List<UserDocumentId> docList = new ArrayList<UserDocumentId>();
	Panel rootPanel;
	UserId userId;
	
	public ComponentChooseDocument(Panel rootPanel, UserId userId, IChooseResult<UserDocumentId> result)
	{
		this.rootPanel = rootPanel;
		this.userId = userId;
		this.result = result;
	}

	public ComponentChooseDocument()
	{
	}

	// Create a data provider.
	AsyncDataProvider<UserDocumentId> dataProvider = new AsyncDataProvider<UserDocumentId>() {
		@Override
		protected void onRangeChanged(HasData<UserDocumentId> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= docList.size() ) end = docList.size();
			if (docList.size() != 0)
			{
				List<UserDocumentId> dataInRange = docList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
				cellTable.redraw();				
			}
		}
	};

	void getAllContent()
	{		
		AsyncCallback<Vector<UserDocumentId>> callback = new AsyncCallback<Vector<UserDocumentId>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Vector<UserDocumentId> result) {
				docList.clear();
				docList.addAll(result);				
				dataProvider.updateRowCount(docList.size(), true);
				dataProvider.updateRowData(0, docList.subList(0, docList.size()));
				cellTable.redraw();				
			}
		};
		userService.getUserDocumentIdList(userId, callback);		
		dataProvider.updateRowCount(docList.size(), true);
		cellTable.redraw();
	}
	
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{	
		Lang lang = GWT.create(Lang.class);

		final DialogBox dBox = new DialogBox();
		dBox.setText("Choisissez un document à ajouter");
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		VerticalPanel hp = new VerticalPanel();		
		
		// Create name column.
		TextColumn<UserDocumentId> nameColumn = new TextColumn<UserDocumentId>() 	{
			@Override
			public String getValue(UserDocumentId document) 
			{
				return document.name;
			}
		};
		cellTable.addColumn(nameColumn, lang._TextName());
		
		// Create file column.
		TextColumn<UserDocumentId> fileColumn = new TextColumn<UserDocumentId>() 	{
			@Override
			public String getValue(UserDocumentId document) 
			{
				return document.fileName;
			}
		};
		cellTable.addColumn(fileColumn, langDocument._TextFilename());
		
		// Create update date column.
		TextColumn<UserDocumentId> dateColumn = new TextColumn<UserDocumentId>() 	{
			@Override
			public String getValue(UserDocumentId document) 
			{
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(document.lastUpdate);
			}
		};
		cellTable.addColumn(dateColumn, langDocument._TextLastUpdate());

		hp.add(cellTable);		
		
		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(docList.size(), true);
		getAllContent();
		
		// Add a selection model to handle user selection.
		final SingleSelectionModel<UserDocumentId> selectionModel = new SingleSelectionModel<UserDocumentId>();
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
				UserDocumentId doc = selectionModel.getSelectedObject();
				//System.out.println("Selected " + doc);
				result.setResult(doc);
				dBox.hide();
			}
		});
		hp.add(okCancel);
		
		dBox.setWidget(hp);
		dBox.center();
	}

	@Override
	public IChooseDialogModel<UserDocumentId> clone(Panel rootPanel,
			UserId userId, IChooseResult<UserDocumentId> result)
	{
		return new ComponentChooseDocument(rootPanel, userId, result);
	}
}
