package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.client.DialogBlockApplyReset.IApply;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PanelCoach extends HorizontalPanel  implements EntryPoint, IChanged, ReturnValue, IApply {

	UserId user;

	final static Lang lang = GWT.create(Lang.class);
	
	final SimplePanel coachPanel = new SimplePanel();
	final DecoratorPanel coachAdviceDecorator = new DecoratorPanel();
	//final ScrollPanel adviceListPanel = new ScrollPanel();
	//final SimplePanel adviceListPanel = new SimplePanel();
	HTML label = new HTML("Salut, bienvenue sur TheJobCoach.fr<br/>Je suis votre coach, ensemble on va vous trouver un super job !");

	
	ClientUserValuesUtils values = null;

	public PanelCoach(Panel panel, UserId _user)
	{
		super();
		rootPanel = panel;
		user = _user;
		values = new ClientUserValuesUtils(rootPanel, user);
	}

	Panel rootPanel = null;
	
	void getValues()
	{	
		values.preloadValueList(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, this);
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{			
		setSize("100%", "");
		clear();
		
		coachPanel.setSize("150px","150px");
		//coachAdviceDecorator.setSize("100%", "100%");
		
		add(coachPanel);
		
		SimplePanel sp = new SimplePanel();
		SimplePanel sp2 = new SimplePanel();
		add(sp);
		sp.add(sp2);
		sp2.add(label);
		DOM.setStyleAttribute(sp2.getElement(), "overflow", "hidden");
		DOM.setStyleAttribute(sp.getElement(), "overflow", "hidden");
		sp.setSize("100%", "150px");
		DOM.setStyleAttribute(sp.getElement(), "position", "relative");
		DOM.setStyleAttribute(sp2.getElement(), "position", "absolute");
		
		DOM.setStyleAttribute(sp2.getElement(), "bottom", "5px");
		DOM.setStyleAttribute(sp2.getElement(), "left", "15px");
		sp.addStyleName("coachblabla");
		//add(coachAdviceDecorator);
		//add(coachAdviceDecorator);
		this.setCellWidth(coachPanel, "150px");
		//this.setCellWidth(coachAdviceDecorator, "100%");
		this.setCellWidth(sp, "100%");
		
		//coachAdviceDecorator.setWidget(label);
		
		
		//this.setCellWidth(coachAdviceDecorator, "100%");
		
		//coachAdviceDecorator.add(adviceListPanel);
		//adviceListPanel.setSize("100%", "100%");
		
		//adviceListPanel.add(label);
		//label.setSize("100%", "150px");
		//label.setText("toto oh mon toto<br/>toto<br/>toto<br/>toto<br/>toto<br/>toto<br/>toto<br/>toto<br/>toto<br/>toto<br/>toto<br/>toto<br/>toto<br/>");
		//adviceListPanel.setHeight("150px");
		//coachAdvicePanel.setHeight("150px");
		//adviceListPanel.setWidth("100%");
		//coachAdvicePanel.setWidth("100%");
		//scrollAdvicePanel.setHeight("150px");
		
		//hPanel.setCellWidth(coachAdvicePanel, "100%");
		//hPanel.setCellHeight(coachAdvicePanel, "150px");
		setWidth("100%");
		getValues();
		values.addListener(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, this);	
	}

	@Override
	public void changed(boolean ok, boolean changed, boolean init) 
	{
	}

	@Override
	public void notifyValue(boolean set, String key, String value) 
	{
		if (key.equals(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR))
		{
			coachPanel.clear();
			if (value.equals(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__DEFAULT_MAN))
			{
				coachPanel.add(new Image(ClientImageBundle.INSTANCE.coachIcon()));
			}
			else if (value.equals(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__DEFAULT_WOMAN))
			{
				coachPanel.add(new Image(ClientImageBundle.INSTANCE.coachIconWoman()));
			} else
			{
				coachPanel.add(new Image(ClientImageBundle.INSTANCE.coachIconWoman()));
			}
			/*
			else if (value.equals(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__COACH_SURFER))
			{
				coachPanel.add(new Image(ClientImageBundle.INSTANCE.coachIconSurfer());
			}
			else if (value.equals(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__COACH_MILITARY))
			{
				coachPanel.add(new Image(ClientImageBundle.INSTANCE.coachIconMilitary()));
			}*/
		}
	}

	@Override
	public void apply()
	{
	}
}
