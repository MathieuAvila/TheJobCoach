package com.TheJobCoach.userdata.report;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;

public class ReportHtml {

	public static String writeToString(String src)
	{
		StringWriter sw = new StringWriter();
		try{
			StringEscapeUtils.escapeHtml(sw, src);			
		}
		catch (IOException e)
		{
			return "";
		}
		return sw.toString();
	}

	public static String getHead()
	{
		return "<HTML><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n<BODY>\n";
	}
	
	static String addWithSeparator(String src, String append, String sep, String end)
	{
		if (append != "")
		{
			if (src != "") src += sep;
			src += append + end;
		}
		return src;
	}

	static String addWithSeparator(String src, String append, String sep)
	{
		return addWithSeparator(src, append, sep, "");
	}
	
	public static String getFooter()
	{
		return "</BODY></HTML>\n";
	}
	
	public static String getDate(String lang, Date d)
	{
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("EEE dd MMMM yyyy", new Locale(lang, "", ""));
		return formatter.format(d);
	}
}
