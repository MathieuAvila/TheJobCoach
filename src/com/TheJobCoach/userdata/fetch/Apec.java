package com.TheJobCoach.userdata.fetch;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity.ApplicationStatus;

import org.stringtree.json.JSONReader;

public class Apec extends JobBoard
{
	
	static Pattern patternRef = Pattern.compile("numIdOffre=([0-9_A-Z]*)&.*");

	String getOrVoid(HashMap<String, Object> hash, String key)
	{
		String result = (String) hash.get(key);
		if (result == null)
			return "";
		return result;
	}
	
	@Override
	public UserOpportunity getOpportunityFromText(byte[] textSrc, String url)
	{
		String text = "";
		try
		{
			text = new String(textSrc, "UTF-8");
		}
		catch (UnsupportedEncodingException e3)
		{
			logger.error("Unsupported encoding " + e3.getMessage());
		}
		logger.info("offre :\n" + text);

		JSONReader reader = new JSONReader();
		Object result = reader.read(text);
		if (result == null)
			return null;
		HashMap<String, Object> resultHash = (HashMap<String, Object>)result;
		for (String key: (Set<String>)resultHash.keySet())
		{
			logger.info("key: " + key + "       " + resultHash.get(key));
		}
		String iD = getOrVoid(resultHash, "numeroOffre");
		String title = getOrVoid(resultHash, "intitule");
		String description = getOrVoid(resultHash, "texteHtml");
		String salaryFull = getOrVoid(resultHash, "salaireTexte");
		String location = getOrVoid(resultHash, "lieuTexte");
		Date lastUpdate = new Date(); 
		String companyId = getOrVoid(resultHash, "nomCompteEtablissement");
		
		String pubDateStr = getOrVoid(resultHash, "datePublication");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date pubDate = new Date(); 
		try
		{
			pubDate = sdf.parse(pubDateStr);
		}
		catch (ParseException e1){}
		
		ApplicationStatus status = ApplicationStatus.DISCOVERED;

		return new UserOpportunity(iD, pubDate, lastUpdate,
				title,  description,  companyId,
				"",  salaryFull,  null,  null,
				false, "apec#" + iD, url, location,
				status, "");
		//return null;
	}

	@Override
	public String getOpportunityUrl(String ref)
	{
		String realRef = ref;
		if (ref.contains("http")) 
			realRef = extractPattern(patternRef, ref, "");
		return "https://cadres.apec.fr/cms/webservices/offre/public?numeroOffre=" + realRef ;
	}

}
