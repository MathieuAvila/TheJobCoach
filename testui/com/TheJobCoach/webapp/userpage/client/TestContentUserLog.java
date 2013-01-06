package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestContentUserLog implements EntryPoint {
	
	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		RootPanel root = RootPanel.get("contentuserlog");
		if (root != null)
		{
			root.setStyleName("mainpage-content");		
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("mainpage-content");
			root.add(hp);
			hp.setSize("100%", "100%");
			UserOpportunity opp = new UserOpportunity("opp1", getDate(2000, 1, 1), getDate(2000, 1, 1),
					"title1", "description1", "companyId1",
					"contractType1",  1,  
					getDate(2000, 1, 1), getDate(2000, 1, 1),
					false, "source1", "url1", "location1",
					UserOpportunity.ApplicationStatus.APPLIED, "note1");
			
			ContentUserLog cud = new ContentUserLog(
					hp, 
					new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER),
					opp);
			cud.onModuleLoad();
		}
	}

}
