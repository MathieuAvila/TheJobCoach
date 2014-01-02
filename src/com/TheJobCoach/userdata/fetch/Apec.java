package com.TheJobCoach.userdata.fetch;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity.ApplicationStatus;
import com.ibm.icu.text.SimpleDateFormat;

public class Apec extends JobBoard
{
	static Pattern patternRef = Pattern.compile("'_([0-9A-Z-]*?)____");
	static Pattern patternLieu = Pattern.compile("<th>Lieu :</th>..[ ]*<td>([^<]*)</td>", Pattern.DOTALL);
	static Pattern patternSalaireFull = Pattern.compile("<th>Salaire :</th>(.*)Exp.rience :", Pattern.DOTALL);
	static Pattern patternCompany = Pattern.compile("Soci.t. :</th>(.*)Voir toutes les offres", Pattern.DOTALL);
	static Pattern patternStatus = Pattern.compile("Statut : </th>(.*)<th>Lieu :</th>", Pattern.DOTALL);
	static Pattern patternStatusContract = Pattern.compile("Nombre de postes : </th>(.*)<th>Statut : ", Pattern.DOTALL);
	
	static Pattern patternPublication = Pattern.compile("Date de publication :</th>(.*)Soci.t. :", Pattern.DOTALL);
	static Pattern patternDescription = Pattern.compile("Signaler cette offre(.*)Postuler . cette offre</a></div>", Pattern.DOTALL);
	static Pattern patternDescriptionPrefix = Pattern.compile("<div class=\"boxContentInside\">(.*)<div class=\"spacer\">", Pattern.DOTALL);
	static Pattern patternReference = Pattern.compile("<th>R.f.rence Apec :</th>(.*)Date de publication :</th>", Pattern.DOTALL);
	static Pattern patternTitle = Pattern.compile("<h1 class=\"detailOffre\">D.tail de l'offre : (.*)");
			
	@Override
	public UserOpportunity getOpportunityFromText(byte[] textSrc, String url)
	{
		String text = "";
		try
		{
			text = new String(textSrc, "ISO-8859-1");
		}
		catch (UnsupportedEncodingException e3){}
		String iD = removeHtml(extractPattern(patternReference, text, "")); 
		iD = removeHtml(iD.replaceAll("R.f.rence soci.t.*", "")); 
		if (iD.equals("")) return null;
		Date firstSeen = new Date(); 
		Date lastUpdate = new Date(); 
		String title = removeHtml(extractPattern(patternTitle, text, ""));
		String description = extractPattern(patternDescription, text, "");
		description = extractPattern(patternDescriptionPrefix, description, "");
		String companyId = removeHtml(extractPattern(patternCompany, text, ""));
		companyId = removeHtml(companyId.replace("Voir plus d'infos sur la société", ""));
		String contractType = removeHtml(extractPattern(patternStatus, text, ""));
		String contractType2 = removeHtml(extractPattern(patternStatusContract, text, "").replace("1 en ", ""));
		contractType = contractType2 +" - " + contractType;
		String pubDateStr = removeHtml(extractPattern(patternPublication, text, ""));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			firstSeen = sdf.parse(pubDateStr);
		}
		catch (ParseException e1){}
		
		String salaryFull = removeHtml(extractPattern(patternSalaireFull, text, ""));
		Date startDate = null;
		Date endDate = null;
		String location = removeHtml(extractPattern(patternLieu, text, ""));
		ApplicationStatus status = ApplicationStatus.DISCOVERED;

		return new UserOpportunity(iD, firstSeen, lastUpdate,
				title,  description,  companyId,
				contractType,  salaryFull,  startDate,  endDate,
				false, "apec#" + iD, url, location,
				status, "");
	}

	@Override
	public String getOpportunityUrl(String ref)
	{
		if (ref.contains("http://")) return ref;
		return "http://candidat.pole-emploi.fr/candidat/rechercheoffres/detail/" + ref;
	}

}
