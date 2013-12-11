package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;

public class TodoCommon implements Serializable
{
	private static final long serialVersionUID = 7728348151588542928L;

	// Used in DB. DO NOT CHANGE !
	final public static String SITEMANAGER_SUBSCRIBER_ID = "sitemanager";
	public static final String EXTERNALCONTACTMANAGER_SUBSCRIBER_ID = "externalcontact";

	// not used in DB. But still, do not change.
	public static final String PERSO_SUBSCRIBER_ID = "";

	// Leave them small to minimize client/server load
	// Not used in DB.
	public static final String NAME = "n";
	public static final String FIRSTNAME = "f";
	public static final String LASTNAME = "ln";
	public static final String COMPANY = "c";
	public static final String LAST = "l";
	public static final String ID = "i";
	public static final String OPPID = "o";
	public static final String TYPE = "t";
}
