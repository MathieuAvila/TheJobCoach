package com.TheJobCoach.webapp.userpage.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.impl.ClippedImagePrototype;

public interface ClientImageBundle extends ClientBundle 
{

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
}