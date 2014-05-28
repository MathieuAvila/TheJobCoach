package com.TheJobCoach.webapp.util.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MessageBoxTriState {

	Panel rootPanel;
	
	static final LangUtil lang = GWT.create(LangUtil.class);

	public enum TYPE { WARNING, QUESTION };
	public interface ICallback
	{
		public void complete(int choice);
	}

	TYPE type = TYPE.QUESTION;
	String title;
	String message;
	ICallback callback;
	Button button1;
	Button button2;
	Button buttonCancel;
	
	public MessageBoxTriState(Panel rootPanel, TYPE type, String title, String message, Button button1, Button button2, ICallback callback)
	{
		this.rootPanel = rootPanel;
		this.button1 = button1;
		this.button2 = button2;
		this.type = type;
		this.title = title;
		this.message = message;
		this.callback = callback;
		init();
	}

	static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);
	static ImageResource infoIcon = wpImageBundle.createInfo();
	static ImageResource warningIcon = wpImageBundle.createWarning();
	static ImageResource questionIcon = wpImageBundle.createQuestion();
	static ImageResource errorIcon = wpImageBundle.createError();
	static ImageResource waitIcon = wpImageBundle.createWait();

	final DialogBox dBox = new DialogBox();

	public void close()
	{
		dBox.hide();
	}

	/* For UT purposes only. 
	 * This is used to redirect errors to a specific handler. 
	 * It shouldn't be used in production code. */
	public interface Catcher
	{
		public void event(MessageBoxTriState error, TYPE type, String title, String message);
	}	
	static private Catcher currentCatcher = null;
	static public void registerErrorCatcher(Catcher catcher)	
	{
		currentCatcher = catcher;
	}

	void init()
	{
		if (currentCatcher != null)
		{
			currentCatcher.event(this, type, title, message);
		}
		
		dBox.setText(title);
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);
		dBox.setModal(true);
        
		FlexTable flexTable = new FlexTable();
		dBox.setWidget(flexTable);
		flexTable.setSize("100%", "100%");

		HorizontalPanel horizontalPanelSpaceUp = new HorizontalPanel();
		horizontalPanelSpaceUp.setHeight("10px");
		horizontalPanelSpaceUp.setWidth("100px");
		flexTable.setWidget(0, 0, horizontalPanelSpaceUp);

		Image imageIcon = null;
		switch (type)
		{
		case WARNING:  imageIcon = new Image(warningIcon); break;
		case QUESTION: imageIcon = new Image(questionIcon); break;
		default:
			break;
		}

		if (imageIcon != null) 
		{
			flexTable.setWidget(1, 0, imageIcon);
		}
		else
		{
			horizontalPanelSpaceUp.setWidth("0px");
		}

		HorizontalPanel horizontalPanelSpaceText = new HorizontalPanel();
		horizontalPanelSpaceText.setWidth("10px");
		flexTable.setWidget(1, 1, horizontalPanelSpaceText);

		HTML htmlMessage = new HTML(message, true);
		flexTable.setWidget(1, 2, htmlMessage);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel.setSize("100%", "100%");
		HorizontalPanel innerPanel = new HorizontalPanel();
		innerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel.add(innerPanel);
		flexTable.setWidget(2, 2, horizontalPanel);

		button1.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event)
			{
				dBox.hide();
				if (callback != null)
					callback.complete(0);				
			}
		});
		button2.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event)
			{
				dBox.hide();
				if (callback != null)
					callback.complete(1);				
			}
		});

		buttonCancel = new ButtonImageText(ButtonImageText.Type.CANCEL,  lang._TextCancel());
		
		innerPanel.add(button1);
		innerPanel.add(new HorizontalSpacer("1em"));
		innerPanel.add(button2);
		innerPanel.add(new HorizontalSpacer("10em"));
		innerPanel.add(buttonCancel);

		buttonCancel.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event)
			{
				dBox.hide();
				if (callback != null)
					callback.complete(-1);				
			}
		});
		dBox.getElement().getStyle().setProperty("zIndex", "10" );
		dBox.center();
	}

	// for UT purposes
	public void clickChoice(int num)
	{
		switch(num)
		{
		case 0: button1.click();break;
		case 1: button2.click();break;
		}
	}
}
