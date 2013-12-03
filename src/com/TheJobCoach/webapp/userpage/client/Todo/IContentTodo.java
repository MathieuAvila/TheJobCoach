package com.TheJobCoach.webapp.userpage.client.Todo;

import com.TheJobCoach.webapp.userpage.shared.TodoEvent;

public interface IContentTodo {

	void deleteTodoEvent(final TodoEvent currentTodoEvent);
	void setTodoEvent(TodoEvent todo);
}
