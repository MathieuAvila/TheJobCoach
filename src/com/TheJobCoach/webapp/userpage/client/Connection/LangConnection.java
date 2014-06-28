package com.TheJobCoach.webapp.userpage.client.Connection;

import com.google.gwt.i18n.client.Constants;

public interface LangConnection extends Constants {

	@Key("firstname")
	String _TextFirstName();

	@Key("lastname")
	String _TextLastName();

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

	@Key("myShares")
	String myShares();

	@Key("hisShares")
	String hisShares();

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

}
