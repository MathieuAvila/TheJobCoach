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
	//public ClippedImagePrototype deleteFile();	
	
	@Source("file_delete.png")
	ImageResource deleteFile();

	@Source("file-icon_doc.gif")
	ImageResource docFile();

	@Source("file-icon_pdf.png")
	ImageResource pdfFile();
	
	@Source("file-icon_unk.png")
	ImageResource unkFile();

	@Source("file_edit.png")
	ImageResource updateFile();

	@Source("elementNew.png")
	ImageResource createNew();

	@Source("button_cancel_32.png")
	ImageResource createCancel();

	@Source("button_ok_32.png")
	ImageResource createOk();

	@Source("right_32.png")
	ImageResource right();

	@Source("web_32.png")
	ImageResource urlLink();

	@Source("logout_32.png")
	ImageResource urlLogout();

	@Source("comment_64.jpeg")
	ImageResource sendComment();
}