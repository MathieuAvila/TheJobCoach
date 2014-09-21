package com.TheJobCoach.userdata;

import java.util.Date;
import java.util.Map;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentRevision;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.TheJobCoach.webapp.util.shared.UserId;

public class UserDocumentManager implements IUserDataManager {

	final static String COLUMN_FAMILY_NAME_LIST = "documentlist";
	final static String COLUMN_FAMILY_NAME_DATA = "documentdata";
	final static String COLUMN_FAMILY_NAME_CONTENT = "documentcontent";

	static ColumnFamilyDefinition cfDefList = null;
	static ColumnFamilyDefinition cfDefData = null;
	static ColumnFamilyDefinition cfDefContent = null;

	static UserDocumentManager instance = new UserDocumentManager();

	public static UserDocumentManager getInstance() 
	{
		return instance;
	}

	UserDocumentManager()
	{
		cfDefList = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_LIST, cfDefList);
		cfDefData = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_DATA, cfDefData);
		cfDefContent = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CONTENT, cfDefContent);
	}

	public UserDocumentRevision getUserDocumentRevision(UserId id, String subKey) throws CassandraException
	{
		// Get the revision information
		String revInfoKey = id.userName + "#" + subKey;
		Map<String, String> resultSubReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, revInfoKey);
		if (resultSubReq != null)
		{
			return new UserDocumentRevision(
					Convertor.toDate(resultSubReq.get("lastupdate")),
					subKey,
					Convertor.toString(resultSubReq.get("filename")));
		}
		else
			return null;
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

	public Vector<UserDocumentId> getUserDocumentIdList(UserId id) throws CassandraException 
	{	
		String key = id.userName;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_LIST, key);
		Vector<UserDocumentId> result = new Vector<UserDocumentId>();		
		if (resultReq == null)
			return result;
		for (String oppId: resultReq.keySet())
		{
			UserDocument opp = getUserDocument(id, oppId);
			if ((opp == null)||(opp.revisions.size() == 0))
			{
				deleteUserDocument(id, oppId);
				deleteUserDocumentFromList(id, oppId);
			}
			else 
			{
				UserDocumentRevision rev = opp.revisions.get(opp.revisions.size()-1);
				result.add(new UserDocumentId(opp.ID, rev.ID, opp.name, rev.fileName, rev.date, rev.date));
			}
		}
		return result;
	}

	public UserDocument getUserDocument(UserId id, String ID) throws CassandraException 
	{		
		String subKey = id.userName + "#" + ID;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, subKey);
		if (resultReq == null)
		{
			return null;  // this means it was deleted.
		}
		
		// If this a subKey ?
		String masterKey = resultReq.get("master");
		if (masterKey != null)
		{
			ID = masterKey;
			subKey = id.userName + "#" + masterKey;
			resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, subKey);
		}	
		
		/* Build out the list of revisions */
		Vector<UserDocumentRevision> revisions = new Vector<UserDocumentRevision>();	

		String revisionCount = resultReq.get("revisioncount");
		if (revisionCount != null)
		{
			int count = Convertor.toInt(revisionCount);
			for (int index=0; index != count; index++)
			{
				// Get the revision information
				String revInfo = Convertor.toString(resultReq.get("rev#" + index));
				revisions.add(getUserDocumentRevision(id, revInfo));
			}
		}
		else
		{
			Date revDate = Convertor.toDate(resultReq.get("lastupdate"));
			revisions.add(new UserDocumentRevision(revDate, ID, Convertor.toString(resultReq.get("filename"))));		
		}

		return new UserDocument(
				ID,
				Convertor.toString(resultReq.get("name")),
				Convertor.toString(resultReq.get("description")),
				Convertor.toDate(resultReq.get("lastupdate")),
				Convertor.toString(resultReq.get("filename")),
				UserDocument.documentStatusToString(resultReq.get("status")),
				UserDocument.documentTypeToString(resultReq.get("type")),
				revisions
				);
	}

	public UserDocumentId getUserDocumentId(UserId id, String ID) throws CassandraException 
	{
		UserDocument doc = getUserDocument(id, ID);
		if (doc == null)
		{
			return null;  // this means it was deleted.
		}
		UserDocumentRevision rev = null;
		for (UserDocumentRevision revIndex: doc.revisions)
		{
			if (revIndex.ID.equals(ID))
			{
				rev = revIndex;
			}
		}
		if (rev == null)
		{
			return null;  // this means it was deleted.
		}
		return new UserDocumentId(doc.ID, rev.ID, doc.name, rev.fileName, doc.lastUpdate, rev.date);
	}

	public void setUserDocument(UserId id, UserDocument result) throws CassandraException 
	{
		String docId = id.userName + "#" + result.ID;
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_LIST, 
				id.userName,
				(new ShortMap()).add(result.ID, result.ID).get());
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				docId, 
				(new ShortMap())
				.add("lastupdate", result.lastUpdate)
				.add("name", result.name)
				.add("description", result.description)
				.add("filename", result.fileName)
				.add("status", UserDocument.documentStatusToString(result.status))
				.add("type", UserDocument.documentTypeToString(result.type))
				.get());		
	}

	public void setUserDocumentContent(UserId id, String docId, String fileName, byte[] result) throws CassandraException 
	{
		/* Push new version */
		String newDocId = SiteUUID.getDateUuid();
		String masterKey = id.userName + "#" + docId;
		String subIdKey = id.userName + "#" + newDocId;
		Map<String, String> resultReq = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_DATA, masterKey);
		if (resultReq == null)
		{
			throw new CassandraException();  // this means it is not available.
		}		
		String revisionCount = resultReq.get("revisioncount");
		byte[] checkResultContent = CassandraAccessor.getColumnByte(COLUMN_FAMILY_NAME_CONTENT, masterKey, "content");
		if ((checkResultContent == null) && (revisionCount == null)) revisionCount = "0";
		if ((revisionCount != null) || (checkResultContent == null))
		{
			int count = Convertor.toInt(revisionCount);
			CassandraAccessor.updateColumn(
					COLUMN_FAMILY_NAME_DATA, 
					masterKey,
					(new ShortMap())
					.add("filename", fileName)
					.add("rev#" + count, newDocId)
					.add("revisioncount", count+1).get());
		}
		else
		{			
			newDocId = docId;			
			subIdKey = masterKey;
			CassandraAccessor.updateColumn(
					COLUMN_FAMILY_NAME_DATA, 
					masterKey, 
					(new ShortMap())
					.add("filename", fileName)
					.add("rev#1", newDocId)
					.add("rev#0", docId)
					.add("revisioncount", 2).get());	
		}
		CassandraAccessor.updateColumn(
				COLUMN_FAMILY_NAME_DATA, 
				subIdKey, 
				(new ShortMap())
				.add("lastupdate", new Date())
				.add("filename", fileName)
				.add("master", docId)				
				.get());

		CassandraAccessor.updateColumnByte(
				COLUMN_FAMILY_NAME_CONTENT, 
				subIdKey,
				"content",
				result);
	}

	public byte[] getUserDocumentContent(UserId id, String docId) throws CassandraException 
	{
		docId = id.userName + "#" + docId;
		byte[] resultReq = CassandraAccessor.getColumnByte(COLUMN_FAMILY_NAME_CONTENT, docId, "content");
		if (resultReq == null)
		{
			return new byte[0];  // this means it was deleted.
		}
		return resultReq;
	}


	public void deleteUserDocumentFromList(UserId id, String ID) throws CassandraException
	{
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_LIST, id.userName, ID);	
	}

	public void deleteUserDocument(UserId id, String ID) throws CassandraException 
	{		
		String IdKey = id.userName + "#" + ID;
		UserDocument doc = getUserDocument(id, ID);
		if (doc != null)
		{
			for (UserDocumentRevision revId: doc.revisions)
			{
				if (revId != null)
				{
					String subId = id.userName + "#" + revId.ID;
					CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CONTENT, subId);
					CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, subId);
				}
			}
		}
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CONTENT, IdKey);
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_DATA, IdKey);
	}

	public boolean checkDocExist(String docId, UserId id)
	{
		String subKey = id.userName + "#" + docId;
		String resultReq = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_DATA, subKey, "filename");
		return (resultReq != null);
	}

	@Override
	public void deleteUser(UserId user) throws CassandraException
	{
		Vector<UserDocumentId> list = getUserDocumentIdList(user);
		for (UserDocumentId doc: list) 
		{
			deleteUserDocument(user, doc.ID);
			deleteUserDocumentFromList(user, doc.ID);
		}
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_LIST, user.userName);	
	}

	@Override
	public void createTestUser(UserId user, String lang)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void createUserDefaults(UserId user, String lang)
	{
		/* nothing to do */
	}
}
