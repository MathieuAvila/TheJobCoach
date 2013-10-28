package com.TheJobCoach.userdata;

import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public interface IUserDataManager
{
	void createUser(UserId user);
	void deleteUser(UserId user) throws CassandraException;
	void createTestUser(UserId user, String lang);
}
