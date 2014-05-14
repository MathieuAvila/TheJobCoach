package com.TheJobCoach.userdata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

import com.TheJobCoach.util.StringResourceCache;

public class Lang
{
	
	static HashMap<String, Properties> langProp = new HashMap<String, Properties>();
	
	static {
		langProp.put("fr", new Properties());
		langProp.put("en", new Properties());
		try {
			InputStream stream = (new VoidObject()).getClass().getResourceAsStream("Lang_fr.properties");
			InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
			langProp.get("fr").load(isr);
			stream = (new VoidObject()).getClass().getResourceAsStream("Lang_en.properties");
			isr = new InputStreamReader(stream, "UTF-8");
			langProp.get("en").load(isr);
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
	
	static String _TextActivateAccountBody(String firstName, String name, String email, String site, String userName, String key, String lang)
	{
		String body = StringResourceCache.getStringResource("/com/TheJobCoach/userdata/data/mail_account_new.html");
		String URL= site + "/TheJobCoach.html?action=validate&username=" +userName + "&token=" + key;
		body = body.replace("_URL_", URL);
		body = body.replace("_FIRSTNAME_", firstName);
		body = body.replace("_NAME_", name);
		body = body.replace("_HELLO_", getLangProp(lang).getProperty("hello"));
		body = body.replace("_ACCOUNT_REQUEST_", getLangProp(lang).getProperty("newaccountrequest"));
		body = body.replace("_IGNORE_", getLangProp(lang).getProperty("newaccountrequestignore"));
		return body;
	}

	static String _TextActivateAccountSubject(String lang)
	{
		return getLangProp(lang).getProperty("activateaccountsubject");
	}
	
	static String _TextLostCredentialsSubject(String lang)
	{
		return getLangProp(lang).getProperty("credentialssubject");
	}
	
	static String _TextLostCredentials(String firstName, String name, String userName, String password, String lang)
	{
		String body = StringResourceCache.getStringResource("/com/TheJobCoach/userdata/data/mail_account_credentials.html");
		body = body.replaceAll("_FIRSTNAME_", firstName);
		body = body.replaceAll("_NAME_", name);
		body = body.replaceAll("_HELLO_", getLangProp(lang).getProperty("hello"));
		body = body.replaceAll("_THANKS_AND_CREDS_", getLangProp(lang).getProperty("credentialsrequest"));
		body = body.replaceAll("_USER_SUBJECT_", getLangProp(lang).getProperty("credentialsuser"));
		body = body.replaceAll("_USER_", userName);
		body = body.replaceAll("_PASSWORD_SUBJECT_", getLangProp(lang).getProperty("credentialspassword"));
		body = body.replaceAll("_PASSWORD_", password);
		body = body.replaceAll("_CAN_CONNECT_", getLangProp(lang).getProperty("credentialsconnect"));
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

	static String requestConnectionSubject(String lang)
	{
		return getLangProp(lang).getProperty("requestconnectionsubject");
	}

	static String connectionGrantedSubject(String lang)
	{
		return getLangProp(lang).getProperty("connectiongrantedsubject");
	}

	static String jobMailSubject(String lang)
	{
		return getLangProp(lang).getProperty("jobmailsubject");
	}
	
	static String requestConnection(String lang, String fromFirstName, String fromLastName, String userName, String toFirstName, String toLastName)
	{
		String body = StringResourceCache.getStringResource("/com/TheJobCoach/userdata/data/mail_connection_request_" + lang +".html");
		body = body.replaceAll("_FIRSTNAME_", toFirstName);
		body = body.replaceAll("_NAME_", toLastName);
		body = body.replaceAll("_FIRSTNAMEFROM_", fromFirstName);
		body = body.replaceAll("_NAMEFROM_", fromLastName);
		body = body.replaceAll("_USERNAME_", userName);
		return body;
	}

	static String connectionGranted(String lang, String fromFirstName, String fromLastName, String userName, String toFirstName, String toLastName)
	{
		String body = StringResourceCache.getStringResource("/com/TheJobCoach/userdata/data/mail_connection_granted_" + lang +".html");
		body = body.replaceAll("_FIRSTNAME_", fromFirstName);
		body = body.replaceAll("_NAME_", fromLastName);
		body = body.replaceAll("_FIRSTNAMETO_", toFirstName);
		body = body.replaceAll("_NAMETO_", toLastName);
		body = body.replaceAll("_USERNAME_", userName);
		return body;
	}

	static String jobMail(String lang, String fromFirstName, String fromLastName, String userName, String toFirstName, String toLastName, String message)
	{
		String body = StringResourceCache.getStringResource("/com/TheJobCoach/userdata/data/mail_contact_" + lang +".html");
		body = body.replaceAll("_FIRSTNAME_", fromFirstName);
		body = body.replaceAll("_NAME_", fromLastName);
		body = body.replaceAll("_FIRSTNAMETO_", toFirstName);
		body = body.replaceAll("_NAMETO_", toLastName);
		body = body.replaceAll("_USERNAME_", userName);
		body = body.replaceAll("_MESSAGE_", message);
		return body;
	}
	
}
