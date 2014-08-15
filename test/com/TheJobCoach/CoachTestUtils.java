package com.TheJobCoach;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.userpage.client.Coach.MessagePipe;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;


public class CoachTestUtils
{
	static AccountManager account = new AccountManager();
	
	@SuppressWarnings("deprecation")
	public static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month - 1);
		result.setYear(year - 1900);
		return result;
	}

	@SuppressWarnings("deprecation")
	public static boolean isDateEqualForDay(Date d1, Date d2)
	{
		System.out.println(
				d1.getDate() + " " + d2.getDate() 
				+ " - "	+ d1.getMonth() + " " + d2.getMonth()
				+ " - " + d1.getYear() + " " + d2.getYear());
		return (d1.getDate() == d2.getDate())
				&& (d1.getMonth() == d2.getMonth()) 
				&& (d1.getYear() == d2.getYear());
	}

	public static void createOneAccount(UserId user) throws CassandraException
	{
		UserInformation info = new UserInformation(
				"lastName" + user.userName, 
				user.userName + "@toto.com", 
				"",
				"firstName" + user.userName);
		account.deleteAccount(user.userName);
		CreateAccountStatus status = account.createAccountWithTokenNoMail(user, info, "en");
		assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
		ValidateAccountStatus validated = account.validateAccount(user.userName, user.token);
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, validated);
	}
	
	public static void resetClientState()
	{
		ClientUserValuesUtils.instance = null;
		MessagePipe.instance = null;
	}
	
}
