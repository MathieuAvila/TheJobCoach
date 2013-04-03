package com.TheJobCoach.webapp.userpage.client.Opportunity;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.Browser;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestContentUserOpportunity extends GwtTest {


	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	private ContentUserOpportunity cuo;
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	static String opp1 = "opp1";
	static String opp2 = "opp2";
	static String opp3 = "opp3";
	static UserOpportunity opportunity1 = new UserOpportunity(opp1, getDate(2000, 1, 1), getDate(2000, 2, 1),
			"title1", "description1", "companyId1",
			"contractType1",  1000.10,  
			getDate(2000, 1, 1), getDate(2000, 1, 1),
			false, "source1", "url1", "location1",
			UserOpportunity.ApplicationStatus.APPLIED, "note1");
	
	static UserOpportunity opportunity2 = new UserOpportunity(opp2, getDate(2000, 1, 2), getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  2,  
			getDate(2000, 1, 2), getDate(2000, 1, 2),
			false, "source2", "url2", "location2",
			UserOpportunity.ApplicationStatus.NEW, "note2");

	static UserOpportunity opportunity3 = new UserOpportunity(opp3, getDate(2000, 1, 2), getDate(2000, 2, 2),
			"title2", "description2", "companyId2",
			"contractType2",  2,  
			getDate(2000, 1, 2), getDate(2000, 1, 2),
			false, "source2", "url2", "location2",
			UserOpportunity.ApplicationStatus.CLOSED, "note3");
	
	Vector<UserOpportunity> contactList =  new Vector<UserOpportunity>(Arrays.asList(opportunity1, opportunity2, opportunity3));
	
	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsSet, callsDelete, callsGetSingle;

		@Override
		public void getUserOpportunityList(UserId id, String list,
				AsyncCallback<Vector<UserOpportunity>> callback)
				throws CassandraException {
			callsGet++;
			callback.onSuccess(contactList);
		}
		@Override
		public void getUserOpportunity(UserId id, String oppId,
				AsyncCallback<UserOpportunity> callback)
				throws CassandraException {
			callsGetSingle++;
			callback.onSuccess(opportunity2);	
		}
		@Override
		public void setUserOpportunity(UserId id, String list,
				UserOpportunity opp, AsyncCallback<String> callback)
				throws CassandraException {
			callsSet++;
			callback.onSuccess("");
		}
		
		@Override
		public void deleteUserOpportunity(UserId id, String oppId,
				AsyncCallback<String> callback) throws CassandraException {
			callsDelete++;
			callback.onSuccess("");
		}
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();
	
	HorizontalPanel p;
	
	@Before
	public void beforeContentUserOpportunity()
	{
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					return userService;
				}
				return null;
			}}
		);
		p = new HorizontalPanel();		
	}
	
	@Test
	public void testGetAll() throws InterruptedException
	{
		userService.callsGet = 0;
		cuo = new ContentUserOpportunity(
				p, userId);
		cuo.onModuleLoad();
		assertEquals(1, userService.callsGet);
		assertEquals(3, cuo.cellTable.getRowCount());
		
		assertEquals(opp1, cuo.cellTable.getVisibleItem(0).ID);
		assertEquals(opp2, cuo.cellTable.getVisibleItem(1).ID);
		assertEquals(opp3, cuo.cellTable.getVisibleItem(2).ID);
		
		// Check columns values
		
		assertEquals(11, cuo.cellTable.getColumnCount());
		assertEquals(opportunity1.title,                                         cuo.cellTable.getColumn(3).getValue(opportunity1));
		assertEquals(opportunity1.companyId,                                     cuo.cellTable.getColumn(4).getValue(opportunity1));
		assertEquals("Candidaté",                                                cuo.cellTable.getColumn(5).getValue(opportunity1));
		assertEquals(opportunity1.location,                                      cuo.cellTable.getColumn(6).getValue(opportunity1));
		assertEquals(NumberFormat.getFormat("0.00").format(opportunity1.salary), cuo.cellTable.getColumn(7).getValue(opportunity1));
		assertEquals(opportunity1.contractType,                                  cuo.cellTable.getColumn(8).getValue(opportunity1));
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(opportunity1.firstSeen),
																				 cuo.cellTable.getColumn(9).getValue(opportunity1));
		
		// Click on 1 column		
		Browser.click(cuo.cellTable, opportunity2);
		assertEquals(opportunity2.source,       cuo.labelTextSource.getText());
		assertEquals(opportunity2.description,  cuo.panelDescriptionContent.getHTML());
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(opportunity2.lastUpdate),
				cuo.labelCreationDate.getText());
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(opportunity2.startDate),
				cuo.labelStartDate.getText());
		assertEquals(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(opportunity2.endDate),
				cuo.labelEndDate.getText());
	}	
}
