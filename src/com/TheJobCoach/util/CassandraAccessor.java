package com.TheJobCoach.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.*;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.RangeSubSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;


public class CassandraAccessor {
	
	static CassandraAccessor current = new CassandraAccessor();
	
	static final public String CLUSTERNAME = "TheJobCoach";
	static final public String KEYSPACENAME = "keyspacejobcoach";

	static Cluster myCluster = null;
	static KeyspaceDefinition myKeyspaceDefinition = null;
	static Keyspace myKeyspace = null;
	private static final StringSerializer se = new StringSerializer();
	private static final CompositeSerializer ce = new CompositeSerializer();
	
	private static String location = "localhost:9160";	
	
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
			System.out.println("Get cluster from: " + location);
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

	static public boolean updateColumn(ColumnFamilyTemplate<String, String> cfTemplate, String key, Map<String, String> mapUpdate)
	{	
		ColumnFamilyUpdater<String, String> updater = cfTemplate.createUpdater(key);
		for(Map.Entry<String, String> e : mapUpdate.entrySet())
		{
			System.out.println("Try update for table: " +  cfTemplate.getColumnFamily() + " from key:" + key  + " column: " + e.getKey() + " value: " + e.getValue());
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
				se,        
				se);
		return updateColumn(cfTemplate, key, mapUpdate);
	}

	static public boolean updateSuperColumn(String CF, String SCkey, String key, Map<String, String> mapUpdate)
	{
		Mutator<String> m = HFactory.createMutator(CassandraAccessor.getKeyspace(), se);
		List<HColumn<String, String>> cols =  new ArrayList<HColumn<String, String>>();
		for(Map.Entry<String, String> e : mapUpdate.entrySet())
		{
			cols.add(HFactory.createColumn(e.getKey(), e.getValue(), se, se));
			System.out.println("SC Update from key:" + key  + " column: " + e.getKey() + " value " + e.getValue());
		}		
		HSuperColumn<String, String, String> sc = HFactory.createSuperColumn(key,cols, se, se, se);
		m.addInsertion(SCkey, CF, sc);		
		try
		{
			m.execute();
		}
		catch(Exception e) 
		{
			System.out.println("Error updating SCF");
			e.printStackTrace();
			return false;
		} // Assume it already exists.
		return true;
	}

	static public String getColumn(String CF, String key, String columnName)
	{
		ColumnQuery<String, String, String> cQ = HFactory.createColumnQuery(getKeyspace(), se, se, se);
		QueryResult<HColumn<String, String>> r;
		try {
			r = cQ.setKey(key).setName(columnName).setColumnFamily(CF).execute();
		}
		catch (Exception e){
			System.out.println(e);
			return null;
		}
		if (r == null) return null;
		System.out.println("CF=" + CF + " KEY=" + key +" COL=" + columnName + " RESULT=" + r.get());
		HColumn<String, String> c = r.get();
		if (c == null) return null;
		return  c.getValue();
	}

	static public Map<String, String> getRow(String CF, String key)
	{
		SliceQuery<String, String, String> cQ = HFactory.createSliceQuery(getKeyspace(), se, se, se);
		QueryResult<ColumnSlice<String, String>> r;
		try {
			r = cQ.setKey(key).setColumnFamily(CF).setRange("", "", false, 100).execute();
		}
		catch (Exception e){
			System.out.println("getRow CF:" + CF + " key: " + key);
			e.printStackTrace();			
			return null;
		}
		if (r == null) return null;
		ColumnSlice<String, String> c = r.get();
		if (c == null) return null;
		List<HColumn<String, String>> l = c.getColumns();
		HashMap<String, String> map = new HashMap<String, String>();
		for (HColumn<String, String> i: l)
		{
			map.put(i.getName(), i.getValue());
		}
		return  map;
	}



	static public boolean deleteKey(String CF, String key)
	{
		Mutator<String> mutator = HFactory.createMutator(getKeyspace(), se);
		mutator.delete(key, CF, null, se);
		mutator.execute();
		mutator.discardPendingMutations();
		return true;
	}
   
	static public Set<String> getColumnRange(String CF, String keyFirst, String keyLast, int number)
	{
		RangeSlicesQuery<String, String, String> rangeSlicesQuery =
				HFactory.createRangeSlicesQuery(getKeyspace(), se, se, se);
		rangeSlicesQuery.setColumnFamily(CF);
		rangeSlicesQuery.setKeys(keyFirst, keyLast);
		rangeSlicesQuery.setRange("", "", false, number);
		//QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
		//If you did not want to explicitly set the column names, or as above, get the first three columns in order, but instead wanted to return all columns starting with a common prefix, you could change the RangeSlicesQuery from the example above as follows:
		rangeSlicesQuery.setRange("fake_column_", "", false, 3);
		return null;
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

	public static boolean createSuperColumnFamily(String SCFName, ComparatorType ct) {

		//logger.debug("Creating "+(isSuper ? "Super" : "Standard")+" column family '" + columnFamily + "'");

		ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
				getKeyspace().getKeyspaceName(),
				SCFName, ct);
		ThriftCfDef thriftCfDef = new ThriftCfDef(cfDef);
		thriftCfDef.setColumnType(ColumnType.SUPER);
		thriftCfDef.setComparatorType(ct);
		thriftCfDef.setSubComparatorType(ct);
		try 
		{
			getCluster().addColumnFamily(thriftCfDef);
		}
		catch (Exception e)
		{			
			return false;
		}
		return true;
	}

