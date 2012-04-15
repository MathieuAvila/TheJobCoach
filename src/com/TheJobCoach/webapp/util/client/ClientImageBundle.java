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
	
	@Source("button_cancel_32.png")
	ImageResource createCancel();

	@Source("button_ok_32.png")
	ImageResource createOk();
	
	@Source("elementNew.png")
	ImageResource createNew();

	@Source("question_64.png")
	ImageResource createQuestion();

	@Source("info_64.png")
	ImageResource createInfo();

	@Source("error_64.png")
	ImageResource createError();

	@Source("warning_64.png")
	ImageResource createWarning();

	@Source("wait_64.png")
	ImageResource createWait();

	@Source("left_32.png")
	ImageResource backIcon();

	@Source("email_32.png")
	ImageResource emailIcon();

}