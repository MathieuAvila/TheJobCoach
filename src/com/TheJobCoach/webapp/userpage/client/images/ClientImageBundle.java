package com.TheJobCoach.webapp.userpage.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ClientImageBundle extends ClientBundle 
{
	public static final ClientImageBundle INSTANCE = GWT.create(ClientImageBundle.class);
	 
	/**
	 * Would match the file 'open_file_icon.gif' located in the same
	 * package as this type.
	 * 
	 * @gwt.resource deleteFile.gif
	 */
	@Source("jobcoach.gif")
	ImageResource coachIcon();

	@Source("jobcoach_military.jpg")
	ImageResource coachIconMilitary();

	@Source("jobcoach_surfer.gif")
	ImageResource coachIconSurfer();

	@Source("jobcoach_woman.png")
	ImageResource coachIconWoman();

	
	@Source("jobcoach_small.png")
	ImageResource coachIconSmall();

	@Source("jobcoach_military_small.png")
	ImageResource coachIconMilitarySmall();

	@Source("jobcoach_surfer_small.png")
	ImageResource coachIconSurferSmall();

	@Source("jobcoach_woman_small.png")
	ImageResource coachIconWomanSmall();

	@Source("file-delete-24.png")
	ImageResource deleteFile();

	@Source("file-icon_doc.png")
	ImageResource docFile();

	@Source("file-icon_pdf.png")
	ImageResource pdfFile();
	
	@Source("file-icon_unk.png")
	ImageResource unkFile();

	@Source("file-edit-24.png")
	ImageResource updateFile();

	@Source("elementNew.png")
	ImageResource createNew();

	@Source("button_cancel_32.png")
	ImageResource createCancel();

	@Source("button_ok_32.png")
	ImageResource createOk();

	@Source("right_24.png")
	ImageResource right();

	@Source("web_24.png")
	ImageResource urlLink();
	
	@Source("email-24.png")
	ImageResource emailLink();

	@Source("logout_32.png")
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

}