package com.TheJobCoach.webapp.thejobcoach.client;

import com.TheJobCoach.webapp.mainpage.client.MainPage;
import com.TheJobCoach.webapp.mainpage.client.Validate;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.LocaleInfo;
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
				System.out.println("Applied locale fr");
			}
		}
		
		// Check if we are validating an account
		String action = com.google.gwt.user.client.Window.Location.getParameter("action");
		if (action != null)			
		{	
			System.out.println("Action is: " + action);
			if (action.equals("validate"))
			{					
				String userName = com.google.gwt.user.client.Window.Location.getParameter("username");
				String token = com.google.gwt.user.client.Window.Location.getParameter("token");				
				System.out.println("Trying to validate user: " + userName + " with token " + token);
				
				Validate validate = new Validate();
				validate.setRootPanel(RootPanel.get("content"));
				validate.onModuleLoad();
				//RootPanel.get("nameFieldContainer").add(main);
				System.out.println("Validate an account...");
				return;
			}
		}

		MainPage main = new MainPage();
		main.onModuleLoad();
		//RootPanel.get("nameFieldContainer").add(main);
		System.out.println("Load TheJobCoach...");
		return;
	}
}
