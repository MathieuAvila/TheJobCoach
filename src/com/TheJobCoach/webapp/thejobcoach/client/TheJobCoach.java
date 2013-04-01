package com.TheJobCoach.webapp.thejobcoach.client;

import com.TheJobCoach.webapp.mainpage.client.MainPage;
import com.TheJobCoach.webapp.mainpage.client.Validate;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TheJobCoach implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	

	/**
	 * This is the entry point method.
	 */
	
	static int loadCount = 0;
	
	public void onModuleLoad()
	{
		String locale = LocaleInfo.getCurrentLocale().getLocaleName();
		if ((locale == null) || (locale.equals("default")))
		{
			String cookie = LocaleInfo.getLocaleCookieName();
			String cookieLang = com.google.gwt.user.client.Cookies.getCookie(cookie);
			if ((cookieLang == null) && (loadCount != 1))
			{
				loadCount++;
				com.google.gwt.user.client.Cookies.setCookie(cookie, "fr");
				Window.Location.reload();
			}
		}
		
		// Check if we are validating an account
		String action = com.google.gwt.user.client.Window.Location.getParameter("action");
		if (action != null)			
		{	
			if (action.equals("validate"))
			{
				Validate validate = new Validate();
				validate.setRootPanel(RootPanel.get("content"));
				validate.onModuleLoad();
				return;
			}
		}

		MainPage main = new MainPage();
		main.onModuleLoad();
				
		return;	
	}
}
