package com.TheJobCoach.util;


import java.util.ArrayList;
import java.util.Map;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.*;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;


public class CassandraAccessor {

	static final public String CLUSTERNAME = "TheJobCoach";
	static final public String KEYSPACENAME = "keyspacejobcoach";

	static Cluster myCluster = null;
	static KeyspaceDefinition myKeyspaceDefinition = null;
	static Keyspace myKeyspace = null;
	
	static public void setCluster(String clusterName)
	{		
		myCluster = HFactory.getOrCreateCluster(clusterName, "localhost:9160");
	}
	
	static public Cluster getCluster()
	{
		if (myCluster == null)
		{
			myCluster = HFactory.getOrCreateCluster(CLUSTERNAME, "localhost:9160");
		}
		return myCluster;
	}
	
	
	static public KeyspaceDefinition getKeyspaceDefinition()
	{
		if (myKeyspaceDefinition == null)
		{
			myKeyspaceDefinition = getCluster().describeKeyspace(KEYSPACENAME);
			if (myKeyspaceDefinition == null)
			{
				myKeyspaceDefinition = HFactory.createKeyspaceDefinition(KEYSPACENAME,ThriftKsDef.DEF_STRATEGY_CLASS, 1, new ArrayList<ColumnFamilyDefinition>());
			}
		}
	    return myKeyspaceDefinition;
	}
	
	static public Keyspace getKeyspace()
	{
		if (myKeyspace == null)
		{
			myKeyspace = HFactory.createKeyspace(KEYSPACENAME, getCluster());
			KeyspaceDefinition localKeyspaceDefinition = getCluster().describeKeyspace(KEYSPACENAME);
			if (localKeyspaceDefinition == null)
			{
				getCluster().addKeyspace(getKeyspaceDefinition(), true);
			}
		}
	    return myKeyspace;
	}
	
	static public boolean updateColumn(ColumnFamilyTemplate<String, String> cfTemplate, String key, Map<String, String> mapUpdate)
	{	
		ColumnFamilyUpdater<String, String> updater = cfTemplate.createUpdater(key);
		for(Map.Entry<String, String> e : mapUpdate.entrySet())
		{
			updater.setString(e.getKey(), e.getValue());
			System.out.println("Update from key:" + key  + " column: " + e.getKey() + " value " + e.getValue());
		}
		//updater.setLong("time", System.currentTimeMillis());		

		try 
		{
			cfTemplate.update(updater);
		} 
		catch (HectorException e) 
		{
			// do something ...
			System.out.println("Updater error");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	static public boolean updateColumn(String CF, String key, Map<String, String> mapUpdate)
	{
		ColumnFamilyTemplate<String, String> cfTemplate = new ThriftColumnFamilyTemplate<String, String>(CassandraAccessor.getKeyspace(),
				CF, 
				StringSerializer.get(),        
				StringSerializer.get());
		return updateColumn(cfTemplate, key, mapUpdate);
	}
	
	static public String getColumn(String CF, String key, String columnName)
	{
		ColumnQuery<String, String, String> cQ = HFactory.createColumnQuery(getKeyspace(), StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
		QueryResult<HColumn<String, String>> r;
		try {
			r = cQ.setKey(key).setName(columnName).setColumnFamily(CF).execute();
		}
		catch (Exception e){
			return null;
		}
		if (r == null) return null;
		HColumn<String, String> c = r.get();
		if (c == null) return null;
		return  c.getValue();
	}
	
	static public boolean deleteKey(String CF, String key)
	{
		Mutator<String> mutator = HFactory.createMutator(getKeyspace(), StringSerializer.get());
		mutator.delete(key, CF, null, StringSerializer.get());
		mutator.execute();
		mutator.discardPendingMutations();
		return true;
	}
}
