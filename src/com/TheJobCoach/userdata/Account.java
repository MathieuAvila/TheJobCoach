package com.TheJobCoach.userdata;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MailerInterface;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.adminpage.shared.UserReport;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin.LoginStatus;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;



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
	
	Logger logger = LoggerFactory.getLogger(Account.class);
	
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
				.add("type", UserId.userTypeToString(id.type))
				.get());
	}
	
	public boolean getUserInformation(UserId id, UserInformation info) throws CassandraException
	{
		info.name = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, "name");
		info.email = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, "email");
		info.password = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, "password");
		info.firstName = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, "firstname");
		return true;
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
		String body = Lang._TextActivateAccountBody(info.firstName, info.name, info.email, com.TheJobCoach.util.SiteDef.getAddress(), id.userName, id.token, langStr);
		Map<String, MailerInterface.Attachment> parts = new HashMap<String, MailerInterface.Attachment>();
		parts.put("thejobcoachlogo", new MailerInterface.Attachment("/com/TheJobCoach/webapp/mainpage/client/thejobcoach-icon.png", "image/png", "img_logo.png"));
		MailerFactory.getMailer().sendEmail(info.email, Lang._TextActivateAccountSubject(langStr), body, "noreply@www.thejobcoach.fr", parts);
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
		String token = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "token");
		if (token == null)
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_UNKNOWN_USER);
		String validatedStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "validated");
		if (validatedStr == null)
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_NOT_VALIDATED);
		boolean validated = Convertor.toBoolean(validatedStr);
		if (!validated) return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_NOT_VALIDATED);
		String passwordStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "password");
		if (passwordStr == null)
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD);
		if (!passwordStr.equals(password)) 			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD);
		String typeStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "type");
		return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_OK, new UserId(userName, token, UserId.stringToUserType(typeStr)));
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
				Map<String, String> accountTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, userName);			
				if (accountTable != null && accountTable.containsKey("token"))
				{
					UserId newUserId = new UserId(userName, accountTable.get("token"), UserId.stringToUserType(accountTable.get("type")));
					result.add(newUserId);
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
				UserId.stringToUserType(result.get("type")),
				Convertor.toDate(result.get("date")),
				Convertor.toDate(result.get("date")), 
				Convertor.toBoolean(result.get("validated"), true)
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
		UserId id = new UserId(userName, "", UserId.UserType.USER_TYPE_SEEKER);
		UserReport user = getUserReport(id);
		if (user != null)
		{
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_NOT_VALIDATED, id.userName);
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_EMAIL, user.mail);
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_ACCOUNT, id.userName);			
		}
		else
		{
			logger.info("COULD NOT DELETE ACCOUNT: " + id.userName + " NO SUCH ACCOUNT FOUND");
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
				Map<String, String> userNameDB = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, k);
				if (userNameDB.containsKey("validated") || userNameDB.containsKey("username") || userNameDB.containsKey("date"))
				{
					if (!userNameDB.containsKey("validated") || !userNameDB.containsKey("username") || !userNameDB.containsKey("date"))
					{
						// Delete this account.
						deleteAccount(k);
						CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_ACCOUNT, k);
					}
				}
			}
		} while (resultRows.size() > 1);
	}

	@Override
	public void sendComment(UserId id, String comment) throws CassandraException
	{
		UserReport report = getUserReport(id);
		MailerFactory.getMailer().sendEmail("mathieu.avila@laposte.net", 
				"Comment on TheJobCoach from '" + report.mail + "' user '" + report.userName + "'", 
				report.mail + "\n" + comment, 
				report.mail);
	}

	public Boolean lostCredentials(String email, String lang) throws CassandraException 
	{	
		String userName = getUsernameFromEmail(email);
		if (userName == null) return new Boolean(false);
		UserReport info = getUserReport(new UserId(userName, "", UserId.UserType.USER_TYPE_SEEKER));
		if (info == null) return new Boolean(false);
		UserInformation fullinfo = new UserInformation();
		getUserInformation(new UserId(info.userName, info.token, info.type), fullinfo);
		String body = Lang._TextLostCredentials(fullinfo.firstName, fullinfo.name, info.userName, info.password, lang);
		Map<String, MailerInterface.Attachment> parts = new HashMap<String, MailerInterface.Attachment>();
		parts.put("thejobcoachlogo", new MailerInterface.Attachment("/com/TheJobCoach/webapp/mainpage/client/thejobcoach-icon.png", "image/png", "img_logo.png"));
		
		MailerFactory.getMailer().sendEmail(info.mail, Lang._TextLostCredentialsSubject(lang), body, "noreply@www.thejobcoach.fr", parts);
		return new Boolean(true);
	}

	public void deleteUserAccount(UserId id) throws CassandraException
	{
		deleteAccount(id.userName);
		UserDataCentralManager.deleteUser(id);
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
			if (currentTime.compareTo(key) > 0) // purge
			{
				String userName = testAccountList.get(key);				
				deleteUserAccount(new UserId(userName, "", UserId.UserType.USER_TYPE_SEEKER));
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
		UserDataCentralManager.createTestUser(result, langStr);
		return result;
	}


}
