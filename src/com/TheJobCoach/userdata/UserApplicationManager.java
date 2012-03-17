package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserApplication;

public class UserApplicationManager {

	final static String COLUMN_FAMILY_NAME_LIST = "opportunitieslist";
	final static String COLUMN_FAMILY_NAME_DATA = "opportunitiesdata";
	
	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;
	
	public UserApplicationManager()
	{
		if (cfDefList == null)
		{
			cfDefList = HFactory.createColumnFamilyDefinition(
					CassandraAccessor.KEYSPACENAME,                              
					COLUMN_FAMILY_NAME_LIST, 
					ComparatorType.ASCIITYPE);
			try{
				CassandraAccessor.getCluster().addColumnFamily(cfDefList);
			}
			catch(Exception e) {} // Assume it already exists.
		}
		if (cfDefData == null)
		{
			cfDefData = HFactory.createColumnFamilyDefinition(
					CassandraAccessor.KEYSPACENAME,                              
					COLUMN_FAMILY_NAME_DATA, 
					ComparatorType.ASCIITYPE);
			try{
				CassandraAccessor.getCluster().addColumnFamily(cfDefData);
			}
			catch(Exception e) {} // Assume it already exists.
		}
	}

	public List<String> getOpportunitiesList(UserId id) throws CassandraException 
	{	
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_LIST, id.userName);
		if (resultReq == null)
		{
			throw new CassandraException();
		}		
		return new ArrayList<String>(resultReq.keySet());
	}

	public UserApplication getApplication(UserId id, String ID) throws CassandraException 
	{
		String reqId = ID;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (resultReq == null)
		{
			throw new CassandraException(); 
		}		
		Vector<String> list = new Vector<String>();
		for (String k:resultReq.keySet())
		{
			if (k.contains("l-"))
			{
				list.add(resultReq.get(k));
			}
		}
		return new UserApplication();
		/*
				ID,
				Convertor.toDate(resultReq.get("lastupdate")),
				list,
				statusFromString(resultReq.get("status")));*/
	}

	public void setUserApplication(UserId id, UserApplication result) throws CassandraException 
	{
		ShortMap keyValues = (new ShortMap())
		.add("lastupdate", result.lastUpdate
		//.add("status", statusToString(result.status)
				);
		boolean resultReq = CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				result.ID,				
				keyValues.get());	
		if (resultReq == false)
		{
			throw new CassandraException(); 
		}
		resultReq = CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_LIST, 
				id.userName,
				(new ShortMap()).add(result.ID, result.ID).get());
		if (resultReq == false)
		{
			throw new CassandraException(); 
		}
	}
	
	public void deleteUserApplication(UserId id, String ID) throws CassandraException 
	{
		boolean resultReq = CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, ID);	
		if (resultReq == false)
		{
			throw new CassandraException();
		}
		resultReq = CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST, id.userName, ID);	
		if (resultReq == false)
		{
			throw new CassandraException();
		}
	}
}
