package com.TheJobCoach.userdata;

public class Lang
{

	static String _TextBody(String firstName, String site, String userName, String key, String lang)
	{
		String body="";
		if (lang.equals("en"))
		{
			body="Hello {0}\n\n"+
					"You can activate your account by clicking on the following link:\n"+
					"{1}/TheJobCoach.html?action=validate&username={2}&token={3}\n\n" +
					"We are happy to to see you soon on The Job Coach !\n\n"+
					"The Job Coach team\n";
		}
		if (lang.equals("fr") || lang.equals("") || lang == null)
		{
			body="Bonjour {0}\n\n"+
					"Vous pouvez activer votre compte en cliquant sur le lien suivant:\n"+
					"{1}/TheJobCoach.html?action=validate&username={2}&token={3}\n\n"+
					"Nous sommes heureux de vous voir sur The Job Coach !\n\n"+
					"L'equipe The Job Coach\n";
		}
		body = body.replace("{0}", firstName);
		body = body.replace("{1}", site);
		body = body.replace("{2}", userName);
		body = body.replace("{3}", key);
		return body;
	}

	static String _TextSubject(String lang)
	{
		String subject="";
		if (lang.equals("en"))
		{
			subject="Activate Account on www.TheJobCoach.fr";
		}
		if (lang.equals("fr"))
		{
			subject="Activer votre compte www.TheJobCoach.fr";
		}
		return subject;
	}

}
