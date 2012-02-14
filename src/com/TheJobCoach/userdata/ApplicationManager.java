package com.TheJobCoach.userdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.EasyComposite;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.util.CassandraAccessor.CompositeColumnEntry;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserApplication;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.userpage.shared.UserApplication.UserApplicationStatus;

public class ApplicationManager {

	final static String COLUMN_FAMILY_NAME_LIST = "applicationlist";
	final static String COLUMN_FAMILY_NAME_DATA = "applicationdata";
	
	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;
	
	public ApplicationManager()
	{
		if (cfDefList == null)
		{
			CassandraAccessor.createCompositeColumnFamily(COLUMN_FAMILY_NAME_LIST, "(UTF8Type)");
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

	public List<String> getApplicationList(UserId id) throws CassandraException 
	{	
		ArrayList<CompositeColumnEntry> resultReq = new ArrayList<CompositeColumnEntry>();
		if (!CassandraAccessor.getCompositeColumnsRange(
				COLUMN_FAMILY_NAME_LIST, 
				id.userName,
				(Composite)(new EasyComposite().easyAdd("0000000000000000000")), 
				(Composite)(new EasyComposite().easyAdd("9999999999999999999")), 
				resultReq))
		{
			throw new CassandraException();
		}
		List<String> idList = new ArrayList<String>();
		for (CompositeColumnEntry col: resultReq)
			{
			idList.add(col.value);
			//System.out.println("Found new ID : " + col.value);
			}
		return idList;
	}

	public UserApplication getApplication(UserId id, String ID) throws CassandraException 
	{
		String reqId = id.userName + "_" + ID;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (resultReq == null)
		{
			throw new CassandraException(); 
		}
		
		//public UserApplication(String _ID, Date _lastUpdate, Vector<String> _logEntryList, UserApplicationStatus _status)
		
		return new UserApplication(
				ID,
				Convertor.toDate(resultReq.get("lastvisit")),
				new Vector<String>(),
				UserApplicationStatus.NEW);
	}
/*
	public void setApplication(UserId id, UserApplication result) throws CassandraException 
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
	*/
	public void deleteApplication(UserId id, String ID) throws CassandraException 
	{
		// TODO: Perform the 2 updates in a single command...
		String reqId = id.userName + "_" + ID;
		boolean resultReq = CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, reqId);	
		if (resultReq == false)
		{
			throw new CassandraException();
		}
		resultReq = CassandraAccessor.deleteCompositeColumn(COLUMN_FAMILY_NAME_LIST, id.userName, (Composite)(new EasyComposite().easyAdd(ID)));	
		if (resultReq == false)
		{
			throw new CassandraException();
		}
	}

	public String addApplication(UserId id, String applicationId) throws CassandraException 
	{
		long d = new Date().getTime();
		String val = String.valueOf(d);
		CassandraAccessor.updateCompositeColumn(COLUMN_FAMILY_NAME_LIST, id.userName, (Composite)(new EasyComposite().easyAdd(val)), val);
		UserJobSite ujs = new UserJobSite();
		ujs.ID = val;
	  //  setUserSite(id, ujs);
		return val;
	}
}
