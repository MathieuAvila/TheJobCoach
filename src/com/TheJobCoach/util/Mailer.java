package com.TheJobCoach.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.userdata.VoidObject;

public class Mailer implements MailerInterface {

    static Logger logger = LoggerFactory.getLogger(Mailer.class);
	
	class AuthenticatorX extends javax.mail.Authenticator {
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
		return sendEmail(_dstMail, _subject, _body, _src, null);
	}
	
	public class InputStreamDataSource implements DataSource {
	    private InputStream inputStream;

	    public InputStreamDataSource(InputStream inputStream) {
	    	System.out.println("IS = " + inputStream);
	        this.inputStream = inputStream;
	    }

	    @Override
	    public InputStream getInputStream() throws IOException {
	        return inputStream;
	    }

	    @Override
	    public OutputStream getOutputStream() throws IOException {
	        throw new UnsupportedOperationException("Not implemented");
	    }

	    @Override
	    public String getContentType() {
	        return "image/png";
	    }

	    @Override
	    public String getName() {
	        return "InputStreamDataSource";
	    }
	}


	private class InputStreamMimeBodyPart extends MimeBodyPart {

		private InputStream inputStream;

		public InputStreamMimeBodyPart(InputStream source) {
			this.inputStream = source;
			if(!inputStream.markSupported()) {
				throw new IllegalArgumentException("only streams with mark supported are ok");
			}
			inputStream.mark(Integer.MAX_VALUE); // remeber the whole stream.
			
			{
				this.headers.addHeader("Content-Type", "image/png");
				//updateHeaders();
			}			
		}

		@Override
		protected InputStream getContentStream() throws MessagingException {
			return inputStream;
			
			//throw new IllegalStateException("getContentStream is not implemented on purpose.");
		}

		@Override
		public void writeTo(OutputStream os) throws IOException, MessagingException {
			System.out.println("writing to somewhere.");
			byte[] buf = new byte[32];
			int length;
			inputStream.reset();
			while((length = inputStream.read(buf)) > -1 ) {
				os.write(buf, 0, length);
			}
		}
	}

	public boolean sendEmail(String _dstMail, String _subject, String _body, String _src, Map<String, Attachment> parts)
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
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(_dstMail));

			Address reply[] = new InternetAddress[1];
			reply[0] = new InternetAddress(_src);
			message.setReplyTo(reply);
			message.setFrom(new InternetAddress("contact@thejobcoach.fr"));
			message.setSender(new InternetAddress("contact@thejobcoach.fr"));				

			// Create a message part to represent the body text
			BodyPart messageBodyPart = new MimeBodyPart();
			
			StringBuffer sb = new StringBuffer();
			sb.append("<HTML>\n");
			sb.append("<BODY>\n");			
			sb.append(_body);
			sb.append("</BODY>\n");
			sb.append("</HTML>\n");

			messageBodyPart.setDataHandler(new DataHandler(
				new ByteArrayDataSource(sb.toString(), "text/html")));
			
			messageBodyPart.setContent( _body, "text/html; charset=utf-8" );

			// use a MimeMultipart as we need to handle the file attachments
			MimeMultipart multipart = new MimeMultipart();
			multipart.setSubType("related"); 
			
			// add the message body to the mime message
			multipart.addBodyPart(messageBodyPart);

			// Add all additional parts
			if (parts != null)
				for (Map.Entry<String, Attachment> entry : parts.entrySet()) {
/*
					// Read image from file system. 
					InputStream is = new VoidObject().getClass().getResourceAsStream(entry.getValue());
					InputStreamMimeBodyPart messageBodyPart2 = new InputStreamMimeBodyPart(is);
					//messageBodyPart2.setFileName(entry.getKey());
					
					//DataHandler dataHandler = new DataHandler(new InputStreamDataSource(is));
					//messageBodyPart2.setDataHandler(dataHandler);  

					// Set the content-ID of the image attachment.  
					// Enclose the image CID with the lesser and greater signs.  
					messageBodyPart2.setHeader("Content-ID", "<" + entry.getKey() + ">");  
					messageBodyPart2.setHeader("Content-Type", "image/png");
					// Add image attachment to multipart.  
					multipart.addBodyPart(messageBodyPart2); 
					*/
					 // Part two is attachment
			         messageBodyPart = new MimeBodyPart();
			         String filename = entry.getValue().resource;
			         DataSource source = new FileDataSource(new File(this.getClass().getResource(filename).toURI()));
			         messageBodyPart.setDataHandler(new DataHandler(source));
			         messageBodyPart.setFileName(entry.getValue().filename);
			         messageBodyPart.setHeader("Content-ID", "<" + entry.getKey() + ">"); 
			         messageBodyPart.setHeader("Content-Type", entry.getValue().mime);
			         messageBodyPart.setDisposition(MimeBodyPart.INLINE);
			         multipart.addBodyPart(messageBodyPart);
					
					
				}
			
			// Put all message parts in the message
			message.setContent(multipart);
			message.saveChanges();

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
			logger.error("Unable to send mail: " + _dstMail + " - " +  _subject + " - " + _body + " - " + _src);
			logger.error(e.toString());
			e.printStackTrace();
			return false;			
		}

		return true;
	}
	
	public static String getEncodedContent(String ressource)
	{
		InputStream is = new VoidObject().getClass().getResourceAsStream(ressource);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream b64os = null;
		try
		{
			b64os = MimeUtility.encode(baos, "base64");
		}
		catch (MessagingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        int b;
        try
		{
			while ((b = is.read()) != -1)
			{
			b64os.write(b);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try
		{
			b64os.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return baos.toString();
	}
	
}
