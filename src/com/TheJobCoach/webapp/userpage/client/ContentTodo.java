package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentTodo implements EntryPoint {

	final Lang lang = GWT.create(Lang.class);
	
	UserId user;

	final ExtendedCellTable<TodoEvent> cellTable = new ExtendedCellTable<TodoEvent>();
	TodoEvent currentTodoEvent = null;

	public ContentTodo(Panel rootPanel, UserId user) {
		this.user = user;
		this.rootPanel = rootPanel;
	}

	private void setTodoEvent(TodoEvent TodoEvent)
	{
		currentTodoEvent = TodoEvent;
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
	private List<TodoEvent> jobTodoEventList = new ArrayList<TodoEvent>();

	// Create a data provider.
	AsyncDataProvider<TodoEvent> dataProvider = new AsyncDataProvider<TodoEvent>() {
		@Override
		protected void onRangeChanged(HasData<TodoEvent> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= jobTodoEventList.size() ) end = jobTodoEventList.size();
			if (jobTodoEventList.size() != 0)
			{
				List<TodoEvent> dataInRange = jobTodoEventList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
			}
		}
	};

	void getAllContent()
	{		
		AsyncCallback<Vector<TodoEvent>> callback = new AsyncCallback<Vector<TodoEvent>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Vector<TodoEvent> result) {
				System.out.println(result);
				jobTodoEventList.clear();				
				dataProvider.updateRowCount(jobTodoEventList.size(), true);
				cellTable.redraw();
			}
		};
		userService.getTodoEventList(user, "FR", callback);
	}


	void deleteTodoEvent(final TodoEvent currentTodoEvent)
	{
		MessageBox mb = new MessageBox(rootPanel, true, true, MessageBox.TYPE.QUESTION, 
				"Confirmation", "Vraiment supprimer ?", new MessageBox.ICallback() {
					
					public void complete(boolean ok) {
						if(ok)
						{
							userService.deleteTodoEvent(user, currentTodoEvent, new AsyncCallback<Boolean>() {
								public void onFailure(Throwable caught) {
									// Show the RPC error message to the user
									MessageBox.messageBoxException(rootPanel, caught.toString());										
								}
								public void onSuccess(Boolean result)
								{
									getAllContent();
								}
							});
						}
					}
				});
	mb.onModuleLoad();
	}

	public void newTodoEvent()
	{
		TodoEvent event = new TodoEvent();
		userService.setTodoEvent(user, event, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert(caught.toString());
				//connectButton.setEnabled(true);
			}
			public void onSuccess(Boolean result)
			{
				getAllContent();
			}
		});
	}

	void updateTodoEvent(TodoEvent currentTodoEvent)
	{
		userService.setTodoEvent(user, currentTodoEvent, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert(caught.toString());
				//connectButton.setEnabled(true);
			}
			public void onSuccess(Boolean result)
			{
				getAllContent();
			}
		});
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "");
		rootPanel.add(simplePanelCenter);

		ContentHelper.insertTitlePanel(simplePanelCenter, "Reste à faire", ClientImageBundle.INSTANCE.newsContent());
		
		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<TodoEvent, String>() {
			@Override
			public void update(int index, TodoEvent object, String value) {
				deleteTodoEvent(object);
			}}	
				);

		cellTable.addColumnWithIcon(IconCellSingle.IconType.UPDATE, new FieldUpdater<TodoEvent, String>() {
			@Override
			public void update(int index, TodoEvent object, String value) {
				updateTodoEvent(object);
			}}	
				);
		
		// Create text column.
		TextColumn<TodoEvent> textColumn = new TextColumn<TodoEvent>() 	{
			@Override
			public String getValue(TodoEvent TodoEvent) 
			{
				return TodoEvent.trText;
			}
		};

		// Create eventDate column.
		TextColumn<TodoEvent> eventDateColumn = new TextColumn<TodoEvent>() {
			@Override
			public String getValue(TodoEvent TodoEvent) 
			{
				return TodoEvent.eventDate.toString();
			}
		};

		textColumn.setSortable(true);
		eventDateColumn.setSortable(true);
		
		cellTable.addColumn(textColumn, lang._TextName());
		cellTable.addColumn(eventDateColumn, lang._TextDescription());
		
		// Add a selection model to handle user selection.
		final SingleSelectionModel<TodoEvent> selectionModel = new SingleSelectionModel<TodoEvent>();
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{
			public void onSelectionChange(SelectionChangeEvent event) 
			{
				TodoEvent selected = selectionModel.getSelectedObject();
				if (selected != null) 
				{
					setTodoEvent(selected);
				}
			}
		});

		dataProvider.addDataDisplay(cellTable);
		dataProvider.updateRowCount(jobTodoEventList.size(), true);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		getAllContent();
		cellTable.setRowData(0, jobTodoEventList);
		cellTable.setRowCount(jobTodoEventList.size(), true);
		cellTable.addColumnSortHandler(columnSortHandler);
		
		simplePanelCenter.add(cellTable);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

		ButtonImageText button = new ButtonImageText(ButtonImageText.Type.NEW, "Nouveau post-it");
		button.addClickHandler(new ClickHandler()
		{			
			public void onClick(ClickEvent event) {
				newTodoEvent();
			}
		});
		simplePanelCenter.add(button);
	}
}
