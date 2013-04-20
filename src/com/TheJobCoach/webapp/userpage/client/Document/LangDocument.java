package com.TheJobCoach.webapp.userpage.client.Document;

import java.util.Map;

import com.google.gwt.i18n.client.Constants;

public interface LangDocument extends Constants {

	@Key("type")
	String _TextType();

	@Key("title")
	String _TextTitle();
	
	@Key("documenttitle")
	String _TextDocumentTitle();
	
	@Key("documentStatusMap")	
	Map<String, String> documentStatusMap();
	
	@Key("documentTypeMap")	
	Map<String, String> documentTypeMap();

	@Key("downloadfile")
	String _TextDownloadFile();

	@Key("mydocuments")
	String _TextUserDocument();

	@Key("editdocument")	
	String _TextEditUserDocument();

	@Key("updatedocument")
	String _TextUpdateUserDocument();

	@Key("newdocument")
	String _TextNewUserDocument();

	@Key("deletedocument")
	String _TextDeleteUserDocument();

	@Key("filename")
	String _TextFilename();

	@Key("lastupdate")
	String _TextLastUpdate();
	
	@Key("createanewdocument")
	String _TextCreateANewUserDocument();
	
	@Key("updateadocument")
	String _TextUpdateANewUserDocument();

	@Key("confirmdeletedocumenttitle")
	String _TextConfirmDeleteUserDocumentTitle();

	@Key("confirmdeletedocument")
	String _TextConfirmDeleteUserDocument();

	@Key("uploadinprogress")
	String _TextUploadInProgress();
	
	@Key("selectfile")
	String _TextSelectFile();
	
	@Key("needfile")
	String _TextNeedFilename();
	
	@Key("attachuserdocument")
	String _TextAttachUserDocument();
	
}
