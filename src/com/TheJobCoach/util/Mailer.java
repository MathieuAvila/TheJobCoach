package com.TheJobCoach.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;


public class Mailer implements MailerInterface {
	
	public boolean sendEmail(String _dstMail, String _subject, String _body, String _src)
	{
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.free.fr");
		props.setProperty("mail.user", "mathieu.avila");
		props.setProperty("mail.password", "lvveumda");

		Session mailSession = Session.getDefaultInstance(props, null);
		Transport transport;
		try {
			transport = mailSession.getTransport();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return false;			
		}
		catch (Exception e)
		{
			System.out.println("Exception à la con");
			e.printStackTrace();
			return false;			
		}

		MimeMessage message = new MimeMessage(mailSession);
		try 
		{
			message.setSubject(_subject);
			message.setContent(_body, "text/plain");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(_dstMail));
			message.setFrom(new InternetAddress("contact@thejobcoach.fr"));
			message.setSender(new InternetAddress("contact@thejobcoach.fr"));
			
			transport.connect();
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			//transport.send(message);
			transport.close();
		} 
		catch (MessagingException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Excpetion à la con");
			e.printStackTrace();
			return false;			
		}

		return true;
	}
	
}
