package com.TheJobCoach.webapp.userpage.client.Todo;

import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.google.gwt.user.client.ui.HasVisibility;

interface ITodoContainer extends HasVisibility{

	public void onTodoEventReceived(TodoEvent TodoEvent);
	
	public void onTodoEventCreated(TodoEvent TodoEvent);

	public void onRemoveTodoEvent(TodoEvent id);

	public void confirmRemoveTodoEvent(TodoEvent id);

	public void notifyVisibility(String key, int valueInt);
}