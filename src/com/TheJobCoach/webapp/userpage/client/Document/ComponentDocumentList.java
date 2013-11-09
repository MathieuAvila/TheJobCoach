package com.TheJobCoach.webapp.userpage.client.Document;

import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.DownloadIFrame;
import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.client.IChooseDialogModel;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class ComponentDocumentList extends CaptionPanel 
{	
	final Lang lang = GWT.create(Lang.class);
	final LangDocument langDocument = GWT.create(LangDocument.class);
	final ExtendedCellTable<UserDocumentId> cellTable = new ExtendedCellTable<UserDocumentId>();
	
	Vector<UserDocumentId> docList;
	Panel rootPanel;
	UserId userId;
	IChooseDialogModel<UserDocumentId> chooseDocument;
	
	public ComponentDocumentList(Vector<UserDocumentId> docList, Panel rootPanel, UserId userId, IChooseDialogModel<UserDocumentId> chooseDocument)
	{
		this.docList = docList;
		this.rootPanel = rootPanel;
		this.userId = userId;
		this.chooseDocument = chooseDocument;
	}
	
	public ComponentDocumentList(Vector<UserDocumentId> docList, Panel rootPanel, UserId userId)
	{
		this.docList = docList;
		this.rootPanel = rootPanel;
		this.userId = userId;
		this.chooseDocument = new ComponentChooseDocument();
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
		dataProvider.updateRowCount(docList.size(), true);
		dataProvider.updateRowData(0, docList.subList(0, docList.size()));
		cellTable.redraw();	
	}

	private void deleteDoc(UserDocumentId object) 
	{
		docList.remove(object);
		getAllContent();
	}
	
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{	
		rootPanel.add(this);

		final VerticalPanel vp = new VerticalPanel();
		this.add(vp);
		vp.setWidth("100%");
		vp.setSpacing(5);
		this.setCaptionText(langDocument._TextDocumentTitle());
		this.setStyleName("SelectorList-CaptionPanel");
		
		
		// Create name column.
		TextColumn<UserDocumentId> nameColumn = new TextColumn<UserDocumentId>() 	{
			@Override
			public String getValue(UserDocumentId document) 
			{
				return document.name;
			}
		};
		cellTable.addColumn(nameColumn, lang._TextName());

		cellTable.addColumnWithIconCellFile(
				new FieldUpdater<UserDocumentId, String>() {
					@Override
					public void update(int index, UserDocumentId object, String value) {
						String copyURL = GWT.getModuleBaseURL() + "DownloadServlet?docid=" + URL.encodeQueryString(object.updateId) + "&userid=" + URL.encodeQueryString(userId.userName)+ "&token=" + URL.encodeQueryString(userId.token);
						DownloadIFrame iframe = new DownloadIFrame(copyURL);
						vp.add(iframe);
					}},
					new GetValue<String, UserDocumentId>() {
						@Override
						public String getValue(UserDocumentId contact) {
							return contact.fileName;
						}},
						langDocument._TextFilename());
		
		// Create update date column.
		TextColumn<UserDocumentId> dateColumn = new TextColumn<UserDocumentId>() 	{
			@Override
			public String getValue(UserDocumentId document) 
			{
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(document.lastUpdate);
			}
		};
		cellTable.addColumn(dateColumn, langDocument._TextLastUpdate());

		// Create remove column.
		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<UserDocumentId, String>() {
			@Override
			public void update(int index, UserDocumentId object, String value) {
				deleteDoc(object);
			}
			});
		
		vp.add(cellTable);		
		ButtonImageText buttonAdd = new ButtonImageText(ButtonImageText.Type.ADD_16, langDocument._TextAttachUserDocument());		
		vp.add(buttonAdd);
		
		buttonAdd.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) 
			{
				IChooseDialogModel<UserDocumentId> cud = chooseDocument.clone(rootPanel, userId, new IChooseResult<UserDocumentId>() 
				{
					@Override
					public void setResult(UserDocumentId result) 
					{
						docList.add(result);
						getAllContent();
					}
				});
				cud.onModuleLoad();							
			}});
		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(docList.size(), true);

		getAllContent();
	}
}
