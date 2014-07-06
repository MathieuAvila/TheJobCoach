package com.TheJobCoach.util;

public class MailerFactory {

	static MailerInterface myMailer = new Mailer();
	
	public static MailerInterface getMailer()
	{
		return myMailer;
	}
	
	public static void setMailer(MailerInterface _mailer)
	{
		myMailer = _mailer;
	}
	
	public static void reset()
	{
		myMailer = new Mailer();
	}
}
