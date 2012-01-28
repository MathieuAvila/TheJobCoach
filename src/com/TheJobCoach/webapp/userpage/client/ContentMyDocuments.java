package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentMyDocuments implements EntryPoint {

	UserId user;

	final CellTable<UserDocument> cellTable = new CellTable<UserDocument>();
	TextBox textBoxName = new TextBox();
	TextArea textAreaDescription = new TextArea();
	DatePicker datePickerLastVisit = new DatePicker();
	UserDocument currentSite = null;

	private void setUserDocument(UserDocument document)
	{
		textBoxName.setValue(document.name);
		textAreaDescription.setValue(document.description);
		datePickerLastVisit.setValue(document.lastVisit);
		currentSite = document;
	}

	UserDocument getUserDocument()
	{
		if (currentSite == null) return null;
		UserDocument result = new UserDocument();
		result.ID = currentSite.ID;
		result.name = textBoxName.getValue();
		result.description = textAreaDescription.getValue();
		result.lastVisit = datePickerLastVisit.getValue();
		return result;
	}


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
	private List<UserDocument> jobSiteList = new ArrayList<UserDocument>();

	// Create a data provider.
	AsyncDataProvider<UserDocument> dataProvider = new AsyncDataProvider<UserDocument>() {
		@Override
		protected void onRangeChanged(HasData<UserDocument> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= jobSiteList.size() ) end = jobSiteList.size();
			if (jobSiteList.size() != 0)
			{
				List<UserDocument> dataInRange = jobSiteList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
			}
		}
	};

	void getOneSite(String documentId)
	{
		AsyncCallback<UserDocument> callback = new AsyncCallback<UserDocument>()	{
			@Override
			public void onFailure(Throwable caught)
			{
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(UserDocument result)
			{
				System.out.println(result);
				// Find position.
				for (int count=0; count != jobSiteList.size(); count++) {
					if (jobSiteList.get(count).ID.equals(result.ID))
					{
						jobSiteList.set(count, result);
					}
				}
				dataProvider.updateRowData(0, jobSiteList);
			}
		};
		try {
			userService.getUserDocument(user, documentId, callback);
		} catch (CassandraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void getAllContent()
	{		
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(List<String> result) {
				System.out.println(result);
				jobSiteList.clear();
				for (String idRes: result)
				{
					jobSiteList.add(new UserDocument(idRes,"", "" , new Date()));
					getOneSite(idRes);
				}
				dataProvider.updateRowCount(jobSiteList.size(), true);
				cellTable.redraw();
			}
		};
		try {
			userService.getUserDocumentList(user, callback);
		} catch (CassandraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	class DeleteHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			try {
				userService.deleteUserDocument(user, currentSite.ID, new AsyncCallback<Integer>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						Window.alert(caught.toString());
						//connectButton.setEnabled(true);
					}
					public void onSuccess(Integer result)
					{
						getAllContent();
					}
				});
			} 
			catch (CassandraException e) 
			{
				Window.alert(e.toString());
			}
		}
	}

	class SaveHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			try {
				userService.setUserDocument(user, getUserDocument(), new AsyncCallback<Integer>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						Window.alert(caught.toString());
						//connectButton.setEnabled(true);
					}
					public void onSuccess(Integer result)
					{
						getAllContent();
					}
				});
			} 
			catch (CassandraException e) 
			{
				Window.alert(e.toString());
			}
		}
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

		VerticalPanel simplePanelCenter = new VerticalPanel();
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

		// Create lastVisit column.
		TextColumn<UserDocument> downloadLastUpdate = new TextColumn<UserDocument>() {
			@SuppressWarnings("deprecation")
			@Override
			public String getValue(UserDocument document) 
			{
				return document.lastVisit.toLocaleString();
			}
		};

		nameColumn.setSortable(true);
		descriptionColumn.setSortable(true);
		downloadLastUpdate.setSortable(true);
		cellTable.addColumn(nameColumn, lang._TextName());
		cellTable.addColumn(descriptionColumn, lang._TextDescription());
		cellTable.addColumn(downloadLastUpdate, "Last update");
		cellTable.getColumnSortList().push(nameColumn);	

		// Add a selection model to handle user selection.
		final SingleSelectionModel<UserDocument> selectionModel = new SingleSelectionModel<UserDocument>();
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{
			public void onSelectionChange(SelectionChangeEvent event) 
			{
				UserDocument selected = selectionModel.getSelectedObject();
				if (selected != null) 
				{
					setUserDocument(selected);
					//Window.alert("You selected: " + selected.name);
				}
			}
		});

		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(jobSiteList.size(), true);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		getAllContent();
		cellTable.setRowData(0, jobSiteList);
		cellTable.setRowCount(jobSiteList.size(), true);
		cellTable.setVisibleRange(0, 5);
		cellTable.addColumnSortHandler(columnSortHandler);
		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");		

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");






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
			Button button = new Button("Submit");
			button.addClickHandler(new ClickHandler()
			{			
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					form.submit();
				}
			});
			button.setText(lang.button_text());
			holder.add(button);

			form.add(holder);
			form.setAction("/UploadFileServlet");

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
					// TODO Auto-generated method stub
					Window.alert(event.getResults());
				}
			});
			horizontalPanel.add(form);
		}

		Button buttonDeleteDocument = new Button("");
		buttonDeleteDocument.setText(lang._TextDeleteSite());
		horizontalPanel.add(buttonDeleteDocument);

		SimplePanel simplePanel = new SimplePanel();
		simplePanelCenter.add(simplePanel);
		simplePanel.setHeight("40px");

		Grid grid = new Grid(4, 2);
		simplePanelCenter.add(grid);
		simplePanelCenter.setCellHeight(grid, "100%");
		simplePanelCenter.setCellWidth(grid, "100%");
		grid.setSize("100%", "");

		Label lblName = new Label(lang._TextName());
		grid.setWidget(0, 0, lblName);

		grid.setWidget(0, 1, textBoxName);
		textBoxName.setWidth("100%");

		Label lblDescription = new Label(lang._TextDescription());
		grid.setWidget(1, 0, lblDescription);

		grid.setWidget(1, 1, textAreaDescription);
		textAreaDescription.setSize("100%", "50px");

		Label lblLastvisit = new Label(lang._TextLastVisit());
		grid.setWidget(2, 0, lblLastvisit);

		grid.setWidget(2, 1, datePickerLastVisit);

		Button buttonSave = new Button(lang._TextSave());
		grid.setWidget(3, 0, buttonSave);
		buttonSave.setWidth("150px");
		//buttonSave.addClickHandler(saveHandler);
		grid.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_LEFT);

		// Add a handler to the delete button.
		DeleteHandler deleteHandler = new DeleteHandler();
		buttonDeleteDocument.addClickHandler(deleteHandler);

		// Add a handler to the save button.
		SaveHandler saveHandler = new SaveHandler();
	}
}
