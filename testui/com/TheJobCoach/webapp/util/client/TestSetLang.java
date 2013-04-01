package com.TheJobCoach.webapp.util.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.LocaleInfo;

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
		if ((locale == null) || (locale.equals("default")))
		{
			String cookie = LocaleInfo.getLocaleCookieName();
			String cookieLang = com.google.gwt.user.client.Cookies.getCookie(cookie);
			if ((cookieLang == null) && (loadCount != 1))
			{
				loadCount++;
				com.google.gwt.user.client.Cookies.setCookie(cookie, "fr");
			}
		}
	}

}
