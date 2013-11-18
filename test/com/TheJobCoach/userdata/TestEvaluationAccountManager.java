package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.util.MockMailer;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;


public class TestEvaluationAccountManager
{

	static EvaluationAccountManager evaluationAccount = new EvaluationAccountManager();
	static AccountManager account = new AccountManager();

	static String id =  "toto" + UUID.randomUUID().hashCode();
	static String email =  id + "@toto.com";
	static MockMailer mockMail = new MockMailer();
	static String token;

	static String idAdmin =  "admin" + UUID.randomUUID().hashCode();
	static String tokenAdmin = "tokenAdmin" + UUID.randomUUID().hashCode();

	static String idCoach =  "coach" + UUID.randomUUID().hashCode();
	static String tokenCoach = "tokenCoach" + UUID.randomUUID().hashCode();

	static String idSeeker =  "seeker" + UUID.randomUUID().hashCode();
	static String tokenSeeker = "tokenSeeker" + UUID.randomUUID().hashCode();

	@Test
	public void test06GetTestAccountList() throws CassandraException
	{
		Map<String, String> accountList = evaluationAccount.getTestAccountList();
		Collection<String> accountNames = accountList.values();
		evaluationAccount.purgeTestAccount(0);
		accountList = evaluationAccount.getTestAccountList();
		accountNames = accountList.values();
		assertEquals(0, accountNames.size());
	}
	
	@Test
	public void test07CreateTestAccount() throws CassandraException, SystemException, InterruptedException
	{
		String beforeTime = SiteUUID.getDateUuid();		
		UserId user1 = evaluationAccount.createTestAccount("FR", UserId.UserType.USER_TYPE_SEEKER);
		String afterTime = SiteUUID.getDateUuid();		
		Map<String, String> accountList = evaluationAccount.getTestAccountList();
		assertEquals(1, accountList.size());
		Vector<String> accountNames = new Vector<String>(accountList.values());
		Vector<String> accountTimes = new Vector<String>(accountList.keySet());
		assertEquals(1, accountNames.size());
		assertEquals(user1.userName, accountNames.get(0));
		assertEquals(1, accountTimes.size());
		String time = accountTimes.get(0);
		assertTrue(beforeTime.compareTo(time) < 0);
		assertTrue(time.compareTo(afterTime) < 0);
		
		Thread.currentThread();
		Thread.sleep(2000);
		
		UserId user2 = evaluationAccount.createTestAccount("FR", UserId.UserType.USER_TYPE_SEEKER);
		String afterTime2 = SiteUUID.getDateUuid();		
		accountList = evaluationAccount.getTestAccountList();
		assertEquals(2, accountList.size());
		assertTrue(accountList.values().contains(user1.userName));
		assertTrue(accountList.values().contains(user2.userName));
		Vector<String> accountTimes2 = new Vector<String>(accountList.keySet());
		assertTrue(accountTimes2.get(0).equals(time));
		String time2 = accountTimes2.get(1);
		assertTrue(afterTime.compareTo(time2) < 0);
		assertTrue(time2.compareTo(afterTime2) < 0);
						
		MainPageReturnLogin loginCred1 = account.loginAccount(user1.userName, "recherche");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred1.getLoginStatus());
		
		MainPageReturnLogin loginCred2 = account.loginAccount(user2.userName, "recherche");
		assertEquals(MainPageReturnLogin.LoginStatus.CONNECT_STATUS_OK, loginCred2.getLoginStatus());

		evaluationAccount.purgeTestAccount(1);
		accountList = evaluationAccount.getTestAccountList();
		Vector<String> accountNames2 = new Vector<String>(accountList.values());
		assertEquals(1, accountNames2.size());
		assertEquals(user2.userName, accountNames2.get(0));
		
	}
	
}
