package com.TheJobCoach.webapp.util.client;

import com.google.gwt.core.client.EntryPoint;
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
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MessageBox implements EntryPoint {

	Panel rootPanel;
	
	static final LangUtil lang = GWT.create(LangUtil.class);

	public enum TYPE { INFO, WARNING, QUESTION, ERROR, WAIT, NONE };
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
	Button buttonOk;
	Button buttonCancel;
	
	String okMsg = null;
	String cancelMsg = null;
	
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

	public MessageBox(Panel rootPanel, boolean ok, boolean cancel, TYPE type, String title, String message, ICallback callback,
			String okMsg, String cancelMsg)
	{
		this.rootPanel = rootPanel;
		this.ok = ok;
		this.cancel = cancel;
		this.type = type;
		this.title = title;
		this.message = message;
		this.callback = callback;
		this.okMsg = okMsg;
		this.cancelMsg = cancelMsg;
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
	public interface ErrorCatcher
	{
		public void errorEvent(MessageBox error, TYPE type, String title, String message);
	}	
	static private ErrorCatcher currentErrorCatcher = null;
	static public void registerErrorCatcher(ErrorCatcher catcher)	
	{
		currentErrorCatcher = catcher;
	}
	public void clickOk()
	{
		buttonOk.click();
	}
	public void clickCancel()
	{
		buttonCancel.click();
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		if (currentErrorCatcher != null)
		{
			currentErrorCatcher.errorEvent(this, type, title, message);
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
		case INFO:     imageIcon = new Image(infoIcon); break;
		case WARNING:  imageIcon = new Image(warningIcon); break;
		case QUESTION: imageIcon = new Image(questionIcon); break;
		case ERROR:    imageIcon = new Image(errorIcon); break;
		case WAIT:     imageIcon = new Image(waitIcon); break;
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

		buttonOk = new ButtonImageText(ButtonImageText.Type.OK, okMsg == null ? lang._TextOk(): okMsg);
		if (ok) innerPanel.add(buttonOk);
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

		buttonCancel = new ButtonImageText(ButtonImageText.Type.CANCEL,  cancelMsg == null ? lang._TextCancel(): cancelMsg);
		if (cancel) 
		{
			innerPanel.add(horizontalPanelSpace);
			innerPanel.add(buttonCancel);
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
		dBox.getElement().getStyle().setProperty("zIndex", "10" );
		dBox.center();
	}

	public static MessageBox messageBox(Panel rootPanel, TYPE type, String title, String message)
	{
		MessageBox mb = new MessageBox(rootPanel, true, false, type, title, message, null);
		mb.onModuleLoad();
		return mb;
	}

	public static MessageBox messageBox(Panel rootPanel, TYPE type, String message)
	{
		String title = null;
		LangUtil lang = GWT.create(LangUtil.class);
		switch (type)
		{
		case INFO: title = lang._TextINFO(); break;
		case WARNING:  title = lang._TextWARNING(); break;
		case QUESTION:  title = lang._TextQUESTION(); break;
		case ERROR:  title = lang._TextERROR(); break;
		case WAIT:  title = lang._TextWAIT(); break;
		case NONE: title = lang._TextNONE(); break;
		}
		MessageBox mb = new MessageBox(rootPanel, true, false, type, title, message, null);
		mb.onModuleLoad();
		return mb;
	}

	public static void messageBoxException(Panel rootPanel, String exception)
	{
		messageBox(rootPanel, TYPE.ERROR, "Unexpected system error.", "An unexpected system error occured. Please try again later. Details: " + exception);		
	}

	public static void messageBoxException(Panel rootPanel, Throwable exception)
	{
		messageBox(rootPanel, TYPE.ERROR, "Unexpected system error.", "An unexpected system error occured. Please try again later. Details: " + exception);		
	}

	public static void messageBoxException(Panel rootPanel, Exception exception)
	{
		messageBoxException(rootPanel, exception.toString());		
	}


	public static MessageBox messageBox(TYPE type, String title, String message)
	{
		return messageBox(RootPanel.get(), type, title, message);
	}

	public static MessageBox messageBox(TYPE type, String message)
	{
		return messageBox(RootPanel.get(), type, message);
	}

	public static void messageBoxException(String exception)
	{
		messageBoxException(RootPanel.get(), exception);
	}

	public static void messageBoxException(Throwable exception)
	{
		messageBoxException(RootPanel.get(), exception);
	}

	public static void messageBoxException(Exception exception)
	{
		messageBoxException(RootPanel.get(), exception.toString());		
	}
}
