package com.TheJobCoach.userdata.fetch;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;

import org.apache.commons.lang.StringEscapeUtils;

public abstract class JobBoard
{
	public abstract UserOpportunity getOpportunityFromText(byte[] textSrc, String url);
	public abstract String getOpportunityUrl(String ref);
	
	public String extractPattern(Pattern pattern, String text, String defaultString)
	{
		Matcher matcher = pattern.matcher(text);
		if (matcher.find() && matcher.groupCount() >= 1)
		{
		    return matcher.group(1);
		}
		else return defaultString;
	}
	
	public UserOpportunity getOpportunity(String ref) throws IOException
	{
		String url = getOpportunityUrl(ref);
		if (url == null) return null;
		byte[] text = Web.get(url);
		if (text == null) return null;
		return getOpportunityFromText(text, url);
	}
	
	public String removeHtml(String origTxt)
	{
		origTxt = origTxt.replaceAll("\\<[^>]*>","");
		origTxt = origTxt.replaceAll("\n","");
		origTxt = origTxt.replaceAll("\r", "");  
		origTxt = origTxt.replaceAll("[ ]*$","");
		origTxt = origTxt.replaceAll("^[ ]*","");
		origTxt = StringEscapeUtils.unescapeHtml(origTxt);
		return origTxt;
	}
}
