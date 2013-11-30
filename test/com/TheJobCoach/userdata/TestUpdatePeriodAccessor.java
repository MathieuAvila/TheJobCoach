package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod.PeriodType;
import com.TheJobCoach.webapp.util.shared.FormatUtil;

public class TestUpdatePeriodAccessor
{
	Logger logger = LoggerFactory.getLogger(TestUpdatePeriodAccessor.class);

	public void testUpdatePeriodEquals(UpdatePeriod up1, UpdatePeriod up2)
	{
		logger.info(FormatUtil.getDateString(up1.last) + " " + up1.length + " " + up1.needRecall + " " + up1.periodType);
		logger.info(FormatUtil.getDateString(up2.last) + " " + up2.length + " " + up2.needRecall + " " + up2.periodType);
		assertEquals(up1, up2);
	}

	@Test
	public void getFromTo()
	{
		{
			UpdatePeriod period = new UpdatePeriod(CoachTestUtils.getDate(2012,  11,  1), 1, PeriodType.DAY, true);
			ShortMap orig = new ShortMap();
			ShortMap sm = UpdatePeriodAccessor.toCassandra(orig, period);
			logger.info(sm.get().toString());
			UpdatePeriod period_result = UpdatePeriodAccessor.fromCassandra(sm.get());
			testUpdatePeriodEquals(period, period_result);
		}
		{
			UpdatePeriod period = new UpdatePeriod(CoachTestUtils.getDate(2012,  11,  1), 10, PeriodType.MONTH, false);
			ShortMap orig = new ShortMap();
			ShortMap sm = UpdatePeriodAccessor.toCassandra(orig, period);
			logger.info(sm.get().toString());
			UpdatePeriod period_result = UpdatePeriodAccessor.fromCassandra(sm.get());
			testUpdatePeriodEquals(period, period_result);
		}
	}	
}
