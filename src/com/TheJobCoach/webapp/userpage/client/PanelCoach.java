package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.client.DialogBlockApplyReset.IApply;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PanelCoach implements EntryPoint, IChanged, ReturnValue, IApply {

	UserId user;

	final static Lang lang = GWT.create(Lang.class);
	
	final Panel coachPanel = new SimplePanel();
	final SimplePanel scrollAdvicePanel = new SimplePanel();
	final DecoratorPanel coachAdvicePanel = new DecoratorPanel();
	final Panel adviceListPanel = new VerticalPanel();
	
	ClientUserValuesUtils values = null;

	public PanelCoach(Panel panel, UserId _user)
	{
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
		rootPanel.setSize("100%", "");
		rootPanel.clear();
		
		HorizontalPanel hPanel = new HorizontalPanel();
		rootPanel.add(hPanel);
				
		hPanel.add(coachPanel);
		hPanel.add(coachAdvicePanel);
		
		coachAdvicePanel.add(scrollAdvicePanel);		
		scrollAdvicePanel.add(adviceListPanel);
		
		//adviceListPanel.setHeight("200px");
		//coachAdvicePanel.setHeight("200px");
		//adviceListPanel.setWidth("100%");
		//coachAdvicePanel.setWidth("100%");
		//scrollAdvicePanel.setHeight("200px");
		
		//hPanel.setCellWidth(coachAdvicePanel, "100%");
		//hPanel.setCellHeight(coachAdvicePanel, "200px");
		hPanel.setWidth("100%");
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
		System.out.println("Coach " + key + " " + value);
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
			}
			else if (value.equals(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__COACH_SURFER))
			{
				coachPanel.add(new Image(ClientImageBundle.INSTANCE.coachIconSurfer()));
			}
			else if (value.equals(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__COACH_MILITARY))
			{
				coachPanel.add(new Image(ClientImageBundle.INSTANCE.coachIconMilitary()));
			}
		}
	}

	@Override
	public void apply()
	{
	}
}
