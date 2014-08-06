package com.TheJobCoach.webapp.util.client;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class TestCheckedCheckBox extends GwtTest {
	
	static Logger logger = LoggerFactory.getLogger(TestCheckedCheckBox.class);

	public class CheckedElement implements IChanged
	{
		boolean ok;
		boolean isDefault;
		@Override
		public void changed(boolean ok, boolean isDefault, boolean init)
		{
			this.ok = ok;
			this.isDefault = isDefault;
			logger.info("ok: " + ok + " isDefault: " + isDefault);
		}
		public CheckedElement(IExtendedField from)
		{
			from.registerListener(this);
		}
	}

	@Test
	public void testMessageBox()
	{
		CheckedCheckBox checkBox = new CheckedCheckBox();
		CheckedElement lblCheck = new CheckedElement(checkBox);
		
		checkBox.setDefault(true);		
		checkBox.setValue(true);		
		assertTrue(lblCheck.ok);
		assertTrue(lblCheck.isDefault);
		
		checkBox.setDefault(true);		
		checkBox.setValue(false);		
		assertTrue(lblCheck.ok);
		assertFalse(lblCheck.isDefault);
		
		checkBox.setDefault(false);		
		checkBox.setValue(true);		
		assertTrue(lblCheck.ok);
		assertFalse(lblCheck.isDefault);
		
		checkBox.setDefault(false);		
		checkBox.setValue(false);		
		assertTrue(lblCheck.ok);
		assertTrue(lblCheck.isDefault);
	}
}
