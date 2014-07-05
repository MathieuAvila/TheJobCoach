package com.TheJobCoach.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class MockMailer implements MailerInterface {

	public String lastDst;
	public String lastSubject;
	public String lastBody;
	public String lastSrc;
	public Map<String, Attachment> lastParts;
	
	public void writeToFile(String path) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(path, "UTF-8");
		writer.println(lastBody);
		writer.close();
	}

	public boolean sendEmail(String _dstMail, String _subject, String _body, String _src)
	{
		lastDst = _dstMail;
		lastSubject = _subject;
		lastBody = _body;
		lastSrc = _src;
		lastParts = null;
		return true;
	}

	@Override
	public boolean sendEmail(String _dstMail, String _subject, String _body,
			String _src, Map<String, Attachment> parts)
	{
		lastDst = _dstMail;
		lastSubject = _subject;
		lastBody = _body;
		lastSrc = _src;
		lastParts = parts;
		return true;
	}

	public void reset()
	{
		lastDst = null;
		lastSubject = null;
		lastBody = null;
		lastSrc = null;
		lastParts = null;
	}
}
