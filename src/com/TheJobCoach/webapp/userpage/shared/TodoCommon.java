package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Map;

public class TodoCommon implements Serializable
{
	private static final long serialVersionUID = 7728348151588542928L;

	final public static String SITEMANAGER_SUBSCRIBER_ID = "sitemanager";
	public static final String EXTERNALCONTACTMANAGER_SUBSCRIBER_ID = "externalcontact";

	public static final String PERSO_SUBSCRIBER_ID = "";

	// Leave them small to minimize client/server load
	public static final String NAME = "n";
	public static final String FIRSTNAME = "f";
	public static final String LASTNAME = "ln";
	public static final String COMPANY = "c";
	public static final String LAST = "l";
	public static final String ID = "i";
}
