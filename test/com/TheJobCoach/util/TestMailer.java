package com.TheJobCoach.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestMailer
{

	@Test
	public void test_getEncodedContent()
	{
		String result = Mailer.getEncodedContent("/com/TheJobCoach/util/test/test.png");
		System.out.println(result);
		assertEquals(result, "MTIzNAo=");
	}
	
	@Test
	public void test_realMail()
	{
		Mailer mailer = new Mailer();
		Map<String, MailerInterface.Attachment> parts = new HashMap<String, MailerInterface.Attachment>();
		parts.put("firstimg", new MailerInterface.Attachment("/com/TheJobCoach/util/test/test2.png", "image/png", "img_one"));
		parts.put("secondimg", new MailerInterface.Attachment("/com/TheJobCoach/util/test/test3.png", "image/png", "img_two"));
		
		//parts = null;
		
		String msg = "FIRST <img src=\"cid:firstimg\"><BR/>SECOND <img src=\"cid:secondimg\"><BR/>";
		assertEquals(true, mailer.sendEmail("mathieu.avila@gmail.com", "test_subject", msg, "test@test.com", parts));
	}
	
	
}
