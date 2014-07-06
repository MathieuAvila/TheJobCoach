package com.TheJobCoach.userdata.background;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.userdata.Lang;
import com.TheJobCoach.userdata.UserValues;
import com.TheJobCoach.userdata.report.GoalReport;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MailerInterface;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.userpage.shared.GoalReportInformation;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstants;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;

public class BackgroundProcess
{
	Logger logger = LoggerFactory.getLogger(BackgroundProcess.class);

	AccountManager account = new AccountManager();
	UserValues values = new UserValues();
	GoalReport report = new GoalReport();

	String replaceStatus(String orig, String key, String text, int result, String goalStr)
	{
		int status = 0;
		int goal = 0;
		try {
			goal = Integer.parseInt(goalStr);
			status = (result - goal) <= 0 ? 1:-1;
		} catch (Exception e){};
		
		//orig = orig.replace(key, text);
		
		logger.info( key );
		logger.info("_" + key + "_IMG_" + goalStr + "   " + (status == 0 ? "nostatuslogo" : (status < 0 ? "failurelogo":"successlogo")));
		orig = orig.replace("_" + key + "_IMG_", status == 0 ? "nostatuslogo" : (status < 0 ? "failurelogo":"successlogo"));
		
		logger.info("_" + key + "_GOAL_" + goalStr );
		orig = orig.replace("_" + key + "_GOAL_", goalStr);
		
		logger.info("_" + key + "_RESULT_" + "   " + (status != 0 ? String.valueOf(result) : ""));
		orig = orig.replace("_" + key + "_RESULT_", status != 0 ? String.valueOf(result) : "");
		
		return orig;
	}
	
	void sendReportMail(UserId user, Date periodStart, Date periodEnd, Map<String, String> vals, GoalReportInformation reportInformation)
	{		
		try
		{
			// retrieve email & lang.
			UserInformation info = new UserInformation();
			account.getUserInformation(user, info);
			String lang = account.getUserLanguage(user);
			String coach = values.getValue(user, UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR);

			logger.info("====== Send email to user " + user.userName + " for period starting at:" + periodStart + " to: " + periodEnd 
					+ " email: " + info.email + " coach: " + coach + " lang: " + lang);

			Map<String, MailerInterface.Attachment> parts = new HashMap<String, MailerInterface.Attachment>();
			parts.put("thejobcoachlogo", new MailerInterface.Attachment("/com/TheJobCoach/webapp/mainpage/client/thejobcoach-icon-300x64.png", "image/png", "img_logo.png"));
			parts.put("successlogo", new MailerInterface.Attachment("/com/TheJobCoach/webapp/util/client/images/success_24.png", "image/png", "img_success.png"));
			parts.put("failurelogo", new MailerInterface.Attachment("/com/TheJobCoach/webapp/util/client/images/failure_24.png", "image/png", "img_failure.png"));
			parts.put("nostatuslogo", new MailerInterface.Attachment("/com/TheJobCoach/webapp/util/client/images/unknown_24.png", "image/png", "img_nostatus.png"));
			parts.put("coachlogo", UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__DEFAULT_MAN.equals(coach) ? 
					new MailerInterface.Attachment("/com/TheJobCoach/webapp/userpage/client/images/george_2_150_150.png", "image/png", "img_coach.png"):
						new MailerInterface.Attachment("/com/TheJobCoach/webapp/userpage/client/images/christine_2_150_150.png", "image/png", "img_nostatus.png")
					);

			// build email
			String body = Lang.performanceReport(lang);
			body = body.replace("_FIRSTNAME_", info.firstName);
			body = body.replace("_NAME_", info.name);
			
			DateFormat df = Lang.getDateFormat(lang);
			
			body = body.replace("_START_", df.format(periodStart));
			body = body.replace("_END_", df.format(periodEnd));
			
			body = replaceStatus(body, "ARRIVAL", "", reportInformation.succeedStartDay, vals.get(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO));
			body = replaceStatus(body, "DEPARTURE", "", reportInformation.succeedEndDay, vals.get(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO));
			body = replaceStatus(body, "CONNECTED", "", reportInformation.connectedDays, vals.get(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO));

			body = replaceStatus(body, "OPP", "", reportInformation.newOpportunities, vals.get(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY));

			body = replaceStatus(body, "APPLICATION", "", reportInformation.log.get(LogEntryType.APPLICATION), vals.get(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY));
			body = replaceStatus(body, "INTERVIEW", "", reportInformation.log.get(LogEntryType.INTERVIEW), vals.get(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW));
			body = replaceStatus(body, "PROPOSAL", "", reportInformation.log.get(LogEntryType.PROPOSAL), vals.get(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL));
			body = replaceStatus(body, "PHONECALL", "", reportInformation.log.get(LogEntryType.RECALL), vals.get(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL));
			
			String subject =  Lang.performanceReportSubject(lang);
			boolean sent = MailerFactory.getMailer().sendEmail(info.email, subject, body, "noreply@www.thejobcoach.fr", parts);
			if (!sent)
				logger.warn("Failed to send email to "+info.email + ", to user: " + user.userName);
		}

		catch (Exception e)
		{
			logger.error("received exception in backgroundProcess " + e.getMessage());
			e.printStackTrace();
		}
		
	}

