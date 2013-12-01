package com.TheJobCoach.userdata;

import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;

public interface ITodoList 
{	
	public void setTodoEvent(UserId id, TodoEvent result) throws CassandraException;
}
