package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

import java.util.Set;

/**
 * A {@link Cell} decorator that adds an icon to another {@link Cell}.
 * 
 * @param <C> the type that this Cell represents
 */
public class IconCellUrl implements Cell<String> {

  interface Template extends SafeHtmlTemplates {
    @Template("<div style=\"position:relative;padding-{0}:{1}px;zoom:1;\">{2}<div>{3}</div></div>")
    SafeHtml outerDiv(String direction, int width, SafeHtml icon,
        SafeHtml cellContents);

    /**
     * The wrapper around the image vertically aligned to the bottom.
     */
    @Template("<div style=\"position:absolute;{0}:0px;bottom:0px;line-height:0px;\">{1}</div>")
    SafeHtml imageWrapperBottom(String direction, SafeHtml image);

    /**
     * The wrapper around the image vertically aligned to the middle.
     */
    @Template("<div style=\"position:absolute;{0}:0px;top:50%;line-height:0px;"
        + "margin-top:-{1}px;\">{2}</div>")
    SafeHtml imageWrapperMiddle(String direction, int halfHeight, SafeHtml image);

    /**
     * The wrapper around the image vertically aligned to the top.
     */
    @Template("<div style=\"position:absolute;{0}:0px;top:0px;line-height:0px;\">{1}</div>")
    SafeHtml imageWrapperTop(String direction, SafeHtml image);
  }
  
  static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);
  static ImageResource res = wpImageBundle.urlLink();
  
  /**
   * The default spacing between the icon and the text in pixels.
   */
  private static final int DEFAULT_SPACING = 6;

  private static Template template;

  private final Cell<String> cell;

  private final String direction = LocaleInfo.getCurrentLocale().isRTL()
      ? "right" : "left";

  private final int imageWidth;

  private final SafeHtml placeHolderHtml;

  /**
   * Construct a new {@link IconCellDecorator}. The icon and the content will be
   * middle aligned by default.
   * 
   * @param icon the icon to use
   * @param cell the cell to decorate
   */
  public IconCellUrl(Cell<String> cell) {
    this(cell, HasVerticalAlignment.ALIGN_MIDDLE, DEFAULT_SPACING);
  }

  /**
   * Construct a new {@link IconCellDecorator}.
   * 
   * @param icon the icon to use
   * @param cell the cell to decorate
   * @param valign the vertical alignment attribute of the contents
   * @param spacing the pixel space between the icon and the cell
   */
  public IconCellUrl(Cell<String> cell,
      VerticalAlignmentConstant valign, int spacing) {
    if (template == null) {
      template = GWT.create(Template.class);
    }
    this.cell = cell;
    this.imageWidth = res.getWidth() + spacing;
    this.placeHolderHtml = getImageHtml(res, valign, true);
  }

  public boolean dependsOnSelection() {
    return cell.dependsOnSelection();
  }

  public Set<String> getConsumedEvents() {
    return cell.getConsumedEvents();
  }

  public boolean handlesSelection() {
    return cell.handlesSelection();
  }

  public boolean isEditing(Context context, Element parent, String value) {
    return cell.isEditing(context, getCellParent(parent), value);
  }

  public void onBrowserEvent(Context context, Element parent, String value,
      NativeEvent event, ValueUpdater<String> valueUpdater) {
    cell.onBrowserEvent(context, getCellParent(parent), value, event,
        valueUpdater);
  }

  public void render(Context context, String value, SafeHtmlBuilder sb) {
    SafeHtmlBuilder cellBuilder = new SafeHtmlBuilder();
    cell.render(context, "", cellBuilder);
    if (!"".equals(value))
    {
    	sb.append(template.outerDiv(direction, imageWidth, isIconUsed(value)
    			? getIconHtml(value) : placeHolderHtml, cellBuilder.toSafeHtml()));
    }
  }

  public boolean resetFocus(Context context, Element parent, String value) {
    return cell.resetFocus(context, getCellParent(parent), value);
  }

  public void setValue(Context context, Element parent, String value) {
    cell.setValue(context, getCellParent(parent), value);
  }

  protected SafeHtml getIconHtml(String value) 
  {
	  SafeHtml iconHTML = getImageHtml(wpImageBundle.urlLink(), HasVerticalAlignment.ALIGN_MIDDLE, false); 
	  SafeHtml linkHtml = SafeHtmlUtils.fromSafeConstant("<a style='cursor: pointer;' onClick='javascript:window.open(\"" + value + "\", \"opportunity\")'>" + iconHTML.asString() + "</a>") ;
	return linkHtml;
   // return getImageHtml(wpImageBundle.urlLink(), HasVerticalAlignment.ALIGN_MIDDLE, false);
  }

  protected boolean isIconUsed(String value) {
    return true;
  }

  SafeHtml getImageHtml(ImageResource res, VerticalAlignmentConstant valign,
      boolean isPlaceholder) {
    // Get the HTML for the image.
    SafeHtml image;
    if (isPlaceholder) {
      image = SafeHtmlUtils.fromTrustedString("<div></div>");
    } else {
      AbstractImagePrototype proto = AbstractImagePrototype.create(res);
      image = SafeHtmlUtils.fromTrustedString(proto.getHTML());
    }

    // Create the wrapper based on the vertical alignment.
    if (HasVerticalAlignment.ALIGN_TOP == valign) {
      return template.imageWrapperTop(direction, image);
    } else if (HasVerticalAlignment.ALIGN_BOTTOM == valign) {
      return template.imageWrapperBottom(direction, image);
    } else {
      int halfHeight = (int) Math.round(res.getHeight() / 2.0);
      return template.imageWrapperMiddle(direction, halfHeight, image);
    }
  }

  /**
   * Get the parent element of the decorated cell.
   * 
   * @param parent the parent of this cell
   * @return the decorated cell's parent
   */
  private Element getCellParent(Element parent) {
    return parent.getFirstChildElement().getChild(1).cast();
  }

}
