package com.TheJobCoach.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import org.junit.Test;

import com.TheJobCoach.webapp.util.shared.CassandraException;

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
		
		assertNull(CassandraAccessor.getColumn("TestColumnFamily2", "myobject", "k1"));
		assertNotNull(CassandraAccessor.getColumn("TestColumnFamily2", "myobject2", "2k1"));		
	}
	
	@Test
	public void testGetRow() throws CassandraException
	{
		Map<String, String> map = null;
			map = CassandraAccessor.getRow("TestColumnFamily", "myobject");
		assertEquals(map.size(), 3);
		assertEquals(map.get("k1"), "v11");
		assertEquals(map.get("k2"), "v22");
		assertEquals(map.get("k3"), "v31");
		assertNull(map.get("toto"));
	}
	
	static private void checkByteArray(byte[] b1, byte[] b2)
	{
		assertEquals(b1.length, b2.length);
		for (int i = 0; i != b1.length; i++)
		{
			assertEquals(b1[i], b2[i]);
		}
	}
	
	@Test
	public void testSetGetByteColumn() throws CassandraException
	{
		CassandraAccessor.checkColumnFamilyAscii("TestByteRow", null);
		byte[] toto11 = { 1,2,3,4};
		byte[] toto12 = { 10,20,30,40};
		byte[] toto21 = { 11,21,31,41};
		byte[] toto22 = { 12,22,32,42};
		CassandraAccessor.updateColumnByte(
				"TestByteRow", 
				"binary1",
				"content1",
				toto11);
		CassandraAccessor.updateColumnByte(
				"TestByteRow", 
				"binary1",
				"content2",
				toto12);
		CassandraAccessor.updateColumnByte(
				"TestByteRow", 
				"binary2",
				"content1",
				toto21);
		CassandraAccessor.updateColumnByte(
				"TestByteRow", 
				"binary2",
				"content2",
				toto22);
		byte[] resultReq11 = CassandraAccessor.getColumnByte("TestByteRow", "binary1", "content1");
		byte[] resultReq12 = CassandraAccessor.getColumnByte("TestByteRow", "binary1", "content2");
		byte[] resultReq21 = CassandraAccessor.getColumnByte("TestByteRow", "binary2", "content1");
		byte[] resultReq22 = CassandraAccessor.getColumnByte("TestByteRow", "binary2", "content2");
		checkByteArray(resultReq11, toto11);				
		checkByteArray(resultReq12, toto12);				
		checkByteArray(resultReq21, toto21);				
		checkByteArray(resultReq22, toto22);				
	}
	
	@Test
	public void test_getKeyRange() throws CassandraException
	{
		String start = "";
		ColumnFamilyDefinition cfDef = null;
		String COLUMN_FAMILY_NAME = "testkeyrange";
		cfDef = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME, cfDef);
		Vector<String> result = new Vector<String>();
		
		// create 30 strings
		for (int i = 0 ; i != 30; i++)
			CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME, "myobject" + i, 
					(new ShortMap())
					.add("checker", "myobject" + i)
					.add("checker2", "myobject" + i).get());	

		// delete 1 row, 1 cheker column and 1 invalid column
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME, "myobject3", "checker");
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME, "myobject4", "checker2");
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME, "myobject5");
		
		// get all, by increasing number of ranges
		for (int count = 2 ; count != 30; count++)
		{
			start = "";
			Vector<String> fullRange = new Vector<String>();
			do {
				start = CassandraAccessor.getKeyRange(COLUMN_FAMILY_NAME, start, count, result, "checker");
				fullRange.addAll(result);
			} while (result.size() != 0);
			// now check counts
			assertEquals(30 - 2, fullRange.size());
			assertTrue(fullRange.contains("myobject0"));
			assertTrue(fullRange.contains("myobject1"));
			assertTrue(fullRange.contains("myobject4"));
			assertTrue(fullRange.contains("myobject6"));
			assertFalse(fullRange.contains("myobject3"));
			assertFalse(fullRange.contains("myobject5"));
		}
	}
	
	@Test
	public void test_getColumnRange() throws CassandraException
	{
		ColumnFamilyDefinition cfDef = null;
		String COLUMN_FAMILY_NAME = "testcolumnrange";
		cfDef = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME, cfDef);
		Map<String, String> result = new HashMap<String, String>();
		
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME, "myobject");
		
		// create 50 strings, NAME matters, hence starting from 10 and not 0
		for (int i = 10 ; i != 50; i++)
			CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME, "myobject", 
					(new ShortMap())
					.add("checker" + i, "myobject" + i).get());	

		// get in increasing order in the middle
		result = CassandraAccessor.getColumnRange(COLUMN_FAMILY_NAME, "myobject", "checker20", "checker40", 10);
		assertEquals(10, result.size());
		assertEquals("myobject20", result.get("checker20"));
		assertEquals("myobject29", result.get("checker29"));

		// get in increasing order from the start with limit
		result = CassandraAccessor.getColumnRange(COLUMN_FAMILY_NAME, "myobject", "", "checker15", 10);
		System.out.println(result);
		assertEquals(6, result.size());
		assertEquals("myobject10", result.get("checker10"));
		assertEquals("myobject15", result.get("checker15"));
		
		// get in decreasing order
		result = CassandraAccessor.getColumnRangeReversed(COLUMN_FAMILY_NAME, "myobject", "checker40", "checker20", 10);
		System.out.println(result);
		assertEquals(10, result.size());
		assertEquals("myobject40", result.get("checker40"));
		assertEquals("myobject31", result.get("checker31"));
	
		// get in decreasing order from the end with limit
		result = CassandraAccessor.getColumnRangeReversed(COLUMN_FAMILY_NAME, "myobject", "checker40", "checker35", 10);
		System.out.println(result);
		assertEquals(6, result.size());
		assertEquals("myobject40", result.get("checker40"));
		assertEquals("myobject35", result.get("checker35"));
	}
}
