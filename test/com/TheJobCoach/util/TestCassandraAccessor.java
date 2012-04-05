package com.TheJobCoach.util;

import static org.junit.Assert.*;

import java.util.Map;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import org.junit.Test;

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
	
}
