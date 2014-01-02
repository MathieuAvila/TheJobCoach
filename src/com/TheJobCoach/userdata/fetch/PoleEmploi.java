package com.TheJobCoach.userdata.fetch;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity.ApplicationStatus;
import com.ibm.icu.text.SimpleDateFormat;

public class PoleEmploi extends JobBoard
{
	static Pattern patternRef = Pattern.compile(".*detail/(.*)");
	static Pattern patternLieu = Pattern.compile("Lieu de travail</span></div>(.*)</li><li class=\"link-geolocation\">", Pattern.DOTALL);
	static Pattern patternSalaire = Pattern.compile("Salaire indicatif</span></div>(.*)Dur.e hebdomadaire de travail", Pattern.DOTALL);
	static Pattern patternCompany = Pattern.compile("<p class=\"title\" itemprop=\"hiringOrganization\" itemscope=\"\" itemtype=\"http://schema.org/Organization\">(.*)</span>.*<!-- Détail de l'offre -->", Pattern.DOTALL);
	
	static Pattern patternStatus = Pattern.compile("<span itemprop=\"employmentType\">(.*)Nature d'offre", Pattern.DOTALL);
	static Pattern patternPublication = Pattern.compile("<span itemprop=\"datePosted\">(.*)</span></div></li><!-- /secondary -->", Pattern.DOTALL);
	static Pattern patternDescription = Pattern.compile("!-- Description de l'offre -->.*itemprop=\"description\">(.*)<!-- Entreprise -->", Pattern.DOTALL);
	static Pattern patternReference = Pattern.compile("t:ac=([0-9A-Za-z-_]*)\">.*Offre suivante", Pattern.DOTALL);
	static Pattern patternTitle = Pattern.compile("</span></div></li><!-- /secondary --></ul><h4 class=\"small-title\" itemprop=\"title\">(.*)</h4><p class=\"first\" itemprop=\"occupationalCategory\">", Pattern.DOTALL);
	
	static Logger logger = LoggerFactory.getLogger(PoleEmploi.class);
		
	@Override
	public UserOpportunity getOpportunityFromText(byte[] textSrc, String url)
	{
		String text = "";
		try
		{
			text = new String(textSrc, "UTF-8");
		}
		catch (UnsupportedEncodingException e3){
			logger.error("Error converting to UTF-8");
		}
		
		String iD = removeHtml(extractPattern(patternReference, text, "")); 
		Date firstSeen = new Date(); 
		Date lastUpdate = new Date(); 
		String title = removeHtml(extractPattern(patternTitle, text, ""));
		String description = extractPattern(patternDescription, text, "");
		String companyId = removeHtml(extractPattern(patternCompany, text, ""));
		String contractType = removeHtml(extractPattern(patternStatus, text, ""));
		String pubDateStr = removeHtml(extractPattern(patternPublication, text, ""));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			firstSeen = sdf.parse(pubDateStr);
		}
		catch (ParseException e1){}
		
		String salaryStr = removeHtml(extractPattern(patternSalaire, text, ""));
		
		Date startDate = null;
		Date endDate = null;
		String location = removeHtml(extractPattern(patternLieu, text, ""));
		ApplicationStatus status = ApplicationStatus.DISCOVERED;

		return new UserOpportunity(iD, firstSeen, lastUpdate,
				title,  description,  companyId,
				contractType,  salaryStr,  startDate,  endDate,
				false, "poleemploi#" + iD, url, location,
				status, "");
	}

	@Override
	public String getOpportunityUrl(String ref)
	{
		if (ref.contains("http://")) return ref;
		return ref;
	}

}
