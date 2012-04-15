package com.TheJobCoach.webapp.util.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;

public class ButtonImageText extends Button
{	
	public enum Type { NEW, OK, CANCEL, BACK, MAIL };

	Type type;

	static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);
	static ImageResource newIcon = wpImageBundle.createNew();
	static ImageResource okIcon = wpImageBundle.createOk();
	static ImageResource cancelIcon = wpImageBundle.createCancel();
	static ImageResource backIcon = wpImageBundle.backIcon();
	static ImageResource mailIcon = wpImageBundle.emailIcon();

	public ButtonImageText(Type type, String text)
	{
		super();
		this.type = type;	
		setText(text);
		switch (type)
		{
		case OK: setResource(okIcon); break;
		case NEW: setResource(newIcon); break;
		case CANCEL: setResource(cancelIcon); break;
		case BACK: setResource(backIcon); break;
		case MAIL: setResource(mailIcon); break;
		}
	}

	public void setResource(ImageResource imageResource)
	{
		Image img = new Image(imageResource);
		String definedStyles = img.getElement().getAttribute("style");
		img.getElement().setAttribute("style", definedStyles + "; vertical-align:middle;");
		DOM.insertBefore(getElement(), img.getElement(), DOM.getFirstChild(getElement()));
	}

	@Override
	public void setText(String text)
	{
		Element span = DOM.createElement("span");
		span.setInnerText(text);
		span.setAttribute("style", "padding-left:10px; vertical-align:middle;");

		DOM.insertChild(getElement(), span, 0);
	}
}
