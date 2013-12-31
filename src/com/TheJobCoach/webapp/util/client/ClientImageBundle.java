package com.TheJobCoach.webapp.util.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;

public interface ClientImageBundle extends ClientBundle 
{
	@Source("images/button_cancel_24.png")
	ImageResource createCancel();

	@Source("images/button_ok_24.png")
	ImageResource createOk();
	
	@Source("images/new_24.png")
	ImageResource createNew();

	@Source("images/button_add_24.png")
	ImageResource buttonAdd24();

	@Source("images/button_add_16.png")
	ImageResource buttonAdd16();

	@Source("images/question_48.png")
	ImageResource createQuestion();

	@Source("images/info_48.png")
	ImageResource createInfo();

	@Source("images/error_48.png")
	ImageResource createError();

	@Source("images/warning_48.png")
	ImageResource createWarning();

	@Source("images/wait_48.png")
	ImageResource createWait();

	@Source("images/left_24.png")
	ImageResource backIcon();

	@Source("images/right_24.png")
	ImageResource nextIcon();

	@Source("images/mail_32.png")
	ImageResource emailIcon();

	@Source("images/failure_24.png")
	ImageResource failure_24();

	@Source("images/success_24.png")
	ImageResource success_24();

	@Source("images/unknown_24.png")
	ImageResource unknown_24();
	
	@Source("images/delete_file_24.png")
	ImageResource deleteFile();
	
	@Source("images/update_file_24.png")
	ImageResource updateFile();
	
	//@Source("images/left_file_24.png")
	//ImageResource wpImageBundle();

	@Source("images/web_24.png")
	ImageResource urlLink();
	

	@Source("images/file-icon-doc-24.png")
	ImageResource docFile();

	@Source("images/file-icon-pdf-24.png")
	ImageResource pdfFile();
	
	@Source("images/file-icon-unk-24.png")
	ImageResource unkFile();

	

	@Source("images/email_24.png")
	ImageResource emailLink24();


	
}