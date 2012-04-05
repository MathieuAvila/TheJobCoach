package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.EasyComposite;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;

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
		String reqId = id.userName + "_" + result.ID;
		boolean resultReq = CassandraAccessor.updateColumn(
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
		if (resultReq == false)
		{
			throw new CassandraException(); 
		}
	}
	
	public void deleteUserSite(UserId id, String ID) throws CassandraException 
	{
		// TODO: Perform the 2 updates in a single command...
		String reqId = id.userName + "_" + ID;
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, reqId);	
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST, id.userName, ID);		
	}

	public String addUserSite(UserId id) throws CassandraException 
	{
		long d = new Date().getTime();
		String val = String.valueOf(d);
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_LIST, id.userName, (new ShortMap()).add(val, val).get());
		UserJobSite ujs = new UserJobSite();
		ujs.ID = val;
	    setUserSite(id, ujs);
		return val;
	}
	
}
