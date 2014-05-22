package com.TheJobCoach.webapp.userpage.client.Connection;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;

import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
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
public class IconCellShares extends AbstractSafeHtmlCell<ContactInformation> 
{

	public interface IGetShare
	{
		public ContactInformation.Visibility getShare();
	}

	static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);

	static ImageResource documentIcon = wpImageBundle.userDocumentContent_menu();
	static ImageResource opportunityIcon = wpImageBundle.opportunityContent_menu();
	static ImageResource addressIcon = wpImageBundle.userExternalContactContent_menu();
	static ImageResource logIcon = wpImageBundle.userLogContent_menu();

	@SuppressWarnings("unused")
	private IGetShare getShare;

	private static Template template;

	interface Template extends SafeHtmlTemplates {
		@Template("<div style=\"{0}position:absolute;top:50%;line-height:0px;\">{1}</div>")
		SafeHtml imageWrapperMiddle(SafeStyles styles, SafeHtml image);
	}

	public IconCellShares(final IGetShare getShare) 
	{

		this(new SafeHtmlRenderer<ContactInformation>()
				{

			SafeHtml getImageHtml(ImageResource res) 
			{
				// Get the HTML for the image.
				SafeHtml image;			 
				AbstractImagePrototype proto = AbstractImagePrototype.create(res);
				image = SafeHtmlUtils.fromTrustedString(proto.getHTML());

				// Create the wrapper based on the vertical alignment.
				SafeStylesBuilder cssStyles =  new SafeStylesBuilder();

				int halfHeight = (int) Math.round(res.getHeight() / 2.0);
				cssStyles.appendTrustedString("margin-top:-" + halfHeight + "px;");
				return template.imageWrapperMiddle(cssStyles.toSafeStyles(), image);
			}

			@Override
			public SafeHtml render(ContactInformation object)
			{
				ContactInformation.Visibility vis = getShare.getShare();
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				if (vis.contact)
					sb.append(getImageHtml(addressIcon));
				if (vis.document)
					sb.append(getImageHtml(documentIcon));
				if (vis.opportunity)
					sb.append(getImageHtml(opportunityIcon));
				if (vis.log)
					sb.append(getImageHtml(logIcon));
				return sb.toSafeHtml();
			}

			@Override
			public void render(ContactInformation object, SafeHtmlBuilder builder)
			{
				builder.append(render(object));
			}});
		this.getShare = getShare;
	}

	public IconCellShares(SafeHtmlRenderer<ContactInformation> renderer) {
		super(renderer, CLICK, KEYDOWN);
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
	public void onBrowserEvent(Context context, Element parent, ContactInformation value,
			NativeEvent event, ValueUpdater<ContactInformation> valueUpdater) 
	{
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if (CLICK.equals(event.getType())) 
		{
			onEnterKeyDown(context, parent, value, event, valueUpdater);
		}
	}

	@Override
	protected void onEnterKeyDown(Context context, Element parent, ContactInformation value,
			NativeEvent event, ValueUpdater<ContactInformation> valueUpdater) 
	{
		if (valueUpdater != null) 
		{
			valueUpdater.update(value);
		}
	}
}