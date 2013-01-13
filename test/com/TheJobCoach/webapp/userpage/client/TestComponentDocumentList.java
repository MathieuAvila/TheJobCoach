package com.TheJobCoach.webapp.userpage.client;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtTest;

public class TestComponentDocumentList extends GwtTest {

	
	private ComponentDocumentList cdl;

	
	@Override
	public String getModuleName() {		
		return "com.TheJobCoach.webapp.userpage.UserPage";
	}

	@Test
	public void testToto()
	{

	}

	@Before
	public void beforeContentUserDocument()
	{
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					return new DefaultUserServiceAsync();
				}
				return null;
			}}
		);
		Vector<UserDocumentId> docList = new Vector<UserDocumentId>();
		HorizontalPanel p = new HorizontalPanel();
		cdl = new ComponentDocumentList(
				docList,
				p, 
				new UserId()
				);
		cdl.onModuleLoad();		

	}
	
}
