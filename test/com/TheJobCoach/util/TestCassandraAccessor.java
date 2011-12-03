package com.TheJobCoach.util;

import static org.junit.Assert.*;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import org.junit.Test;

public class TestCassandraAccessor {
		
	@Test
	public void testGetCluster() {
		Cluster cluster = CassandraAccessor.getCluster();
		assertNotNull(cluster);
	}

	@Test
	public void testGetKeyspaceDefinition() {
		KeyspaceDefinition ks = CassandraAccessor.getKeyspaceDefinition();
		assertNotNull(ks);
	}

	@Test
	public void testGetKeyspace() {
		Keyspace ks = CassandraAccessor.getKeyspace();
		assertNotNull(ks);
	}

	@Test
	public void testUpdateColumn() {
		ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
				CassandraAccessor.KEYSPACENAME,                              
				"TestColumnFamily", 
				ComparatorType.ASCIITYPE);
		try{
			CassandraAccessor.getCluster().addColumnFamily(cfDef);
		}
		catch(Exception e) {} // Assume it already exists.
		
		// Insert/Update 2 elements		
		CassandraAccessor.updateColumn("TestColumnFamily", "myobject", (new ShortMap()).add("k1", "v11").add("k2", "v21").add("k3", "v31").get());	
		CassandraAccessor.updateColumn("TestColumnFamily", "myobject2", (new ShortMap()).add("2k1", "2v11").add("2k2", "2v21").add("2k3", "2v31").get());	
		assertEquals(CassandraAccessor.getColumn("TestColumnFamily", "myobject", "k1"), "v11");
		assertEquals(CassandraAccessor.getColumn("TestColumnFamily", "myobject", "k2"), "v21");
		assertEquals(CassandraAccessor.getColumn("TestColumnFamily", "myobject", "k3"), "v31");
		CassandraAccessor.updateColumn("TestColumnFamily", "myobject", (new ShortMap()).add("k2", "v22").get());	
		assertEquals(CassandraAccessor.getColumn("TestColumnFamily", "myobject", "k1"), "v11");
		assertEquals(CassandraAccessor.getColumn("TestColumnFamily", "myobject", "k2"), "v22");
		assertEquals(CassandraAccessor.getColumn("TestColumnFamily", "myobject", "k3"), "v31");

		assertEquals(CassandraAccessor.getColumn("TestColumnFamily", "myobject2", "2k1"), "2v11");
		assertEquals(CassandraAccessor.getColumn("TestColumnFamily", "myobject2", "2k2"), "2v21");
		assertEquals(CassandraAccessor.getColumn("TestColumnFamily", "myobject2", "2k3"), "2v31");

		assertNull(CassandraAccessor.getColumn("TestColumnFamily", "myobject3", "1"));
		assertNull(CassandraAccessor.getColumn("TestColumnFamily", "myobject2", "1"));
		assertNull(CassandraAccessor.getColumn("TestColumnFamilyNull", "myobject2", "1"));
	}

	@Test
	public void testDeleteRow() 
	{
		ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
				CassandraAccessor.KEYSPACENAME,                              
				"TestColumnFamily2", 
				ComparatorType.ASCIITYPE);
		try
		{
			CassandraAccessor.getCluster().addColumnFamily(cfDef);
		}
		catch(Exception e) {} // Assume it already exists.
		CassandraAccessor.updateColumn("TestColumnFamily2", "myobject", (new ShortMap()).add("k1", "v11").add("k2", "v21").add("k3", "v31").get());	
		CassandraAccessor.updateColumn("TestColumnFamily2", "myobject2", (new ShortMap()).add("2k1", "2v11").add("2k2", "2v21").add("2k3", "2v31").get());	

		assertNotNull(CassandraAccessor.getColumn("TestColumnFamily2", "myobject2", "2k1"));
		assertNotNull(CassandraAccessor.getColumn("TestColumnFamily2", "myobject", "k1"));
		assertEquals(CassandraAccessor.deleteKey("TestColumnFamily2", "fail"), true);
		assertEquals(CassandraAccessor.deleteKey("TestColumnFamily2", "myobject"), true);
		
		System.out.println(CassandraAccessor.getColumn("TestColumnFamily2", "myobject2", "2k1"));		
		assertNull(CassandraAccessor.getColumn("TestColumnFamily2", "myobject", "k1"));
		assertNotNull(CassandraAccessor.getColumn("TestColumnFamily2", "myobject2", "2k1"));		
	}
}
