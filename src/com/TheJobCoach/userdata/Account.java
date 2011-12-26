package com.TheJobCoach.userdata;


import java.util.Date;
import java.util.UUID;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin.LoginStatus;

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

	public CreateAccountStatus createAccountWithToken(String token, String userName, String name, String firstName, String email, String password, String langStr)
	{
		boolean result = CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, 
				(new ShortMap())
				.add("email", email)
				.add("name", name)
				.add("password", password)
				.add("firstname", firstName)
				.add("date", new Date())
				.add("token", token)
				.get());
		if (!result) return CreateAccountStatus.CREATE_STATUS_ERROR;
		result = CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_TOKEN, token, 
				(new ShortMap())
				.add("username", userName)
				.add("validated", false)
				.get());
		if (!result) return CreateAccountStatus.CREATE_STATUS_ERROR;
		String body = Lang._TextBody(firstName, com.TheJobCoach.util.SiteDef.getAddress(), userName, token, langStr);
		MailerFactory.getMailer().sendEmail(email, Lang._TextSubject(langStr), body, "noreply@thejobcoach.fr");
		return CreateAccountStatus.CREATE_STATUS_OK;
	}
	
	public CreateAccountStatus createAccount(String userName, String name, String firstName, String email, String password, String langStr)
	{
		UUID uuid = UUID.randomUUID();
		String token = userName + "_" + uuid.toString();
		return createAccountWithToken(token, userName, name, firstName, email, password, langStr);
	}
	public ValidateAccountStatus validateAccount(String userName, String token)
	{
		String userNameDB = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_TOKEN, token, "username");
		if ((userName == null) || (!userNameDB.equals(userName)))
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
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_UNKNOWN_USER, "");
		System.out.println("Token is: " + token);
		String validatedStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_TOKEN, token, "validated");
		if (validatedStr == null)
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_NOT_VALIDATED, "");
		boolean validated = ShortMap.getBoolean(validatedStr);
		if (!validated) return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_NOT_VALIDATED, "");
		String passwordStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "password");
		if (passwordStr == null)
			return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD, "");
		if (!passwordStr.equals(password)) 
			new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_PASSWORD, "");
		return new MainPageReturnLogin(LoginStatus.CONNECT_STATUS_OK, token);
	}
}
