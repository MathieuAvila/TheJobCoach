package com.TheJobCoach.util;

import com.TheJobCoach.userdata.Account;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;

import org.junit.Test;

public class AdminSiteSimple {
	
	@Test
	public void testCreateAccount() throws CassandraException
	{
		CassandraAccessor.setLocation("192.168.0.50:9160");
		
		Account account = new Account();
		/*
		account.purgeAccount();
		account.listUser();
		 */
		{
		CreateAccountStatus status = account.createAccountWithToken(
				new UserId("admin","mytokenadmin", UserType.USER_TYPE_ADMIN),
				new UserInformation("nom", "toto@toto.com", "lvveumda", "prenom"), "en");
		System.out.println("Created account returned: " + status.toString());
		ValidateAccountStatus validate = account.validateAccount("admin", "mytokenadmin");
		System.out.println("Validate account returned: " + validate.toString());
		MainPageReturnLogin token = account.loginAccount("admin", "lvveumda");
		System.out.println("Login account returned: " + token.getLoginStatus() + " with token: " + token.id.token + " with type : " + token.id.type);
		}
	
		{
		CreateAccountStatus status = account.createAccountWithToken(
				new UserId("avila","mytokenuser", UserType.USER_TYPE_SEEKER),
				new UserInformation("nom", "toto@toto.com", "lvveumda", "prenom"), "en");
		System.out.println("Created account returned: " + status.toString());
		ValidateAccountStatus validate = account.validateAccount("avila", "mytokenuser");
		System.out.println("Validate account returned: " + validate.toString());
		MainPageReturnLogin token = account.loginAccount("avila", "lvveumda");
		System.out.println("Login account returned: " + token.getLoginStatus() + " with token: " + token.id.token + " with type : " + token.id.type);
		}
	}
}
