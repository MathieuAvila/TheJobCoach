package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.client.DialogBlockApplyReset.IApply;
import com.TheJobCoach.webapp.util.client.HorizontalSpacer;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
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
		
		add(new HorizontalSpacer("25px"));
		add(coachPanel);
		add(new HorizontalSpacer("25px"));
		Image bulle = new Image(ClientImageBundle.INSTANCE.bulle());
		bulle.addStyleName("img-no-border");
		bulle.addStyleName("img-no-border2");
		add(bulle);
		this.setBorderWidth(0);
		this.setCellWidth(bulle, "24px");
		//this.setC(0);
		
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
		
		add(new HorizontalSpacer("40px"));
		
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
