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

	//@Test
	public void test_getOpportunityFromText()
	{
		byte[] apec_0 = get_file("apec_0.txt");

		UserOpportunity apec_0_opp = apec.getOpportunityFromText(apec_0, "myurl");

		assertEquals("Conducteur de travaux principal TCE H/F", apec_0_opp.title);
		assertEquals("Île-de-France", apec_0_opp.location);
		assertEquals("HAYS", apec_0_opp.companyId);
		assertEquals("", apec_0_opp.contractType);
		assertEquals("apec#161059483W", apec_0_opp.source);
		assertTrue(apec_0_opp.description.contains("nous recherchons une personne"));
		assertEquals("50000EUR - 55000EUR an", apec_0_opp.salary);
		assertEquals("myurl", apec_0_opp.url);
		assertTrue(CoachTestUtils.isDateEqualForDay(CoachTestUtils.getDate(2015, 9, 26), apec_0_opp.pubDate));
	}

	@Test
	public void test_getUrl()
	{
		String url = "https://cadres.apec.fr/offres-emploi-cadres/0_0_0_161077852W__________offre-d-emploi-ingenieur-systeme-devops-h-f.html?numIdOffre=161077852W&selectedElement=0&sortsType=DATE&sortsDirection=DESCENDING&nbParPage=20&typeAffichage=detaille&page=0&fonctions=101833&retour=%2Fliste-offres-emploi-cadres%2F8_0____101833________offre-d-emploi-informatique.html%3FsortsType%3DDATE%26sortsDirection%3DDESCENDING%26nbParPage%3D20%26typeAffichage%3Ddetaille%26page%3D0%26fonctions%3D101833";
		String computedUrl = apec.getOpportunityUrl(url);
		assertEquals(computedUrl, "https://cadres.apec.fr/cms/webservices/offre/public?numeroOffre=161077852W");
		
		url = "161077852W";
		computedUrl = apec.getOpportunityUrl(url);
		assertEquals(computedUrl, "https://cadres.apec.fr/cms/webservices/offre/public?numeroOffre=161077852W");
		
		
	}
	
	//@Test
	public void test_RealForTest() throws IOException
	{
		/*byte[] apec = Web.get("https://cadres.apec.fr/home/mes-offres/recherche-des-offres-demploi/liste-des-offres-demploi/detail-de-loffre-demploi.html?numIdOffre=161059483W");
		String offre = new String(apec, "ISO-8859-1");
		System.out.println(offre);
		UserOpportunity uopp = apec.getOpportunity("161059483W");
		System.out.println(uopp.title);
		System.out.println(uopp.location);
		*/
	}

}
