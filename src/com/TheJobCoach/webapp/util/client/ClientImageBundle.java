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
	
	@Source("button_cancel_24.png")
	ImageResource createCancel();

	@Source("button_ok_24.gif")
	ImageResource createOk();
	
	@Source("new_24.png")
	ImageResource createNew();

	@Source("button_add_24.png")
	ImageResource buttonAdd24();

	@Source("button_add_16.png")
	ImageResource buttonAdd16();

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

	@Source("left_24.png")
	ImageResource backIcon();

	@Source("right_24.png")
	ImageResource nextIcon();

	@Source("email_32.png")
	ImageResource emailIcon();

	@Source("failure_24.png")
	ImageResource failure_24();

	@Source("success_24.png")
	ImageResource success_24();

	@Source("unknown_24.png")
	ImageResource unknown_24();
}