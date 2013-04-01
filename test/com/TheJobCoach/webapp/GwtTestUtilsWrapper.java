package com.TheJobCoach.webapp;

import com.googlecode.gwt.test.BrowserSimulator;
import com.googlecode.gwt.test.GwtTest;

public class GwtTestUtilsWrapper
{
	static public void waitCallProcessor(GwtTest test, BrowserSimulator browser)
	{
		browser.fireLoopEnd();
	}
}
