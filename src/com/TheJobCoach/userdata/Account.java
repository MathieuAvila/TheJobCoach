package com.TheJobCoach.userdata;


import java.util.Date;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.util.SiteDef;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;

import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;


public class Account implements AccountInterface {

	static ColumnFamilyDefinition cfDef = null;
	static ColumnFamilyTemplate<String, String> cfTemplate = null; 
	final static String COLUMN_FAMILY_NAME_ACCOUNT = "account";

	public Account()
	{
		if (cfDef == null)
		{
			ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
					CassandraAccessor.KEYSPACENAME,                              
					COLUMN_FAMILY_NAME_ACCOUNT, 
					ComparatorType.ASCIITYPE);
			try{
				CassandraAccessor.getCluster().addColumnFamily(cfDef);
			}
			catch(Exception e) {} // Assume it already exists.
		}		
	}

	public boolean existsAccount(String userName)
	{
		String email = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, "email");
		return (email == null);
	}

	public CreateAccountStatus createAccount(String userName, String name, String firstName, String email, String lang)
	{
		boolean result = CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, 
				(new ShortMap())
				.add("email", email)
				.add("validated", false)
				.add("name", name)
				.add("firstname", firstName)
				.add("date", new Date())
				.get());
		if (!result) return CreateAccountStatus.CREATE_STATUS_ERROR;
		String body = "Hello ," + firstName + "\n\n";
		body += "You can activate your account by clicking on the folowing link:\n";
		body += "http://" + SiteDef.getAddress() + "/ValidateAccount.html?username=" + userName+ "\n\n";
		body += "We are happy to to see you soon on The Job Coach !\n\n";
		body += "The Job Coach team\n\n";
		MailerFactory.getMailer().sendEmail(email, "Activate Account on www.TheJobCoach.fr", body, "noreply@thejobcoach.fr");
		return CreateAccountStatus.CREATE_STATUS_OK;
	}

	public ValidateAccountStatus validateAccount(String userName)
	{
		boolean result = 
		 CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_ACCOUNT, userName, 
				(new ShortMap())
				.add("validated", true)
				.add("date", new Date())
				.get());
		if (!result) return ValidateAccountStatus.VALIDATE_STATUS_ERROR;
		return ValidateAccountStatus.VALIDATE_STATUS_OK;
	}
}
