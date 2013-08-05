package com.TheJobCoach.util;

import java.util.Map;

public interface MailerInterface {

	public class Attachment
	{
		public String resource;
		public String mime;
		public String filename;
		public Attachment(String resource, String mime, String filename)
		{
			super();
			this.resource = resource;
			this.mime = mime;
			this.filename = filename;
		}
	}
	
	public boolean sendEmail(String _dstMail, String _subject, String _body, String _src);
	public boolean sendEmail(String _dstMail, String _subject, String _body, String _src, Map<String, Attachment> parts);
}
