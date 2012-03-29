package com.TheJobCoach.webapp.userpage.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;

public class ButtonImageText extends Button
{

	private String text;

	enum Type { NEW };

	Type type;

	static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);
	static ImageResource newIcon = wpImageBundle.createNew();

	public ButtonImageText(Type type, String text)
	{
		super();
		this.type = type;	
		setText(text);
		setResource(newIcon);
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
		this.text = text;
		Element span = DOM.createElement("span");
		span.setInnerText(text);
		span.setAttribute("style", "padding-left:3px; vertical-align:middle;");

		DOM.insertChild(getElement(), span, 0);
	}
}
