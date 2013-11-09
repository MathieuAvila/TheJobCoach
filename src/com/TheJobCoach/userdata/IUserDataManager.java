package com.TheJobCoach.userdata;

import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;

public interface IUserDataManager
{
	void createUser(UserId user);
	void deleteUser(UserId user) throws CassandraException;
	void createTestUser(UserId user, String lang);
}
