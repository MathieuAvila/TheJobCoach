package com.TheJobCoach.webapp.userpage.client.Library;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Vector;

import org.junit.Test;

public class TestASiteLibrary {
	
	WebSiteDefinition voidSite = new WebSiteDefinition();
	WebSiteDefinition rSite = new WebSiteDefinition(Arrays.asList("R1"), null, null, "", "", "");
	WebSiteDefinition multiSite = new WebSiteDefinition(Arrays.asList("R1"), null, Arrays.asList("S2"), "", "", "NAME");
	WebSiteDefinition multiSiteName = new WebSiteDefinition(Arrays.asList("R1"), null, Arrays.asList("S2"), "", "", "NAMEa");
	
	@Test
	public void testFilter()
	{
		SiteLibraryTest library = new SiteLibraryTest();
		Vector<WebSiteDefinition> voidResult = library.getSiteFilter(voidSite);
		assertEquals(9, voidResult.size());
		voidResult = library.getSiteFilter(rSite);
		assertEquals(7, voidResult.size());
		voidResult = library.getSiteFilter(multiSite);
		assertEquals(4, voidResult.size());
		voidResult = library.getSiteFilter(multiSiteName);
		assertEquals(1, voidResult.size());
	}
}
