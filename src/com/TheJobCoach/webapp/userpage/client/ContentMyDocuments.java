package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.EditUserDocument.EditUserDocumentResult;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
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
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentMyDocuments implements EntryPoint, ValueUpdater<UserDocument> {

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
				userDocumentList = result;				
				dataProvider.updateRowCount(userDocumentList.size(), true);
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
		Lang lang = GWT.create(Lang.class);
		System.out.println("Load Content User Site, locale is: " + LocaleInfo.getCurrentLocale().getLocaleName());				

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel rootPanel = RootPanel.get("centercontent");
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		final VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "100%");
		rootPanel.add(simplePanelCenter);
		//cellTable
		//cellTable.setRowCount(5);
		//cellTable.setVisibleRange(0, 3);

		// Create name column.
		TextColumn<UserDocument> nameColumn = new TextColumn<UserDocument>() 	{
			@Override
			public String getValue(UserDocument document) 
			{
				return document.name;
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
		
		IconCellFile iconCell = new IconCellFile(anchorcolumn);

		cellTable.addColumn(addColumn(iconCell, new GetValue<String>() {
			public String getValue(UserDocument contact) {
				return contact.fileName;
			}
		},
		new FieldUpdater<UserDocument, String>() {
			public void update(int index, UserDocument object, String value) {
				Window.alert("You clicked " + object.name);
				String copyURL = "http://127.0.0.1:8888/thejobcoach/DownloadServlet?ID=" + URL.encode(object.ID);
				DownloadIFrame iframe = new DownloadIFrame(copyURL);
				simplePanelCenter.add(iframe);
				}
		}), "File name");
		
		
		IconCellSingle deleteCell =	new IconCellSingle(IconCellSingle.IconType.DELETE);		
		cellTable.addColumn(addColumn(deleteCell, new GetValue<String>() {
			public String getValue(UserDocument contact) {
				return "&nbsp;";//contact.fileName;
			}
		},
		new FieldUpdater<UserDocument, String>() {
			public void update(int index, UserDocument object, String value) {
				Window.alert("You delete " + object.name);				
			}
		}), "Delete");
		
		IconCellSingle updateCell =	new IconCellSingle(IconCellSingle.IconType.UPDATE);		
		cellTable.addColumn(addColumn(updateCell, new GetValue<String>() {
			public String getValue(UserDocument contact) {
				return "&nbsp;";//contact.fileName;
			}
		},
		new FieldUpdater<UserDocument, String>() {
			public void update(int index, UserDocument object, String value) {
				Window.alert("You update " + object.name);
			}
		}), "Update");
		
		
		nameColumn.setSortable(true);
		descriptionColumn.setSortable(true);
		downloadLastUpdate.setSortable(true);
		cellTable.addColumn(nameColumn, lang._TextName());
		cellTable.addColumn(descriptionColumn, lang._TextDescription());
		cellTable.addColumn(downloadLastUpdate, "Last update");
		cellTable.getColumnSortList().push(nameColumn);		

		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(userDocumentList.size(), true);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		getAllContent();
		cellTable.setRowData(0, userDocumentList);
		cellTable.setRowCount(userDocumentList.size(), true);
		cellTable.setVisibleRange(0, 5);
		cellTable.setStyleName("filecelltable");
		cellTable.addColumnSortHandler(columnSortHandler);
		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");		

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

/*
		{

			final FormPanel form = new FormPanel();
			form.setEncoding(FormPanel.ENCODING_MULTIPART);
			form.setMethod(FormPanel.METHOD_POST);
			form.addStyleName("table-center");
			form.addStyleName("demo-panel-padded");

			HorizontalPanel holder = new HorizontalPanel();
			FileUpload upload = new FileUpload();
			upload.setName("upload");
			holder.add(upload);

			HTML uploadHtml = new HTML("<hr />");
			holder.add(uploadHtml);
			holder.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
			ButtonImageText button = new ButtonImageText(ButtonImageText.Type.NEW, "Add new document");
			button.addClickHandler(new ClickHandler()
			{			
				public void onClick(ClickEvent event) {
					form.submit();
				}
			});
			holder.add(button);

			form.add(holder);
			String copyURL = "http://127.0.0.1:8888/thejobcoach/UploadServlet?docid=" + URL.encode("toto.xml");
			form.setAction(copyURL);

			form.addSubmitHandler(new FormPanel.SubmitHandler() 
			{		
				@Override
				public void onSubmit(SubmitEvent event) 
				{		       
				}			
			});
			form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() 
			{		      
				@Override
				public void onSubmitComplete(SubmitCompleteEvent event)
				{
					Window.alert(event.getResults());
				}
			});
			horizontalPanel.add(form);
			
			}
			*/

			ButtonImageText button = new ButtonImageText(ButtonImageText.Type.NEW, "Add new document");
			button.addClickHandler(new ClickHandler()
			{			
				public void onClick(ClickEvent event) {
						EditUserDocument eud = new EditUserDocument();						
						eud.setRootPanel(rootPanel, new UserDocument(), 
								new EditUserDocumentResult() {

									@Override
									public void setResult(UserDocument result) {
										
									}

						}, 
						"Create new user document");
						eud.setUserParameters(user);
						eud.onModuleLoad();
				}
			});
			simplePanelCenter.add(button);
			
	}

	@Override
	public void update(UserDocument value) {
		System.out.println("Something happened in " + value.fileName);
	}
}
