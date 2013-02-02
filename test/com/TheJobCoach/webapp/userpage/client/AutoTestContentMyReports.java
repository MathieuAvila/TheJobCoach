package com.TheJobCoach.webapp.userpage.client;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentRevision;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.ConstantsMyReports;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtTest;

public class AutoTestContentMyReports extends GwtTest {


	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	private ContentMyReports cud;
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int calls;
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();
	
	@Override
	public String getModuleName() {		
		return "com.TheJobCoach.webapp.userpage.UserPage";
	}
	
	HorizontalPanel p;
	
	@Before
	public void beforeContentMyReports()
	{
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					return userService;
				}
				return null;
			}}
		);
		p = new HorizontalPanel();		
	}
	
	@Test
	public void testGetAll() throws InterruptedException
	{
		userService.calls = 0;
		cud = new ContentMyReports(
				p, userId);
		cud.onModuleLoad();
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfStartDate.getItem().getElement(), "disabled"));
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfEndDate.getItem().getElement(), "disabled"));
		Date d = new Date();
		Date week = FormatUtil.startOfTheDay(new Date()); CalendarUtil.addDaysToDate(week, -7);
		Date week2 = FormatUtil.startOfTheDay(new Date()); CalendarUtil.addDaysToDate(week2, -7 * 2);
		Date month = FormatUtil.startOfTheDay(new Date()); CalendarUtil.addMonthsToDate(month, -1);
		Date month2 = FormatUtil.startOfTheDay(new Date()); CalendarUtil.addMonthsToDate(month2, -1 * 2);
		Date start = FormatUtil.startOfTheUniverse();

		// PERIOD_LAST_WEEK
		assertEquals(cud.tfEndDate.getValue(), FormatUtil.getDateString(FormatUtil.endOfTheDay(d)));
		assertEquals(cud.tfStartDate.getValue(), FormatUtil.getDateString(week));
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfStartDate.getItem().getElement(), "disabled"));
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfEndDate.getItem().getElement(), "disabled"));

		//PERIOD_LAST_2WEEKS,
		cud.tfPeriod.setValue(ConstantsMyReports.PERIOD_LAST_2WEEKS);
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), cud.tfPeriod.getItem(), cud.tfPeriod.getItem().getElement());
		assertEquals(cud.tfEndDate.getValue(), FormatUtil.getDateString(FormatUtil.endOfTheDay(d)));
		assertEquals(cud.tfStartDate.getValue(), FormatUtil.getDateString(week2));
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfStartDate.getItem().getElement(), "disabled"));
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfEndDate.getItem().getElement(), "disabled"));

		//PERIOD_LAST_MONTH,
		cud.tfPeriod.setValue(ConstantsMyReports.PERIOD_LAST_MONTH);
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), cud.tfPeriod.getItem(), cud.tfPeriod.getItem().getElement());
		assertEquals(cud.tfEndDate.getValue(), FormatUtil.getDateString(FormatUtil.endOfTheDay(d)));
		assertEquals(cud.tfStartDate.getValue(), FormatUtil.getDateString(month));
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfStartDate.getItem().getElement(), "disabled"));
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfEndDate.getItem().getElement(), "disabled"));
		
		//PERIOD_LAST_2MONTHS,
		cud.tfPeriod.setValue("2MONTHS");
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), cud.tfPeriod.getItem(), cud.tfPeriod.getItem().getElement());
		assertEquals(cud.tfEndDate.getValue(), FormatUtil.getDateString(FormatUtil.endOfTheDay(d)));
		assertEquals(cud.tfStartDate.getValue(), FormatUtil.getDateString(month2));
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfStartDate.getItem().getElement(), "disabled"));
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfEndDate.getItem().getElement(), "disabled"));

		//PERIOD_SET,
		cud.tfPeriod.setValue("SET");
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), cud.tfPeriod.getItem(), cud.tfPeriod.getItem().getElement());
		assertEquals(false, DOM.getElementPropertyBoolean(cud.tfStartDate.getItem().getElement(), "disabled"));
		assertEquals(false, DOM.getElementPropertyBoolean(cud.tfEndDate.getItem().getElement(), "disabled"));
		cud.tfStartDate.setValue(FormatUtil.getDateString(month));
		cud.tfEndDate.setValue(FormatUtil.getDateString(month2));
		
		//PERIOD_ALL
		cud.tfPeriod.setValue("ALL");
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), cud.tfPeriod.getItem(), cud.tfPeriod.getItem().getElement());
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfStartDate.getItem().getElement(), "disabled"));
		assertEquals(true, DOM.getElementPropertyBoolean(cud.tfEndDate.getItem().getElement(), "disabled"));
		assertEquals(cud.tfEndDate.getValue(), FormatUtil.getDateString(FormatUtil.endOfTheDay(d)));
		assertEquals(cud.tfStartDate.getValue(), FormatUtil.getDateString(FormatUtil.startOfTheDay(start)));
		
		//cud.buttonActivityReport.click();
	}
}
