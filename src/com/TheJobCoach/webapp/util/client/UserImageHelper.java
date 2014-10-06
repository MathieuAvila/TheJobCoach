package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

public class UserImageHelper
{
	public static HTML getImage(UserId from, String userName, int sizeMax)
	{
		return getImage(from, userName, sizeMax, false);
	}
	
	public static HTML getImage(UserId from, String userName, int sizeMax, boolean border)
	{
		String imgURL = GWT.getModuleBaseURL() + "DownloadImage"
				+ "?userid=" + userName 
				+ "&fromuserid=" + from.userName 
				+ "&token=" + from.token 
				+ "&type=portrait&format=" + sizeMax;
		String img = "<img src=\"" + imgURL 
				+ "\" style=\" vertical-align:middle; max-width:" + sizeMax 
				+ "px; max-height:" + sizeMax 
				+ "px; width: auto; height: auto;\"/>";
		
		String complete = border ? 
				("<div style='   white-space: nowrap; text-align:center; vertical-align: middle; display: block; margin-left: auto; margin-right: auto ; width:" + sizeMax + "px; height:" + sizeMax + "px;' >"
						+ "<span style=\"display: inline-block; height: 100%; vertical-align: middle;\"></span>"
						+ img + "</div>") : img;
		return new HTML(complete);
	}
	
	public static HTML getImage(UserId from, int sizeMax)
	{
		return getImage(from, from.userName, sizeMax, false);
	}
	
	public static HTML getImage(UserId from, int sizeMax, boolean border)
	{
		return getImage(from, from.userName, sizeMax, border);
	}

}
