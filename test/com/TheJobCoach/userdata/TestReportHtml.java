package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.Test;

public class TestReportHtml {

	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}

	@Test
	public void testAddWithSeparatorStringStringStringString() {
		assertEquals(ReportHtml.addWithSeparator("SRC", "APPEND", "-", "="), "SRC-APPEND=");
		assertEquals(ReportHtml.addWithSeparator("", "APPEND", "-", "="), "APPEND=");
	}

}
