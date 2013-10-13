package com.TheJobCoach.webapp.userpage.shared;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;

public class TestUpdatePeriod {
	
	@Test
	public void testGetDateUuid()
	{
		UpdatePeriod up1 = new UpdatePeriod(CoachTestUtils.getDate(2000, 11, 20), 15, PeriodType.DAY, true);
		Date next1 = up1.getNextCall();
		System.out.println(next1);
		System.out.println(up1.last);
		assertTrue(CoachTestUtils.isDateEqualForDay(next1, CoachTestUtils.getDate(2001, 0, 4)));
		
		UpdatePeriod up2 = new UpdatePeriod(CoachTestUtils.getDate(2000, 11, 20), 6, PeriodType.WEEK, true);
		Date next2 = up2.getNextCall();
		System.out.println(next2);
		System.out.println(up2.last);
		assertTrue(CoachTestUtils.isDateEqualForDay(next2, CoachTestUtils.getDate(2001, 0, 31)));
		
		UpdatePeriod up3 = new UpdatePeriod(CoachTestUtils.getDate(2000, 11, 20), 5, PeriodType.MONTH, true);
		Date next3 = up3.getNextCall();
		System.out.println(next3);
		System.out.println(up3.last);
		assertTrue(CoachTestUtils.isDateEqualForDay(next3, CoachTestUtils.getDate(2001, 4, 20)));
	}
	
}
