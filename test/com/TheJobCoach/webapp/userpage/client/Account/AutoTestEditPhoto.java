package com.TheJobCoach.webapp.userpage.client.Account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestEditPhoto extends GwtTest {
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	
	Logger logger = LoggerFactory.getLogger(AutoTestEditPhoto.class);

	class EditPhotoTestResult implements IChooseResult<String>
	{
		public int count = 0;
		@Override
		public void setResult(String result)
		{
			logger.info("has called result callback");
			count++;
		}
	}

	public static Map<String, String> getQueryMap(String query)
	{
		String[] listParam = query.split("\\?");
		String[] params = listParam[1].split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params)
		{
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			map.put(name, value);
		}
		return map;
	}

	@Test
	public void testValid() throws MalformedURLException 
	{
		EditPhotoTestResult result = new EditPhotoTestResult();
		final EditPhoto ep = new EditPhoto(userId, result);
		assertTrue(ep.isShowing());	
		
		// can't select file.
		// ep.upload.WTF ?
		
		// commit
		ep.okCancel.getOk().click();
		
		// check request content
		logger.info(ep.form.getAction());
		Map<String, String> args = getQueryMap(ep.form.getAction());
		
		logger.info(args.toString());
		
		assertEquals(userId.userName, args.get("userid"));
		assertEquals(userId.token, args.get("token"));
		assertEquals("photo", args.get("type"));
		
		assertFalse(ep.isShowing());	
	}

}
