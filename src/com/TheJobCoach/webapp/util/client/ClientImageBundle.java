package com.TheJobCoach.webapp.util.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ClientImageBundle extends ClientBundle 
{

	/**
	 * Would match the file 'open_file_icon.gif' located in the same
	 * package as this type.
	 * 
	 * @gwt.resource deleteFile.gif
	 */
	//public ClippedImagePrototype deleteFile();	
	
	@Source("images/button_cancel_24.png")
	ImageResource createCancel();

	@Source("images/button_ok_24.gif")
	ImageResource createOk();
	
	@Source("images/new_24.png")
	ImageResource createNew();

	@Source("images/button_add_24.png")
	ImageResource buttonAdd24();

	@Source("images/button_add_16.png")
	ImageResource buttonAdd16();

	@Source("images/question_64.png")
	ImageResource createQuestion();

	@Source("images/info_64.png")
	ImageResource createInfo();

	@Source("images/error_64.png")
	ImageResource createError();

	@Source("images/warning_64.png")
	ImageResource createWarning();

	@Source("images/wait_64.png")
	ImageResource createWait();

	@Source("images/left_24.png")
	ImageResource backIcon();

	@Source("images/right_24.png")
	ImageResource nextIcon();

	@Source("images/email_32.png")
	ImageResource emailIcon();

	@Source("images/failure_24.png")
	ImageResource failure_24();

	@Source("images/success_24.png")
	ImageResource success_24();

	@Source("images/unknown_24.png")
	ImageResource unknown_24();
}