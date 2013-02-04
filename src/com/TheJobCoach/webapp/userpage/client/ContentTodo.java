package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.Date;
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
import com.TheJobCoach.webapp.util.shared.SiteUUID;
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



import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.WidgetCollection;

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

	/**
	 * A widget to display the collection of TodoEvents that are on a particular
	 * {@link Surface}.
	 *
	 */
	class SurfaceView extends FlowPanel {

	  /**
	   * A widget for displaying a single {@link TodoEvent}.
	   */
	  private class TodoEventView extends SimplePanel implements //TodoEvent.Observer,
	      MouseUpHandler, MouseDownHandler, MouseMoveHandler,
	      ValueChangeHandler<String> {
	    private final TodoEvent TodoEvent;

	    private final DivElement titleElement;

	    private final TextArea content = new TextArea();

	    private boolean dragging;

	    private int dragOffsetX, dragOffsetY;

	    private int orgX, orgY;
	    
	    /**
	     * @param TodoEvent
	     *          the TodoEvent to render
	     */
	    public TodoEventView(TodoEvent TodoEvent) {
	      this.TodoEvent = TodoEvent;
	      setStyleName("TodoEvent");
	      //TodoEvent.setObserver(this);

	      final Element elem = getElement();
	      elem.getStyle().setProperty("position", "absolute");
	      //elem.getStyle().setProperty("position", "relative");
	      titleElement = elem.appendChild(Document.get().createDivElement());
	      titleElement.setClassName("TodoEvent-title");

	      content.setStyleName("TodoEvent-content");
	      content.addValueChangeHandler(this);
	      setWidget(content);

	      render();

	      addDomHandler(this, MouseDownEvent.getType());
	      addDomHandler(this, MouseMoveEvent.getType());
	      addDomHandler(this, MouseUpEvent.getType());
	      
	      final Style style = getElement().getStyle();
	      orgX = Integer.parseInt(style.getLeft().substring(0, (style.getLeft().length() - 2)));
	      orgY = Integer.parseInt(style.getTop().substring(0, (style.getTop().length() - 2)));
	      System.out.println("OrgX : " + orgX + " OrgY : " + orgY);
	    }

	    public void onMouseDown(MouseDownEvent event) {
	      SurfaceView.this.select(this);
	      
	      final EventTarget target = event.getNativeEvent().getEventTarget();
	      assert Element.is(target);
	      if (!Element.is(target)) {
	        return;
	      }

	      if (titleElement.isOrHasChild(Element.as(target))) {
	        dragging = true;
	        final Element elem = getElement().cast();
	        dragOffsetX = event.getX();
	        dragOffsetY = event.getY();
	        DOM.setCapture(elem);
	        event.preventDefault();
	      }
	    }

	    public void onMouseMove(MouseMoveEvent event) {
	      if (dragging) {
	    	  
	    	  final Style style = getElement().getStyle();
	    	  orgX = Integer.parseInt(style.getLeft().substring(0, (style.getLeft().length() - 2)));
	    	  orgY = Integer.parseInt(style.getTop().substring(0, (style.getTop().length() - 2)));
	    	  System.out.println("OrgX : " + orgX + " OrgY : " + orgY);

	        //setPixelPosition(event.getX() + getAbsoluteLeft() - dragOffsetX - orgX,
	        //    event.getY() + getAbsoluteTop() - dragOffsetY - orgY);
	    	setPixelPosition(event.getX() + orgX - dragOffsetX,
		                     event.getY() + orgY - dragOffsetY);
	        event.preventDefault();
	      }
	    }

	    public void onMouseUp(MouseUpEvent event) {
	      if (dragging) {
	        dragging = false;
	        DOM.releaseCapture(getElement());
	        event.preventDefault();
	        //model.updateTodoEventPosition(TodoEvent, getAbsoluteLeft(), getAbsoluteTop(),
	        //    TodoEvent.getWidth(), TodoEvent.getHeight());
	      }
	    }

	    public void onUpdate(TodoEvent TodoEvent) {
	      render();
	    }

	    public void onValueChange(ValueChangeEvent<String> event) {
	      //model.updateTodoEventContent(TodoEvent, event.getValue());
	    }

	    public void setPixelPosition(int x, int y) {
	      final Style style = getElement().getStyle();
	      style.setPropertyPx("left", x);
	      style.setPropertyPx("top", y);
	    }

	    public void setPixelSize(int width, int height) {
	      content.setPixelSize(width, height);
	    }

	    private void render() {
	      setPixelPosition(TodoEvent.x, TodoEvent.y);

	      setPixelSize(100, 100);

	      titleElement.setInnerHTML("Todo");

	      final String TodoEventContent = TodoEvent.trText;
	      content.setText((TodoEventContent == null) ? "" : TodoEventContent);	      
	    }

	    private void select() {
	      getElement().getStyle().setProperty("zIndex", "" + 1 ); //nextZIndex());
	    }
	  }

	  //private final Model model;

	  private TodoEventView selectedTodoEventView;

	  private int zIndex = 1;

	  /**
	   * @param model
	   *          the model to which the Ui will bind itself
	   */
	  public SurfaceView() { //Model model) {
	    //this.model = model;
	    final Element elem = getElement();
	    elem.setId("surface");
	    elem.getStyle().setProperty("position", "absolute");
	   // model.addDataObserver(this);
	  }

	  public void onTodoEventCreated(TodoEvent TodoEvent) {
	    final TodoEventView view = new TodoEventView(TodoEvent);
	    add(view);
	    select(view);
	  }

	  public void onSurfaceTodoEventsReceived(TodoEvent[] TodoEvents) {
	    removeAllTodoEvents();
	    for (int i = 0, n = TodoEvents.length; i < n; ++i) {
	      add(new TodoEventView(TodoEvents[i]));
	    }
	  }

	  private void removeAllTodoEvents() {
	    final WidgetCollection kids = getChildren();
	    while (kids.size() > 0) {
	      remove(kids.size() - 1);
	    }
	  }

	  private void select(TodoEventView TodoEventView) {
	    assert TodoEventView != null;
	    if (selectedTodoEventView != TodoEventView) {
	      TodoEventView.select();
	      selectedTodoEventView = TodoEventView;
	    }
	  }
	}

	// The list of data to display.
	private List<TodoEvent> userTodoEventList = new ArrayList<TodoEvent>();

	SurfaceView surface = new SurfaceView();
	
	// Create a data provider.
	AsyncDataProvider<TodoEvent> dataProvider = new AsyncDataProvider<TodoEvent>() {
		@Override
		protected void onRangeChanged(HasData<TodoEvent> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= userTodoEventList.size() ) end = userTodoEventList.size();
			System.out.println("Sizes: " + userTodoEventList.size());
			if (userTodoEventList.size() != 0)
			{
				List<TodoEvent> dataInRange = userTodoEventList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
				System.out.println("datainrange: " + dataInRange);
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
				userTodoEventList.clear();
				userTodoEventList.addAll(result);
				dataProvider.updateRowCount(userTodoEventList.size(), true);
				dataProvider.updateRowData(0, userTodoEventList.subList(0, userTodoEventList.size()));
				
				cellTable.redraw();

				for (TodoEvent todo: userTodoEventList)
				{
					surface.onTodoEventCreated(todo);
				}
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
		TodoEvent event = new TodoEvent("id" + SiteUUID.getDateUuid(), "todo1", "todo1#test#FR",  "test", TodoEvent.Priority.URGENT,  new Date(1), TodoEvent.EventColor.BLUE, 11, 1);		
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
		dataProvider.updateRowCount(userTodoEventList.size(), true);

		AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
		getAllContent();
		cellTable.setRowData(0, userTodoEventList);
		cellTable.setRowCount(userTodoEventList.size(), true);
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

		simplePanelCenter.add(surface);
	}
}
