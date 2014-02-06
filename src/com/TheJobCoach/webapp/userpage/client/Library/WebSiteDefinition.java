package com.TheJobCoach.webapp.userpage.client.Library;

import java.util.List;

public class WebSiteDefinition implements Comparable<WebSiteDefinition> 
{
	List<String> locationId;
	String peopleTarget;
	List<String> sectorId;
	String typeId;

	String name;
	String url;

	public WebSiteDefinition(List<String> locationId, String peopleTarget,
			List<String> sectorId, String typeId, String url, String name)
	{
		super();
		this.locationId = locationId;
		this.peopleTarget = peopleTarget;
		this.sectorId = sectorId;
		this.typeId = typeId;
		this.url = url;
		this.name = name;
	}
	public WebSiteDefinition()
	{
		super();
		this.name = "";
		this.locationId = null;
		this.peopleTarget = null;
		this.sectorId = null;
		this.typeId = null;
	}
	@Override
	public int compareTo(WebSiteDefinition o)
	{
		return name.compareTo(o.name);
	}
}

