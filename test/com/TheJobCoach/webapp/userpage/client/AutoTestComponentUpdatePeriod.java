package com.TheJobCoach.webapp.userpage.client;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.Browser;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestComponentUpdatePeriod extends GwtTest {
	
	HorizontalPanel p;
	
	class OurChangeEvent extends ValueChangeEvent<Date> {

		protected OurChangeEvent(Object value)
		{
			super((Date) value);
		}};
	
	@Test
	public void testFull() 
	{
		UpdatePeriod up = new UpdatePeriod(CoachTestUtils.getDate(2013, 11, 4), 10, UpdatePeriod.PeriodType.WEEK, true);
		ComponentUpdatePeriod ep = new ComponentUpdatePeriod(up, ComponentUpdatePeriod.RecallType.RECONTACT);
		ep.onModuleLoad();
		
		assertTrue(ep.getCaptionText().contains("contact"));
		assertTrue(ep.needRecallCheck.getValue());
		assertTrue(10 == ep.intBox.getValue());
		assertTrue(ep.periodType.isItemSelected(1));
		assertTrue(CoachTestUtils.isDateEqualForDay(up.last, ep.dateBox.getValue()));
		
		Browser.fillText(ep.intBox, "11");
		ep.periodType.setSelectedIndex(2); //month
		Browser.change(ep.periodType);
		ep.dateBox.setValue(CoachTestUtils.getDate(2200, 11, 4)); 
		ep.dateBox.fireEvent(new OurChangeEvent(CoachTestUtils.getDate(2200, 11, 4)));
		Browser.click(ep.needRecallCheck);
		
		assertFalse(up.needRecall);
		assertEquals(11, up.length);
		assertEquals(UpdatePeriod.PeriodType.MONTH, up.periodType);
		assertTrue(CoachTestUtils.isDateEqualForDay(CoachTestUtils.getDate(2200, 11, 4), up.last));
		
		// Check UPDATE instead of RECALL
		up = new UpdatePeriod(CoachTestUtils.getDate(2013, 11, 4), 10, UpdatePeriod.PeriodType.WEEK, true);
		ep = new ComponentUpdatePeriod(up, ComponentUpdatePeriod.RecallType.UPDATE);
		ep.onModuleLoad();
		assertTrue(ep.getCaptionText().contains("jour"));
	}

}
