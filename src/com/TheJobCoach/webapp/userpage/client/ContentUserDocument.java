package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.EditUserDocument.EditUserDocumentResult;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserDocument implements EntryPoint, EditUserDocumentResult {

	final static Lang lang = GWT.create(Lang.class);

	UserId user;
	final ExtendedCellTable<UserDocument> cellTable = new ExtendedCellTable<UserDocument>();
	UserDocument currentSite = null;
	private final UserServiceAsync userService = GWT.create(UserService.class);
	Panel rootPanel;

	public ContentUserDocument(Panel _rootPanel, UserId userId)
	{
		user = userId;
		rootPanel = _rootPanel;
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
		EditUserDocument eud = new EditUserDocument(rootPanel, user, object, new EditUserDocument.EditUserDocumentResult() {
			@Override
			public void setResult() {							
				getAllContent();
			}
		});
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

		// Create type column.
		TextColumn<UserDocument> typeColumn = new TextColumn<UserDocument>() 	{
			@Override
			public String getValue(UserDocument document) 
			{
				return lang.documentTypeMap().get("documentTypeMap_" + UserDocument.documentTypeToString(document.type));
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
			@Override
			public String getValue(UserDocument document) 
			{
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(document.lastUpdate);							
			}
		};
		
		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<UserDocument, String>() {
			@Override
			public void update(int index, UserDocument object, String value) {
				deleteDoc(object);
			}}	
				);

		cellTable.addColumnWithIcon(IconCellSingle.IconType.UPDATE, new FieldUpdater<UserDocument, String>() {
			@Override
			public void update(int index, UserDocument object, String value) {
				updateDoc(object);
			}}	
				);
		cellTable.addColumnWithIconCellFile(
				new FieldUpdater<UserDocument, String>() {
					@Override
					public void update(int index, UserDocument object, String value) {
						if (object.revisions.size() != 0)
						{
							String id = object.revisions.get(object.revisions.size() - 1).ID;
							String copyURL = GWT.getModuleBaseURL() + "DownloadServlet?docid=" + URL.encodeQueryString(id) + "&userid=" + URL.encodeQueryString(user.userName)+ "&token=" + URL.encodeQueryString(user.token);
							DownloadIFrame iframe = new DownloadIFrame(copyURL);
							simplePanelCenter.add(iframe);
						}
					}},
					new GetValue<String, UserDocument>() {
						@Override
						public String getValue(UserDocument contact) {
							return contact.fileName;
						}},
						lang._TextFilename());

		statusColumn.setSortable(true);
		cellTable.addColumn(statusColumn, lang._TextStatus());
		cellTable.addColumn(typeColumn, lang._TextType());

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
		cellTable.addColumnSortHandler(columnSortHandler);
		simplePanelCenter.add(cellTable);		

		ButtonImageText button = new ButtonImageText(ButtonImageText.Type.NEW, lang._TextNewUserDocument());
		button.addClickHandler(new ClickHandler()
		{			
			public void onClick(ClickEvent event) {
				EditUserDocument eud = new EditUserDocument(rootPanel, user, null, new EditUserDocument.EditUserDocumentResult() {

					@Override
					public void setResult() {							
						getAllContent();
					}

				});
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
