package com.TheJobCoach.userdata;

import org.junit.Test;

import com.TheJobCoach.util.MailerFactory;

public class TestMailer
{
	@Test
	public void testMail()
	{
		MailerFactory.getMailer().sendEmail(
				"mathieu.avila@laposte.net",
				"sujet à lire testé", 
				"suj===et<BR/>à<BR/>lire<BR/><a href='http://www.google.com/index.html?wxclkjxcvmlj=ddf&dkljdqsl=qsdfs'>testé</a><BR/>", 
				"noreply@www.thejobcoach.fr");
	}

	
}
