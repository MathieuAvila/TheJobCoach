package com.TheJobCoach.webapp.userpage.client.Todo;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.shared.TodoCommon;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent.EventColor;
import com.TheJobCoach.webapp.userpage.shared.TodoEvent.Priority;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;


@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class TestTodoStrings extends GwtTest
{
	static LangTodo langTodo = GWT.create(LangTodo.class);

	HashMap<String, String> systemTextPerso = new HashMap<String, String>(ImmutableMap.of(
	        "b", "b_v",
	        "a", "a_v",
	        "c", "c_v"
	        ));
	
	TodoEvent perso = new TodoEvent("perso", "text perso", systemTextPerso, TodoCommon.PERSO_SUBSCRIBER_ID,
			Priority.NORMAL, CoachTestUtils.getDate(2013, 1, 1), EventColor.BLUE);
	
	HashMap<String, String> systemTextLog = new HashMap<String, String>(ImmutableMap.of(
			TodoCommon.NAME, "name",
			TodoCommon.FIRSTNAME, "firstname",
			TodoCommon.LASTNAME, "lastsname",
			TodoCommon.COMPANY, "company",
			TodoCommon.LAST, FormatUtil.getDateString(CoachTestUtils.getDate(2013, 1, 10))
	        ));
	TodoEvent interview = new TodoEvent("interview", "text interview", systemTextLog, UserLogEntry.entryTypeToString(UserLogEntry.LogEntryType.INTERVIEW),
			Priority.NORMAL, CoachTestUtils.getDate(2013, 1, 1), EventColor.BLUE);
	TodoEvent event = new TodoEvent("event", "text event", systemTextLog, UserLogEntry.entryTypeToString(UserLogEntry.LogEntryType.EVENT),
			Priority.NORMAL, CoachTestUtils.getDate(2013, 1, 1), EventColor.BLUE);
	TodoEvent recall = new TodoEvent("recall", "text recall", systemTextLog, UserLogEntry.entryTypeToString(UserLogEntry.LogEntryType.RECALL),
			Priority.NORMAL, CoachTestUtils.getDate(2013, 1, 1), EventColor.BLUE);
	
	@Test
	public void testGetTitle()
	{
		assertEquals(langTodo.Text_perso(), TodoStrings.getTitle(perso));
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.MONTH_ABBR_DAY).format(perso.eventDate), TodoStrings.getTitle(interview));
	}

	@Test
	public void testGetText()
	{
		assertEquals(perso.text, TodoStrings.getText(perso));
		assertEquals("Préparer l'entretien (" + systemTextLog.get(TodoCommon.NAME)+ ")", TodoStrings.getText(interview));
		assertEquals("Faire le suivi: " + systemTextLog.get(TodoCommon.NAME), TodoStrings.getText(recall));
		assertEquals("Evènement " + systemTextLog.get(TodoCommon.NAME), TodoStrings.getText(event));
		
	}

}
