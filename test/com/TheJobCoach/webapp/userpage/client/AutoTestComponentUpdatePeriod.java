package com.TheJobCoach.webapp.userpage.client;

import static org.junit.Assert.*;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.Browser;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestComponentUpdatePeriod extends GwtTest {
	
	HorizontalPanel p;
    
	@Test
	public void testChange() 
	{
		UpdatePeriod up = new UpdatePeriod(CoachTestUtils.getDate(2013, 11, 4), 10, UpdatePeriod.PeriodType.WEEK, true);
		ComponentUpdatePeriod ep = new ComponentUpdatePeriod(up, ComponentUpdatePeriod.RecallType.RECONTACT);
		ep.onModuleLoad();
		assertTrue(ep.needRecallCheck.getValue());
		assertTrue(10 == ep.intBox.getValue());
		//assertEquals(1 ,ep.periodType.get()); // doesn't work with gwt-test-utils
		assertTrue(CoachTestUtils.isDateEqualForDay(up.last, ep.dateBox.getValue()));
		
		Browser.fillText(ep.intBox, "11");
		ep.periodType.setSelectedIndex(2); //month
		Browser.change(ep.periodType);
		ep.dateBox.setValue(CoachTestUtils.getDate(2200, 11, 4)); 
		//Browser.fillText(ep.dateBox.getTextBox(), "2200 11 4");
		//Browser.change(ep.dateBox);
		Browser.click(ep.needRecallCheck);
		
		assertFalse(up.needRecall);
		assertEquals(11, up.length);
		assertEquals(UpdatePeriod.PeriodType.MONTH, up.periodType);
		//assertTrue(CoachTestUtils.isDateEqualForDay(CoachTestUtils.getDate(2200, 11, 4), up.last));
		
	}

}
