package com.TheJobCoach.util;

public class MailerFactory {

	static MailerInterface myMailer = new Mailer();
	
	public static MailerInterface getMailer()
	{
		return myMailer;
	}
	
	public void setMailer(MailerInterface _mailer)
	{
		myMailer = _mailer;
	}
}
