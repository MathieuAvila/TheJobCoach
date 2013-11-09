package com.TheJobCoach.userdata;

import java.util.HashSet;
import java.util.Set;

import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;

public class UserDataCentralManager
{
	static Set<IUserDataManager> managerList = new HashSet<IUserDataManager>();
		
	static public void addManager(IUserDataManager manager)
	{
		managerList.add(manager);
	}

	public static void createUser(UserId user)
	{
		for (IUserDataManager manager: managerList) manager.createUser(user);
	}

	public static void deleteUser(UserId user) throws CassandraException
	{
		for (IUserDataManager manager: managerList) manager.deleteUser(user);
	}

	public static void createTestUser(UserId user, String lang)
	{
		for (IUserDataManager manager: managerList) manager.createTestUser(user, lang);
	}
}
