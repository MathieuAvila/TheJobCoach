package com.TheJobCoach.util;

import com.TheJobCoach.userdata.Account;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;

import org.junit.Test;

public class AdminSiteSimple {
	
	@Test
	public void testCreateAccount()
	{
		CassandraAccessor.setLocation("192.168.0.50:9160");

		Account account = new Account();
		account.purgeAccount();
		account.listUser();


	}
	
}
