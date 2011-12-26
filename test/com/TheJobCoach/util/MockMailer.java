package com.TheJobCoach.util;

public class MockMailer implements MailerInterface {

	public String lastDst;
	public String lastSubject;
	public String lastBody;
	public String lastSrc;

	public boolean sendEmail(String _dstMail, String _subject, String _body, String _src)
	{
		lastDst = _dstMail;
		lastSubject = _subject;
		lastBody = _body;
		System.out.println( _dstMail +"-"+ _subject +"-"+  _body +"-"+  _src);
		lastSrc = _src;
		return true;
	}

}
