package com.TheJobCoach.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import org.junit.Test;

import com.TheJobCoach.util.CassandraAccessor.CompositeColumnEntry;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;

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
	public void testUpdateColumn() throws CassandraException {
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
	public void testDeleteRow() throws CassandraException 
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
		CassandraAccessor.deleteKey("TestColumnFamily2", "fail");
		CassandraAccessor.deleteKey("TestColumnFamily2", "myobject");
		
		System.out.println(CassandraAccessor.getColumn("TestColumnFamily2", "myobject2", "2k1"));		
		assertNull(CassandraAccessor.getColumn("TestColumnFamily2", "myobject", "k1"));
		assertNotNull(CassandraAccessor.getColumn("TestColumnFamily2", "myobject2", "2k1"));		
	}
	
	@Test
	public void testGetRow() throws CassandraException
	{
		Map<String, String> map = null;
			map = CassandraAccessor.getRow("TestColumnFamily", "myobject");
		System.out.println(map.size());
		assertEquals(map.size(), 3);
		assertEquals(map.get("k1"), "v11");
		assertEquals(map.get("k2"), "v22");
		assertEquals(map.get("k3"), "v31");
		assertNull(map.get("toto"));
	}
		
	/*
	@Test 
	public void testCompositeColumnFamily()
	{
		CassandraAccessor.deleteColumnFamily("TESTCOMPOSITE");
		assertEquals(true, CassandraAccessor.createCompositeColumnFamily("TESTCOMPOSITE", "(UTF8Type)"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("firstkey")), "mydata"));
		assertEquals(false, CassandraAccessor.createCompositeColumnFamily("TESTCOMPOSITE", "(UTF8Type)"));
		assertEquals(true, CassandraAccessor.deleteColumnFamily("TESTCOMPOSITE"));
	}	
	*/
	@Test 
	public void testGetCompositeColumnRange()
	{
		CassandraAccessor.deleteColumnFamily("TESTCOMPOSITE");
		assertEquals(true, CassandraAccessor.createCompositeColumnFamily("TESTCOMPOSITE", "(UTF8Type)"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("keyZ")), "mydata0"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key0")), "mydata0"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key0")), "mydata0u"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key1")), "mydata1"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key2")), "mydata2"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key3")), "mydata3"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key4")), "mydata4"));
		ArrayList<CompositeColumnEntry> result = new ArrayList<CompositeColumnEntry>();
		CassandraAccessor.getCompositeColumnsRange(
				"TESTCOMPOSITE", 
				"TOTO", 
				(Composite)(new EasyComposite().easyAdd("key0")), 
				(Composite)(new EasyComposite().easyAdd("key2Z")), 
				result);
		assertEquals(3, result.size());	
		CompositeColumnEntry key = result.get(0);
		assertEquals("key0", key.key.get(0, new StringSerializer()));
		assertEquals("mydata0u", key.value);

		key = result.get(1);
		assertEquals("key1", key.key.get(0, new StringSerializer()));
		assertEquals("mydata1", key.value);
		
		key = result.get(2);
		assertEquals("key2", key.key.get(0, new StringSerializer()));		
		assertEquals("mydata2", key.value);
		
		/*
		assertTrue(keySet.contains((Composite)new EasyComposite().easyAdd("key0")));
		assertTrue(keySet.contains(new EasyComposite().easyAdd("key1")));
		assertTrue(keySet.contains(new EasyComposite().easyAdd("key2")));
		assertEquals(result.get(new EasyComposite().easyAdd("key0")), "mydata0u");
		assertEquals(result.get(new EasyComposite().easyAdd("key1")), "mydata1");
		assertEquals(result.get(new EasyComposite().easyAdd("key2")), "mydata2");*/
	/*
		CassandraAccessor.deleteColumnFamily("TESTCOMPOSITE");
		assertEquals(true, CassandraAccessor.createCompositeColumnFamily("TESTCOMPOSITE", "(UTF8Type,UTF8Type)"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key0").easyAdd("key0")), "mydata0"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key0").easyAdd("key1")), "mydata1"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key0").easyAdd("key1")), "mydata1u"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key1").easyAdd("key0")), "mydata0"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key1").easyAdd("key1")), "mydata1"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO", (Composite)(new EasyComposite().easyAdd("key1").easyAdd("key2")), "mydata2"));

		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO2", (Composite)(new EasyComposite().easyAdd("key0").easyAdd("key0")), "2mydata0"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO2", (Composite)(new EasyComposite().easyAdd("key0").easyAdd("key1")), "2mydata1"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO2", (Composite)(new EasyComposite().easyAdd("key0").easyAdd("key1")), "2mydata1u"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO2", (Composite)(new EasyComposite().easyAdd("key1").easyAdd("key0")), "2mydata0"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO2", (Composite)(new EasyComposite().easyAdd("key1").easyAdd("key1")), "2mydata1"));
		assertEquals(true, CassandraAccessor.updateCompositeColumn("TESTCOMPOSITE", "TOTO2", (Composite)(new EasyComposite().easyAdd("key1").easyAdd("key2")), "2mydata2"));

		Map<Composite, String> result = new HashMap<Composite, String>();
		CassandraAccessor.getCompositeColumnsRange(
				"TESTCOMPOSITE", 
				"TOTO", 
				(Composite)(new EasyComposite().easyAdd("key0")), 
				(Composite)(new EasyComposite().easyAdd("key0")), 
				result);
		*/
		assertEquals(true, CassandraAccessor.deleteColumnFamily("TESTCOMPOSITE"));
	}
	/*
	@Test 
	public void testGetKeyRange()
	{
		CassandraAccessor.deleteColumnFamily("TestRange");
		ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
				CassandraAccessor.KEYSPACENAME,                              
				"TestRange", 
				ComparatorType.ASCIITYPE);
		try
		{
			CassandraAccessor.getCluster().addColumnFamily(cfDef);
		}
		catch(Exception e) {} // Assume it already exists.
		
		CassandraAccessor.updateColumn("TestRange", "1", (new ShortMap()).add("id", "").get());	
		CassandraAccessor.updateColumn("TestRange", "2", (new ShortMap()).add("id", "tata#0").get());	
		CassandraAccessor.updateColumn("TestRange", "3", (new ShortMap()).add("id", "tata#1").get());	
		CassandraAccessor.updateColumn("TestRange", "4", (new ShortMap()).add("id", "tata#2").get());	
		CassandraAccessor.updateColumn("TestRange", "5", (new ShortMap()).add("id", "tata#3").get());	
		CassandraAccessor.updateColumn("TestRange", "6", (new ShortMap()).add("id", "tata#4").get());	
		CassandraAccessor.updateColumn("TestRange", "7", (new ShortMap()).add("id", "tata#5").get());	
		CassandraAccessor.updateColumn("TestRange", "8", (new ShortMap()).add("id", "tata#6").get());	
		CassandraAccessor.updateColumn("TestRange", "9", (new ShortMap()).add("id", "tata#7").get());	
		CassandraAccessor.updateColumn("TestRange", "10", (new ShortMap()).add("id", "titi#8").get());
		Set<String> result = new HashSet<String>();
		CassandraAccessor.getKeyRange("TestRange", "1", "3", "id", result);
		//assertEquals(5, result.size());
		System.out.println(result);
		assertTrue(result.contains("tata#1"));
		assertTrue(result.contains("tata#2"));
		assertTrue(result.contains("tata#3"));
		assertTrue(result.contains("tata#4"));
		assertTrue(result.contains("tata#5"));
	}
	*/
}
