package com.TheJobCoach.userdata;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;

public class EvaluationAccountManager
{
	final static String COLUMN_FAMILY_TEST_LIST = "accounttestlist";
	private static final String CONSTANT_TEST_LIST_ROW = "testlist";

	static AccountManager accountManager = new AccountManager();
	
	protected Map<String, String> getTestAccountList() throws CassandraException
	{
		Map<String, String> result = CassandraAccessor.getRow(COLUMN_FAMILY_TEST_LIST, CONSTANT_TEST_LIST_ROW);
		if (result == null)
		{
			return new TreeMap<String, String>();
		}
		return result;
	}
	
	public void purgeTestAccount(int purgeTime) throws CassandraException
	{
		Map<String, String> testAccountList = getTestAccountList();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, -purgeTime);
		String currentTime = SiteUUID.dateFormatter(c.getTime());
		// purge.
		Set<String> keys =  testAccountList.keySet();
		for (String key: keys)
		{
			if (currentTime.compareTo(key) > 0) // purge
			{
				String userName = testAccountList.get(key);				
				accountManager.deleteUserAccount(new UserId(userName, "", UserId.UserType.USER_TYPE_SEEKER));
				CassandraAccessor.deleteColumn(COLUMN_FAMILY_TEST_LIST, CONSTANT_TEST_LIST_ROW, key);
			}
		}
	}
	
	public UserId createTestAccount(String langStr, UserId.UserType userType) throws CassandraException, SystemException
	{
		if (userType == UserId.UserType.USER_TYPE_ADMIN) throw new SystemException();
		boolean exist = true;
		String userName = null;
		int counter = 0;
		Map<String, String> testAccountList = getTestAccountList();	
		// Get a valid time stamp.
		String time = null;
		do 
		{
			time = SiteUUID.getDateUuid();
		}
		while (testAccountList.get(time) != null);
		
		do 
		{
			counter++;
			if (counter == 1000)
			{
				throw new SystemException();
			}
			time = SiteUUID.getDateUuid();
			if (testAccountList.get(time) == null)
			{
				userName = "test#" + new Random().nextInt(1000);
				if (!accountManager.existsAccount(userName))
				{
					exist = false;
				}
			}
		}
		while (exist);
		CassandraAccessor.updateColumn(COLUMN_FAMILY_TEST_LIST, CONSTANT_TEST_LIST_ROW,
				(new ShortMap())
				.add(time, userName)
				.get());
		UserId result = new UserId(userName, userName, userType);
		UserInformation info = new UserInformation(
				Lang.getTestName(langStr), 
				userName + "@recherche.com", 
				"recherche", 
				Lang.getTestLastName(langStr));
		accountManager.createAccountWithTokenNoMail(result, info, langStr);
		accountManager.validateAccount(userName, userName);
		UserDataCentralManager.createTestUser(result, langStr);
		return result;
	}

}
