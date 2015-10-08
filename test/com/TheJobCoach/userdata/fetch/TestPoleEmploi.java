package com.TheJobCoach.userdata.fetch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;

public class TestPoleEmploi
{
	PoleEmploi poleemploi = new PoleEmploi();
	
	TestApec fileLoader = new TestApec();
	
	@Test
	public void test_getOpportunityFromText_newTitle()
	{
		byte[] poleemploi_2 = fileLoader.get_file("poleemploi_2.txt");
		UserOpportunity poleemploi_2_opp = poleemploi.getOpportunityFromText(poleemploi_2, "myurl");
		assertTrue(poleemploi_2_opp.title.contains("Administrateur / Administratrice de serveurs"));
		assertEquals("ASTON CARTER INTERNATIONAL SAS", poleemploi_2_opp.companyId);
		assertEquals("Annuel de 35000.00 Euros à 45000.00 Euros sur 12 mois", poleemploi_2_opp.salary);
		assertEquals("poleemploi#032NRFB", poleemploi_2_opp.source);
		assertEquals("06 - Alpes Maritimes", poleemploi_2_opp.location);
	}
	
	@Test
	public void test_getOpportunityFromText_LocationGeoloc()
	{
		byte[] poleemploi_3 = fileLoader.get_file("poleemploi_3.txt");
		UserOpportunity poleemploi_3_opp = poleemploi.getOpportunityFromText(poleemploi_3, "myurl");
		assertEquals("LE BOURGET DU LAC", poleemploi_3_opp.location);
	}
}
