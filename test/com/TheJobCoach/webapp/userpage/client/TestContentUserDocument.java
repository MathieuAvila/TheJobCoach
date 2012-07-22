package com.TheJobCoach.webapp.userpage.client;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.octo.gwt.test.GwtCreateHandler;
import com.octo.gwt.test.GwtTest;

public class TestContentUserDocument extends GwtTest {

	
	private ContentUserDocument cud;

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
		HorizontalPanel p = new HorizontalPanel();
		cud = new ContentUserDocument(
				p, 
				new UserId()
				);
		cud.onModuleLoad();		

		// Some pre-assertions

	}
	
}
