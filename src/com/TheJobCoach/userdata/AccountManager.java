package com.TheJobCoach.userdata;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.userdata.UserValues.ValueCallback;
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
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstants;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;

public class AccountManager implements AccountInterface, ValueCallback {

	static ColumnFamilyDefinition cfDef = null;
	static ColumnFamilyDefinition cfDefEmail = null;
	static ColumnFamilyDefinition cfDefValidation = null;
	static ColumnFamilyDefinition cfDefTestList = null;

	final static String COLUMN_FAMILY_NAME_ACCOUNT = "account";
	final static String COLUMN_FAMILY_NAME_EMAIL = "accountemail";
	final static String COLUMN_FAMILY_NAME_NOT_VALIDATED = "accountvalidation";
	final static String COLUMN_FAMILY_TEST_LIST = "accounttestlist";

	final static int LAST_VERSION = 2;

	Logger logger = LoggerFactory.getLogger(AccountManager.class);

	UserValues userValues = new UserValues();

	static boolean inited = false;
	
	public AccountManager()
	{
		cfDef = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_ACCOUNT, cfDef);
		cfDefEmail = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_EMAIL, cfDefEmail);
		cfDefValidation = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_NOT_VALIDATED, cfDefValidation);
		cfDefTestList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_TEST_LIST, cfDefTestList);
		
		if (!inited)
		{
			inited = true;
			UserValues.registerCallback(UserValuesConstantsAccount.ACCOUNT_FIRSTNAME, this);
			UserValues.registerCallback(UserValuesConstantsAccount.ACCOUNT_LASTNAME, this);
		}
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
		userValues.setValueSafe(id, UserValuesConstantsAccount.ACCOUNT_LANGUAGE, langStr, false);
		return CreateAccountStatus.CREATE_STATUS_OK;
	}

	public CreateAccountStatus createAccountWithTokenNoMail(UserId id, UserInformation info, String langStr) throws CassandraException
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
		if (version >= 2)
		{
			userValues.setValueSafe(id, UserValuesConstantsAccount.ACCOUNT_FIRSTNAME, info.firstName, false);
			userValues.setValueSafe(id, UserValuesConstantsAccount.ACCOUNT_LASTNAME, info.name, false);
			userValues.setValueSafe(id, UserValuesConstantsAccount.ACCOUNT_EMAIL, info.email, false);
		}
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
		{
			logger.error("validate account " + userName + " " + token + " " + tokenDB);
			return ValidateAccountStatus.VALIDATE_STATUS_UNKNOWN;
		}
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
			logger.warn("Login refused: No token for user: " + userName + " . Account deleted ?");
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
			updateUserInformation(id, info, 1);
			accountTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, userName);	
			v = "1";
		}
		if (v.equals("1"))
		{
			// update with name/firstname in uservalues.
			UserInformation info = new UserInformation();
			getUserInformation(id, info);
			userValues.setValueSafe(id, UserValuesConstantsAccount.ACCOUNT_FIRSTNAME, info.firstName, false);
			userValues.setValueSafe(id, UserValuesConstantsAccount.ACCOUNT_LASTNAME, info.name, false);
			userValues.setValueSafe(id, UserValuesConstantsAccount.ACCOUNT_EMAIL, info.email, false);
			CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, 
					(new ShortMap())
					.add("version", "2").get());
			v = "2";
		}
		String h = accountTable.get("hashedpassword");
		String s = accountTable.get("salt");
		if (!UtilSecurity.compareHashedSaltedPassword(password, s, h))
		{
			logger.warn("Login refused: bad password with salt: " + userName);
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD);
		}
		return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_OK, id);
	}

	public Vector<UserId> listUser(boolean onlyLive) throws CassandraException
	{	
		Vector<UserId> result = new Vector<UserId>();	
		Vector<String> resultRows = new Vector<String>();
		String last = "";
		do
		{
			last = CassandraAccessor.getKeyRange(COLUMN_FAMILY_NAME_ACCOUNT, last, 100, resultRows, onlyLive ? "token":"email");
			for (String userName: resultRows)
			{
				Map<String, String> accountTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, userName);	
				boolean hasToken = accountTable.containsKey("token");
				boolean valid = (!onlyLive) || (hasToken) ;
				if ((accountTable != null) && valid)
				{
					UserId newUserId = new UserId(userName, hasToken ? accountTable.get("token"):"", UserId.stringToUserType(accountTable.get("type")));
					result.add(newUserId);
				}
			}
		}
		while (resultRows.size() != 0);
		return result;
	}

	public UserReport getUserReport(UserId id) throws CassandraException
	{		
		Map<String, String> result = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, id.userName);
		if (result == null) return null;
		boolean deletion = false;
		Date deleteDate = new Date();
		try {
			deletion = UserValuesConstants.YES.equals(userValues.getValue(id, UserValuesConstantsAccount.ACCOUNT_DELETION)); 
			deleteDate = FormatUtil.getStringDate(userValues.getValue(id, UserValuesConstantsAccount.ACCOUNT_DELETION_DATE));
		} catch (SystemException e) {}
		return new UserReport(				
				id.userName, 
				result.get("password"),
				result.get("email"),
				id.token, 
				UserId.stringToUserType(result.get("type")),
				Convertor.toDate(result.get("date")),
				Convertor.toDate(result.get("date")), 
				Convertor.toBoolean(result.get("validated"), true),
				Convertor.toBoolean(result.get("dead"), false),
				deletion,
				deleteDate
				);
	}

	public List<UserReport> getUserReportList() throws CassandraException
	{
		List<UserReport> report = new ArrayList<UserReport>();
		Vector<UserId> userList = listUser(false);
		for (UserId uId: userList)
		{
			report.add(getUserReport(uId));
		}
		return report;
	}

	public void deleteAccount(String userName) throws CassandraException
	{
		logger.info("Delete account " + userName);
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
		Vector<String> resultRows = new Vector<String>();
		String last = "";
		do
		{
			last = CassandraAccessor.getKeyRange(COLUMN_FAMILY_NAME_ACCOUNT, last, 3, resultRows, "token");
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
		} while (resultRows.size() != 0);
	}

	@Override
	public void sendComment(UserId id, String comment) throws CassandraException
	{
		if (id.testAccount)
		{
			logger.error("SECURITY: trying to send comment in test account");
			return;
		}
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

	// This one is really used when user requests deletion.
	// All data is deleted, but account is marked blocked, and email is not usable anymore.
	public void markUserAccountDeleted(UserId id) throws CassandraException
	{
		UserReport user = getUserReport(id);
		if (user != null)
		{
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_NOT_VALIDATED, id.userName);
			CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_EMAIL, user.mail);
			CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, "token");
			CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName,
					(new ShortMap()).add("dead", true).get());
			UserDataCentralManager.deleteUser(id);
		}
		else
		{
			logger.info("COULD NOT DELETE ACCOUNT: " + id.userName + " NO SUCH ACCOUNT FOUND");
		}
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

		Vector<String> resultRows = new Vector<String>();
		String last = "";
		do
		{
			last = getUserRange(last, 100, resultRows);
			for (String userName: resultRows)
			{
				Map<String, String> accountTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, userName);
				if (accountTable != null)
				{
					String nameResult = accountTable.get("name");
					String firstNameResult = accountTable.get("firstname");
					UserId.UserType type = UserId.stringToUserType(accountTable.get("type"));

					if ((accountTable.get("testaccount") == null) 
							&& Convertor.toBoolean(accountTable.get("validated"), false) 
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
		while (resultRows.size() != 0);

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
			lang = userValues.getValue(user, UserValuesConstantsAccount.ACCOUNT_LANGUAGE); 
		}
		catch (SystemException e) {} // this cannot happen
		return lang;
	}

	public void markTestAccount(String userName) throws CassandraException
	{
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, 
				(new ShortMap())
				.add("testaccount", true)
				.get());
	}

	public String getUserRange(String start, int count, Vector<String> rows)
	{
		String last = CassandraAccessor.getKeyRange(COLUMN_FAMILY_NAME_ACCOUNT, start, count, rows, "token");
		return last;
	}

	protected void toggleAccountDeletion(UserId userId, boolean delete, Date deletionDate) throws CassandraException, SystemException
	{
		// toggle state
		userValues.setValue(userId, UserValuesConstantsAccount.ACCOUNT_DELETION, delete ? UserValuesConstants.YES : UserValuesConstants.NO, false); 
		userValues.setValue(userId, UserValuesConstantsAccount.ACCOUNT_DELETION_DATE, FormatUtil.getDateString(deletionDate), false); 

		// Send information mail if deletion is requested.
		if (delete)
		{
			UserInformation fullinfo = new UserInformation();
			getUserInformation(userId, fullinfo);
			String lang = getUserLanguage(userId);
			String body = Lang.accountDeletion(lang, fullinfo.firstName, fullinfo.name, deletionDate);
			Map<String, MailerInterface.Attachment> parts = new HashMap<String, MailerInterface.Attachment>();
			parts.put("thejobcoachlogo", new MailerInterface.Attachment("/com/TheJobCoach/webapp/mainpage/client/thejobcoach-icon.png", "image/png", "img_logo.png"));
			MailerFactory.getMailer().sendEmail(fullinfo.email, Lang.accountDeletionSubject(lang), body, "noreply@www.thejobcoach.fr", parts);
		}
	}

	public void toggleAccountDeletion(UserId userId, boolean delete) throws CassandraException, SystemException
	{
		Date deletionDate = FormatUtil.dateAddDays(new Date(), 15); // 15 days
		toggleAccountDeletion(userId, delete, deletionDate);
	}

	public boolean checkDeletionAccount(UserId userId, Date d) throws CassandraException, SystemException
	{
		Map<String, String> accountTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, userId.userName);
		if (accountTable == null) return false;
		boolean deleted = Convertor.toBoolean(accountTable.get("dead"), false);
		if (deleted) return false;
		boolean deletion = UserValuesConstants.YES.equals(userValues.getValue(userId, UserValuesConstantsAccount.ACCOUNT_DELETION)); 
		if (!deletion) return false;
		Date deleteDate = FormatUtil.getStringDate(userValues.getValue(userId, UserValuesConstantsAccount.ACCOUNT_DELETION_DATE));
		if (deleteDate.after(d)) return false;
		markUserAccountDeleted(userId);
		return true;
	}

	@Override
	public void notify(UserId id, String key, String value)
	{
		if ((key.equals(UserValuesConstantsAccount.ACCOUNT_FIRSTNAME)) ||
				(key.equals(UserValuesConstantsAccount.ACCOUNT_LASTNAME)))
		{
			String k = null;
			if (key.equals(UserValuesConstantsAccount.ACCOUNT_FIRSTNAME))
				k = "firstname";
			else
				k = "name";
			try
			{
				CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, id.userName, 
						new ShortMap().add(k, value).get());
			}
			catch (CassandraException e)
			{
				logger.warn("error while updating " + k + " of user: " + id.userName + " to: " + value);
			}
		}
	}
}
