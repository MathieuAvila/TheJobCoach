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

	@Source("news_32.png")
	ImageResource newsContent();

	@Source("todo-32.png")
	ImageResource todoContent();

	@Source("sites-32.png")
	ImageResource userJobSiteContent();

	@Source("documents-32.png")
	ImageResource userDocumentContent();

	@Source("parameters-32.png")
	ImageResource parametersContent();

	@Source("logout_32.png")
	ImageResource urlLogout();

	@Source("comment-32.png")
	ImageResource sendComment();
	
	@Source("opportunity-32.png")
	ImageResource opportunityContent();

	@Source("log-32.png")
	ImageResource userLogContent();

	@Source("goals-32.png")
	ImageResource userVirtualCoachGoals();

	@Source("addressbook-32.png")
	ImageResource userExternalContact();

	@Source("myreports-32.png")
	ImageResource userMyReports();
}