package com.TheJobCoach.userdata;


import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin.LoginStatus;
import com.TheJobCoach.webapp.mainpage.shared.UserId;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;


public class Account implements AccountInterface {

	static ColumnFamilyDefinition cfDef = null;

	static ColumnFamilyDefinition cfDefToken = null;

	final static String COLUMN_FAMILY_NAME_ACCOUNT = "account";
	final static String COLUMN_FAMILY_NAME_TOKEN = "token";

	public Account()
	{
		if (cfDef == null)
		{
			cfDef = HFactory.createColumnFamilyDefinition(
					CassandraAccessor.KEYSPACENAME,                              
					COLUMN_FAMILY_NAME_ACCOUNT, 
					ComparatorType.ASCIITYPE);
			try{
				CassandraAccessor.getCluster().addColumnFamily(cfDef);
			}
			catch(Exception e) {} // Assume it already exists.
		}
		if (cfDefToken == null)
		{
			cfDefToken = HFactory.createColumnFamilyDefinition(
					CassandraAccessor.KEYSPACENAME,                              
					COLUMN_FAMILY_NAME_TOKEN, 
					ComparatorType.ASCIITYPE);
			try{
				CassandraAccessor.getCluster().addColumnFamily(cfDefToken);
			}
			catch(Exception e) {} // Assume it already exists.
		}
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

	public boolean updateUserInformation(UserId id, UserInformation info)
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

	public CreateAccountStatus createAccountWithToken(UserId id, UserInformation info, String langStr)
	{
		if (existsAccount(id.userName))
			return CreateAccountStatus.CREATE_STATUS_ALREADY_EXISTS;
		boolean result = CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_TOKEN, id.token, 
				(new ShortMap())
				.add("username", id.userName)
				.add("validated", false)
				.get());
		if (!result) return CreateAccountStatus.CREATE_STATUS_ERROR;
		result = updateUserInformation(id, info);
		if (!result) return CreateAccountStatus.CREATE_STATUS_ERROR;
		String body = Lang._TextBody(info.firstName, com.TheJobCoach.util.SiteDef.getAddress(), id.userName, id.token, langStr);
		MailerFactory.getMailer().sendEmail(info.email, Lang._TextSubject(langStr), body, "noreply@thejobcoach.fr");
		return CreateAccountStatus.CREATE_STATUS_OK;
	}

	public CreateAccountStatus createAccount(UserId id, UserInformation info, String langStr)
	{
		UUID uuid = UUID.randomUUID();
		String token = id.userName + "_" + uuid.toString();
		id.token = token;
		return createAccountWithToken(id, info, langStr);
	}

	public ValidateAccountStatus validateAccount(String userName, String token)
	{
		String userNameDB = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_TOKEN, token, "username");
		if ((userName == null) || (userNameDB == null) || (!userNameDB.equals(userName)))
			return ValidateAccountStatus.VALIDATE_STATUS_UNKNOWN;
		boolean result = 
				CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_TOKEN, token,
						(new ShortMap())
						.add("validated", true)
						.add("date", new Date())
						.get());
		if (!result) return ValidateAccountStatus.VALIDATE_STATUS_ERROR;
		return ValidateAccountStatus.VALIDATE_STATUS_OK;
	}

	public MainPageReturnLogin loginAccount(String userName, String password)
	{
		System.out.println("Try to login as: " + userName + " with password: " + password);
		String token = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "token");
		if (token == null)
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_UNKNOWN_USER);
		System.out.println("Token is: " + token);
		String validatedStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_TOKEN, token, "validated");
		if (validatedStr == null)
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_NOT_VALIDATED);
		boolean validated = ShortMap.getBoolean(validatedStr);
		if (!validated) return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_NOT_VALIDATED);
		String passwordStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "password");
		if (passwordStr == null)
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD);
		if (!passwordStr.equals(password)) 
			new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD);
		String typeStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "type");
		return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_OK, new UserId(userName, token, stringToUserType(typeStr)));
	}

	public Vector<UserId> listUser()
	{		
		Set<String> resultRows = new HashSet<String>();
		String last = "";
		do
		{
			resultRows = new HashSet<String>();
			Vector<String> lastRow = new Vector<String>();
			CassandraAccessor.getKeyRange(COLUMN_FAMILY_NAME_TOKEN, last, 3, resultRows, lastRow);
			if (lastRow.size() != 0)
				last = lastRow.get(0);
			for (String k: resultRows)
			{
				System.out.println("FOUND USER NAME: " + k);
				Map<String, String> accountTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_TOKEN, k);			
				if (accountTable.containsKey("username"))
				{
					String userName = accountTable.get("username");
					Map<String, String> userNameDB = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_ACCOUNT, userName);			
					System.out.println("USER: " + userNameDB + "\nACCOUNT_TABLE: " + accountTable);					
				}
			}
		}
		while (resultRows.size() > 1);
		return null;
	}

	public void purgeAccount()
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
				Map<String, String> userNameDB = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_TOKEN, k);
				if (userNameDB.containsKey("validated") || userNameDB.containsKey("username") || userNameDB.containsKey("date"))
				{
					if (!userNameDB.containsKey("validated") || !userNameDB.containsKey("username") || !userNameDB.containsKey("date"))
					{
						// Delete this key.
						System.out.println(CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_TOKEN, k, "validated"));
						System.out.println(CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_TOKEN, k, "username"));
						System.out.println(CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_TOKEN, k, "date"));
						System.out.println(CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_TOKEN, k));

						System.out.println(CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_ACCOUNT, k, "token"));
						System.out.println(CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_ACCOUNT, k, "email"));
						System.out.println(CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_ACCOUNT, k, "name"));
						System.out.println(CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_ACCOUNT, k, "password"));
						System.out.println(CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_ACCOUNT, k, "firstname"));
						System.out.println(CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_ACCOUNT, k, "date"));
						System.out.println(CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_ACCOUNT, k, "type"));
						System.out.println(CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_ACCOUNT, k));

						System.out.println("DELETED TOKEN: " + k);
					}
				}
			}
		} while (resultRows.size() > 1);
	}

}
