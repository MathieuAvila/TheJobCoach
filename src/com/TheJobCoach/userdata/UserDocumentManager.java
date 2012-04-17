package com.TheJobCoach.userdata;

import java.util.Map;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;

public class UserDocumentManager {

	final static String COLUMN_FAMILY_NAME_LIST = "documentlist";
	final static String COLUMN_FAMILY_NAME_DATA = "documentdata";
	final static String COLUMN_FAMILY_NAME_CONTENT = "documentcontent";

	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;
	static ColumnFamilyDefinition cfDefContent = null;

	public UserDocumentManager()
	{
		cfDefList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST, cfDefList);
		cfDefData = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_DATA, cfDefData);
		cfDefData = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CONTENT, cfDefContent);
	}

	public Vector<UserDocument> getUserDocumentList(UserId id) throws CassandraException 
	{	
		String key = id.userName;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_LIST, key);
		Vector<UserDocument> result = new Vector<UserDocument>();		
		if (resultReq == null)
			return result;
		for (String oppId: resultReq.keySet())
		{
			UserDocument opp = getUserDocument(id, oppId);
			if (opp == null)
			{
				deleteUserDocument(id, oppId);
				deleteUserDocumentFromList(id, oppId);
			}
			else 
				result.add(opp);
		}
		return result;
	}

	public UserDocument getUserDocument(UserId id, String ID) throws CassandraException 
	{
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, ID);
		if (resultReq == null)
		{
			return null;  // this means it was deleted.
		}
		return new UserDocument(
				ID,
				Convertor.toString(resultReq.get("name")),
				Convertor.toString(resultReq.get("description")),
				Convertor.toDate(resultReq.get("lastupdate")),
				Convertor.toString(resultReq.get("filename")),
				UserDocument.documentStatusToString(resultReq.get("status"))
				);
	}

	public void setUserDocument(UserId id, UserDocument result) throws CassandraException 
	{
		String key = id.userName;
		System.out.println(result.name);
		System.out.println(result.ID);
		System.out.println(result.description);
		System.out.println(result.fileName);
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_LIST, 
				key, 
				(new ShortMap())
				.add(result.ID, result.ID)
				.get());
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				result.ID, 
				(new ShortMap())
				.add("lastupdate", result.lastUpdate)
				.add("name", result.name)
				.add("description", result.description)
				.add("filename", result.fileName)
				.add("status", UserDocument.documentStatusToString(result.status))
				.get());		
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_LIST, 
				id.userName,
				(new ShortMap()).add(result.ID, result.ID).get());
	}
	
	public void setUserDocumentContent(UserId id, String docId, String fileName, byte[] result) throws CassandraException 
	{
		//UserDocument userDoc = getUserDocument(id, docId);
		//if (userDoc == null) throw new CassandraException();
		//userDoc.fileName = fileName;
		//setUserDocument(id, userDoc);
		CassandraAccessor.updateColumnByte(
				COLUMN_FAMILY_NAME_CONTENT, 
				docId,
				"content",
				result);
	}
	
	public byte[] getUserDocumentContent(UserId id, String docId) throws CassandraException 
	{
		byte[] resultReq = CassandraAccessor.getColumnByte(COLUMN_FAMILY_NAME_CONTENT, docId, "content");
		if (resultReq == null)
		{
			System.out.println("Fetched document: " + docId + " has 0 length. Deleted ?");		
			return new byte[0];  // this means it was deleted.
		}
		System.out.println("Fetched document: " + docId + " has length " + resultReq.length);		
		return resultReq;
	}
	
	
	public void deleteUserDocumentFromList(UserId id, String ID) throws CassandraException
	{
		System.out.println("DELETE UserDocument " + ID);		
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST, id.userName, ID);	
	}

	public void deleteUserDocument(UserId id, String ID) throws CassandraException 
	{		
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CONTENT, ID);
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, ID);
	}
}
