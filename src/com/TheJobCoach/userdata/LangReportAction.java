package com.TheJobCoach.userdata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

public class LangReportAction
{	
	String lang;
	Properties currentProperties;
	Properties currentWebappProperties;
	
	static HashMap<String, Properties> langProp = new HashMap<String, Properties>();
	static HashMap<String, Properties> langPropWebapp = new HashMap<String, Properties>();
	
	static {
		langProp.put("fr", new Properties());
		langProp.put("en", new Properties());

		langPropWebapp.put("fr", new Properties());
		langPropWebapp.put("en", new Properties());

		try {
			InputStreamReader isr = new InputStreamReader((new VoidObject()).getClass().getResourceAsStream("LangReportAction_fr.properties"), "UTF-8");
			langProp.get("fr").load(isr);
			isr = new InputStreamReader((new VoidObject()).getClass().getResourceAsStream("LangReportAction_en.properties"), "UTF-8");
			langProp.get("en").load(isr);

			isr = new InputStreamReader((new VoidObject()).getClass().getResourceAsStream("/com/TheJobCoach/webapp/userpage/client/LangLogEntry_fr.properties"), "UTF-8");
			langPropWebapp.get("fr").load(isr);
			isr = new InputStreamReader((new VoidObject()).getClass().getResourceAsStream("/com/TheJobCoach/webapp/userpage/client/LangLogEntry_en.properties"), "UTF-8");
			langPropWebapp.get("en").load(isr);
		} 
		catch (FileNotFoundException e) 
		{			
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	String getLang(String lang)
	{
		if (lang == null) return "fr";
		if (lang.equals("FR")) return "fr";
		if (lang.equals("fr")) return "fr";
		if (lang.equals("EN")) return "en";
		if (lang.equals("en")) return "en";
		return "fr";
	}
	
	Properties getLangProp(String lang)
	{
		return langProp.get(getLang(lang));
	}

	public LangReportAction(String lang)
	{
		this.lang = lang;
		currentProperties = getLangProp(lang);
		currentWebappProperties = langPropWebapp.get(getLang(lang));
	}	

	String getTimeReport()
	{
		return currentProperties.getProperty("timereport");
	}

	String getDate()
	{
		return currentProperties.getProperty("date");
	}
	
	String getType()
	{
		return currentProperties.getProperty("type");
	}

	String getAction()
	{
		return currentProperties.getProperty("action");
	}
	
	String getActionName(String action)
	{
		return currentWebappProperties.getProperty("logEntryStatus_" + action);
	}
	
}
