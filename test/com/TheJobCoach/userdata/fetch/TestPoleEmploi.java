package com.TheJobCoach.userdata.fetch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;

public class TestPoleEmploi
{
	PoleEmploi poleemploi = new PoleEmploi();
	
	@Test
	public void test_getOpportunityFromText()
	{
		byte[] poleemploi_0 = null;
		InputStream contentFile = this.getClass().getResourceAsStream("/com/TheJobCoach/userdata/fetch/poleemploi_0.txt");
		try
		{
			poleemploi_0 = IOUtils.toByteArray(contentFile);
		}
		catch (IOException e){}
		UserOpportunity poleemploi_0_opp = poleemploi.getOpportunityFromText(poleemploi_0, "myurl");
		
		assertEquals("Electronicien / Electronicienne d'équipements embarqués", poleemploi_0_opp.title);
		assertEquals("44 - ST NAZAIRE", poleemploi_0_opp.location);
		assertEquals("N/A", poleemploi_0_opp.companyId);
		assertEquals("Contrat à durée indéterminée", poleemploi_0_opp.contractType);
		assertEquals("poleemploi#010LDQX", poleemploi_0_opp.source);
		assertTrue(poleemploi_0_opp.description.contains("TECHNICIEN TACHYGRAPHE"));
		assertEquals(9.43, poleemploi_0_opp.salary, 1);
		assertTrue(CoachTestUtils.isDateEqualForDay(CoachTestUtils.getDate(2013, 12, 13), poleemploi_0_opp.firstSeen));
	}	
}
