package com.TheJobCoach.webapp.adminpage.client;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentNews implements EntryPoint {

	UserId user;
	DatePicker datePicker;
	RichTextArea richTextArea;
	TextBox titleTextBox;
	
	public void setUserParameters(UserId _user)
	{
		user = _user;
	}

	private final AdminServiceAsync adminService = GWT.create(AdminService.class);

	Panel rootPanel;

	public void setRootPanel(Panel panel)
	{
		rootPanel = panel;
	}

	// The list of data to display.
	private List<NewsInformation> newsList = new ArrayList<NewsInformation>();

	final CellTable<NewsInformation> cellTable = new CellTable<NewsInformation>();

	// Create a data provider.
	AsyncDataProvider<NewsInformation> dataProvider = new AsyncDataProvider<NewsInformation>() {

		@Override
		protected void onRangeChanged(HasData<NewsInformation> display) {
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= newsList.size() ) end = newsList.size();
			if (newsList.size() != 0)
			{
				List<NewsInformation> dataInRange = newsList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
			}
		}
	};

	@SuppressWarnings("deprecation")
	void getAllContent()
	{		
		ServerCallHelper<Vector<NewsInformation>> callback = new ServerCallHelper<Vector<NewsInformation>>(rootPanel) {
			@Override
			public void onSuccess(Vector<NewsInformation> result) {
				newsList = result;
				cellTable.setVisibleRange(0, newsList.size());
				dataProvider.updateRowData(0, newsList);
				dataProvider.updateRowCount(newsList.size(), true);
				cellTable.redraw();
			}
		};
		Date start = new Date();
		start.setYear(start.getYear() - 1);
		Date end = new Date();
		end.setYear(end.getYear() + 1);
		adminService.getNewsInformationList(user, start, end, callback);
	}

	String newsId = "";
	
	NewsInformation getNewsInformation()
	{
		Date d = new Date();
		Date pick = datePicker.getValue();
		if (pick == null) pick = d;
		return new NewsInformation(newsId, pick, titleTextBox.getValue(), richTextArea.getHTML());
	}
	
	void setNewsInformation(NewsInformation news)
	{
		newsId = news.ID;
		datePicker.setValue(news.created);
		titleTextBox.setValue(news.title);
		richTextArea.setHTML(news.text);
	}

	class DeleteHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			adminService.deleteNewsInformation(user, getNewsInformation(), new ServerCallHelper<String>(rootPanel) {
				public void onSuccess(String result)
				{
					getAllContent();
				}
			});
		}
	}

	class SaveHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{		
			adminService.createNewsInformation(user, getNewsInformation(), new ServerCallHelper<String>(rootPanel) {
				public void onSuccess(String result)
				{
					getAllContent();
				}
			});
		}
	}

	class NewSiteHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			NewsInformation news = getNewsInformation();
			news.ID = SiteUUID.getDateUuid();
			adminService.createNewsInformation(user, news, new ServerCallHelper<String>(rootPanel) {
				public void onSuccess(String result)
				{
					getAllContent();
				}
			});
		}
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
		simplePanelCenter.setSize("100%", "634px");
		rootPanel.add(simplePanelCenter);

		// Create name column.
		TextColumn<NewsInformation> idColumn = new TextColumn<NewsInformation>() 	{
			@Override
			public String getValue(NewsInformation report) 
			{
				return report.ID;
			}
		};
		idColumn.setSortable(true);
		cellTable.addColumn(idColumn, "Id");

		// Create token column.
		TextColumn<NewsInformation> titleColumn = new TextColumn<NewsInformation>() 	{
			@Override
			public String getValue(NewsInformation report) 
			{
				return report.title;
			}
		};
		titleColumn.setSortable(true);
		cellTable.addColumn(titleColumn, "Title");

		// Create validated column.
		TextColumn<NewsInformation> textColumn = new TextColumn<NewsInformation>() 	{
			@Override
			public String getValue(NewsInformation report) 
			{
				return report.text;
			}
		};
		textColumn.setSortable(true);
		cellTable.addColumn(textColumn, "Text");

		// Create created date column.
		TextColumn<NewsInformation> createdColumn = new TextColumn<NewsInformation>() 	{
			@SuppressWarnings("deprecation")
			@Override
			public String getValue(NewsInformation report) 
			{
				return report.created.toGMTString();
			}
		};
		createdColumn.setSortable(true);
		cellTable.addColumn(createdColumn, "Created on");

		// Add a selection model to handle user selection.
		final SingleSelectionModel<NewsInformation> selectionModel = new SingleSelectionModel<NewsInformation>();
		cellTable.setSelectionModel(selectionModel);		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{
			public void onSelectionChange(SelectionChangeEvent event) 
			{
				NewsInformation selected = selectionModel.getSelectedObject();
				if (selected != null) 
				{
					setNewsInformation(selected);
				}
			}
		});		

		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(newsList.size(), true);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		getAllContent();
		cellTable.setRowData(0, newsList);
		cellTable.setRowCount(newsList.size(), true);
		cellTable.setVisibleRange(0, newsList.size());
		cellTable.addColumnSortHandler(columnSortHandler);
		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		simplePanelCenter.setCellWidth(horizontalPanel, "100%");
		horizontalPanel.setWidth("100%");

		Button btnNew = new Button("New");
		horizontalPanel.add(btnNew);

		Button btnUpdate = new Button("Update");
		horizontalPanel.add(btnUpdate);

		Button btnDelete = new Button("Delete");
		horizontalPanel.add(btnDelete);

		Grid grid = new Grid(3, 2);
		simplePanelCenter.add(grid);
		grid.setSize("100%", "100%");

		Label lblDate = new Label("Date");
		grid.setWidget(0, 0, lblDate);

		datePicker = new DatePicker();
		grid.setWidget(0, 1, datePicker);

		Label lblText = new Label("Text");
		grid.setWidget(1, 0, lblText);
		lblText.setHeight("43px");

		richTextArea = new RichTextArea();
		grid.setWidget(1, 1, richTextArea);

		Label lblTitle = new Label("Title");
		grid.setWidget(2, 0, lblTitle);

		titleTextBox = new TextBox();
		grid.setWidget(2, 1, titleTextBox);

		// Add a handler to the delete button.
		DeleteHandler deleteHandler = new DeleteHandler();
		btnDelete.addClickHandler(deleteHandler);

		// Add a handler to the save button.
		SaveHandler saveHandler = new SaveHandler();
		btnUpdate.addClickHandler(saveHandler);

		NewSiteHandler newHandler = new NewSiteHandler();
		btnNew.addClickHandler(newHandler);
	}
}
