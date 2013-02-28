package com.TheJobCoach.webapp.userpage.client.Opportunity;

import java.util.Map;

import com.google.gwt.i18n.client.Constants;

public interface LangLogEntry extends Constants {

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

	@Key("attachedfiles")
	String _TextFiles();
	
	@Key("done")
	String _TextDone();
	
	@Key("questiondeleteeditlogtitle")
	String _Text_DeleteEditLogTitle();

	@Key("questiondeleteeditlog")
	String _Text_DeleteEditLog();

}
