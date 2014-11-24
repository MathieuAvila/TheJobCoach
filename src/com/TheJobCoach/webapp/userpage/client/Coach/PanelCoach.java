package com.TheJobCoach.webapp.userpage.client.Coach;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.client.DialogBlockApplyReset.IApply;
import com.TheJobCoach.webapp.util.client.HorizontalSpacer;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstants;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsPreferences;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelCoach extends HorizontalPanel  implements IChanged, ReturnValue, IApply {

	UserId user;

	MessagePipe pipe;

	final static Lang lang = GWT.create(Lang.class);

	final SimplePanel coachPanel = new SimplePanel();
	final DecoratorPanel coachAdviceDecorator = new DecoratorPanel();
	HTML label = new HTML("");
	Image bulle = new Image(ClientImageBundle.INSTANCE.bulle());
	
	VerticalPanel sp2 = new VerticalPanel();

	ClientUserValuesUtils values = null;

	int appendCompleted = 0;
	int totalSize = 0;
	String currentMessage;

	Timer timer = new Timer() 
	{
		public void run() 
		{
			String newMessage = null;
			if (appendCompleted != 0)
			{
				appendCompleted += 10;
				timer.scheduleRepeating(100);
				if (appendCompleted <= 100)
				{
					DOM.setStyleAttribute(sp2.getElement(), "bottom", ((float)(-totalSize * (100.0 - appendCompleted)) / 100.0)  + "px");
				}
				else if (appendCompleted > 100)
				{
					appendCompleted = 0;
				}
			}
			else if ((newMessage = pipe.getMessage()) != null)
			{
				// Get the diff in size.
				int orgSize = label.getOffsetHeight();
				currentMessage = newMessage;
				label.setHTML(label.getHTML() + "<br/>" + currentMessage);
				totalSize = label.getOffsetHeight() - orgSize;
				timer.scheduleRepeating(100);
				DOM.setStyleAttribute(sp2.getElement(), "bottom", ((float)(-totalSize)  + "px"));
				appendCompleted += 10;
			}
			else
			{
				timer.scheduleRepeating(1000);
			}
		}
	};

	public PanelCoach(Panel panel, UserId _user)
	{
		super();
		rootPanel = panel;
		user = _user;
		values = ClientUserValuesUtils.getInstance(user);
		pipe = MessagePipe.getMessagePipe(_user, panel);
		init();
	}

	Panel rootPanel = null;

	void getValues()
	{	
		values.preloadValueList(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, this);
	}

	boolean isSqueeze = false;
	String coach = UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__DEFAULT_MAN;
	SimplePanel sp = new SimplePanel();
	
	Image imgWoman = new Image(ClientImageBundle.INSTANCE.coachIconWoman());
	Image imgMan = new Image(ClientImageBundle.INSTANCE.coachIcon());
	Image imgPlus = new Image(ClientImageBundle.INSTANCE.max_24());
	Image imgLess = new Image(ClientImageBundle.INSTANCE.min_24());
	
	VerticalPanel vpBulle = new VerticalPanel();
	
	private void update_isSqueeze(boolean squeeze)
	{
		isSqueeze = squeeze;
		System.out.println("update_isSqueeze: " + squeeze);
		coachPanel.clear();
		
		if (squeeze)
		{
			coachPanel.setSize("30px","30px");
			this.setCellWidth(coachPanel, "30px");
			vpBulle.setVisible(false);
			sp.setSize("100%", "30px");
			coachPanel.add(imgPlus);
		}
		else
		{
			Image img = null;
			if (coach.equals(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__DEFAULT_MAN))
			{
				img = imgMan;
			}
			else if (coach.equals(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__DEFAULT_WOMAN))
			{
				img = imgWoman;
			}
			coachPanel.setSize("150px","150px");
			this.setCellWidth(coachPanel, "150px");
			vpBulle.setVisible(true);
			coachPanel.add(img);
			sp.setSize("100%", "150px");
		}
	}

	private void init()
	{			
		setSize("100%", "");
		clear();

		add(new HorizontalSpacer("10px"));
		add(coachPanel);
		bulle.addStyleName("img-no-border");
		bulle.addStyleName("img-no-border2");
		add(vpBulle);
		
		vpBulle.add(imgLess);
		vpBulle.add(bulle);
	
		imgLess.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				update_isSqueeze(true);
				values.setValue(UserValuesConstantsPreferences.PREF_COACH_SQUEEZE, UserValuesConstants.YES);
			}
		});
		imgPlus.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				update_isSqueeze(false);
				values.setValue(UserValuesConstantsPreferences.PREF_COACH_SQUEEZE, UserValuesConstants.NO);
			}
		});
		
		this.setBorderWidth(0);
		this.setCellWidth(bulle, "24px");

		add(sp);
		sp.add(sp2);
		sp2.add(label);
		label.setHTML("<br/>");
		label.setStyleName("coachblablacontent");

		DOM.setStyleAttribute(sp2.getElement(), "overflow", "hidden");
		DOM.setStyleAttribute(sp.getElement(), "overflow", "hidden");
		DOM.setStyleAttribute(sp.getElement(), "position", "relative");
		DOM.setStyleAttribute(sp2.getElement(), "position", "absolute");

		DOM.setStyleAttribute(sp2.getElement(), "bottom", "5px");
		DOM.setStyleAttribute(sp2.getElement(), "left", "15px");
		sp.addStyleName("coachblabla");
		this.setCellWidth(coachPanel, "150px");
		this.setCellWidth(sp, "100%");

		add(new HorizontalSpacer("40px"));
		
		update_isSqueeze(isSqueeze);
		
		setWidth("100%");
		getValues();
		values.addListener(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, this);
		values.preloadValueList(UserValuesConstantsPreferences.PREF_COACH_SQUEEZE, this);
		timer.scheduleRepeating(100);

	}

	@Override
	public void changed(boolean ok, boolean changed, boolean init) 
	{
	}

	@Override
	public void notifyValue(boolean set, String key, String value) 
	{
		System.out.println(key);
		if (key.equals(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR))
		{
			coach = value;
			update_isSqueeze(isSqueeze);
		} 
		else if (key.equals(UserValuesConstantsPreferences.PREF_COACH_SQUEEZE))
		{
			update_isSqueeze(value.equals(UserValuesConstants.YES));
		}
	}

	@Override
	public void apply()
	{
	}
}
