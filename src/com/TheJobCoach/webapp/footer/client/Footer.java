package com.TheJobCoach.webapp.footer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Footer implements EntryPoint {
	
	Panel rootPanel;

	public void setRootPanel(Panel panel)
	{
		rootPanel = panel;
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		
		//UserPage uP = new UserPage();
		//uP.onModuleLoad();
		//return;
		
		Lang lang = GWT.create(Lang.class);
		System.out.println("Footer Panel Locale is: " + LocaleInfo.getCurrentLocale().getLocaleName());				
		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		rootPanel.clear();
		rootPanel.setStyleName("footer-content");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.setSize("100%", "100%");
			
		HorizontalPanel horizontalPanel_9 = new HorizontalPanel();
		horizontalPanel_9.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_9.setStyleName("mainpage-content");
		rootPanel.add(horizontalPanel_9);
		horizontalPanel_9.setHeight("");
		
		Label labelConditions = new Label(lang._TextTermsOfUse());
		labelConditions.setStyleName("mainpage-label-clickable");
		horizontalPanel_9.add(labelConditions);
		horizontalPanel_9.setCellHorizontalAlignment(labelConditions, HasHorizontalAlignment.ALIGN_CENTER);
		
		Label label_3 = new Label("  -  ");
		label_3.setStyleName("toto");
		horizontalPanel_9.add(label_3);
		horizontalPanel_9.setCellHorizontalAlignment(label_3, HasHorizontalAlignment.ALIGN_CENTER);
		label_3.setWidth("20px");
		
		Label labelQuiSommesNous = new Label(lang._TextWhoWeAre());
		labelQuiSommesNous.setStyleName("mainpage-label-clickable");
		horizontalPanel_9.add(labelQuiSommesNous);
		
		Label label_4 = new Label("  -  ");
		label_4.setStyleName("toto");
		horizontalPanel_9.add(label_4);
		horizontalPanel_9.setCellHorizontalAlignment(label_4, HasHorizontalAlignment.ALIGN_CENTER);
		label_4.setWidth("20px");
		
		Label lblConfidentialite = new Label(lang._TextConfidentiality());
		horizontalPanel_9.add(lblConfidentialite);
		lblConfidentialite.setStyleName("mainpage-label-clickable");
		horizontalPanel_9.setCellVerticalAlignment(lblConfidentialite, HasVerticalAlignment.ALIGN_BOTTOM);
				
	}
	
}
