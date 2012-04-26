package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.EditUserDocument.EditUserDocumentResult;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.IconCellFile;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserDocument implements EntryPoint, EditUserDocumentResult {
	
	final static Lang lang = GWT.create(Lang.class);
	
	UserId user;

	final CellTable<UserDocument> cellTable = new CellTable<UserDocument>();
	UserDocument currentSite = null;

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
	private List<UserDocument> userDocumentList = new ArrayList<UserDocument>();

	// Create a data provider.
	AsyncDataProvider<UserDocument> dataProvider = new AsyncDataProvider<UserDocument>() {
		@Override
		protected void onRangeChanged(HasData<UserDocument> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= userDocumentList.size() ) end = userDocumentList.size();
			if (userDocumentList.size() != 0)
			{
				List<UserDocument> dataInRange = userDocumentList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
				cellTable.redraw();				
			}
		}
	};

	void getAllContent()
	{		
		AsyncCallback<Vector<UserDocument>> callback = new AsyncCallback<Vector<UserDocument>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Vector<UserDocument> result) {
				System.out.println(result);
				userDocumentList.clear();
				userDocumentList.addAll(result);				
				dataProvider.updateRowCount(userDocumentList.size(), true);
				dataProvider.updateRowData(0, userDocumentList.subList(0, userDocumentList.size()));
				cellTable.redraw();				
			}
		};
		try {
			userService.getUserDocumentList(user, callback);
		} catch (CassandraException e) {
			e.printStackTrace();
		}		
		dataProvider.updateRowCount(userDocumentList.size(), true);
		cellTable.redraw();
	}
	
	void updateDoc(final UserDocument object)
	{
		EditUserDocument eud = new EditUserDocument();
		eud.setRootPanel(rootPanel, object, lang._TextUpdateANewUserDocument(), new EditUserDocument.EditUserDocumentResult() {

			@Override
			public void setResult() {							
				getAllContent();
			}

		});
		eud.setUserParameters(user);
		eud.onModuleLoad();
	}
	
	void deleteDoc(final UserDocument object)
	{
		MessageBox mb = new MessageBox(
				rootPanel, true, true, MessageBox.TYPE.QUESTION, lang._TextConfirmDeleteUserDocumentTitle(), 
				lang._TextConfirmDeleteUserDocument() + object.fileName, new MessageBox.ICallback() {
					
					@Override
					public void complete(boolean ok) {
						if (ok == true)
						{
							AsyncCallback<String> callback = new AsyncCallback<String>() {
								@Override
								public void onFailure(Throwable caught) {
									Window.alert(caught.getMessage());
								}
								@Override
								public void onSuccess(String result) {
									System.out.println(result);						
									getAllContent();
								}
							};
							try {
								userService.deleteUserDocument(user, object.ID, callback);
							} catch (CassandraException e) {
								e.printStackTrace();
							}		
							dataProvider.updateRowCount(userDocumentList.size(), true);
							cellTable.redraw();							
						}
					}
				});
		mb.onModuleLoad();
	}

	private <C> Column<UserDocument, C> addColumn(Cell<C> cell,final GetValue<C> getter, FieldUpdater<UserDocument, C> fieldUpdater) 
	{
		Column<UserDocument, C> column = new Column<UserDocument, C>(cell) 
				{

			@Override
			public C getValue(UserDocument object) 
			{
				return getter.getValue(object);
			}
				};
				column.setFieldUpdater(fieldUpdater);

				return column;
	}


	private static interface GetValue<C> {
		C getValue(UserDocument contact);
	}


	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{		
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		final VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "");
		rootPanel.add(simplePanelCenter);
				
		ContentHelper.insertTitlePanel(simplePanelCenter, lang._TextUserDocument(), ClientImageBundle.INSTANCE.userDocumentContent());
		
		// Create name column.
		TextColumn<UserDocument> nameColumn = new TextColumn<UserDocument>() 	{
			@Override
			public String getValue(UserDocument document) 
			{
				return document.name;
			}
		};

		// Create status column.
		TextColumn<UserDocument> statusColumn = new TextColumn<UserDocument>() 	{
			@Override
			public String getValue(UserDocument document) 
			{
				//return UserDocument.documentStatusToString(document.status);
				return lang.documentStatusMap().get("documentStatusMap_" + UserDocument.documentStatusToString(document.status));
			}
		};

		// Create description column.
		TextColumn<UserDocument> descriptionColumn = new TextColumn<UserDocument>() {
			@Override
			public String getValue(UserDocument document) 
			{
				return document.description;
			}
		};

		// Create lastUpdate column.
		TextColumn<UserDocument> downloadLastUpdate = new TextColumn<UserDocument>() {
			@SuppressWarnings("deprecation")
			@Override
			public String getValue(UserDocument document) 
			{
				return document.lastUpdate.toLocaleString();				
			}
		};

		ClickableTextCell anchorcolumn = new ClickableTextCell()
		{
			@Override
			protected void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
				if (value != null) {
					sb.appendHtmlConstant(
							"<div class=\"clickableText\">" + 
							"<a style=\"clickableText\">");
					sb.append(value);
					sb.appendHtmlConstant("</a></div>");
				}
			}
		};

		IconCellSingle deleteCell =	new IconCellSingle(IconCellSingle.IconType.DELETE);		
		cellTable.addColumn(addColumn(deleteCell, new GetValue<String>() {
			public String getValue(UserDocument contact) {
				return "&nbsp;";//contact.fileName;
			}
		},
		new FieldUpdater<UserDocument, String>() {
			public void update(int index, UserDocument object, String value) {				
				deleteDoc(object);
			}
		}), lang._TextDeleteUserDocument());

		IconCellSingle updateCell =	new IconCellSingle(IconCellSingle.IconType.UPDATE);		
		cellTable.addColumn(addColumn(updateCell, new GetValue<String>() {
			public String getValue(UserDocument contact) {
				return "&nbsp;";//contact.fileName;
			}
		},
		new FieldUpdater<UserDocument, String>() {
			public void update(int index, UserDocument object, String value) {
				updateDoc(object);
			}
		}), lang._TextUpdateUserDocument());
		
		statusColumn.setSortable(true);
		cellTable.addColumn(statusColumn, lang._TextStatus());
		
		IconCellFile iconCellFile = new IconCellFile(anchorcolumn);
		cellTable.addColumn(addColumn(iconCellFile, new GetValue<String>() {
			public String getValue(UserDocument contact) {
				return contact.fileName;
			}
		},
		new FieldUpdater<UserDocument, String>() {
			public void update(int index, UserDocument object, String value) {
				String copyURL = GWT.getModuleBaseURL() + "DownloadServlet?docid=" + URL.encode(object.ID);
				DownloadIFrame iframe = new DownloadIFrame(copyURL);
				simplePanelCenter.add(iframe);
			}
		}), lang._TextFilename());

		nameColumn.setSortable(true);
		descriptionColumn.setSortable(true);
		downloadLastUpdate.setSortable(true);
		cellTable.addColumn(nameColumn, lang._TextName());
		cellTable.addColumn(descriptionColumn, lang._TextDescription());
		cellTable.addColumn(downloadLastUpdate, lang._TextLastUpdate());
		cellTable.getColumnSortList().push(nameColumn);		

		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(userDocumentList.size(), true);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);

		cellTable.setRowData(0, userDocumentList);
		cellTable.setRowCount(userDocumentList.size(), true);
		cellTable.setVisibleRange(0, 20);
		cellTable.setStyleName("filecelltable");
		cellTable.addColumnSortHandler(columnSortHandler);
		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");
		
		ButtonImageText button = new ButtonImageText(ButtonImageText.Type.NEW, lang._TextNewUserDocument());
		button.addClickHandler(new ClickHandler()
		{			
			public void onClick(ClickEvent event) {
				EditUserDocument eud = new EditUserDocument();
				eud.setRootPanel(rootPanel, null, lang._TextCreateANewUserDocument(), new EditUserDocument.EditUserDocumentResult() {

					@Override
					public void setResult() {							
						getAllContent();
					}

				});
				eud.setUserParameters(user);
				eud.onModuleLoad();
			}
		});
		simplePanelCenter.add(button);

		getAllContent();
	}

	@Override
	public void setResult() {
	}
}
