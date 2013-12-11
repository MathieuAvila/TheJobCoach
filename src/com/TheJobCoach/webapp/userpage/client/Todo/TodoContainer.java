package com.TheJobCoach.webapp.userpage.client.Todo;

import java.util.Date;
import java.util.HashMap;

import sun.java2d.Surface;

import com.TheJobCoach.webapp.userpage.shared.TodoCommon;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachSettings;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * A widget to display the collection of TodoEvents that are on a particular
 * {@link Surface}.
 *
 */
class TodoContainer extends FlowPanel implements ITodoContainer {

	private class TodoEventView extends SimplePanel implements
	MouseUpHandler, MouseDownHandler, MouseMoveHandler,
	ValueChangeHandler<String>, KeyUpHandler
	{
		final TodoEvent myTodoEvent;

		private final DivElement titleElement;
		private final DivElement bcolor;
		private final DivElement done;

		private final TextArea content = new TextArea();

		private boolean dragging, colorChange;

		private int dragOffsetX, dragOffsetY;

		private int org_x, org_y, org_w, org_h;

		private TodoContainer surface;
		/**
		 * @param TodoEvent
		 *          the TodoEvent to render
		 */
		public TodoEventView(TodoEvent TodoEvent, TodoContainer surface) 
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

			if (org_w < ContentTodo.MINIMUM_SIZE_W) org_w = ContentTodo.MINIMUM_SIZE_W;
			if (org_h < ContentTodo.MINIMUM_SIZE_H) org_h = ContentTodo.MINIMUM_SIZE_H;

			return result;
		}

		void updateTodoEvent()
		{
			myTodoEvent.x = org_x;
			myTodoEvent.y = org_y;
			myTodoEvent.w = org_w;
			myTodoEvent.h = org_h;
			contentTodo.setTodoEvent(myTodoEvent);
		}

		public void onMouseDown(MouseDownEvent event) {
			TodoContainer.this.select(this);

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
			titleElement.setInnerHTML(TodoStrings.getTitle(myTodoEvent));
			content.setText(TodoStrings.getText(myTodoEvent));
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

	private final IContentTodo contentTodo;

	private HashMap<String, Integer> visibilitySettings = new HashMap<String, Integer>();
	
	final static HashMap<String, String> visibilityKeyMap = new HashMap<String, String>();
	{
        // Initialize association between KEY value and TodoEvent's system key
		visibilityKeyMap.put(
				TodoCommon.SITEMANAGER_SUBSCRIBER_ID, 
				UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_SITE_DELAY);
		visibilityKeyMap.put(
				TodoCommon.PERSO_SUBSCRIBER_ID, 
				"PERSO");
		visibilityKeyMap.put(
				TodoCommon.EXTERNALCONTACTMANAGER_SUBSCRIBER_ID, 
				UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_CONTACT_DELAY);
		visibilityKeyMap.put(
				UserLogEntry.entryTypeToString(UserLogEntry.LogEntryType.INTERVIEW), 
				UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_INTERVIEW);
		visibilityKeyMap.put(
				UserLogEntry.entryTypeToString(UserLogEntry.LogEntryType.RECALL), 
				UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_OPPORTUNITY_RECALL);
		visibilityKeyMap.put(
				UserLogEntry.entryTypeToString(UserLogEntry.LogEntryType.EVENT), 
				UserValuesConstantsCoachSettings.COACHSETTINGS_TODO_EVENT);
		
		// Initialize PERSO visibility
		visibilitySettings.put("PERSO", Integer.MAX_VALUE / 1000 /25 /100/100); // Over the beginning of the universe
	}
	
	public boolean isVisible(TodoEvent event)
	{
		if (TodoCommon.PERSO_SUBSCRIBER_ID.equals(event.eventSubscriber))
		{
			return true;
		}
		if (visibilitySettings.containsKey(visibilityKeyMap.get(event.eventSubscriber)))
		{
			int days = visibilitySettings.get(visibilityKeyMap.get(event.eventSubscriber));
			Date newDate = new Date(event.eventDate.getTime() - days * 24*60*60*1000);
			return (newDate.before(new Date()));
		}
		return false;
	}
	
	public TodoContainer(IContentTodo contentTodo) 
	{
		final Element elem = getElement();
		elem.setId("surface");
		elem.getStyle().setProperty("position", "absolute");
		this.contentTodo = contentTodo;
	}

	public void onTodoEventReceived(TodoEvent todoEvent) 
	{
		final TodoEventView view = new TodoEventView(todoEvent, this);
		add(view);
		select(view);
		content.put(todoEvent.ID, view);
		view.setVisible(isVisible(todoEvent));
	}

	public void onTodoEventCreated(TodoEvent TodoEvent) 
	{
		onTodoEventReceived(TodoEvent);
		content.get(TodoEvent.ID).updateTodoEvent();
	}

	public void onRemoveTodoEvent(TodoEvent id) 
	{			
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

	@Override
	public void notifyVisibility(String key, int valueInt)
	{
		visibilitySettings.put(key, valueInt);
		updateVisibility();
	}

	private void updateVisibility()
	{
		for (TodoEventView ev: content.values())
		{
			ev.setVisible(isVisible(ev.myTodoEvent));
		}
	}
}