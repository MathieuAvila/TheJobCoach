package com.TheJobCoach.webapp.util.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

public class ContentHelper {

	public static void insertTitlePanel(Panel rootPanel, String text, ImageResource img)
	{
		HorizontalPanel panelTitle = new HorizontalPanel();
		rootPanel.add(panelTitle);
		Image imageLogout = new Image(img);
		imageLogout.setWidth(String.valueOf(imageLogout.getWidth()) + "px");
		panelTitle.add(imageLogout);
		
		HorizontalPanel panelSpace = new HorizontalPanel();
		panelSpace.setWidth("20px");
		panelTitle.add(panelSpace);
		
		Label labelTitle = new Label(text);
		labelTitle.setStyleName("label-content");		
		
		panelTitle.add(labelTitle);
		panelTitle.setCellVerticalAlignment(labelTitle, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
}
