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
		siteList.add(new WebSiteDefinition(Arrays.asList("R1", "R2", "R3"),"T1", Arrays.asList("S1", "S2", "S3"), "T1", "URL1", "NAME1"));
		siteList.add(new WebSiteDefinition(Arrays.asList(      "R2", "R3"),"T1", Arrays.asList("S1", "S2"      ), "T1", "URL2", "NAME2"));
		siteList.add(new WebSiteDefinition(Arrays.asList(      "R2", "R3"),"T1", Arrays.asList("S1", "S2"      ), "T1", "URL3", "NAME3"));
		siteList.add(new WebSiteDefinition(Arrays.asList("R1", "R2"      ),"T1", Arrays.asList(      "S2", "S3"), "T1", "URL4", "NAMEa4"));
		siteList.add(new WebSiteDefinition(Arrays.asList("R1", "R2"      ),"T1", Arrays.asList(      "S2", "S3"), "T1", "URL4", "NAMEb4"));
		siteList.add(new WebSiteDefinition(Arrays.asList("R1", "R2"      ),"T2", Arrays.asList(      "S2", "S3"), "T1", "URL5", "NAME5"));
		siteList.add(new WebSiteDefinition(Arrays.asList("R1", "R2"      ),"T2", Arrays.asList("S1",       "S3"), "T1", "URL6", "NAME6"));
		siteList.add(new WebSiteDefinition(Arrays.asList("R1",       "R3"),"T2", Arrays.asList("S1",       "S3"), "T1", "URL7", "NAME8"));
		siteList.add(new WebSiteDefinition(Arrays.asList("R1",       "R3"),"T2", Arrays.asList("S1",       "S3"), "T1", "URL8", "NAME9"));
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
