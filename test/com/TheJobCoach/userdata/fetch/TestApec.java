package com.TheJobCoach.userdata.fetch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.TheJobCoach.CoachTestUtils;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;

public class TestApec
{
	Apec apec = new Apec();
	
	@Test
	public void test_getOpportunityFromText()
	{
		byte[] apec_0 = null;
		InputStream contentFile = this.getClass().getResourceAsStream("/com/TheJobCoach/userdata/fetch/apec_0.txt");
		try
		{
			apec_0 = IOUtils.toByteArray(contentFile);
		}
		catch (IOException e){}
		UserOpportunity apec_0_opp = apec.getOpportunityFromText(apec_0, "myurl");
		
		assertEquals("H/F Administrateur systèmes et réseaux", apec_0_opp.title);
		assertEquals("Montpellier", apec_0_opp.location);
		assertEquals("Université Montpellier", apec_0_opp.companyId);
		assertEquals("Cadre du secteur public / Armées", apec_0_opp.contractType);
		assertEquals("apec#55074623W-2160-6693", apec_0_opp.source);
		assertTrue(apec_0_opp.description.contains("RECRUTEMENT D'UN AGENT CONTRACTUEL"));
		assertEquals(25000.0, apec_0_opp.salary, 100);
		assertTrue(CoachTestUtils.isDateEqualForDay(CoachTestUtils.getDate(2013, 11, 28), apec_0_opp.firstSeen));
	}	
}
