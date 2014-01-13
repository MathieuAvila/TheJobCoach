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
	
	protected byte[] get_file(String resource)
	{
		byte[] content = null;
		InputStream contentFile = this.getClass().getResourceAsStream("/com/TheJobCoach/userdata/fetch/" + resource);
		try
		{
			content = IOUtils.toByteArray(contentFile);
		}
		catch (IOException e){}
		return content;
	}
	
	@Test
	public void test_getOpportunityFromText()
	{
		byte[] apec_0 = get_file("apec_0.txt");
		
		UserOpportunity apec_0_opp = apec.getOpportunityFromText(apec_0, "myurl");
		
		assertEquals("H/F Administrateur systèmes et réseaux", apec_0_opp.title);
		assertEquals("Montpellier", apec_0_opp.location);
		assertEquals("Université Montpellier", apec_0_opp.companyId);
		assertEquals("CDD de 12 mois - Cadre du secteur public / Armées", apec_0_opp.contractType);
		assertEquals("apec#55074623W-2160-6693", apec_0_opp.source);
		assertTrue(apec_0_opp.description.contains("RECRUTEMENT D'UN AGENT CONTRACTUEL"));
		assertEquals("25 K€ brut/an", apec_0_opp.salary);
		assertEquals("myurl", apec_0_opp.url);
		assertTrue(CoachTestUtils.isDateEqualForDay(CoachTestUtils.getDate(2013, 11, 28), apec_0_opp.pubDate));
	}	
	
	@Test
	public void test_getOpportunity_company_without_garbage_text()
	{
		byte[] apec_0 = get_file("apec_1.txt");
		
		UserOpportunity apec_0_opp = apec.getOpportunityFromText(apec_0, "myurl");
		
		assertEquals("GFI INFORMATIQUE", apec_0_opp.companyId);
	}	
	
	@Test
	public void test_getOpportunity_reference_without_garbage_text()
	{
		byte[] apec_0 = get_file("apec_2.txt");
		
		UserOpportunity apec_0_opp = apec.getOpportunityFromText(apec_0, "myurl");
		
		assertEquals("apec#56268943W-5417-6876", apec_0_opp.source);
	}
	
	@Test
	public void test_getOpportunity_multiple_places()
	{
		byte[] apec_0 = get_file("apec_3.txt");
		
		UserOpportunity apec_0_opp = apec.getOpportunityFromText(apec_0, "myurl");
		
		assertEquals("3 en CDI - Cadre du secteur privé", apec_0_opp.contractType);
	}	
}
