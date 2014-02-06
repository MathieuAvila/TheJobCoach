package com.TheJobCoach.webapp.userpage.client.Library;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public abstract class ASiteLibrary
{
	abstract Map<String, LocationDefinition> getLocationDefinition();
	abstract Vector<WebSiteDefinition> getFullSiteList();

	boolean listContains(List<String> l , List<String> sv)
	{
		if (sv == null) return true;
		if (sv.size() == 0) return true;
		String s = sv.get(0);
		if (s == null) return true;
		if (s.equals("")) return true;
		if (l == null) return false;
		for (String e: l)
		{
			if (e.equals(s)) return true;
		}
		return false;
	}

	boolean checkEquals(String l , String s)
	{
		if (s == null) return true;
		if (s.equals("")) return true;
		if (l == null) return true;
		return (l.equals(s));
	}
	
	Vector<WebSiteDefinition> getSiteFilter(WebSiteDefinition filter)
	{
		Vector<WebSiteDefinition> result = new Vector<WebSiteDefinition>();
		Vector<WebSiteDefinition> vSite = getFullSiteList();
		for (WebSiteDefinition site: vSite)
		{
			if (!(site.name.toLowerCase().contains(filter.name.toLowerCase()))) continue;
			if (!listContains(site.sectorId , filter.sectorId)) continue;
			if (!listContains(site.locationId, filter.locationId)) continue;
			if (!checkEquals(site.typeId, filter.typeId)) continue;
			if (!checkEquals(site.peopleTarget, filter.peopleTarget)) continue;
			result.add(site);
		}
		return result;
	}
}
