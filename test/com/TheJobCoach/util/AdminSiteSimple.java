package com.TheJobCoach.util;

import com.TheJobCoach.userdata.Account;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;


import org.junit.Test;

public class AdminSiteSimple {

	@Test
	public void testCreateAccount() throws CassandraException
	{
		//CassandraAccessor.setLocation("192.168.0.50:9160");

		Account account = new Account();		
		{
			account.deleteAccount("admin");
			account.createAccountWithToken(
					new UserId("admin","mytokenadmin", UserId.UserType.USER_TYPE_ADMIN),
					new UserInformation("nom", "toto@totoadmin.com", "lvveumda", "prenom"), "en");
			account.validateAccount("admin", "mytokenadmin");
			account.loginAccount("admin", "lvveumda");
		}

		{
			account.createAccountWithToken(
					new UserId("avila","mytokenuser", UserId.UserType.USER_TYPE_SEEKER),
					new UserInformation("nom", "toto@totoseeker.com", "lvveumda", "prenom"), "en");
			account.validateAccount("avila", "mytokenuser");
			account.loginAccount("avila", "lvveumda");
		}
	}
}
