package com.TheJobCoach.webapp.userpage.client.Todo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentTodo implements EntryPoint {

	final static Lang lang = GWT.create(Lang.class);
	final static LangTodo langTodo = GWT.create(LangTodo.class);

	final static int MINIMUM_SIZE_W = 120;
	final static int MINIMUM_SIZE_H = 120;
	
	UserId user;

	public ContentTodo(Panel rootPanel, UserId user) {
		this.user = user;
		this.rootPanel = rootPanel;
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
		
		private class TodoEventView extends SimplePanel implements
		MouseUpHandler, MouseDownHandler, MouseMoveHandler,
		ValueChangeHandler<String>, KeyUpHandler
		{
			private final TodoEvent myTodoEvent;

			private final DivElement titleElement;
			private final DivElement bcolor;
			private final DivElement done;

			private final TextArea content = new TextArea();

			private boolean dragging, colorChange;

			private int dragOffsetX, dragOffsetY;

			private int org_x, org_y, org_w, org_h;
			
			private SurfaceView surface;
			/**
			 * @param TodoEvent
			 *          the TodoEvent to render
			 */
			public TodoEventView(TodoEvent TodoEvent, SurfaceView surface) 
			{
				this.surface = surface;
				this.myTodoEvent = TodoEvent;
				setStyleName("TodoEvent");

				final Element elem = getElement();
				elem.getStyle().setProperty("position", "absolute");
				
				HorizontalPanel hp = new HorizontalPanel();
				SimplePanel titlePanel = new SimplePanel();
				titlePanel.setWidth("100%");
				hp.add(titlePanel);
				hp.setWidth("100%");
				elem.appendChild(hp.getElement());
				titleElement = titlePanel.getElement().appendChild(Document.get().createDivElement());
				titleElement.setClassName("TodoEvent-title");

				content.setStyleName("TodoEvent-content");
				content.addValueChangeHandler(this);
				content.addKeyUpHandler(this);

				content.addMouseUpHandler(this);

				setPixelPosition(myTodoEvent.x, myTodoEvent.y);
				setPixelSize(myTodoEvent.w, myTodoEvent.h);
				
				this.setWidget(content);

				addDomHandler(this, MouseDownEvent.getType());
				addDomHandler(this, MouseMoveEvent.getType());
				addDomHandler(this, MouseUpEvent.getType());
				

				checkForChange();
				
				SimplePanel colorPanel = new SimplePanel();
				colorPanel.setWidth("2em");
				hp.add(colorPanel);
				hp.setCellWidth(colorPanel, "2em");
				bcolor = colorPanel.getElement().appendChild(Document.get().createDivElement());
				bcolor.setClassName("TodoEvent-title");
				bcolor.setInnerHTML("C");
				colorPanel.getElement().appendChild(bcolor);
				
				SimplePanel donePanel = new SimplePanel();
				donePanel.setWidth("2em");
				hp.add(donePanel);
				hp.setCellWidth(donePanel, "2em");
				done = donePanel.getElement().appendChild(Document.get().createDivElement());
				done.setClassName("TodoEvent-title");
				done.setInnerHTML("X");
				donePanel.getElement().appendChild(done);
								
				render();
			}

			private boolean checkForChange()
			{
				final Style style = getElement().getStyle();
				int new_org_x = Integer.parseInt(style.getLeft().substring(0, (style.getLeft().length() - 2)));
				int new_org_y = Integer.parseInt(style.getTop().substring(0, (style.getTop().length() - 2)));
				
				int new_org_w = content.getOffsetWidth() - 2;
				int new_org_h = content.getOffsetHeight() - 2;
				
				boolean result = (new_org_x != org_x) || (new_org_y != org_y) || (new_org_h != org_h) || (new_org_w != org_w);
				
				org_x = new_org_x;
				org_y = new_org_y;
				org_w = new_org_w;
				org_h = new_org_h;
				
				if (org_w < MINIMUM_SIZE_W) org_w = MINIMUM_SIZE_W;
				if (org_h < MINIMUM_SIZE_H) org_h = MINIMUM_SIZE_H;
				
				return result;
			}

			void updateTodoEvent()
			{
				myTodoEvent.x = org_x;
				myTodoEvent.y = org_y;
				myTodoEvent.w = org_w;
				myTodoEvent.h = org_h;
				userService.setTodoEvent(user, myTodoEvent, new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) 
					{
						
					}
					public void onSuccess(Boolean result)
					{	
						// Really ? ... Don't care.
					}
				});
			}

			public void onMouseDown(MouseDownEvent event) {
				SurfaceView.this.select(this);

				final EventTarget target = event.getNativeEvent().getEventTarget();
				assert Element.is(target);
				if (!Element.is(target)) {
					return;
				}
				colorChange = false;				
				if (bcolor.isOrHasChild(Element.as(target))) 
				{
					myTodoEvent.color = myTodoEvent.getNextColor();
					render();
					updateTodoEvent();
					event.preventDefault();
					colorChange = true;
					return;
				}
				if (done.isOrHasChild(Element.as(target))) 
				{
					surface.onRemoveTodoEvent(myTodoEvent);
					event.preventDefault();
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
				if (dragging)
				{	    	  
					final Style style = getElement().getStyle();
					int orgX = Integer.parseInt(style.getLeft().substring(0, (style.getLeft().length() - 2)));
					int orgY = Integer.parseInt(style.getTop().substring(0, (style.getTop().length() - 2)));
					setPixelPosition(event.getX() + orgX - dragOffsetX,
							event.getY() + orgY - dragOffsetY);
					event.preventDefault();
				}
			}

			public void onMouseUp(MouseUpEvent event) 
			{
				if (colorChange)
				{
					colorChange = false;
					event.preventDefault();
					return;
				}
				if (dragging)
				{
					dragging = false;
					DOM.releaseCapture(getElement());
					event.preventDefault();
					if (checkForChange())
					{
						updateTodoEvent();
					}
					return;
				}				
				// Also check for resize.
				if (checkForChange())
				{
					updateTodoEvent();
				}				
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
				setPixelPosition(myTodoEvent.x, myTodoEvent.y);
				setPixelSize(myTodoEvent.w, myTodoEvent.h);
				titleElement.setInnerHTML("Todo");
				final String TodoEventContent = myTodoEvent.text;
				content.setText((TodoEventContent == null) ? "" : TodoEventContent);

				getElement().getStyle().setBackgroundColor(myTodoEvent.getHtmlColor());
				bcolor.getStyle().setBackgroundColor(myTodoEvent.getHtmlColor(myTodoEvent.getNextColor()));
				
			}

			private void select(int zIndex) {
				getElement().getStyle().setProperty("zIndex", "" + zIndex );
			}

			@Override
			public void onValueChange(ValueChangeEvent<String> event)
			{
				myTodoEvent.text = event.getValue();
				updateTodoEvent();
			}
			
			@Override
			public void onKeyUp(KeyUpEvent event)
			{
				if (!myTodoEvent.text.equals(content.getValue()))
				{
					myTodoEvent.text = content.getValue();			
					updateTodoEvent();
				}
			}
		}

		private TodoEventView selectedTodoEventView;

		private HashMap<String, TodoEventView> content = new HashMap<String, TodoEventView>();
		
		private final ContentTodo contentTodo;
		
		public SurfaceView(ContentTodo contentTodo) {
			final Element elem = getElement();
			elem.setId("surface");
			elem.getStyle().setProperty("position", "absolute");
			this.contentTodo = contentTodo;
		}


		public void onTodoEventReceived(TodoEvent TodoEvent) {
			final TodoEventView view = new TodoEventView(TodoEvent, this);
			add(view);
			select(view);
			content.put(TodoEvent.ID, view);
		}

		public void onTodoEventCreated(TodoEvent TodoEvent) {
			onTodoEventReceived(TodoEvent);
			content.get(TodoEvent.ID).updateTodoEvent();
		}

		public void onRemoveTodoEvent(TodoEvent id) {			
				//this.remove(content.get(id.ID));
				//content.remove(id.ID);
				contentTodo.deleteTodoEvent(id);
		}

		public void confirmRemoveTodoEvent(TodoEvent id)
		{
			this.remove(content.get(id.ID));
			content.remove(id.ID);
		}
		
		private void select(TodoEventView TodoEventView) 
		{			
			assert TodoEventView != null;
			if (selectedTodoEventView != TodoEventView)
			{
				selectedTodoEventView = TodoEventView;
				for (TodoEventView view: content.values())
				{
					view.select(TodoEventView == view ? 1 : 0);				
				}
			}
		}
	}

	// The list of data to display.
	private List<TodoEvent> userTodoEventList = new ArrayList<TodoEvent>();

	SurfaceView surface = new SurfaceView(this);

	void getAllContent()
	{		
		AsyncCallback<Vector<TodoEvent>> callback = new AsyncCallback<Vector<TodoEvent>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Vector<TodoEvent> result) {
				userTodoEventList.clear();
				userTodoEventList.addAll(result);				
				for (TodoEvent todo: userTodoEventList)
				{
					surface.onTodoEventReceived(todo);
				}
			}
		};
		userService.getTodoEventList(user, "FR", callback);
	}
	
	void deleteTodoEvent(final TodoEvent currentTodoEvent)
	{
		MessageBox mb = new MessageBox(rootPanel, true, true, MessageBox.TYPE.QUESTION, 
				langTodo._TextConfirmDeleteTitle(), langTodo._TextConfirmDelete(), new MessageBox.ICallback() {

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
							userTodoEventList.remove(currentTodoEvent);
							surface.confirmRemoveTodoEvent(currentTodoEvent);							
						}
					});
				}
			}
		});
		mb.onModuleLoad();
	}

	public void newTodoEvent()
	{
		TodoEvent.EventColor color = TodoEvent.EventColor.YELLOW;
		int colorInt = Random.nextInt(10);
		switch (colorInt)
		{
		case 0: color = TodoEvent.EventColor.WHITE; break;
		case 1: color = TodoEvent.EventColor.GREEN; break;
		case 2: color = TodoEvent.EventColor.BLUE; break;
		case 3: color = TodoEvent.EventColor.RED; break;
		case 4: color = TodoEvent.EventColor.LIGHTGREEN; break;
		case 5: color = TodoEvent.EventColor.LIGHTBLUE; break;
		case 6: color = TodoEvent.EventColor.LIGHTRED; break;
		case 7: color = TodoEvent.EventColor.YELLOW; break;
		case 8: color = TodoEvent.EventColor.ORANGE; break;		
		}
		TodoEvent event = new TodoEvent(
				SiteUUID.getDateUuid(), "", new HashMap<String,String>(), "", TodoEvent.Priority.URGENT,  new Date(), color, 
				TodoEvent.NO_PLACE, TodoEvent.NO_PLACE, MINIMUM_SIZE_W, MINIMUM_SIZE_H);
		TodoEvent.orderOneTodoEvent(userTodoEventList, event, 1000);
		surface.onTodoEventCreated(event);

		userService.setTodoEvent(user, event, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) 
			{
				MessageBox.messageBoxException(rootPanel, caught.getLocalizedMessage());
			}
			public void onSuccess(Boolean result)
			{
				// Get away.
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

		ContentHelper.insertTitlePanel(simplePanelCenter, lang._TextTodo(), ClientImageBundle.INSTANCE.todoContent());

		getAllContent();
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

		ButtonImageText button = new ButtonImageText(ButtonImageText.Type.NEW, langTodo._TextNewPostIt());
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
