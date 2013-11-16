package com.TheJobCoach.webapp.userpage.client.MyGoals;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.GoalReportInformation;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.util.client.DefaultUtilServiceAsync;
import com.TheJobCoach.webapp.util.client.IExtendedField;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestContentMyGoals extends GwtTest {

	static Logger logger = LoggerFactory.getLogger(AutoTestContentMyGoals.class);

	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	private ContentMyGoals cud;
	
	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public Date start;
		public Date end;
		
		public int succeedStartDay = 4;
		public int succeedEndDay = 5;
		
		@Override
		public void getUserGoalReport(UserId id, Date start, Date end,
				AsyncCallback<GoalReportInformation> callback)
		{
			this.start = start;
			this.end = end;
			calls++;
			GoalReportInformation result = new GoalReportInformation();
			result.connectedDays = 3;
			result.succeedStartDay = this.succeedStartDay;
			result.succeedEndDay = this.succeedEndDay;
			result.newOpportunities = 10;
			result.log.put(UserLogEntry.LogEntryType.APPLICATION, 4);
			result.log.put(UserLogEntry.LogEntryType.INTERVIEW, 5);
			result.log.put(UserLogEntry.LogEntryType.PROPOSAL, 6);
			result.log.put(UserLogEntry.LogEntryType.RECALL, 7);
			logger.info("getUserGoalReport start:" + start + " end:" + end);
			callback.onSuccess(result);
		}
		
		public void reset()
		{
			  succeedStartDay = 4;
			  succeedEndDay = 5;
			  calls = 0;
		}
		
	}

	class SpecialUtilServiceAsync extends DefaultUtilServiceAsync
	{
		
	}

	static SpecialUserServiceAsync userService;
	static SpecialUtilServiceAsync utilService;
	
	HorizontalPanel p;
	
	@Before
	public void beforeContentMyReports()
	{
		if ( userService == null) userService = new SpecialUserServiceAsync();
		if ( utilService == null) utilService = new SpecialUtilServiceAsync();
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					return userService;
				}
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.util.client.UtilService"))
				{
					return utilService;
				}
				return null;
			}}
		);
		p = new HorizontalPanel();		
	}

	private void printAndCheck(String name, String toCheck, String value)
	{
		logger.info("Check field " + name + " value:" + value + " expected:" + toCheck);
		assertEquals(toCheck, value);
	}
	
	void checkFields(ContentMyGoals cmg, 
			String tfGoalPeriod,
			String tfConnectBefore, String reConnectBefore,
			String tfConnectAfter, String reConnectAfter,
			String tfConnectRatio, String reConnectRatio,
			String tfCreateOpportunity,	String reCreateOpportunity,
			String tfCandidateOpportunity, String reCandidateOpportunity,
			String tfInterviewOpportunity, String reInterviewOpportunity,
			String tfProposal, String reProposal,
			String tfPhoneCall,	String rePhoneCall)
	{
		printAndCheck("tfConnectBefore", tfConnectBefore, cmg.tfConnectBefore.getItem().getText());
		printAndCheck("reConnectBefore", reConnectBefore, cmg.reConnectBefore.actualPerformance.getText());

		printAndCheck("tfConnectAfter",tfConnectAfter , cmg.tfConnectAfter.getItem().getText());
		printAndCheck("reConnectAfter",reConnectAfter , cmg.reConnectAfter.actualPerformance.getText());

		printAndCheck("tfConnectRatio", tfConnectRatio, cmg.tfConnectRatio.getText());
		printAndCheck("reConnectRatio", reConnectRatio, cmg.reConnectRatio.actualPerformance.getText());

		printAndCheck("tfCreateOpportunity", tfCreateOpportunity, cmg.tfCreateOpportunity.getText());
		printAndCheck("reCreateOpportunity", reCreateOpportunity, cmg.reCreateOpportunity.actualPerformance.getText());

		printAndCheck("tfCandidateOpportunity", tfCandidateOpportunity, cmg.tfCandidateOpportunity.getText());
		printAndCheck("reCandidateOpportunity", reCandidateOpportunity, cmg.reCandidateOpportunity.actualPerformance.getText());

		printAndCheck("tfInterviewOpportunity", tfInterviewOpportunity, cmg.tfInterviewOpportunity.getText());
		printAndCheck("reInterviewOpportunity", reInterviewOpportunity, cmg.reInterviewOpportunity.actualPerformance.getText());

		printAndCheck("tfProposal", tfProposal, cmg.tfProposal.getText());
		printAndCheck("reProposal", reProposal, cmg.reProposal.actualPerformance.getText());

		printAndCheck("tfPhoneCall", tfPhoneCall, cmg.tfPhoneCall.getText());
		printAndCheck("rePhoneCall", rePhoneCall, cmg.rePhoneCall.actualPerformance.getText());
	}
	
	@Test
	public void testNoValue()
	{
		SpecialUtilServiceAsync.calls = 0;		
		userService.reset();		

		cud = new ContentMyGoals(
				p, userId);
		cud.onModuleLoad();		
		assertEquals(1, SpecialUtilServiceAsync.calls);		
		assertEquals(2, SpecialUserServiceAsync.calls);		
		checkFields(cud, "",
				"12:00", "",
				"23:59", "",
				"", "",
				"", "",
				"", "",
				"", "",
				"", "",
				"", ""
				);
	}
	
	void checkValueChange(IExtendedField w, ResultEvaluation re, int realValue)
	{
		w.setValue(String.valueOf(realValue - 1));
		TestResultEvaluation.checkResultEvaluationContext(re, TestResultEvaluation.STATUS.SUCCESS, realValue);
		w.setValue(String.valueOf(realValue + 1));
		TestResultEvaluation.checkResultEvaluationContext(re, TestResultEvaluation.STATUS.FAILURE, realValue);
		w.setValue("");
		TestResultEvaluation.checkResultEvaluationContext(re, TestResultEvaluation.STATUS.NOTSET, realValue);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testAllValue()
	{
		SpecialUtilServiceAsync.calls = 0;		
		userService.reset();		
		
		SpecialUtilServiceAsync.addValue(
				UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD, 
				UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD__2WEEK);
		SpecialUtilServiceAsync.addValue(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, new Long(9*60*60*1000).toString());
		SpecialUtilServiceAsync.addValue(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR, new Long(18*60*60*1000).toString());
		SpecialUtilServiceAsync.addValue(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO, "1");
		
		SpecialUtilServiceAsync.addValue(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY, "2");
		SpecialUtilServiceAsync.addValue(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY, "3");
		SpecialUtilServiceAsync.addValue(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW, "4");
		SpecialUtilServiceAsync.addValue(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL, "5");		
		SpecialUtilServiceAsync.addValue(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL, "6");
		SpecialUtilServiceAsync.addValue(UserValuesConstantsMyGoals.PERFORMANCE_RECALL_GOAL_MIDDLE, "7");
			
		cud = new ContentMyGoals(
				p, userId);
		cud.onModuleLoad();		
		assertEquals(1, SpecialUtilServiceAsync.calls);		
		assertEquals(5, SpecialUserServiceAsync.calls);
		SpecialUtilServiceAsync.calls = 0;		
		SpecialUserServiceAsync.calls = 0;	

		checkFields(cud, "1",
				"09:00", "4",
				"18:00", "5",
				"1", "3",
				"2", "10",
				"3", "4",
				"4", "5",
				"5", "6",
				"6", "7"
				);
		
		Date prev = new Date(cud.previousDate.getText());
		Date next = new Date(cud.nextDate.getText());
		long diff = next.getTime() - prev.getTime();
		assertTrue(diff > 24*60*60*1000 * 12);
		assertTrue(diff < 24*60*60*1000 * 15);
		Date current = new Date();
		assertTrue(prev.before(current));
		assertTrue(next.after(current));
		assertFalse(cud.nextButton.isEnabled());
		assertTrue(cud.prevButton.isEnabled());
		// Check start/end on request
		long timeStart = userService.start.getTime() - prev.getTime();
		long timeEnd = userService.end.getTime() - next.getTime();
		assertEquals(timeStart, 60*60*1000 * 9);
		assertEquals(timeEnd, 60*60*1000 * 18);
		
		
		cud.prevButton.click();
		assertEquals(0, SpecialUtilServiceAsync.calls);		
		assertEquals(1, SpecialUserServiceAsync.calls);		
		
		assertTrue(cud.nextButton.isEnabled());
		assertTrue(cud.prevButton.isEnabled());
		
		Date prev2 = new Date(cud.previousDate.getText());
		Date next2 = new Date(cud.nextDate.getText());
		diff = next2.getTime() - prev2.getTime();
		assertTrue(diff > 24*60*60*1000 * 12);
		assertTrue(diff < 24*60*60*1000 * 15);
		assertTrue(prev.before(current));
		assertTrue(next.after(current));
		assertTrue(next2.before(prev));
		assertTrue(cud.nextButton.isEnabled());
		assertTrue(cud.prevButton.isEnabled());
		
		// test content change 
		checkValueChange(cud.tfConnectRatio, cud.reConnectRatio, 3);
		checkValueChange(cud.tfCreateOpportunity, cud.reCreateOpportunity, 10);
		checkValueChange(cud.tfCandidateOpportunity, cud.reCandidateOpportunity, 4);
		checkValueChange(cud.tfInterviewOpportunity, cud.reInterviewOpportunity, 5);
		checkValueChange(cud.tfProposal, cud.reProposal, 6);
		checkValueChange(cud.tfPhoneCall, cud.rePhoneCall, 7);
		
		// check actual success for start time and end time. 1/ not set
		TestResultEvaluation.checkResultEvaluationContext(cud.reConnectBefore, TestResultEvaluation.STATUS.NOTSET, 4);
		TestResultEvaluation.checkResultEvaluationContext(cud.reConnectAfter, TestResultEvaluation.STATUS.NOTSET, 5);
		// 2/ set value to success
		cud.tfConnectRatio.setValue("3");
		TestResultEvaluation.checkResultEvaluationContext(cud.reConnectBefore, TestResultEvaluation.STATUS.SUCCESS, 4);
		TestResultEvaluation.checkResultEvaluationContext(cud.reConnectAfter, TestResultEvaluation.STATUS.SUCCESS, 5);
		// 3/ set connect ratio goal to upper value and check for failure.
		cud.tfConnectRatio.setValue("50");
		TestResultEvaluation.checkResultEvaluationContext(cud.reConnectBefore, TestResultEvaluation.STATUS.FAILURE, 4);
		TestResultEvaluation.checkResultEvaluationContext(cud.reConnectAfter, TestResultEvaluation.STATUS.FAILURE, 5);
		
		// special case for start time change
		DefaultUserServiceAsync.calls = 0;
		userService.succeedStartDay = 100;
		cud.tfConnectBefore.setValue(String.valueOf(7*60*60*1000)); // 7:00
		assertTrue(SpecialUserServiceAsync.calls != 0);
		diff = userService.start.getTime() - FormatUtil.startOfTheDay(userService.start).getTime();
		assertEquals(7*60*60*1000, diff);
		assertEquals(100, cud.reConnectBefore.value);
		TestResultEvaluation.checkResultEvaluationContext(cud.reConnectBefore, TestResultEvaluation.STATUS.SUCCESS, 100);
		TestResultEvaluation.checkResultEvaluationContext(cud.reConnectAfter, TestResultEvaluation.STATUS.FAILURE, 5);

		// special case for end time change
		DefaultUserServiceAsync.calls = 0;
		userService.succeedEndDay = 200;
		cud.tfConnectAfter.setValue(String.valueOf(9*60*60*1000)); // 9:00
		assertTrue(SpecialUserServiceAsync.calls != 0);
		diff = userService.end.getTime() - FormatUtil.startOfTheDay(userService.end).getTime();
		assertEquals(9*60*60*1000, diff);
		assertEquals(200, cud.reConnectAfter.value);
		TestResultEvaluation.checkResultEvaluationContext(cud.reConnectBefore, TestResultEvaluation.STATUS.SUCCESS, 100);
		TestResultEvaluation.checkResultEvaluationContext(cud.reConnectAfter, TestResultEvaluation.STATUS.SUCCESS, 200);
}

}
