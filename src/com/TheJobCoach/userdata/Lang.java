package com.TheJobCoach.userdata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Lang
{
	
	static HashMap<String, Properties> langProp = new HashMap<String, Properties>();
	
	static {
		langProp.put("fr", new Properties());
		langProp.put("en", new Properties());
		try {
			langProp.get("fr").load((new VoidObject()).getClass().getResourceAsStream("Lang_fr.properties"));
			langProp.get("en").load((new VoidObject()).getClass().getResourceAsStream("Lang_en.properties"));
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
	
	static String getLang(String lang)
	{
		if (lang == null) return "fr";
		if (lang.equals("FR")) return "fr";
		if (lang.equals("fr")) return "fr";
		if (lang.equals("EN")) return "en";
		if (lang.equals("en")) return "en";
		return "fr";
	}
	
	static Properties getLangProp(String lang)
	{
		return langProp.get(getLang(lang));
	}
	
	static String _TextActivateAccountBody(String firstName, String site, String userName, String key, String lang)
	{
		String body=getLangProp(lang).getProperty("activateaccountbody");
		body = body.replace("{0}", firstName);
		body = body.replace("{1}", site);
		body = body.replace("{2}", userName);
		body = body.replace("{3}", key);
		return body;
	}

	static String _TextActivateAccountSubject(String lang)
	{
		return getLangProp(lang).getProperty("credentialssubject");
	}
	
	static String _TextLostCredentialsSubject(String lang)
	{
		return getLangProp(lang).getProperty("activateaccountsubject");
	}
	
	static String _TextLostCredentials(String user, String password, String lang)
	{
		String body=getLangProp(lang).getProperty("credentialsbody");
		body = body.replace("{0}", user);
		body = body.replace("{1}", password);
		return body;
	}
	
	static String getTestName(String lang)
	{
		return getLangProp(lang).getProperty("testusername");
	}
	
	static String getTestLastName(String lang)
	{
		return getLangProp(lang).getProperty("testuserlastname");
	}
	
}
