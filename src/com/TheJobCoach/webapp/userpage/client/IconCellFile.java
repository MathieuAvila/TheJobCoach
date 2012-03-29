/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.TheJobCoach.webapp.userpage.client;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.IconCellDecorator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class IconCellFile extends IconCellDecorator<String> 
{
	static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);
	IconCellFile(Cell<String> cell)
	{				
		super(wpImageBundle.deleteFile(), cell, HasVerticalAlignment.ALIGN_MIDDLE, 10);
	}


	protected SafeHtml getIconHtml(String value) 
	{
		String ext4 = value.substring(value.length() - 4 ).toLowerCase();
		ImageResource res = wpImageBundle.unkFile();
		if (".doc".equals(ext4)) res = wpImageBundle.docFile();
		if (".pdf".equals(ext4)) res = wpImageBundle.pdfFile();
		SafeHtml html = SafeHtmlUtils.fromSafeConstant("");
		html = SafeHtmlUtils.fromTrustedString(AbstractImagePrototype.create(res).getHTML());

		// Create the wrapper based on the vertical alignment.
		SafeStylesBuilder cssStyles =
				new SafeStylesBuilder().appendTrustedString("left" + ":-15px;");

		//int height = (int)wpImageBundle.deleteFile().getHeight();
		//cssStyles.appendTrustedString("margin-top:-" + height + "px;");
		return SafeHtmlUtils.fromSafeConstant("<div style=\"" + cssStyles.toSafeStyles().asString() + "position:absolute;line-height:0px;text-decoration:underline;color:blue;a:hover{color:#001760;text-decoration: underline;}\">" + html.asString() + "</div>");
		// return this.template.imageWrapperMiddle(cssStyles.toSafeStyles(), wpImageBundle.deleteFile());
	}
}