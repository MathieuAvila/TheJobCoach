package com.TheJobCoach.webapp.userpage.client.Coach;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.client.DialogBlockApplyReset.IApply;
import com.TheJobCoach.webapp.util.client.HorizontalSpacer;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
import com.google.gwt.core.client.GWT;
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
		values = new ClientUserValuesUtils(rootPanel, user);
		pipe = new MessagePipe(_user, panel);
		init();
	}

	Panel rootPanel = null;
	
	void getValues()
	{	
		values.preloadValueList(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, this);
	}

	private void init()
	{			
		setSize("100%", "");
		clear();
		
		coachPanel.setSize("150px","150px");
		
		add(new HorizontalSpacer("10px"));
		add(coachPanel);
		//add(new HorizontalSpacer("25px"));
		Image bulle = new Image(ClientImageBundle.INSTANCE.bulle());
		bulle.addStyleName("img-no-border");
		bulle.addStyleName("img-no-border2");
		add(bulle);
		this.setBorderWidth(0);
		this.setCellWidth(bulle, "24px");
		
		SimplePanel sp = new SimplePanel();
		add(sp);
		sp.add(sp2);
		sp2.add(label);
		label.setHTML("<br/>");
		label.setStyleName("coachblablacontent");
		
		DOM.setStyleAttribute(sp2.getElement(), "overflow", "hidden");
		DOM.setStyleAttribute(sp.getElement(), "overflow", "hidden");
		sp.setSize("100%", "150px");
		DOM.setStyleAttribute(sp.getElement(), "position", "relative");
		DOM.setStyleAttribute(sp2.getElement(), "position", "absolute");
		
		DOM.setStyleAttribute(sp2.getElement(), "bottom", "5px");
		DOM.setStyleAttribute(sp2.getElement(), "left", "15px");
		sp.addStyleName("coachblabla");
		this.setCellWidth(coachPanel, "150px");
		this.setCellWidth(sp, "100%");
		
		add(new HorizontalSpacer("40px"));
		
		setWidth("100%");
		getValues();
		values.addListener(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, this);
		timer.scheduleRepeating(100);
		
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
		}
	}

	@Override
	public void apply()
	{
	}
}
