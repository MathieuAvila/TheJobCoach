package com.TheJobCoach.userdata.fetch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;

public class TestPoleEmploi
{
	PoleEmploi poleemploi = new PoleEmploi();
	
	TestApec fileLoader = new TestApec();
	
	@Test
	public void test_getOpportunityFromText()
	{
		byte[] poleemploi_0 = fileLoader.get_file("poleemploi_0.txt");
		
		UserOpportunity poleemploi_0_opp = poleemploi.getOpportunityFromText(poleemploi_0, "myurl");
		
		assertEquals("Electronicien / Electronicienne d'équipements embarqués", poleemploi_0_opp.title);
		assertEquals("44 - ST NAZAIRE", poleemploi_0_opp.location);
		assertEquals("groupe dubreuil (APA)", poleemploi_0_opp.companyId);
		assertEquals("Contrat à durée indéterminée", poleemploi_0_opp.contractType);
		assertEquals("poleemploi#010LDQX", poleemploi_0_opp.source);
		assertTrue(poleemploi_0_opp.description.contains("TECHNICIEN TACHYGRAPHE"));
		assertEquals("Horaire de 9,43 euros", poleemploi_0_opp.salary);
		assertTrue(CoachTestUtils.isDateEqualForDay(CoachTestUtils.getDate(2013, 12, 13), poleemploi_0_opp.pubDate));
		assertEquals("myurl", poleemploi_0_opp.url);
	}
	
	@Test
	public void test_getOpportunityFromText_description()
	{
		byte[] poleemploi_1 = fileLoader.get_file("poleemploi_1.txt");
		UserOpportunity poleemploi_1_opp = poleemploi.getOpportunityFromText(poleemploi_1, "myurl");
		assertTrue(poleemploi_1_opp.description.contains("Le chef de projets infrastructure système "));
		assertEquals("KALYPTUS", poleemploi_1_opp.companyId);
		
	}
}
