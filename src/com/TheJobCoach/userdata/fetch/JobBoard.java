package com.TheJobCoach.userdata.fetch;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.userpage.shared.JobBoardDefinition;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;

public abstract class JobBoard
{
	public abstract UserOpportunity getOpportunityFromText(byte[] textSrc, String url);
	public abstract String getOpportunityUrl(String ref);
	
	final static HashMap<String, JobBoard> jobBoardList = new HashMap<String, JobBoard>();
	
	static Logger logger = LoggerFactory.getLogger(JobBoard.class);
	
	static {
		jobBoardList.put(JobBoardDefinition.APEC_ID, new Apec());
		jobBoardList.put(JobBoardDefinition.POLE_EMPLOI_ID, new PoleEmploi());
	}
	
	public static UserOpportunity getOpportunity(String site, String ref)
	{
		JobBoard jobboard = jobBoardList.get(site);
		if (jobboard == null) return null;
		UserOpportunity opp = jobboard.getOpportunity(ref);
		return opp;
	}
	
	public String extractPattern(Pattern pattern, String text, String defaultString)
	{
		Matcher matcher = pattern.matcher(text);
		if (matcher.find() && matcher.groupCount() >= 1)
		{
		    return matcher.group(1);
		}
		else return defaultString;
	}
	
	public UserOpportunity getOpportunity(String ref)
	{
		String url = getOpportunityUrl(ref);
		logger.info("getOpportunity complete URL: " + ref + " " + url);
		if (url == null) return null;
		byte[] text = null;
		try 
		{
			text = Web.get(url);
		} 
		catch (Exception e) 
		{ 
			logger.info("getOpportunity exception: " + e.getMessage());
			return null;
		} // I plead not guilty, Mr Judge.
		if (text == null) return null;
		return getOpportunityFromText(text, url);
	}
	
	static public String removeHtml(String origTxt)
	{
		origTxt = origTxt.replaceAll("\\<[^>]*>","");
		origTxt = origTxt.replaceAll("\n","");
		origTxt = origTxt.replaceAll("\r", "");  
		origTxt = origTxt.replaceAll("[ ]*$","");
		origTxt = origTxt.replaceAll("^[ ]*","");
		origTxt = StringEscapeUtils.unescapeHtml(origTxt);
		char current = ' ';
		char lastCurrent = ' ';
		String result = "";
		int position = 0;
		while (position < origTxt.length()) {
			current = origTxt.charAt(position);
			if ((current != lastCurrent) || (current != ' '))
				result += current;
			lastCurrent = current;
			position++;
		};
		return result;
	}
}
