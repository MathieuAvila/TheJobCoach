package com.TheJobCoach.webapp.footer.client;

import com.TheJobCoach.webapp.util.client.HorizontalSpacer;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.VerticalSpacer;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Footer extends VerticalPanel implements EntryPoint {

	Panel rootPanel;
	final static Lang lang = GWT.create(Lang.class);		

	public Footer(Panel panel)
	{
		rootPanel = panel;
	}
	
	class ClickMb implements ClickHandler
	{
		String url;
		String title;
		public ClickMb(String title, String url)
		{
			this.url = url;
			this.title = title;
		}
		public void onClick(ClickEvent event) 
		{
			MessageBox mb = new MessageBox(rootPanel, true, false, MessageBox.TYPE.NONE, title, "<iframe style=\"width:500px; height:500px;\" src=\"static/" + url + "\"></iframe>", null);
			mb.onModuleLoad();
		}
	};

	static Footer commonFooter = new Footer(null);
	
	static {
		commonFooter.staticLoad();
	}
	
	public void staticLoad()
	{
		setStyleName("footer-content");
		getElement().getStyle().setPosition(Position.RELATIVE);
		setSize("100%", "100%");

		HorizontalPanel horizontalPanel_9 = new HorizontalPanel();
		horizontalPanel_9.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_9.setStyleName("mainpage-content");
		add(horizontalPanel_9);
		horizontalPanel_9.setHeight("");

		Label labelConditions = new Label(lang._TextTermsOfUse());
		labelConditions.setStyleName("mainpage-label-clickable");
		horizontalPanel_9.add(labelConditions);
		horizontalPanel_9.setCellHorizontalAlignment(labelConditions, HasHorizontalAlignment.ALIGN_CENTER);

		Label label_3 = new Label("  -  ");
		horizontalPanel_9.add(label_3);
		horizontalPanel_9.setCellHorizontalAlignment(label_3, HasHorizontalAlignment.ALIGN_CENTER);
		label_3.setWidth("20px");

		Label labelQuiSommesNous = new Label(lang._TextWhoWeAre());
		labelQuiSommesNous.setStyleName("mainpage-label-clickable");
		horizontalPanel_9.add(labelQuiSommesNous);

		Label label_4 = new Label("  -  ");
		horizontalPanel_9.add(label_4);
		horizontalPanel_9.setCellHorizontalAlignment(label_4, HasHorizontalAlignment.ALIGN_CENTER);
		label_4.setWidth("20px");

		Label lblConfidentialite = new Label(lang._TextConfidentiality());
		horizontalPanel_9.add(lblConfidentialite);
		lblConfidentialite.setStyleName("mainpage-label-clickable");
		horizontalPanel_9.setCellVerticalAlignment(lblConfidentialite, HasVerticalAlignment.ALIGN_BOTTOM);

		// Add a handler to Who We Are
		labelQuiSommesNous.addClickHandler(new ClickMb(lang._TextWhoWeAre(), "whoweare_fr.html"));
		
		// Add a handler to Confidentiality
		lblConfidentialite.addClickHandler(new ClickMb(lang._TextConfidentiality(), "confidentiality_fr.html"));

		// Add a handler to Conditions
		labelConditions.addClickHandler(new ClickMb(lang._TextTermsOfUse(), "termsofuse_fr.html"));
		
		add(new VerticalSpacer("20px"));
		
		HorizontalPanel horizontalPanel_10 = new HorizontalPanel();
		horizontalPanel_10.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_10.setStyleName("mainpage-content");
		add(horizontalPanel_10);
		
		// Add a GooglePlus button
		HTML googlePlus = new HTML("<div class=\"g-plusone\" data-annotation=\"inline\" data-width=\"120\" data-href=\"https://www.thejobcoach.fr\"></div>");
		horizontalPanel_10.add(googlePlus);
		horizontalPanel_10.add(new HorizontalSpacer("40px"));
		
	    Document doc = Document.get();
	    ScriptElement script = doc.createScriptElement();
	    script.setSrc("https://apis.google.com/js/plusone.js");
	    script.setType("text/javascript");
	    script.setLang("javascript");
	    this.getElement().appendChild(script);
	    
	    // Add a LinkedIn button
	    HTML linkedIn = new HTML("<script type=\"IN/Share\" data-url=\"https://www.thejobcoach.fr\" data-counter=\"right\"></script>");
		horizontalPanel_10.add(linkedIn);
		horizontalPanel_10.add(new HorizontalSpacer("40px"));
		
		ScriptElement scriptLInkedIn = doc.createScriptElement();
	    scriptLInkedIn.setSrc("//platform.linkedin.com/in.js");
	    scriptLInkedIn.setType("text/javascript");
	    scriptLInkedIn.setLang("javascript");
	    this.getElement().appendChild(scriptLInkedIn);
	    
	    // Add a Facebook button
	    HTML facebook = new HTML("<div id=\"fb-root\"></div><div class=\"fb-like\" data-href=\"https://www.thejobcoach.fr\" data-layout=\"button_count\" data-action=\"recommend\" data-show-faces=\"true\" data-share=\"true\"></div>");
		horizontalPanel_10.add(facebook);
		horizontalPanel_10.add(new HorizontalSpacer("40px"));
	
		ScriptElement scriptFacebook = doc.createScriptElement();
		scriptFacebook.setSrc("//connect.facebook.net/fr_FR/all.js#xfbml=1");
		scriptFacebook.setType("text/javascript");
		scriptFacebook.setLang("javascript");
		this.getElement().appendChild(scriptFacebook);
		
		System.out.println(scriptFacebook.toString());
		System.out.println(facebook.toString());
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		this.add(commonFooter);
	}

}
