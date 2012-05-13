package com.TheJobCoach.webapp.userpage.client;

import java.util.Map;

import com.google.gwt.i18n.client.Constants;

public interface Lang extends Constants {

	@Key("ok")
	String _TextOk();

	@Key("cancel")
	String _TextCancel();

	// Context UP
	
	//@Key("connectedas")
	//String _TextConnectedAs();
	
	// Common
	
	@Key("type")
	String  _TextType();
	
	// Menu
	
	@Key("account")
	String _TextAccount();

	@Key("myprofile")
	String _TextMyProfile();

	@Key("myparameters")
	String _TextMyAccount();

	@Key("goals")
	String _TextGoals();

	@Key("mygoals")
	String _TextMyGoals();

	@Key("mytools")
	String _TextMyTools();

	@Key("myaddressbook")
	String _TextMyAddressBook();

	@Key("myjobsites")
	String _TextMyJobBoards();

	@Key("mydocuments")
	String _TextMyDocuments();

	@Key("mysearch")
	String _TextMySearch();

	@Key("myresearches")
	String _TextMyOpportunities();

	@Key("researchresults")
	String _TextSearchResults();

	@Key("applicationfollowup")
	String _TextApplicationFollowUp();

	@Key("actionagenda")
	String _TextAgenda();

	@Key("archivedapplications")
	String _TextArchivedApplications();

	@Key("evaluations")
	String _TextEvaluations();

	@Key("bilans")
	String _TextBilans();

	@Key("performance")
	String _TextPerformance();

	@Key("library")
	String _TextLibrary();

	@Key("sites")
	String _TextSites();

	@Key("advices")
	String _TextAdvices();

	@Key("community")
	String _TextCommunity();

	@Key("forum")
	String _TextForum();

	@Key("news")
	String _TextNews();

	@Key("report")
	String _TextReport();
	
	// Footer
	
	@Key("about")
	String _TextAbout();

	@Key("whoweare")
	String _TextWhoWeAre();

	@Key("confidentiality")
	String _TextConfidentiality();
	
	// User Site manager
	
	@Key("name")
	String _TextName();
	
	@Key("description")
	String _TextDescription();
	
	@Key("login")
	String _TextLogin();
	
	@Key("password")
	String _TextPassword();
	
	@Key("url")
	String _TextURL();
	
	@Key("lastvisit")
	String _TextLastVisit();
	
	@Key("newsite")
	String _TextNewSite();
	
	@Key("deletesite")
	String _TextDeleteSite();
	
	@Key("updatesite")
	String _TextUpdateSite();

	@Key("confirmdeletesitetitle")
	String _TextConfirmDeleteSiteTitle();
	
	@Key("confirmdeletesite")
	String _TextConfirmDeleteSite();
	
	
	// Opportunity
	
	@Key("applicationStatusMap")	
	Map<String, String> applicationStatusMap();

	@Key("company")
	String _TextCompany();
	
	@Key("location")
	String _TextLocation();
	
	@Key("salary")
	String _TextSalary();
	
	@Key("contracttype")
	String _TextContractType();
	
	@Key("firstseen")
	String _TextFirstSeen();

	@Key("status")
	String  _TextStatus();
	
	@Key("titlejobsite")
	String lblJobSites_text();

	@Key("titleopportunities")
	String lblOpportunities_text();
	
	@Key("newopportunity")
	String _TextNewOpportunity();
	
	@Key("deleteopportunity")
	String _TextDeleteOpportunity();
	
	@Key("updateopportunity")
	String _TextUpdateOpportunity();

	@Key("startdate")
	String _TextStartDate();

	@Key("enddate")
	String _TextEndDate();

	@Key("url")
	String _TextUrl();

	@Key("source")
	String _TextSource();

	@Key("positionname")
	String _TextPositionName();

	@Key("editlogs")	
	String _TextEditLogs();

	@Key("updatelogentry")
	String _TextUpdateLogEntry();

	@Key("newlogentry")
	String _TextNewLogEntry();

	@Key("deletelogentry")
	String _TextDeleteLogEntry();

	/// Log entry

	@Key("userlogtitle")
	String _TextUserLogTitle();

	@Key("created")
	String _TextCreated();

	@Key("expectedfollowup")
	String _TextExpectedFollowUp();
	
	@Key("editlogtitle")
	String _Text_EditLog();

	@Key("backtoopportunitylist")
	String _Text_BackToOpportunityList();
	
	@Key("logEntryStatusMap")	
	Map<String, String> logEntryStatusMap();

	
	/// Content Comment
	
	@Key("comment")
	String _TextComment();	

	@Key("makecomment")
	String _TextMakeComment();	

	@Key("aboutcomment")
	String _TextAboutComment();	

	@Key("sendcomment")
	String _TextSendComment();	

	@Key("replycomment")
	String _TextReplyComment();	

	
	// Document
	
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
	
	// Edit user site

	@Key("editusersitetitle")
	String _TextEditUserSiteTitle();

	@Key("createusersitetitle")
	String _TextCreateUserSiteTitle();

}
