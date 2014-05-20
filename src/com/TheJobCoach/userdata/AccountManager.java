package com.TheJobCoach.userdata;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.TheJobCoach.webapp.adminpage.shared.UserSearchEntry;
import com.TheJobCoach.webapp.adminpage.shared.UserSearchResult;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin.LoginStatus;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;

public class AccountManager implements AccountInterface {

	static ColumnFamilyDefinition cfDef = null;
	static ColumnFamilyDefinition cfDefEmail = null;
	static ColumnFamilyDefinition cfDefValidation = null;
	static ColumnFamilyDefinition cfDefTestList = null;

	final static String COLUMN_FAMILY_NAME_ACCOUNT = "account";
	final static String COLUMN_FAMILY_NAME_EMAIL = "accountemail";
	final static String COLUMN_FAMILY_NAME_NOT_VALIDATED = "accountvalidation";
	final static String COLUMN_FAMILY_TEST_LIST = "accounttestlist";

	final static int LAST_VERSION = 1;

	Logger logger = LoggerFactory.getLogger(AccountManager.class);

	UserValues userValues = new UserValues();

	public AccountManager()
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

	public void updatePassword(UserId id, String password, int version) throws CassandraException
	{
		ShortMap sm = new ShortMap();
		if (version == 0) // original one
		{
			sm = sm.add("password", password);
		}
		if (version >= 1)
		{
			String s = UtilSecurity.getSalt();
			String h = UtilSecurity.getHash(s + password);
			sm = sm
					.add("version", 1)
					.add("hashedpassword", h)
					.add("salt", s);
			// upgrade to secured version
			CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, "password");
		}
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, sm.get());
	}

	public boolean updateUserInformation(UserId id, UserInformation info, int version) throws CassandraException
	{
		ShortMap sm = new ShortMap()
		.add("email", info.email)
		.add("name", info.name)
		.add("firstname", info.firstName)
		.add("date", new Date())
		.add("token", id.token)
		.add("type", UserId.userTypeToString(id.type));

		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, sm.get());
		updatePassword(id, info.password, version);
		return true;
	}

	public boolean getUserInformation(UserId id, UserInformation info) throws CassandraException
	{
		Map<String, String> accountTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, id.userName);
		if (accountTable == null) return false;
		info.name = accountTable.get("name");
		info.email = accountTable.get("email");
		info.password = accountTable.get("password"); // may be null
		info.firstName = accountTable.get("firstname");
		return (info.name) != null;
	}

	public String getUsernameFromEmail(String mail)
	{
		String result = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_EMAIL, mail, "username");	
		return result;
	}

	CreateAccountStatus createAccountWithTokenNoMail(UserId id, UserInformation info, String langStr, int version) throws CassandraException
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
		result = updateUserInformation(id, info, version);
		if (!result) return CreateAccountStatus.CREATE_STATUS_ERROR;
		UserDataCentralManager.createUserDefaults(id, langStr);
		UserValues values = new UserValues();
		try	{
			values.setValue(id, UserValuesConstantsAccount.ACCOUNT_LANGUAGE, langStr, false);
		} catch (SystemException e) {} // Ignore this, system cannot be faulty.
		return CreateAccountStatus.CREATE_STATUS_OK;
	}

	CreateAccountStatus createAccountWithTokenNoMail(UserId id, UserInformation info, String langStr) throws CassandraException
	{
		return createAccountWithTokenNoMail(id, info, langStr, LAST_VERSION);
	}

	public CreateAccountStatus createAccountWithToken(UserId id, UserInformation info, String langStr, int version) throws CassandraException
	{
		CreateAccountStatus result = createAccountWithTokenNoMail(id, info, langStr, version);
		if (result != CreateAccountStatus.CREATE_STATUS_OK) return result;
		String body = Lang._TextActivateAccountBody(info.firstName, info.name, info.email, com.TheJobCoach.util.SiteDef.getAddress(), id.userName, id.token, langStr);
		Map<String, MailerInterface.Attachment> parts = new HashMap<String, MailerInterface.Attachment>();
		parts.put("thejobcoachlogo", new MailerInterface.Attachment("/com/TheJobCoach/webapp/mainpage/client/thejobcoach-icon.png", "image/png", "img_logo.png"));
		MailerFactory.getMailer().sendEmail(info.email, Lang._TextActivateAccountSubject(langStr), body, "noreply@www.thejobcoach.fr", parts);
		return CreateAccountStatus.CREATE_STATUS_OK;
	}

	public CreateAccountStatus createAccountWithToken(UserId id, UserInformation info, String langStr) throws CassandraException
	{
		return createAccountWithToken(id, info, langStr, LAST_VERSION);
	}

	public CreateAccountStatus createAccount(UserId id, UserInformation info, String langStr, int version) throws CassandraException
	{
		UUID uuid = UUID.randomUUID();
		String token = id.userName + "_" + uuid.toString();
		id.token = token;
		return createAccountWithToken(id, info, langStr, version);
	}

	public CreateAccountStatus createAccount(UserId id, UserInformation info, String langStr) throws CassandraException
	{
		return createAccount( id,  info, langStr, LAST_VERSION);
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

	public MainPageReturnLogin loginAccount(String userName, String password) throws CassandraException
	{
		Map<String, String> accountTable;
		try {
			accountTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, userName);	
		} 
		catch (Exception e)
		{
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_UNKNOWN_USER);
		}
		if (accountTable == null)
		{
			logger.warn("Login refused: Unknown user: " + userName);
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_UNKNOWN_USER);
		}
		String token = accountTable.get("token");
		if (token == null)
		{
			logger.warn("Login refused: No token for user: " + userName);
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_UNKNOWN_USER);
		}
		String validatedStr = accountTable.get("validated");
		if (validatedStr == null)
		{
			logger.warn("Login refused: user is not validated (no validatation string): " + userName);
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_NOT_VALIDATED);
		}
		boolean validated = Convertor.toBoolean(validatedStr);
		if (!validated) 
		{
			logger.warn("Login refused: user is not validated: " + userName);
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_NOT_VALIDATED);
		}

		String typeStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "type");
		UserId id = new UserId(userName, token, UserId.stringToUserType(typeStr));

		// Get version, and adapt according to value
		String v = accountTable.get("version");
		if (v == null)
		{		
			String passwordStr = accountTable.get("password");
			if (passwordStr == null)
			{
				logger.warn("Login refused: no password: " + userName);
				return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD);
			}
			if (!passwordStr.equals(password))
			{
				logger.warn("Login refused: bad password: " + userName);
				return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD);
			}
			// force upgrade to hashed password (at least version 1)
			UserInformation info = new UserInformation();
			getUserInformation(id, info);
			updateUserInformation(id, info, LAST_VERSION);
		}
		else // If there's a version, it MUST be "1"
		{
			String h = accountTable.get("hashedpassword");
			String s = accountTable.get("salt");
			if (!UtilSecurity.compareHashedSaltedPassword(password, s, h))
			{
				logger.warn("Login refused: bad password with salt: " + userName);
				return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD);
			}
		}
		return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_OK, id);
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
		UserDataCentralManager.deleteUser(id);
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
		UserId id = new UserId(info.userName, info.token, info.type);
		getUserInformation(id, fullinfo);

		// generate new password
		fullinfo.password = UtilSecurity.getPassword();
		info.password = fullinfo.password;
		// update to latest version
		updateUserInformation(id, fullinfo, LAST_VERSION);

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

	@Override
	public void setPassword(UserId id, String newPassword) throws CassandraException
	{
		updatePassword(id, newPassword, LAST_VERSION);
	}

	// XXX NOT OPTIMIZED AT ALL AND NOT SCALABLE. WILL NEED A DEEP REFACTOR.
	public UserSearchResult searchUsers(UserId id, String firstName, String lastName, int sizeRange, int startRange) throws CassandraException, SystemException
	{
		// build a map of ordered users with required characteristics
		TreeMap<String, UserSearchEntry> map = new TreeMap<String, UserSearchEntry>();
		
		firstName = Convertor.stringToSearchable(firstName);
		lastName = Convertor.stringToSearchable(lastName);
		
		Set<String> resultRows = new HashSet<String>();
		String last = "";
		do
		{
			resultRows = new HashSet<String>();
			Vector<String> lastRow = new Vector<String>();
			CassandraAccessor.getKeyRange(COLUMN_FAMILY_NAME_ACCOUNT, last, 10, resultRows, lastRow);
			if (lastRow.size() != 0)
				last = lastRow.get(0);
			for (String userName: resultRows)
			{
				Map<String, String> accountTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, userName);
				if (accountTable != null)
				{
					String nameResult = accountTable.get("name");
					String firstNameResult = accountTable.get("firstname");
					UserId.UserType type = UserId.stringToUserType(accountTable.get("type"));

					if (Convertor.toBoolean(accountTable.get("validated"), false) 
							&& nameResult != null 
							&& firstNameResult != null)
					{
						UserId foundId = new UserId(userName, accountTable.get("token"), type);
						if (Convertor.stringToSearchable(nameResult).contains(lastName) && Convertor.stringToSearchable(firstNameResult).contains(firstName))
						{
							boolean allowed = true;
							String rootKey = null;
							switch (id.type)
							{
							case USER_TYPE_ADMIN:
								break;
							case USER_TYPE_COACH:
								rootKey = UserValuesConstantsAccount.ACCOUNT_PUBLISH_COACH;
								break;
							case USER_TYPE_SEEKER:
								rootKey = UserValuesConstantsAccount.ACCOUNT_PUBLISH_SEEKER;
								break;
							default:
								break;
							}
							if (rootKey != null)
							{
								Map<String, String> clearance = userValues.getValues(foundId, rootKey);
								if (clearance.size() != 0 && clearance.get(rootKey) != null && !clearance.get(rootKey).equals("YES"))
									allowed = false;
							}
							if (allowed)
							{
								Map<String, String> jobMap = userValues.getValues(foundId, UserValuesConstantsAccount.ACCOUNT_TITLE);
								String job = jobMap.get(UserValuesConstantsAccount.ACCOUNT_TITLE);
								if (job == null) job = "";
								UserSearchEntry entry = new UserSearchEntry(userName, firstNameResult, nameResult, job, type);
								map.put(userName, entry);
							}
						}
					}
				}
			}
		}
		while (resultRows.size() > 1);

		// extract result list from map
		Vector<UserSearchEntry> result = new Vector<UserSearchEntry>();
		Vector<String> idList = new Vector<String>(map.keySet());
		Collections.sort(idList);
		for (int i=startRange; i != startRange + sizeRange; i++)
		{
			if (i < idList.size()) result.add(map.get(idList.get(i)));
		}
		return new UserSearchResult(result, map.size());
	}
	
	public String getUserLanguage(UserId user) throws CassandraException
	{
		// send mail in correspondants' language
		String lang = null;
		try { 
			Map<String, String> langMap = userValues.getValues(user, UserValuesConstantsAccount.ACCOUNT_LANGUAGE); 
			lang = langMap.get(UserValuesConstantsAccount.ACCOUNT_LANGUAGE);
		}
		catch (SystemException e) {} // this cannot happen
		return lang;
	}
}
