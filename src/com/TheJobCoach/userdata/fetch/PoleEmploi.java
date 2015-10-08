package com.TheJobCoach.userdata.fetch;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity.ApplicationStatus;

public class PoleEmploi extends JobBoard
{
	static Pattern patternReference = Pattern.compile("Numéro de l'offre</span></div><div class=\"value\"><span>(.*)</span></div></li></ul></div><!-- primary --><div class=\"secondary\"><ul><li><div class=\"label\"><span>Offre actualisée le", Pattern.DOTALL);
	static Pattern patternLieu = Pattern.compile("Lieu de travail</span></dt><dd itemtype=\"http://schema.org/Place\" itemscope=\"\" itemprop=\"jobLocation\" class=\"value\"><ul><li itemprop=\"addressRegion\">(.*)</li></ul></dd><dt class=\"label\"><span>Type ", Pattern.DOTALL);
	static Pattern patternSalaire = Pattern.compile("Salaire indicatif</span></dt><dd class=\"value\"><span itemprop=\"baseSalary\">(.*).</span></dd><dt class=\"label\"><span>Compl", Pattern.DOTALL);
	static Pattern patternCompany = Pattern.compile("<div class=\"vcard\"><h4 class=\"block-subtitle\"><span>Entreprise</span></h4><p>(.*)</p><!-- appel au format hCard -->", Pattern.DOTALL);
	static Pattern patternStatus = Pattern.compile("<span itemprop=\"employmentType\">(.*)Nature d'offre", Pattern.DOTALL);
	static Pattern patternPublication = Pattern.compile("<span itemprop=\"datePosted\">(.*)</span></div></li><!-- /secondary -->", Pattern.DOTALL);
	static Pattern patternDescription = Pattern.compile("!-- Description de l'offre -->.*itemprop=\"description\">(.*)<!-- Entreprise -->", Pattern.DOTALL);
	static Pattern patternTitle = Pattern.compile("<h4 itemprop=\"title\" class=\"small-title\">(.*)</h4>.*<p itemprop=\"occupationalCategory\"", Pattern.DOTALL);
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
		Date pubDate = new Date(); 
		Date lastUpdate = new Date(); 
		String title = removeHtml(extractPattern(patternTitle, text, ""));
		String description = extractPattern(patternDescription, text, "");
		String companyId = removeHtml(extractPattern(patternCompany, text, ""));
		String contractType = removeHtml(extractPattern(patternStatus, text, ""));
		String pubDateStr = removeHtml(extractPattern(patternPublication, text, ""));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			pubDate = sdf.parse(pubDateStr);
		}
		catch (ParseException e1){}
		
		String salaryStr = removeHtml(extractPattern(patternSalaire, text, ""));
		
		Date startDate = null;
		Date endDate = null;
		String location = removeHtml(extractPattern(patternLieu, text, ""));
		ApplicationStatus status = ApplicationStatus.DISCOVERED;

		return new UserOpportunity(iD, pubDate, lastUpdate,
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
