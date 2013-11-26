package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;

public class UserJobSiteManager {

	final static String COLUMN_FAMILY_NAME_LIST = "userjobsitelist";
	final static String COLUMN_FAMILY_NAME_DATA = "userjobsitedata";
	
	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;
	
	public UserJobSiteManager()
	{
		cfDefList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST, cfDefList);
		cfDefData = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_DATA, cfDefData);
	}

	public List<String> getUserSiteList(UserId id) throws CassandraException 
	{	
		Map<String,String> resultReq = null;
		resultReq = CassandraAccessor.getRow(
				COLUMN_FAMILY_NAME_LIST, 
				id.userName);
		if (resultReq == null) return new ArrayList<String>();
		return new ArrayList<String>(resultReq.keySet());
	}

	public UserJobSite getUserSite(UserId id, String ID) throws CassandraException 
	{
		String reqId = id.userName + "_" + ID;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (resultReq == null)
		{
			throw new CassandraException(); 
		}
		return new UserJobSite(
				ID, 
				Convertor.toString(resultReq.get("name")),
				Convertor.toString(resultReq.get("url")),
				Convertor.toString(resultReq.get("description")),
				Convertor.toString(resultReq.get("login")),
				Convertor.toString(resultReq.get("password")),
				Convertor.toDate(resultReq.get("lastvisit"))
				);
	}

	public void setUserSite(UserId id, UserJobSite result) throws CassandraException 
	{
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_LIST, id.userName, (new ShortMap()).add(result.ID, result.ID).get());
		String reqId = id.userName + "_" + result.ID;
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				reqId, 
				(new ShortMap())
				.add("name", result.name)
				.add("url", result.URL)
				.add("description", result.description)
				.add("login", result.login)
				.add("password", result.password)
				.add("lastvisit", result.lastVisit)
				.get());
	}
	
	public void deleteUserSite(UserId id, String ID) throws CassandraException 
	{
		String reqId = id.userName + "_" + ID;
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, reqId);	
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST, id.userName, ID);		
	}

	public void deleteUser(UserId id) throws CassandraException 
	{
		List<String> siteList = getUserSiteList(id);
		for (String siteId: siteList)
		{
			deleteUserSite(id, siteId);
		}
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST, id.userName);	
	}

}
