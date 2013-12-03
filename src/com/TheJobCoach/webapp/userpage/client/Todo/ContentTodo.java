package com.TheJobCoach.webapp.userpage.client.Todo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.TodoCommon;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentTodo implements EntryPoint, IContentTodo {

	final static Lang lang = GWT.create(Lang.class);
	final static LangTodo langTodo = GWT.create(LangTodo.class);

	final static int MINIMUM_SIZE_W = 120;
	final static int MINIMUM_SIZE_H = 120;

	ButtonImageText button = new ButtonImageText(ButtonImageText.Type.NEW, langTodo._TextNewPostIt());
	
	UserId user;

	public ContentTodo(Panel rootPanel, UserId user) {
		this.user = user;
		this.rootPanel = rootPanel;
	}

	final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	// The list of data to display.
	private List<TodoEvent> userTodoEventList = new ArrayList<TodoEvent>();

	ITodoContainer surface = new TodoContainer(this);

	void getAllContent()
	{		
		ServerCallHelper<Vector<TodoEvent>> callback = new ServerCallHelper<Vector<TodoEvent>>(rootPanel) {
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

	public void deleteTodoEvent(final TodoEvent currentTodoEvent)
	{
		if (currentTodoEvent == null) return;
		MessageBox mb = new MessageBox(rootPanel, true, true, MessageBox.TYPE.QUESTION, 
				langTodo._TextConfirmDeleteTitle(), langTodo._TextConfirmDelete(), new MessageBox.ICallback() {

			public void complete(boolean ok) {				
				if(ok)
				{
					userService.deleteTodoEvent(user, currentTodoEvent.ID, false, new ServerCallHelper<Boolean>(rootPanel) {
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
	
	public void setTodoEvent(TodoEvent todo)
	{
		userService.setTodoEvent(this.user, todo, new ServerCallHelper<Boolean>(rootPanel));
	}

	public void newTodoEvent()
	{
		TodoEvent.EventColor color = TodoEvent.EventColor.YELLOW;
		TodoEvent event = new TodoEvent(
				SiteUUID.getDateUuid(), "", new HashMap<String,String>(), TodoCommon.PERSO_SUBSCRIBER_ID, TodoEvent.Priority.URGENT,  new Date(), color, 
				TodoEvent.NO_PLACE, TodoEvent.NO_PLACE, MINIMUM_SIZE_W, MINIMUM_SIZE_H);
		TodoEvent.orderOneTodoEvent(userTodoEventList, event, 1000);
		surface.onTodoEventCreated(event);

		userService.setTodoEvent(user, event, new ServerCallHelper<Boolean>(rootPanel) {
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

		button.addClickHandler(new ClickHandler()
		{			
			public void onClick(ClickEvent event) {
				newTodoEvent();
			}
		});
		simplePanelCenter.add(button);

		simplePanelCenter.add((Widget)surface);
	}
}
