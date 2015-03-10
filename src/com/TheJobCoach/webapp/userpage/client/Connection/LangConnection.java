package com.TheJobCoach.webapp.userpage.client.Connection;

import com.google.gwt.i18n.client.Constants;

public interface LangConnection extends Constants {

	@Key("firstname")
	String _TextFirstName();

	@Key("lastname")
	String _TextLastName();
	
	@Key("nameSearch")
	String nameSearch();
	
	@Key("newConnection")
	String newConnection();

	@Key("connectionsSubtitle")
	String connectionsSubtitle();

	@Key("searchSubtitle")
	String searchSubtitle();

	@Key("runSearch")
	String runSearch();

	@Key("sendMessage")
	String sendMessage();

	@Key("jobTitle")
	String jobTitle();

	@Key("messageStatusOk")
	String messageStatusOk();

	@Key("messageStatusRequested")
	String messageStatusRequested();

	@Key("messageStatusAwaiting")
	String messageStatusAwaiting();

	@Key("messageStatusNone")
	String messageStatusNone();

	@Key("acceptConnection")
	String acceptConnection();

	@Key("refuseConnection")
	String refuseConnection();
	
	// In message dialog box
	@Key("sendMessageTo")
	String sendMessageTo();

	@Key("messageTestAccountDenied")
	String messageTestAccountDenied();

	// In shares dialog box

	@Key("shares")
	String shares();

	@Key("myShares")
	String myShares();

	@Key("hisHerShares")
	String hisHerShares();

	@Key("documents")
	String documents();

	@Key("logs")
	String logs();

	@Key("contacts")
	String contacts();

	@Key("opportunities")
	String opportunities();

	// connection detail
	
	@Key("backToConnections")
	String backToConnections();

	// opp detail

	@Key("opportunityList")
	String opportunityList();

	@Key("opportunityDetail")
	String opportunityDetail();

	@Key("logList")
	String logList();

	// user detail
	
	@Key("actualStatus")
	String actualStatus();

	@Key("skills")
	String skills();
	
	@Key("jobTitleDetail")
	String jobTitleDetail();
	
	// chat
	
	@Key("isTyping")
	String isTyping();

	@Key("isOnline")
	String isOnline();
	
	@Key("isOffline")
	String isOffline();
	
	
}
