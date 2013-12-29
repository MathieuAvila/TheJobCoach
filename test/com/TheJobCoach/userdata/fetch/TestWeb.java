package com.TheJobCoach.userdata.fetch;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class TestWeb
{
	@Test
	public void test_get() throws IOException
	{
		// https://  http://, check begin end
		byte[] gogo = Web.get("http://google.fr");
		byte[] tjc = Web.get("https://www.thejobcoach.fr/TheJobCoach/");
		String gogoStr = new String(gogo);
		assertTrue(gogoStr.contains("/body></html>"));
		assertTrue(gogoStr.contains("<!doctype html>"));
		String tjcStr = new String(tjc);
		assertTrue(tjcStr.contains("<html>"));
		assertTrue(tjcStr.contains("</body>"));
	}
}
