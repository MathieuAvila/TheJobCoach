package com.TheJobCoach.webapp.util.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MessageBox implements EntryPoint {
	
	Panel rootPanel;

	public enum TYPE { INFO, WARNING, QUESTION, ERROR, WAIT };
	public interface ICallback
	{
		public void complete(boolean ok);
	}
	
	boolean ok = true;
	boolean cancel = true;
	TYPE type = TYPE.WAIT;
	String title;
	String message;
	ICallback callback;
	
	public MessageBox(Panel rootPanel, boolean ok, boolean cancel, TYPE type, String title, String message, ICallback callback)
	{
		this.rootPanel = rootPanel;
		this.ok = ok;
		this.cancel = cancel;
		this.type = type;
		this.title = title;
		this.message = message;
		this.callback = callback;
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
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		Lang lang = GWT.create(Lang.class);
		
		dBox.setText(title);
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);
		
		FlexTable flexTable = new FlexTable();
		dBox.setWidget(flexTable);
		flexTable.setSize("100%", "100%");
		
		HorizontalPanel horizontalPanelSpaceUp = new HorizontalPanel();
		horizontalPanelSpaceUp.setHeight("10px");
		horizontalPanelSpaceUp.setWidth("100px");
		flexTable.setWidget(0, 0, horizontalPanelSpaceUp);
		
		Image imageIcon = new Image(infoIcon);
		switch (type)
		{
		case INFO:     imageIcon = new Image(infoIcon); break;
		case WARNING:  imageIcon = new Image(warningIcon); break;
		case QUESTION: imageIcon = new Image(questionIcon); break;
		case ERROR:    imageIcon = new Image(errorIcon); break;
		case WAIT:     imageIcon = new Image(waitIcon); break;
		}
		
		flexTable.setWidget(1, 0, imageIcon);
		
		HorizontalPanel horizontalPanelSpaceText = new HorizontalPanel();
		horizontalPanelSpaceText.setWidth("10px");
		flexTable.setWidget(1, 1, horizontalPanelSpaceText);
		
		HTML htmlMessage = new HTML(message, true);
		flexTable.setWidget(1, 2, htmlMessage);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		flexTable.setWidget(2, 2, horizontalPanel);
		
		Button buttonOk = new ButtonImageText(ButtonImageText.Type.OK, lang._TextOk());
		if (ok) horizontalPanel.add(buttonOk);
		buttonOk.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event)
			{
				dBox.hide();
				if (callback != null)
					callback.complete(true);				
			}
		});
		HorizontalPanel horizontalPanelSpace = new HorizontalPanel();
		horizontalPanelSpace.setWidth("10px");
		
		Button buttonCancel = new ButtonImageText(ButtonImageText.Type.CANCEL, lang._TextCancel());
		if (cancel) 
			{
			horizontalPanel.add(horizontalPanelSpace);
			horizontalPanel.add(buttonCancel);
			}
		
		buttonCancel.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event)
			{
				dBox.hide();
				if (callback != null)
					callback.complete(false);				
			}
		});
		rootPanel.add(dBox);
		dBox.center();		
	}
	
}
