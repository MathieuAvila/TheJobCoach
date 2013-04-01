package com.TheJobCoach.util;


import static me.prettyprint.hector.api.factory.HFactory.createMultigetSliceQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.util.shared.CassandraException;

public class CassandraAccessor {

	static CassandraAccessor current = new CassandraAccessor();

	static final public String CLUSTERNAME = "TheJobCoach";
	static final public String KEYSPACENAME = "keyspacejobcoach";

	static Cluster myCluster = null;
	static KeyspaceDefinition myKeyspaceDefinition = null;
	static Keyspace myKeyspace = null;
	private static final StringSerializer se = new StringSerializer();
	private static final BytesArraySerializer bse = new BytesArraySerializer();

	private static String location = "localhost:9160";	
	
	private static Logger logger = LoggerFactory.getLogger(CassandraAccessor.class);
	
	static public void setLocation(String _location)
	{
		location = _location;
		myKeyspaceDefinition = null;
		myKeyspace = null;
		myCluster = null;
	}

	static public void setCluster(String clusterName)
	{		
		myCluster = HFactory.getOrCreateCluster(clusterName, new CassandraHostConfigurator(location));
	}

	static public Cluster getCluster()
	{
		if (myCluster == null)
		{
			myCluster = HFactory.getOrCreateCluster(CLUSTERNAME, location);
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

	static public boolean updateColumn(ColumnFamilyTemplate<String, String> cfTemplate, String key, Map<String, String> mapUpdate) throws CassandraException
	{	
		ColumnFamilyUpdater<String, String> updater = cfTemplate.createUpdater(key);
		for(Map.Entry<String, String> e : mapUpdate.entrySet())
		{
			updater.setString(e.getKey(), e.getValue());
		}
		//updater.setLong("time", System.currentTimeMillis());		

		try 
		{
			cfTemplate.update(updater);
		} 
		catch (HectorException e) 
		{
			// do something ...
			logger.error("Updater error: " + e.getMessage());
			throw new CassandraException();
		}
		return true;
	}

	static public boolean updateColumn(String CF, String key, Map<String, String> mapUpdate) throws CassandraException
	{
		ColumnFamilyTemplate<String, String> cfTemplate = new ThriftColumnFamilyTemplate<String, String>(CassandraAccessor.getKeyspace(),
				CF, 
				se,        
				se);
		return updateColumn(cfTemplate, key, mapUpdate);
	}

	static public String getColumn(String CF, String key, String columnName)
	{
		ColumnQuery<String, String, String> cQ = HFactory.createColumnQuery(getKeyspace(), se, se, se);
		QueryResult<HColumn<String, String>> r;
		try {
			r = cQ.setKey(key).setName(columnName).setColumnFamily(CF).execute();
		}
		catch (Exception e){
			logger.error("getColumn error: " + e.getMessage());
			return null;
		}
		if (r == null) return null;
		HColumn<String, String> c = r.get();
		if (c == null) return null;
		return  c.getValue();
	}
	
	static public Map<String, String> getRow(String CF, String key) throws CassandraException
	{
		SliceQuery<String, String, String> cQ = HFactory.createSliceQuery(getKeyspace(), se, se, se);
		QueryResult<ColumnSlice<String, String>> r;
		try {
			r = cQ.setKey(key).setColumnFamily(CF).setRange("", "", false, 100).execute();
		}
		catch (Exception e){
			logger.error("getRow error: " + e.getMessage() + "getRow CF:" + CF + " key: " + key);
			throw new CassandraException();
		}
		if (r == null) throw new CassandraException();
		ColumnSlice<String, String> c = r.get();
		if (c == null) throw new CassandraException();
		List<HColumn<String, String>> l = c.getColumns();
		if (l.size() == 0) return null;
		TreeMap<String, String> map = new TreeMap<String, String>();
		for (HColumn<String, String> i: l)
		{
			map.put(i.getName(), i.getValue());
		}
		return  map;
	}



	static public void deleteKey(String CF, String key)
	{
		Mutator<String> mutator = HFactory.createMutator(getKeyspace(), se);
		mutator.delete(key, CF, null, se);
		mutator.execute();
		mutator.discardPendingMutations();		
	}

	static public Map<String, String> getColumnRange(String CF, String row, String keyFirst, String keyLast, int number) throws CassandraException
	{
		MultigetSliceQuery<String, String, String> q = createMultigetSliceQuery(getKeyspace(), se, se, se);
		q.setColumnFamily(CF);
		q.setKeys(row);
		q.setRange(keyFirst, keyLast, false, number);
		QueryResult<Rows<String, String, String>> r = q.execute();
		if (r == null) throw new CassandraException();
		Rows<String, String, String> rows = r.get();
		if (rows == null) return null;
		Row<String, String, String> rowK = rows.getByKey(row);
		if (rowK == null) return null;
		ColumnSlice<String, String> slice = rowK.getColumnSlice();
		Map<String, String> result = new TreeMap<String, String>();
		for (HColumn<String, String> column : slice.getColumns())
		{	
			result.put(column.getName(), column.getValue());	        
		}
		return result;
	}

	public static boolean deleteColumnFamily(String columnFamily)
	{
		try
		{
			getCluster().dropColumnFamily(getKeyspace().getKeyspaceName(), columnFamily, true);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

	static public void deleteColumn(String CF, String key, String col) throws CassandraException
	{
		Mutator<String> mutator = HFactory.createMutator(CassandraAccessor.getKeyspace(), se);		
		mutator.delete(key, CF, col, se);
		try 
		{
			mutator.execute();			
		}
		catch (Exception e)
		{
			throw new CassandraException();
		}		
	}

	static public boolean getKeyRange(String CFName, String start, int count, Set<String> result, Vector<String> last)
	{
		RangeSlicesQuery<String, String, String> rangeSlicesQuery =
				HFactory.createRangeSlicesQuery(CassandraAccessor.getKeyspace(), se, se, se);
		rangeSlicesQuery.setColumnFamily(CFName);
		rangeSlicesQuery.setKeys(start, "");
		rangeSlicesQuery.setRange(start, "", false, count);        
		rangeSlicesQuery.setRowCount(count);

		QueryResult<OrderedRows<String, String, String>> resultQuery = rangeSlicesQuery.execute();
		OrderedRows<String, String, String> orderedRows = resultQuery.get();

		Row<String,String,String> lastRow = orderedRows.peekLast();

		for (Row<String, String, String> r : orderedRows)
		{
			result.add(r.getKey());
		}
		if (lastRow != null) last.add(lastRow.getKey());

		return true;
	}

	public static ColumnFamilyDefinition checkColumnFamilyAscii(String CFName, ColumnFamilyDefinition cfDefList)
	{		
		if (cfDefList == null)
		{
			cfDefList = HFactory.createColumnFamilyDefinition(
					CassandraAccessor.KEYSPACENAME,                              
					CFName, 
					ComparatorType.ASCIITYPE);
			try{
				CassandraAccessor.getCluster().addColumnFamily(cfDefList);
			}
			catch(Exception e) {} // Assume it already exists.
		}
		return cfDefList;
	}

	static public void updateColumnByte(String CF, String key, String column, byte[] data) throws CassandraException
	{
		ColumnFamilyTemplate<String, String> cfTemplate = new ThriftColumnFamilyTemplate<String, String>(CassandraAccessor.getKeyspace(),
				CF, 
				se,
				se        
				);
		ColumnFamilyUpdater<String, String> updater = cfTemplate.createUpdater(key);		
		updater.setByteArray(column, data);
		try 
		{
			cfTemplate.update(updater);
		} 
		catch (HectorException e) 
		{
			logger.error("Updater error" + e.getMessage());
			throw new CassandraException();
		}
	}

	static public byte[] getColumnByte(String CF, String key, String columnName)
	{
		ColumnQuery<String, String, byte[]> cQ = HFactory.createColumnQuery(getKeyspace(), se, se, bse);
		QueryResult<HColumn<String, byte[]>> r;
		try {
			r = cQ.setKey(key).setName(columnName).setColumnFamily(CF).execute();
		}
		catch (Exception e){
			logger.error("getColumnByte error" + e.getMessage());
			return null;
		}
		if (r == null) return null;
		HColumn<String, byte[]> c = r.get();
		if (c == null) return null;
		return c.getValue();
	}
}
