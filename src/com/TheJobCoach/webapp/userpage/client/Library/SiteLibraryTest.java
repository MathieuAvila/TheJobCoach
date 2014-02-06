package com.TheJobCoach.webapp.userpage.client.Library;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class SiteLibraryTest extends ASiteLibrary
{
	static Vector<WebSiteDefinition> siteList = new Vector<WebSiteDefinition>();
	static Map<String, LocationDefinition> locationDefinition = new HashMap<String, LocationDefinition>();
	static Map<String, String> sectorDefinition = new HashMap<String, String> ();
	static Map<String, String> webSiteTypeDefinition = new HashMap<String, String> ();
	
	static {
		siteList.add(new WebSiteDefinition(Arrays.asList("picardie"),"seniors", Arrays.asList("IT"), "cab", "http://toto.com", "TOTO"));
	}

	@Override
	Map<String, LocationDefinition> getLocationDefinition()
	{
		return locationDefinition;
	}

	@Override
	Vector<WebSiteDefinition> getFullSiteList()
	{
		return siteList;
	}

}
