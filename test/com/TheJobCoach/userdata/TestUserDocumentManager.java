package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentRevision;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class TestUserDocumentManager {
	
	static UserDocumentManager manager = new UserDocumentManager();
	
	static UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	static UserId id2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);
	
	static String ud1_id = "doc1";
	static String ud2_id = "doc2";
	static String ud21_id = "doc1";
	static String ud4_id = "doc4";
	
	static UserDocumentRevision rev1 = new UserDocumentRevision(getDate(2000, 12, 1), ud1_id, "file1");
	static UserDocument ud1 = new UserDocument(
			ud1_id, "ndoc1", "description1", getDate(2000, 12, 1), "file1", 
			UserDocument.DocumentStatus.NEW, UserDocument.DocumentType.RESUME, new Vector<UserDocumentRevision>(Arrays.asList(rev1)));
	
	static UserDocumentRevision rev2 = new UserDocumentRevision(getDate(2000, 12, 2), ud2_id, "file2");
	static UserDocument ud2 = new UserDocument(
			ud2_id, "ndoc2", "description2", getDate(2000, 12, 2), "file2", 
			UserDocument.DocumentStatus.OUTDATED, UserDocument.DocumentType.MOTIVATION, new Vector<UserDocumentRevision>(Arrays.asList(rev2)));
	
	static UserDocumentRevision rev3 = new UserDocumentRevision(getDate(2000, 12, 3), ud21_id, "file3");
	static UserDocument ud21 = new UserDocument(
			ud21_id, "ndoc1", "description1", getDate(2000, 12, 3), "file3", 
			UserDocument.DocumentStatus.SECONDARY, UserDocument.DocumentType.OTHER, new Vector<UserDocumentRevision>(Arrays.asList(rev3)));
	
	static UserDocumentRevision rev4 = new UserDocumentRevision(getDate(2000, 12, 1), ud1_id, "file4");
	static UserDocument ud4 = new UserDocument(
			ud4_id, "ndoc4", "description4", getDate(2000, 12, 1), "file4", 
			UserDocument.DocumentStatus.NEW, UserDocument.DocumentType.RESUME, new Vector<UserDocumentRevision>(Arrays.asList(rev4)));
	
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
		
		assertEquals(copy_ud1.revisions.size(), 1);
		assertEquals(copy_ud1.revisions.get(0).fileName, ud1.fileName);
		assertEquals(copy_ud1.revisions.get(0).date, ud1.lastUpdate);
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
		UserDocument copy_ud = manager.getUserDocument(id, ud1_id);
		
		UserDocumentId udid = manager.getUserDocumentId(id, copy_ud.revisions.get(copy_ud.revisions.size() - 1).ID);
		byte[] c1_t = manager.getUserDocumentContent(id, udid.updateId);
		checkContentEquality(c1_t, c1);
		
		copy_ud = manager.getUserDocument(id, ud2_id);
		udid = manager.getUserDocumentId(id, copy_ud.revisions.get(copy_ud.revisions.size() - 1).ID);
		byte[] c2_t = manager.getUserDocumentContent(id, udid.updateId);
		checkContentEquality(c2_t, c2);
		
		copy_ud = manager.getUserDocument(id2, ud21_id);
		udid = manager.getUserDocumentId(id2, copy_ud.revisions.get(copy_ud.revisions.size() - 1).ID);
		byte[] c21_t = manager.getUserDocumentContent(id2, udid.updateId);
		checkContentEquality(c21_t, c21);
	}
	
	@Test
	public void testCheckDocExist()
	{
		assertTrue(manager.checkDocExist(ud1_id, id));
		assertFalse(manager.checkDocExist("toto", id));		
	}
	
	@Test
	public void multipleDocContentRevision() throws CassandraException, InterruptedException
	{
		manager.deleteUserDocument(id, ud4_id);
		manager.setUserDocument(id, ud4);
		manager.setUserDocumentContent(id, ud4_id, "myfile4", c1);
		
		UserDocument copy_ud = manager.getUserDocument(id, ud4_id);
		UserDocumentId udid = manager.getUserDocumentId(id, copy_ud.revisions.get(copy_ud.revisions.size() - 1).ID);
		byte[] c1_t = manager.getUserDocumentContent(id, udid.updateId);
		checkContentEquality(c1_t, c1);
		UserDocument ud = manager.getUserDocument(id, ud4_id);
		assertEquals(ud.fileName, "myfile4");
		assertEquals(ud.revisions.size(), 1);
		
		manager.setUserDocumentContent(id, ud4_id, "myfile1", c2);
		Thread.sleep(100);
		manager.setUserDocumentContent(id, ud4_id, "myfile2", c21);
		
		ud = manager.getUserDocument(id, ud4_id);
		assertEquals(ud.fileName, "myfile2");
		assertEquals(ud.revisions.size(), 3);
		assertEquals(ud.revisions.get(0).fileName, "myfile4");
		String rev0 = ud.revisions.get(0).ID;
		String rev1 = ud.revisions.get(1).ID;
		assertEquals(ud.revisions.get(1).fileName, "myfile1");
		String rev2 = ud.revisions.get(2).ID;
		assertEquals(ud.revisions.get(2).fileName, "myfile2");
				
		UserDocumentId docId = manager.getUserDocumentId(id, rev1);
		assertEquals(ud4_id, docId.ID);
		assertEquals(rev1, docId.updateId);
		
		assertTrue(manager.checkDocExist(ud4_id, id));
		assertTrue(manager.checkDocExist(rev0, id));
		assertTrue(manager.checkDocExist(rev1, id));
		assertTrue(manager.checkDocExist(rev2, id));
		
		byte[] check0 = manager.getUserDocumentContent(id, rev2);
		checkContentEquality(c21, check0);
		check0 = manager.getUserDocumentContent(id, rev1);
		checkContentEquality(c2, check0);
		check0 = manager.getUserDocumentContent(id, rev0);
		checkContentEquality(c1, check0);
		
		manager.deleteUserDocument(id, ud4_id);
		assertFalse(manager.checkDocExist(ud4_id, id));
		assertFalse(manager.checkDocExist(rev0, id));
		assertFalse(manager.checkDocExist(rev1, id));
		assertFalse(manager.checkDocExist(rev2, id));
		
		
	}
	
}
