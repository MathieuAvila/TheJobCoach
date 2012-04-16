package com.TheJobCoach.webapp.util.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestSetLang implements EntryPoint {

	/**
	 * This is the entry point method.
	
	 */
	static int loadCount = 0;
	
	public void onModuleLoad()
	{
		com.google.gwt.user.client.Cookies.setCookie(LocaleInfo.getLocaleCookieName(), "fr");
	
		LocaleInfo.getLocaleCookieName();
		String locale = LocaleInfo.getCurrentLocale().getLocaleName();
		System.out.println("Set Lang.... Locale is:" + locale);
		if ((locale == null) || (locale.equals("default")))
		{
			String cookie = LocaleInfo.getLocaleCookieName();
			String cookieLang = com.google.gwt.user.client.Cookies.getCookie(cookie);
			if ((cookieLang == null) && (loadCount != 1))
			{
				loadCount++;
				com.google.gwt.user.client.Cookies.setCookie(cookie, "fr");
				System.out.println("Applied locale fr");
			}
		}
	}

}
