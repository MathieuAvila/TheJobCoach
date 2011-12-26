package com.TheJobCoach.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	@Test
	public void testGetRow()
	{
		Map<String, String> map = CassandraAccessor.getRow("TestColumnFamily", "myobject");
		System.out.println(map.size());
		assertEquals(map.size(), 3);
		assertEquals(map.get("k1"), "v11");
		assertEquals(map.get("k2"), "v22");
		assertEquals(map.get("k3"), "v31");
		assertNull(map.get("toto"));
	}
	
	@Test 
	public void testCreateSuperColumnFamily()
	{
		CassandraAccessor.deleteColumnFamily("TESTSCF");
		boolean result = CassandraAccessor.createSuperColumnFamily("TESTSCF", ComparatorType.ASCIITYPE);
		assertEquals(result, true);
		result = CassandraAccessor.deleteColumnFamily("TESTSCF");	
		assertEquals(result, true);
		CassandraAccessor.createSuperColumnFamily("TESTSCF", ComparatorType.ASCIITYPE);
		assertEquals(result, true);
	}
	
	@Test 
	public void testUpdateSuperColumn()
	{
		assertEquals(true, CassandraAccessor.updateSuperColumn("TESTSCF", "SC1", "SUB1", (new ShortMap()).add("k11", "v11").add("k2", "v21").add("k3", "v31").get()));
		assertEquals(true, CassandraAccessor.updateSuperColumn("TESTSCF", "SC1", "SUB2", (new ShortMap()).add("k21", "v11").add("k2", "v21").add("k3", "v31").get()));
		assertEquals(true, CassandraAccessor.updateSuperColumn("TESTSCF", "SC1", "SUB3", (new ShortMap()).add("k31", "v11").add("k2", "v21").add("k3", "v31").get()));
		assertEquals(true, CassandraAccessor.updateSuperColumn("TESTSCF", "SC2", "SUB1", (new ShortMap()).add("k1", "v11").add("k2", "v21").add("k3", "v31").get()));
		assertEquals(true, CassandraAccessor.updateSuperColumn("TESTSCF", "SC2", "SUB2", (new ShortMap()).add("k1", "v11").add("k2", "v21").add("k3", "v31").get()));
	}
	
	
	@Test 
	public void testGetColumnsRange()
	{
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		boolean resultExec = CassandraAccessor.getColumnsRange("TESTSCF", "SC1", "SUB1", "SUB3", result);
		assertEquals(true, resultExec);
	}	
	/*
	@Test
	public void testRangeSubSlicesQuery()
	{
		Mutator<String> m = HFactory.createMutator(CassandraAccessor.getKeyspace(), se);
		Keyspace ko = CassandraAccessor.getKeyspace();
		int rowCount = 5;
		int scCount = 5;
		String scPrefix = "SCPREFIX";
		String rowPrefix = "ROWPREFIX";
		String cf = "TESTSC";

		try
		{
			CassandraAccessor.createSuperColumnFamily(cf, ComparatorType.ASCIITYPE);
		}
		catch(Exception e) 
		{} // Assume it already exists.
				
		for (int i = 0; i < rowCount; ++i) {
			for (int j = 0; j < scCount; ++j) {
				@SuppressWarnings("unchecked")
				HSuperColumn<String, String, String> sc = HFactory.createSuperColumn(
						scPrefix + j,
						Arrays.asList(HFactory.createColumn("c0" + i + j, "v0" + i + j, se, se),
								HFactory.createColumn("c1" + 1 + j, "v1" + i + j, se, se)), se, se, se);
				m.addInsertion(rowPrefix + i, cf, sc);
			}
		}
		try
		{
			m.execute();
		}
		catch(Exception e) 
		{
			System.out.println("Error creating SC");
			e.printStackTrace();
		} // Assume it already exists.

	    // get value
	    RangeSubSlicesQuery<String, String,String, String> q = HFactory.createRangeSubSlicesQuery(ko, se, se, se, se);
	    q.setColumnFamily(cf);
	    q.setKeys("ROWPREFIX3", "ROWPREFIX5");
	    // try with column name first
	    q.setSuperColumn("SCPREFIX1");
	    q.setColumnNames("c041", "c111", "c031");
	    QueryResult<OrderedRows<String, String, String>> r = q.execute();
	    assertNotNull(r);
	    OrderedRows<String, String, String> rows = r.get();
	    assertNotNull(rows);
	    assertEquals(2, rows.getCount());
	    Row<String, String, String> row = rows.getList().get(0);
	    assertNotNull(row);
	    assertEquals("ROWPREFIX3", row.getKey());
	    ColumnSlice<String, String> slice = row.getColumnSlice();
	    System.out.println(row.toString());
	    System.out.println(slice.toString());
	    System.out.println(rows.toString());
	    assertNotNull(slice);
	    // Test slice.getColumnByName
	    assertEquals("v031", slice.getColumnByName("c031").getValue());
	    assertEquals("v131", slice.getColumnByName("c111").getValue());
	    assertNull(slice.getColumnByName("c033"));
	  }
*/
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
