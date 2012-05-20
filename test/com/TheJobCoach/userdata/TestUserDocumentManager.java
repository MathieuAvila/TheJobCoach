package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class TestUserDocumentManager {
	
	static UserDocumentManager manager = new UserDocumentManager();
	
	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static UserId id2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);
	
	static String ud1_id = "doc1";
	static String ud2_id = "doc2";
	static String ud21_id = "doc1";
	
	static UserDocument ud1 = new UserDocument(
			ud1_id, "ndoc1", "description1", getDate(2000, 12, 1), "file1", 
			UserDocument.DocumentStatus.NEW, UserDocument.DocumentType.RESUME);
	static UserDocument ud2 = new UserDocument(
			ud2_id, "ndoc2", "description2", getDate(2000, 12, 2), "file2", 
			UserDocument.DocumentStatus.OUTDATED, UserDocument.DocumentType.MOTIVATION);
	
	static UserDocument ud21 = new UserDocument(
			ud21_id, "ndoc1", "description1", getDate(2000, 12, 3), "file1", 
			UserDocument.DocumentStatus.SECONDARY, UserDocument.DocumentType.OTHER);
	
	@SuppressWarnings("deprecation")
	static Date getDate(int year, int month, int day)
	{
		Date result = new Date();
		result.setDate(day);
		result.setMonth(month);
		result.setYear(year - 1900);
		return result;
	}
	
	@SuppressWarnings("deprecation")
	private void checkDate(Date d1, Date d2)
	{
		assertEquals(d1.getYear(), d2.getYear());
		assertEquals(d1.getMonth(), d2.getMonth());
		assertEquals(d1.getDate(), d2.getDate());
	}
	
	@Test
	public void testClean() throws CassandraException
	{
		List<UserDocument> result = manager.getUserDocumentList(id);
		for (UserDocument doc: result)
		{
			manager.deleteUserDocument(id, doc.ID);
		}
		result = manager.getUserDocumentList(id2);
		for (UserDocument doc: result)
		{
			manager.deleteUserDocument(id2, doc.ID);
		}		
	}
	
	@Test
	public void testSetUserDocument() throws CassandraException 
	{
		manager.setUserDocument(id, ud1);
		manager.setUserDocument(id, ud2);
		manager.setUserDocument(id2, ud21);		
	}
	
	private void checkDocument(UserDocument doc1, UserDocument doc2)
	{
		assertEquals(doc1.description, doc2.description);
		assertEquals(doc1.ID, doc2.ID);
		assertEquals(doc1.fileName, doc2.fileName);
		assertEquals(doc1.name, doc2.name);
		assertEquals(doc1.status, doc2.status);
		assertEquals(doc1.type, doc2.type);
		checkDate(doc1.lastUpdate, doc2.lastUpdate);
		
	}
	
	@Test
	public void testgetUserDocument() throws CassandraException 
	{
		UserDocument copy_ud1 = manager.getUserDocument(id, ud1_id);
		UserDocument copy_ud2 = manager.getUserDocument(id, ud2_id);
		UserDocument copy_ud21 = manager.getUserDocument(id2, ud21_id);
		
		checkDocument(ud1, copy_ud1);
		checkDocument(ud2, copy_ud2);
		checkDocument(ud21, copy_ud21);
		
	}
	
	byte[] v = { };
	byte[] c1 = { 0, 1, 2};
	byte[] c2 = { 3, 4, 5};
	byte[] c21 = { 6, 7, 8};
	
	private void checkContentEquality(byte[] c1, byte[] c2)
	{
		assertEquals(c1.length, c2.length);
		for (int i = 0; i != c1.length; i++)
		{
			assertEquals(c1[i], c2[i]);
		}
	}
	
	@Test
	public void testGetDocumentContentVoid() throws CassandraException
	{
		byte[] c1_t = manager.getUserDocumentContent(id, ud1_id);
		checkContentEquality(c1_t, v);
		byte[] c2_t = manager.getUserDocumentContent(id, ud2_id);
		checkContentEquality(c2_t, v);
		byte[] c21_t = manager.getUserDocumentContent(id2, ud21_id);
		checkContentEquality(c21_t, v);
	}

	@Test
	public void testSetDocumentContent() throws CassandraException
	{
		manager.setUserDocumentContent(id, ud1_id, "f1", c1);
		manager.setUserDocumentContent(id, ud2_id, "f2", c2);
		manager.setUserDocumentContent(id2, ud21_id, "f21", c21);
	}

	@Test
	public void testGetDocumentContent() throws CassandraException
	{
		byte[] c1_t = manager.getUserDocumentContent(id, ud1_id);
		checkContentEquality(c1_t, c1);
		byte[] c2_t = manager.getUserDocumentContent(id, ud2_id);
		checkContentEquality(c2_t, c2);
		byte[] c21_t = manager.getUserDocumentContent(id2, ud21_id);
		checkContentEquality(c21_t, c21);
	}

	/*
	@Test
	public void testDeleteUserSite() throws CassandraException 
	{
		manager.deleteUserSite(id, ud2_id);
		manager.deleteUserSite(id2, ud21_id2);
		
		List<String> result = manager.getUserDocumentList(id);
		//System.out.println("***************** GET 1 " + result);
		assertEquals(2, result.size());
		assertTrue(result.contains(site1));
		assertTrue(result.contains(ud21_id));

		List<String> result2 = manager.getUserDocumentList(id2);
		//System.out.println("***************** GET 2 " + result2);
		assertEquals(2, result2.size());
		assertTrue(result2.contains(site12));
		assertTrue(result2.contains(ud2_id2));
	}
	*/
}