	public static boolean getColumnsRange(String SCFName, String Skey, String firstKey, String lastKey, Map<String, Map<String, String>> result)
	{
		// get value
		RangeSubSlicesQuery<String, String,String, String> q = HFactory.createRangeSubSlicesQuery(getKeyspace(), se, se, se, se);
		q.setColumnFamily(SCFName);
		q.setKeys(firstKey, lastKey);
		// try with column name first
		q.setSuperColumn(Skey);
		q.setRange("", "", false, 100);
		QueryResult<OrderedRows<String, String, String>> r = null;
		try
		{
			r = q.execute();
		}
		catch (Exception e)
		{
			System.out.println("Error to get columns in SCF");
			e.printStackTrace();
			return false;
		}	    
		OrderedRows<String, String, String> rows = r.get();
		if (rows == null)
			return false;
		List<Row<String, String, String>> rowsList = rows.getList();
		if (rowsList == null)
			return false;
		for (Row<String, String, String> row: rowsList)
		{
			String mapName = row.getKey();
			Map<String, String> subMap = new HashMap<String, String>();
			ColumnSlice<String, String> slice = row.getColumnSlice();
			System.out.println("Found row with key: " + mapName);
			if (slice == null)
				return false;
			List<HColumn<String, String>> cols = slice.getColumns();
			for (HColumn<String, String> l: cols)
			{
				subMap.put(l.getName(), l.getValue());
				//System.out.println("Found column key: " + l.getName() + " value: " + l.getValue());

			}
			result.put(mapName, subMap);
		}
		return true;
	}

	public static boolean createCompositeColumnFamily(String name, String type)
	{
		ColumnFamilyDefinition cfDef = null;		
		cfDef = HFactory.createColumnFamilyDefinition(
				CassandraAccessor.KEYSPACENAME,                              
				name, 
				ComparatorType.COMPOSITETYPE);
		cfDef.setComparatorTypeAlias("(UTF8Type)");
		try{
			CassandraAccessor.getCluster().addColumnFamily(cfDef);
		}
		catch(Exception e)
		{ 
			//e.printStackTrace();
			return false;
		} 
		return true;
	}

	static public boolean updateCompositeColumn(ColumnFamilyTemplate<String, Composite> cfTemplate, String key, Composite colKey, String colValue)
	{	
		ColumnFamilyUpdater<String, Composite> updater = cfTemplate.createUpdater(key);
		updater.setString(colKey, colValue);		
		//System.out.println("Update from key:" + key  + " column: " + colKey + " value " + colValue);
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


	static public boolean updateCompositeColumn(String CF, String key, Composite colKey, String colValue)
	{
		ColumnFamilyTemplate<String, Composite> cfTemplate = new ThriftColumnFamilyTemplate<String, Composite>(CassandraAccessor.getKeyspace(),
				CF, 
				se,        
				ce);
		return updateCompositeColumn(cfTemplate, key, colKey, colValue);
	}

	public class CompositeColumnEntry {
		public Composite key;
		public String value;
		CompositeColumnEntry(Composite _key, String _value) { key=_key; value = _value;}
	}
	
	static public boolean getCompositeColumnsRange(String CFName, String mainKey, Composite keyStart, Composite keyEnd, List<CompositeColumnEntry> result)
	{
		// get value
		SliceQuery<String, Composite, String> q = HFactory.createSliceQuery(getKeyspace(), se, ce, se);
		q.setColumnFamily(CFName);
		q.setRange(keyStart, keyEnd, false, 10);
		q.setKey(mainKey);
		QueryResult<ColumnSlice<Composite, String>> r = null;
		try
		{
			r = q.execute();
		}
		catch (Exception e)
		{
			System.out.println("Error to get columns in SCF");
			e.printStackTrace();
			return false;
		}	    
		ColumnSlice<Composite, String> columns = r.get();
		//System.out.println(columns);
		if (columns == null)
			return false;
		for (HColumn<Composite, String> col: columns.getColumns())
		{
			//System.out.println(col.getName());
			//System.out.println(col.getValue());
			CompositeColumnEntry entry = current.new CompositeColumnEntry(col.getName(), col.getValue());
			result.add(entry);
		}
		return true;
	}

	static public boolean deleteColumn(String CF, String key, String col)
	{
		Mutator<String> mutator = HFactory.createMutator(CassandraAccessor.getKeyspace(), se);		
		mutator.delete(key, CF, col, se);
		try
		{
			mutator.execute();
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
	static public boolean deleteCompositeColumn(String CF, String key, Composite col)
	{
		Mutator<String> mutator = HFactory.createMutator(CassandraAccessor.getKeyspace(), se);
		//System.out.println("Delete CF: " + CF + " COL: " + col+ " ROW: " + key);
		mutator.delete(key, CF, col, ce);
		try
		{
			MutationResult r = mutator.execute();
			//System.out.println(r);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
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
}
