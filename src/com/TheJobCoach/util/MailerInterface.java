package com.TheJobCoach.util;

public interface MailerInterface {

	public boolean sendEmail(String _dstMail, String _subject, String _body, String _src);
}
