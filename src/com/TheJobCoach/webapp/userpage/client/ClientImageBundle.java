package com.TheJobCoach.webapp.userpage.client;

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

	@Source("sites-32.png")
	ImageResource userJobSiteContent();

	@Source("documents-32.png")
	ImageResource userDocumentContent();

	@Source("parameters_64.png")
	ImageResource parametersContent();

	@Source("logout_32.png")
	ImageResource urlLogout();

	@Source("comment-32.png")
	ImageResource sendComment();
	
	@Source("opportunity-32.png")
	ImageResource opportunityContent();

	@Source("log-32.png")
	ImageResource userLogContent();

	@Source("log-32.png")
	ImageResource userVirtualCoachGoals();
}