package com.TheJobCoach.webapp.userpage.client.MyGoals;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class TestResultEvaluation extends GwtTest {

	private ResultEvaluation re;

	private void checkResult(int value, int minimum, boolean current, String img, String msg)
	{
		re = new ResultEvaluation();
		re.setValue(value);
		re.setMinimum(minimum);
		re.setCurrent(current);
		Image image = (Image)re.getWidget(2);
		assertTrue(image.getUrl().contains(img));
		Label txt = (Label)re.getWidget(4);
		assertTrue(txt.getText().contains(msg));
		Label val = (Label)re.getWidget(0);
		assertTrue(val.getText().equals(String.valueOf(value)));
	}
	
	@Test
	public void statusTest()
	{
		String cookie = LocaleInfo.getLocaleCookieName();
		com.google.gwt.user.client.Cookies.setCookie(cookie, "en");
		
		checkResult(10, 9,  false, "success", "Succ");
		checkResult(10, 10, false, "success", "Succ");
		checkResult(10, 15, false, "failure", "chec");
		
		checkResult(10, 9,  true, "success", "Succ");
		checkResult(10, 10, true, "success", "Succ");
		checkResult(10, 15, true, "unknown", "En cours");
	}
	
	private void checkVoid(ResultEvaluation re)
	{
		Image image = (Image)re.getWidget(2);
		assertTrue(image.getUrl().equals(""));
		Label txt = (Label)re.getWidget(4);
		assertTrue(txt.getText().equals(""));
		Label val = (Label)re.getWidget(0);
		assertTrue(val.getText().equals(""));
	}
	
	private void checkNoVoid(ResultEvaluation re)
	{
		Image image = (Image)re.getWidget(2);
		assertFalse(image.getUrl().equals(""));
		Label txt = (Label)re.getWidget(4);
		assertFalse(txt.getText().equals(""));
		Label val = (Label)re.getWidget(0);
		assertFalse(val.getText().equals(""));
	}
	
	@Test
	public void undefinedTest()
	{
		re = new ResultEvaluation();
		checkVoid(re);
		re.setValue(10);
		checkVoid(re);
		re.setMinimum(9);
		checkVoid(re);
		re.setCurrent(false);
		checkNoVoid(re);
	}

}
