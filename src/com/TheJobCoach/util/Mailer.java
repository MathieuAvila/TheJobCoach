package com.TheJobCoach.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;


public class Mailer implements MailerInterface {
	
	private class AuthenticatorX extends javax.mail.Authenticator {
		private PasswordAuthentication authentication;
		public AuthenticatorX() {
			String username = "contact@thejobcoach.fr";
			String password = "jeparsencrete";
			authentication = new PasswordAuthentication(username, password);
		}
		protected PasswordAuthentication getPasswordAuthentication() {

			return authentication;
		}
	}
	public boolean sendEmail(String _dstMail, String _subject, String _body, String _src)
	{
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.thejobcoach.fr");
		props.setProperty("mail.smtp.host", "smtp.thejobcoach.fr"); 
		props.setProperty("mail.user", "contact@thejobcoach.fr");
		props.setProperty("mail.password", "jeparsencrete");
		props.setProperty("mail.smtp.port", "587"); 
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");

		Session mailSession = Session.getDefaultInstance(props, new AuthenticatorX());
		
		Transport transport;
		try {
			transport = mailSession.getTransport();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return false;			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;			
		}

		MimeMessage message = new MimeMessage(mailSession);
		try 
		{
			message.setSubject(_subject);
			message.setContent(_body, "text/plain");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(_dstMail));
			
			Address reply[] = new InternetAddress[1];
			reply[0] = new InternetAddress(_src);
			message.setReplyTo(reply);
			message.setFrom(new InternetAddress("contact@thejobcoach.fr"));
			message.setSender(new InternetAddress("contact@thejobcoach.fr"));			
			
			transport.connect();
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();			
		} 
		catch (MessagingException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;			
		}

		return true;
	}
	
}
