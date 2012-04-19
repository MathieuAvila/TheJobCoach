package com.TheJobCoach.webapp.mainpage.client;

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
	
	@Source("drapeau_francais.gif")
	ImageResource flag_FR();
	
	@Source("drapeau_anglais.gif")
	ImageResource flag_EN();

	@Source("jobcoach.gif")
	ImageResource coach_logo();
}