package com.TheJobCoach.userdata.background;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.userdata.UserValues;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MockMailer;
import com.TheJobCoach.util.StringResourceCache;
import com.TheJobCoach.webapp.userpage.shared.GoalReportInformation;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstants;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;

public class TestBackgroundProcess
{
	
	BackgroundProcess bp = new BackgroundProcess();
	UserId user = new UserId("userbackground", "password", UserId.UserType.USER_TYPE_SEEKER);
	AccountManager account = new AccountManager();
	MockMailer mockMail = new MockMailer();
	UserValues values = new UserValues();
	
	@Test
	public void test_checkCoachMailForUser() throws CassandraException, SystemException, FileNotFoundException, UnsupportedEncodingException
	{
		MailerFactory.setMailer(mockMail);
		account.deleteAccount(user.userName);
		CoachTestUtils.createOneAccount(user);
		
		// don't receive
		mockMail.reset();
		bp.checkCoachMailForUser(user.userName, CoachTestUtils.getDate(2014, 1, 20));
		assertNull(mockMail.lastDst);
		
		// receive 1st time
		mockMail.reset();
		values.setValue(user, UserValuesConstantsMyGoals.PERFORMANCE_RECEIVE_EMAIL, UserValuesConstants.YES, false);
		bp.checkCoachMailForUser(user.userName, CoachTestUtils.getDate(2014, 1, 20));
		assertNotNull(mockMail.lastDst);
		mockMail.writeToFile("/tmp/file.html");
		// check dates (english)
		assertTrue(mockMail.lastBody.contains("Jan 1, 2014 "));
		assertTrue(mockMail.lastBody.contains("Jan 31, 2014."));

		// already received for period
		mockMail.reset();
		bp.checkCoachMailForUser(user.userName, CoachTestUtils.getDate(2014, 1, 20));
		assertNull(mockMail.lastDst);
		
		// next period
		mockMail.reset();
		bp.checkCoachMailForUser(user.userName, CoachTestUtils.getDate(2014, 2, 20));
		assertNotNull(mockMail.lastDst);
		mockMail.writeToFile("/tmp/file.html");
	
		// change period type, language, goals
		mockMail.reset();
		values.setValue(user, UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD, UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD__WEEK, true);
		values.setValue(user, UserValuesConstantsAccount.ACCOUNT_LANGUAGE, UserValuesConstantsAccount.ACCOUNT_LANGUAGE__FR, true);
		bp.checkCoachMailForUser(user.userName, CoachTestUtils.getDate(2014, 3, 20));
		assertNotNull(mockMail.lastDst);
		mockMail.writeToFile("/tmp/file.html");
		assertTrue(mockMail.lastBody.contains("17 mars 2014 "));
		assertTrue(mockMail.lastBody.contains("23 mars 2014."));

		// check reported values, depending on goals & performance
		Map<String, String> vals = new HashMap<String, String>();
		vals.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO,          "3");
		vals.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY,      "4");
		vals.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY,   "5");
		vals.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW,              "6");
		vals.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL,              "7");
		vals.put(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL,               "800"); // succcess
		Date periodStart = CoachTestUtils.getDate(2014, 1, 1);
		Date periodEnd = CoachTestUtils.getDate(2014, 1, 2);
		GoalReportInformation reportInformation = new GoalReportInformation(periodStart, periodEnd);
		reportInformation.log.put(LogEntryType.APPLICATION, 105);
		reportInformation.log.put(LogEntryType.INTERVIEW, 106);
		reportInformation.log.put(LogEntryType.RECALL, 107);
		reportInformation.log.put(LogEntryType.PROPOSAL, 108);
		reportInformation.succeedStartDay = 101;
		reportInformation.succeedEndDay = 102;
		reportInformation.connectedDays = 103;
		reportInformation.newOpportunities = 104;
		StringResourceCache.getInstance().setStringResource(
				"/com/TheJobCoach/userdata/data/mail_performance_report_fr.html",
				""
						+ "N  _FIRSTNAME_ _NAME_ DATE _START_ _END_."
						+ "A _ARRIVAL_RESULT_ _ARRIVAL_GOAL_ _ARRIVAL_IMG_ "
						+ "B _DEPARTURE_RESULT_ _DEPARTURE_GOAL_ _DEPARTURE_IMG_ "
						+ "C _CONNECTED_RESULT_ _CONNECTED_GOAL_ _DEPARTURE_IMG_ "
						+ "D _OPP_RESULT_ _OPP_GOAL_ _OPP_IMG_ "
						+ "E _APPLICATION_RESULT_ _APPLICATION_GOAL_ _APPLICATION_IMG_ "
						+ "F _INTERVIEW_RESULT_ _INTERVIEW_GOAL_ _INTERVIEW_IMG_ "
						+ "G _PHONECALL_RESULT_ _PHONECALL_GOAL_ _PHONECALL_IMG_ "
						+ "H _PROPOSAL_RESULT_ _PROPOSAL_GOAL_ _PROPOSAL_IMG_ ");
		mockMail.reset();
		bp.sendReportMail(user, periodStart, periodEnd, vals, reportInformation);
		assertNotNull(mockMail.lastDst);
		assertEquals(mockMail.lastBody,
				"N  firstNameuserbackground lastNameuserbackground DATE 1 janv. 2014 2 janv. 2014.A 101 3 failurelogo B 102 3 failurelogo C 103 3 failurelogo D 104 4 failurelogo E 105 5 failurelogo F 106 6 failurelogo G 107 7 failurelogo H 108 800 successlogo ");

		// check reported values in case goal is not set.
		vals.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY,      "");
		vals.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY,   "garbage");
		StringResourceCache.getInstance().setStringResource(
				"/com/TheJobCoach/userdata/data/mail_performance_report_fr.html",
				"D _OPP_RESULT_ _OPP_GOAL_ _OPP_IMG_ "+
			    "E _APPLICATION_RESULT_ _APPLICATION_GOAL_ _APPLICATION_IMG_ "
						);
		mockMail.reset();
		bp.sendReportMail(user, periodStart, periodEnd, vals, reportInformation);
		assertNotNull(mockMail.lastDst);
		assertEquals(mockMail.lastBody, 
				"D   nostatuslogo E  garbage nostatuslogo ");

	}	
}
