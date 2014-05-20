package com.TheJobCoach.webapp.util.client;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;

import java.util.Vector;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;



/**
 * A {@link Cell} used to render text. Clicking on the cell causes its
 * {@link ValueUpdater} to be called.
 */
public class IconsCell<T> extends AbstractSafeHtmlCell<T> 
{

	public interface IGetIcons<T>
	{
		public Vector<ImageResource> getShare(T element);
	}

	@SuppressWarnings("unused")
	private IGetIcons<T> getIcons;

	private static Template template;

	interface Template extends SafeHtmlTemplates {
		@Template("<div style=\"{0};top:50%;line-height:0px;\">{1}</div>")
		SafeHtml imageWrapperMiddle(SafeStyles styles, SafeHtml image);
	}

	public IconsCell(final IGetIcons<T> getIcons) 
	{
		super(new SafeHtmlRenderer<T>()
				{

			SafeHtml getImageHtml(ImageResource res) 
			{
				// Get the HTML for the image.
				SafeHtml image;			 
				AbstractImagePrototype proto = AbstractImagePrototype.create(res);
				image = SafeHtmlUtils.fromTrustedString(proto.getHTML());

				// Create the wrapper based on the vertical alignment.
				SafeStylesBuilder cssStyles =  new SafeStylesBuilder();

				//cssStyles.appendTrustedString("margin-top:-" + halfHeight + "px;" + "margin-left:-" + width + "px;");
				return template.imageWrapperMiddle(cssStyles.toSafeStyles(), image);
			}

			@Override
			public SafeHtml render(T object)
			{
				Vector<ImageResource> imgs = getIcons.getShare(object);
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<div class=\"clickableText\"><a style=\"clickableText\"><table border=0><tr>");
				for (ImageResource img: imgs)
				{
					sb.appendHtmlConstant("<td>");
					sb.append(getImageHtml(img));
					sb.appendHtmlConstant("</td>");
				}
				sb.appendHtmlConstant("</td></tr></table></a></div>");
				return sb.toSafeHtml();
			}

			@Override
			public void render(T object, SafeHtmlBuilder builder)
			{
				builder.append(render(object));
			}}, CLICK, KEYDOWN);
		this.getIcons = getIcons;
		if (template == null) 
		{
			template = GWT.create(Template.class);
		}
	}

	@Override
	protected void render(Context context,
			SafeHtml data, SafeHtmlBuilder sb)
	{
		if (data != null) 
		{
			sb.append(data);
		}
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, T value,
			NativeEvent event, ValueUpdater<T> valueUpdater) 
	{
		System.out.println("WTF ?");
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if (CLICK.equals(event.getType())) 
		{
			onEnterKeyDown(context, parent, value, event, valueUpdater);
		}
	}

	@Override
	protected void onEnterKeyDown(Context context, Element parent, T value,
			NativeEvent event, ValueUpdater<T> valueUpdater) 
	{
		if (valueUpdater != null) 
		{
			valueUpdater.update(value);
		}
	}
}