package com.TheJobCoach.webapp.userpage.client.Todo;

import com.TheJobCoach.webapp.userpage.shared.TodoCommon;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.google.gwt.i18n.client.DateTimeFormat;

public class TodoStrings
{
	static LangTodo langTodo = ContentTodo.langTodo;
	
	static String getTitle(TodoEvent todo)
	{
		if (todo.eventSubscriber.equals(TodoCommon.PERSO_SUBSCRIBER_ID))
		{
			return langTodo.Text_perso();
		}
		return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.MONTH_ABBR_DAY).format(todo.eventDate);
	}

	static String getText(TodoEvent todo)
	{
		if (todo.eventSubscriber.equals(TodoCommon.SITEMANAGER_SUBSCRIBER_ID))
		{
			String lastDate = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG)
			.format(FormatUtil.getStringDate(todo.systemText.get(TodoCommon.LAST)));
			return langTodo.Text_site()
					.replaceAll("%1", todo.systemText.get(TodoCommon.NAME))
					.replaceAll("%2", lastDate);
		}
		if (todo.eventSubscriber.equals(TodoCommon.EXTERNALCONTACTMANAGER_SUBSCRIBER_ID))
		{
			String lastDate = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG)
			.format(FormatUtil.getStringDate(todo.systemText.get(TodoCommon.LAST)));
			return langTodo.Text_contact()
					.replaceAll("%1", todo.systemText.get(TodoCommon.FIRSTNAME))
					.replaceAll("%2", todo.systemText.get(TodoCommon.LASTNAME))
					.replaceAll("%3", todo.systemText.get(TodoCommon.COMPANY))
					.replaceAll("%4", lastDate);
		}
		return todo.text;
	}
}
