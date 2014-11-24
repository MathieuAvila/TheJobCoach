package com.TheJobCoach.webapp.userpage.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ClientImageBundle extends ClientBundle 
{
	public static final ClientImageBundle INSTANCE = GWT.create(ClientImageBundle.class);

	@Source("george_1_150_150.png")
	ImageResource coachIcon();

	@Source("christine_1_150_150.png")
	ImageResource coachIconWoman();

	@Source("george_2_150_150.png")
	ImageResource coachIconSmall();

	@Source("christine_2_150_150.png")
	ImageResource coachIconWomanSmall();

	@Source("logout_24.png")
	ImageResource urlLogout();
	
	@Source("content-comment-32.png")
	ImageResource sendCommentContent();
	
	@Source("content-opportunity-32.png")
	ImageResource opportunityContent();

	@Source("content-log-32.png")
	ImageResource userLogContent();

	@Source("content-goals-32.png")
	ImageResource userVirtualCoachGoalsContent();

	@Source("content-addressbook-32.png")
	ImageResource userExternalContactContent();

	@Source("content-myreports-32.png")
	ImageResource userMyReportsContent();

	@Source("content-news-32.png")
	ImageResource newsContent();

	@Source("content-todo-32.png")
	ImageResource todoContent();

	@Source("content-sites-32.png")
	ImageResource userJobSiteContent();

	@Source("content-documents-32.png")
	ImageResource userDocumentContent();

	@Source("content-parameters-32.png")
	ImageResource parametersContent();

	@Source("content-coachsettings-32.png")
	ImageResource coachSettingsContent();

	@Source("content-library-32.png")
	ImageResource siteLibraryContent();
	
	

	@Source("content-comment-24.png")
	ImageResource sendCommentContent_menu();
	
	@Source("content-opportunity-24.png")
	ImageResource opportunityContent_menu();

	@Source("content-log-24.png")
	ImageResource userLogContent_menu();

	@Source("content-goals-24.png")
	ImageResource userVirtualCoachGoalsContent_menu();

	@Source("content-addressbook-24.png")
	ImageResource userExternalContactContent_menu();

	@Source("content-myreports-24.png")
	ImageResource userMyReportsContent_menu();

	@Source("content-news-24.png")
	ImageResource newsContent_menu();

	@Source("content-todo-24.png")
	ImageResource todoContent_menu();

	@Source("content-sites-24.png")
	ImageResource userJobSiteContent_menu();

	@Source("content-documents-24.png")
	ImageResource userDocumentContent_menu();

	@Source("content-parameters-24.png")
	ImageResource parametersContent_menu();

	@Source("content-coachsettings-24.png")
	ImageResource coachSettingsContent_menu();
	
	@Source("content-library-24.png")
	ImageResource siteLibraryContent_menu();
	
	@Source("content-connection-24.png")
	ImageResource userConnectionContent_menu();


	@Source("bulle.png")
	ImageResource bulle();


	@Source("content-connection-32.png")
	ImageResource userConnectionContent();
	

	@Source("user-awaiting-24.png")
	ImageResource userConnectionAwaiting();

	@Source("user-connected-24.png")
	ImageResource userConnectionOk();
	
	@Source("user-requested-24.png")
	ImageResource userConnectionRequested();
	
	@Source("void_24.png")
	ImageResource userConnectionNone();

	@Source("user-send-mail-24.png")
	ImageResource userSendMail();

	// For shares

	@Source("content-documents-thawed-24.png")
	ImageResource userDocumentContent_thawed();

	@Source("content-opportunity-thawed-24.png")
	ImageResource opportunityContent_thawed();

	@Source("content-addressbook-thawed-24.png")
	ImageResource userExternalContactContent_thawed();

	@Source("content-log-thawed-24.png")
	ImageResource userLogContent_thawed();
	
	@Source("void_24.png")
	ImageResource void_24();



	@Source("content-documents-disabled-24.png")
	ImageResource userDocumentContent_disabled();

	@Source("content-opportunity-disabled-24.png")
	ImageResource opportunityContent_disabled();

	@Source("content-addressbook-disabled-24.png")
	ImageResource userExternalContactContent_disabled();

	@Source("content-log-disabled-24.png")
	ImageResource userLogContent_disabled();
	
	@Source("max_24.png")
	ImageResource max_24();

	@Source("min_24.png")
	ImageResource min_24();
}