	boolean checkCoachMailForUser(String userName, Date currentDate) throws CassandraException, SystemException
	{
		//logger.info("Check user: " + userName);
		UserId user = new UserId(userName);
		Map<String, String> vals;
		vals = values.getValues(user, UserValuesConstantsMyGoals.PERFORMANCE);
		String receive = vals.get(UserValuesConstantsMyGoals.PERFORMANCE_RECEIVE_EMAIL);

		// Does user want to receive email ?
		if (receive.equals(UserValuesConstants.YES_NO_MAP.get(false)))
			return false;

		// now check period
		String periodString = vals.get(UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD);
		FormatUtil.PERIOD_TYPE period = UserValuesConstantsMyGoals.mapStringPeriod.get(periodString);
		Date periodStart = new Date(); 
		Date periodEnd = new Date();
		FormatUtil.getPeriod(period, 0, currentDate, periodStart, periodEnd);

		// Is periodEnd farer than last email.
		String periodLastMailStr = vals.get(UserValuesConstantsMyGoals.PERFORMANCE_LAST_EMAIL);
		Date periodLastMail = FormatUtil.getStringDate(periodLastMailStr);
		
		if (periodLastMail.after(periodStart))
			return false;

		// Now get Evaluation
		GoalReportInformation reportInformation = report.getReport(user, periodStart, periodEnd);
		// Now build mail report from results.
		sendReportMail(user, periodStart, periodEnd, vals, reportInformation);
		
		// Set last period
		values.setValue(user, UserValuesConstantsMyGoals.PERFORMANCE_LAST_EMAIL, FormatUtil.getDateString(currentDate), false);
		
		return true;
	}
	
	void checkCoachMail(Date currentDate)
	{
		String last = "";
		int ucount = 0;
		Vector<String> result = new Vector<String>();
		int count = 0;
		try {
			do
			{
				last = account.getUserRange(last, 100, result);
				for (String userName: result)
				{
					//logger.info("Check user: " + userName);
					last = userName;
					ucount++;
					if (checkCoachMailForUser(userName, currentDate)) count++;
				}
			}
			while (result.size() > 0);
		}
		catch (Exception e)
		{
			logger.error("received exception in backgroundProcess" + e.getMessage() + e.toString());
		}
		logger.info("End sending evaluation emails. Sent " + count + " mails, for a total of " + ucount + " user accounts.");
	}

	void run()
	{
		logger.info("Starting evaluation...");
		checkCoachMail(new Date());
	}
	
	public static void main(String[] args)
	{
		BackgroundProcess bp = new BackgroundProcess();
		bp.run();
	}
}
