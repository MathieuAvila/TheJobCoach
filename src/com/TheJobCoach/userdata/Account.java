package com.TheJobCoach.userdata;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Vector;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.adminpage.shared.UserReport;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin.LoginStatus;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.TheJobCoach.webapp.util.shared.SystemException;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;


public class Account implements AccountInterface {

	static ColumnFamilyDefinition cfDef = null;
	static ColumnFamilyDefinition cfDefEmail = null;
	static ColumnFamilyDefinition cfDefValidation = null;
	static ColumnFamilyDefinition cfDefTestList = null;

	final static String COLUMN_FAMILY_NAME_ACCOUNT = "account";
	final static String COLUMN_FAMILY_NAME_EMAIL = "accountemail";
	final static String COLUMN_FAMILY_NAME_NOT_VALIDATED = "accountvalidation";
	final static String COLUMN_FAMILY_TEST_LIST = "accounttestlist";
	private static final String CONSTANT_TEST_LIST_ROW = "testlist";

	UserOpportunityManager oppManager = new UserOpportunityManager();
	UserValues valuesManager = new UserValues();
	UserJobSiteManager siteManager = new UserJobSiteManager();

	public Account()
	{
		cfDef = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_ACCOUNT, cfDef);
		cfDefEmail = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_EMAIL, cfDefEmail);
		cfDefValidation = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_NOT_VALIDATED, cfDefValidation);
		cfDefTestList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_TEST_LIST, cfDefTestList);
	}

	public boolean existsAccount(String userName)
	{
		String token = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "token");
		return ((token != null)&&(!token.equals("")));
	}

	protected String userTypeToString(UserId.UserType type)
	{
		switch (type)
		{
		case USER_TYPE_SEEKER: return "seeker";
		case USER_TYPE_COACH:  return "coach";
		case USER_TYPE_ADMIN:  return "admin";
		}
		return "";
	}

	protected UserId.UserType stringToUserType(String type)
	{
		if (type.equals("seeker"))
			return UserId.UserType.USER_TYPE_SEEKER;
		if (type.equals("coach"))
			return UserId.UserType.USER_TYPE_COACH;
		if (type.equals("admin"))
			return UserId.UserType.USER_TYPE_ADMIN;
		return UserId.UserType.USER_TYPE_SEEKER;
	}

	public boolean updateUserInformation(UserId id, UserInformation info) throws CassandraException
	{
		return CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, 
				(new ShortMap())
				.add("email", info.email)
				.add("name", info.name)
				.add("password", info.password)
				.add("firstname", info.firstName)
				.add("date", new Date())
				.add("token", id.token)
				.add("type", userTypeToString(id.type))
				.get());
	}

	public String getUsernameFromEmail(String mail)
	{
		String result = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_EMAIL, mail, "username");	
		return result;
	}

	private CreateAccountStatus createAccountWithTokenNoMail(UserId id, UserInformation info, String langStr) throws CassandraException
	{
		if (existsAccount(id.userName))
			return CreateAccountStatus.CREATE_STATUS_ALREADY_EXISTS;
		if (getUsernameFromEmail(info.email) != null)
			return CreateAccountStatus.CREATE_STATUS_ALREADY_EXISTS_EMAIL;
		boolean result = CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_NOT_VALIDATED, id.userName, 
				(new ShortMap())
				.add("validated", false)
				.get());
		if (!result) return CreateAccountStatus.CREATE_STATUS_ERROR;
		result = CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_EMAIL, info.email, 
				(new ShortMap())
				.add("username", id.userName)
				.get());
		if (!result) return CreateAccountStatus.CREATE_STATUS_ERROR;
		result = CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, 
				(new ShortMap())
				.add("username", id.userName)
				.add("validated", false)
				.get());
		if (!result) return CreateAccountStatus.CREATE_STATUS_ERROR;
		result = updateUserInformation(id, info);
		if (!result) return CreateAccountStatus.CREATE_STATUS_ERROR;
		return CreateAccountStatus.CREATE_STATUS_OK;
	}
	
	public CreateAccountStatus createAccountWithToken(UserId id, UserInformation info, String langStr) throws CassandraException
	{
		CreateAccountStatus result = createAccountWithTokenNoMail(id, info, langStr);
		if (result != CreateAccountStatus.CREATE_STATUS_OK) return result;
		String body = Lang._TextBody(info.firstName, com.TheJobCoach.util.SiteDef.getAddress(), id.userName, id.token, langStr);
		MailerFactory.getMailer().sendEmail(info.email, Lang._TextSubject(langStr), body, "noreply@www.thejobcoach.fr");
		return CreateAccountStatus.CREATE_STATUS_OK;
	}

	public CreateAccountStatus createAccount(UserId id, UserInformation info, String langStr) throws CassandraException
	{
		UUID uuid = UUID.randomUUID();
		String token = id.userName + "_" + uuid.toString();
		id.token = token;
		return createAccountWithToken(id, info, langStr);
	}

	public ValidateAccountStatus validateAccount(String userName, String token) throws CassandraException
	{		
		String tokenDB = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "token");
		if ((userName == null) || (token == null) || (tokenDB == null) || (!tokenDB.equals(token)))
			return ValidateAccountStatus.VALIDATE_STATUS_UNKNOWN;
		boolean result = 
				CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName,
						(new ShortMap())
						.add("validated", true)
						.add("date", new Date())
						.get());
		if (!result) return ValidateAccountStatus.VALIDATE_STATUS_ERROR;
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_NOT_VALIDATED, userName);		
		return ValidateAccountStatus.VALIDATE_STATUS_OK;
	}

	public MainPageReturnLogin loginAccount(String userName, String password)
	{
		System.out.println("Try to login as: " + userName + " with password: " + password);
		String token = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "token");
		if (token == null)
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_UNKNOWN_USER);
		System.out.println("Token is: " + token);
		String validatedStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "validated");
		if (validatedStr == null)
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_NOT_VALIDATED);
		boolean validated = ShortMap.getBoolean(validatedStr);
		if (!validated) return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_NOT_VALIDATED);
		String passwordStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "password");
		if (passwordStr == null)
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD);
		System.out.println(passwordStr  + " COMP "+ password);
		if (!passwordStr.equals(password)) 
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD);
		String typeStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "type");
		System.out.println("TYPE "+ typeStr);
		return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_OK, new UserId(userName, token, stringToUserType(typeStr)));
	}

	public Vector<UserId> listUser() throws CassandraException
	{	
		Vector<UserId> result = new Vector<UserId>();	
		Set<String> resultRows = new HashSet<String>();
		String last = "";
		do
		{
			resultRows = new HashSet<String>();
			Vector<String> lastRow = new Vector<String>();
			CassandraAccessor.getKeyRange(COLUMN_FAMILY_NAME_ACCOUNT, last, 100, resultRows, lastRow);
			if (lastRow.size() != 0)
				last = lastRow.get(0);
			for (String userName: resultRows)
			{
				System.out.println("FOUND USER NAME: " + userName);
				Map<String, String> accountTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, userName);			
				if (accountTable != null && accountTable.containsKey("token"))
				{
					System.out.println("USER: " + userName + "\nACCOUNT_TABLE: " + accountTable);
					UserId newUserId = new UserId(userName, accountTable.get("token"), stringToUserType(accountTable.get("type")));
					result.add(newUserId);
					System.out.println("USER: " + newUserId.userName + " TOKEN: " + newUserId.token + " TYPE:" + userTypeToString(newUserId.type));				
				}
			}
		}
		while (resultRows.size() > 1);
		return result;
	}

	public UserReport getUserReport(UserId id) throws CassandraException
	{		
		Map<String, String> result = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, id.userName);
		if (result == null) return null;
		return new UserReport(				
				id.userName, 
				result.get("password"),
				result.get("email"),
				id.token, 
				stringToUserType(result.get("type")),
				Convertor.toDate(result.get("date")),
				Convertor.toDate(result.get("date")), 
				ShortMap.getBoolean(result.get("validated"), true)
				);
	}

	public List<UserReport> getUserReportList() throws CassandraException
	{
		List<UserReport> report = new ArrayList<UserReport>();
		Vector<UserId> userList = listUser();
		for (UserId uId: userList)
		{
			report.add(getUserReport(uId));
		}
		return report;
	}

	public void deleteAccount(String userName) throws CassandraException
	{
		UserId id = new UserId(userName, "", UserType.USER_TYPE_SEEKER);
		UserReport user = getUserReport(id);
		if (user != null)
		{
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_NOT_VALIDATED, id.userName);
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_EMAIL, user.mail);
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_ACCOUNT, id.userName);
			System.out.println("DELETED ACCOUNT: " + id.userName);
		}
		else
		{
			System.out.println("COULD NOT DELETE ACCOUNT: " + id.userName + " NO SUCH ACCOUNT FOUND");
		}
	}

	public void purgeAccount() throws CassandraException
	{
		Set<String> resultRows = new HashSet<String>();
		String last = "";
		do
		{
			resultRows = new HashSet<String>();
			Vector<String> lastRow = new Vector<String>();
			CassandraAccessor.getKeyRange(COLUMN_FAMILY_NAME_ACCOUNT, last, 3, resultRows, lastRow);
			if (lastRow.size() != 0)
				last = lastRow.get(0);
			for (String k: resultRows)
			{
				System.out.println("CHECK USER NAME: " + k);
				Map<String, String> userNameDB = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, k);
				if (userNameDB.containsKey("validated") || userNameDB.containsKey("username") || userNameDB.containsKey("date"))
				{
					if (!userNameDB.containsKey("validated") || !userNameDB.containsKey("username") || !userNameDB.containsKey("date"))
					{
						// Delete this account.
						deleteAccount(k);
						CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_ACCOUNT, k);

						System.out.println("DELETED TOKEN: " + k);
					}
				}
			}
		} while (resultRows.size() > 1);
	}

	@Override
	public void sendComment(UserId id, String comment) throws CassandraException
	{
		UserReport report = getUserReport(id);
		System.out.println("Comment from " + report.mail);
		MailerFactory.getMailer().sendEmail("mathieu.avila@laposte.net", 
				"Comment on TheJobCoach from '" + report.mail + "' user '" + report.userName + "'", 
				report.mail + "\n" + comment, 
				"noreply@www.thejobcoach.fr");
	}

	public Boolean lostCredentials(String email, String lang) throws CassandraException 
	{	
		String userName = getUsernameFromEmail(email);
		if (userName == null) return new Boolean(false);
		UserReport info = getUserReport(new UserId(userName, "", UserType.USER_TYPE_SEEKER));
		if (info == null) return new Boolean(false);
		String body = Lang._TextLostCredentials(info.userName, info.password, lang);
		MailerFactory.getMailer().sendEmail(info.mail, Lang._TextLostCredentialsSubject(lang), body, "noreply@www.thejobcoach.fr");
		return new Boolean(true);
	}

	public void deleteUser(UserId id) throws CassandraException
	{
		oppManager.deleteUser(id);
		valuesManager.deleteUser(id);
		siteManager.deleteUser(id);
		deleteAccount(id.userName);
	}
	
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
			System.out.println("Compare test account: " + currentTime + " with: " + key + " " + currentTime.compareTo(key));
			if (currentTime.compareTo(key) > 0) // purge
			{
				String userName = testAccountList.get(key);				
				System.out.println("Too old, delete: " + userName);
				deleteUser(new UserId(userName, "", UserId.UserType.USER_TYPE_SEEKER));
				CassandraAccessor.deleteColumn(COLUMN_FAMILY_TEST_LIST, CONSTANT_TEST_LIST_ROW, key);
			}
		}
	}
	
	public UserId createTestAccount(String langStr, UserId.UserType userType) throws CassandraException, SystemException
	{
		if (userType == UserType.USER_TYPE_ADMIN) throw new SystemException();
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
				if (!existsAccount(userName))
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
		createAccountWithTokenNoMail(result, info, langStr);
		validateAccount(userName, userName);
		return result;
	}


}
