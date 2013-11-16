package com.TheJobCoach.webapp.userpage.client.MyGoals;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class TestResultEvaluation extends GwtTest {

	private ResultEvaluation re;
	
	static Logger logger = LoggerFactory.getLogger(TestResultEvaluation.class);

	public enum STATUS { SUCCESS, FAILURE, UNKNOWN, NOTSET} ;

	static HashMap<STATUS, String> imgStatus = new HashMap<STATUS , String>() {
		private static final long serialVersionUID = 10000L;
		{
			put(STATUS.SUCCESS,    "success");
			put(STATUS.FAILURE,    "failure");
			put(STATUS.UNKNOWN,    "unknown");
			put(STATUS.NOTSET,     "");
		}
	};

	static HashMap<STATUS, String> txtStatus = new HashMap<STATUS , String>() {
		private static final long serialVersionUID = 10000L;
		{
			put(STATUS.SUCCESS,    "Succ");
			put(STATUS.FAILURE,    "chec");
			put(STATUS.UNKNOWN,    "En cours");
			put(STATUS.NOTSET,     "");
		}
	};

	static protected void checkResultEvaluationContext(ResultEvaluation re, STATUS status, int value)
	{
		Image image = (Image)re.getWidget(2);
		Label txt = (Label)re.getWidget(4);
		Label val = (Label)re.getWidget(0);

		String img = imgStatus.get(status);
		String msg = txtStatus.get(status);
		if (status != STATUS.NOTSET)
		{
			logger.info(image.getUrl() + " " + img);
			logger.info(val.getText() + " " +String.valueOf(value));
			logger.info(txt.getText() + " " +msg);
			logger.info(re.minimum + " " + re.value);
			logger.info(re.isSet.toString());
			
			assertTrue(image.getUrl().contains(img));
			assertTrue(txt.getText().contains(msg));
			assertTrue(val.getText().equals(String.valueOf(value)));
		}
		else
		{
			assertTrue(image.getUrl().equals(""));
			assertTrue(txt.getText().equals(""));
			assertTrue(val.getText().equals(""));
		}
	}


	private ResultEvaluation checkResult(int value, int minimum, boolean current, String img, String msg)
	{
		re = new ResultEvaluation();
		re.setValue(value);
		re.setMinimum(minimum);
		re.setCurrent(current);
		checkResultEvaluationContext(re, value >= minimum ? STATUS.SUCCESS : current ? STATUS.UNKNOWN : STATUS.FAILURE, value);
		return re;
	}

	@Test
	public void statusTest()
	{
		checkResult(10, 9,  false, "success", "Succ");
		checkResult(10, 10, false, "success", "Succ");
		checkResult(10, 15, false, "failure", "chec");

		checkResult(10, 9,  true, "success", "Succ");
		checkResult(10, 10, true, "success", "Succ");
		checkResult(10, 15, true, "unknown", "En cours");
	}

	@Test
	public void resetTest()
	{
		ResultEvaluation re = checkResult(10, 9,  false, "success", "Succ");
		re.resetMinimum();
		checkVoid(re);
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
