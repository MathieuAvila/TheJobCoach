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
import com.TheJobCoach.util.CassandraAccessor.CompositeColumnEntry;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;

public class UserOpportunityManager {

	final static String COLUMN_FAMILY_NAME_LIST = "opportunitieslist";
	final static String COLUMN_FAMILY_NAME_DATA = "opportunitiesdata";
	
	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;
	
	public UserOpportunityManager()
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

	public UserOpportunity getOpportunity(UserId id, String ID) throws CassandraException 
	{
		String reqId = ID;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, reqId);
		if (resultReq == null)
		{
			throw new CassandraException(); 
		}
		return new UserOpportunity(
				ID,
				Convertor.toDate(resultReq.get("firstseen")),
				Convertor.toDate(resultReq.get("lastupdate")),
				Convertor.toString(resultReq.get("title")),
				Convertor.toString(resultReq.get("description")),
				Convertor.toString(resultReq.get("companyid")),
				Convertor.toString(resultReq.get("contracttype")),
				Convertor.toInt(resultReq.get("salary")),
				Convertor.toDate(resultReq.get("startdate")),
				Convertor.toDate(resultReq.get("enddate")),
				Convertor.toBoolean(resultReq.get("systemsource")),
				Convertor.toString(resultReq.get("source")),
				Convertor.toString(resultReq.get("url")),
				Convertor.toString(resultReq.get("location"))
				);
	}

	public void setUserOpportunity(UserId id, UserOpportunity result) throws CassandraException 
	{
		boolean resultReq = CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				result.ID, 
				(new ShortMap())
				.add("firstseen", result.firstSeen)
				.add("lastupdate", result.lastUpdate)
				.add("title", result.title)
				.add("description", result.description)
				.add("companyid", result.companyId)
				.add("contracttype", result.contractType)
				.add("salary", result.salary)
				.add("startdate", result.startDate)
				.add("enddate", result.endDate)
				.add("systemsource", result.systemSource)
				.add("source", result.source)
				.add("url", result.url)
				.add("location", result.location)
				.get());	
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
	
	public void deleteUserOpportunity(UserId id, String ID) throws CassandraException 
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
