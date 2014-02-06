package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.util.client.ClientImageBundle;
import com.google.gwt.cell.client.IconCellDecorator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.cell.client.ClickableTextCell;

public class IconCellSingle extends IconCellDecorator<String> 
{
	static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);
	
	static ImageResource deleteIcon = wpImageBundle.deleteFile();
	static ImageResource updateIcon = wpImageBundle.updateFile();
	static ImageResource rightIcon = wpImageBundle.nextIcon();
	static ImageResource urlIcon = wpImageBundle.urlLink();
	static ImageResource addIcon = wpImageBundle.buttonAdd24();
	
	IconType type;
	
	public enum IconType { DELETE, UPDATE, RIGHT, URL, ADD };
	
	public IconCellSingle(IconType type)
	{			
		super(deleteIcon, new ClickableTextCell() {
		     @Override
		      protected void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {		        
		          sb.appendHtmlConstant(
		        		  "<div class=\"clickableText\">" + 
		        		  "<a style=\"clickableText\">");
		          //sb.append(value);
		          sb.appendHtmlConstant("&nbsp;</a></div>");		        
		      }
			}, HasVerticalAlignment.ALIGN_MIDDLE, 0);
		this.type = type;		
	}

	protected SafeHtml getIconHtml(String value) 
	{		
		SafeHtml html = SafeHtmlUtils.fromSafeConstant("");
		ImageResource ic = deleteIcon;
		switch (type)
		{
		case DELETE: ic = deleteIcon; break;
		case UPDATE: ic = updateIcon; break;
		case RIGHT:  ic = rightIcon; break;
		case URL:    ic = urlIcon; break;
		case ADD:    ic = addIcon; break;
		}
		html = SafeHtmlUtils.fromTrustedString(AbstractImagePrototype.create(ic).getHTML());

		// Create the wrapper based on the vertical alignment.
		SafeStylesBuilder cssStyles =
				new SafeStylesBuilder().appendTrustedString("left" + ":0px;");

		//int height = (int)wpImageBundle.deleteFile().getHeight();
		cssStyles.appendTrustedString("margin-top:-" + "2px;top:2px;");
		return SafeHtmlUtils.fromSafeConstant("<div style=\"" + cssStyles.toSafeStyles().asString() + "position:absolute;line-height:0px;text-decoration:underline;color:blue;a:hover{color:#001760;text-decoration: underline;}\"><a href='javascript:;'>" + html.asString() + "</a></div>");
		// return this.template.imageWrapperMiddle(cssStyles.toSafeStyles(), wpImageBundle.deleteFile());
	}
}