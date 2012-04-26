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
		} else {
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
		else
		{
			subject="Activer votre compte www.TheJobCoach.fr";
		}
		return subject;
	}
	
	static String _TextLostCredentialsSubject(String lang)
	{
		String subject="";
		if (lang.equals("en"))
		{
			subject="Your credentials on www.TheJobCoach.fr";
		} 
		else
		{
			subject="Vos identifiants sur www.TheJobCoach.fr !";
		}
		return subject;
	}
	
	static String _TextLostCredentials(String user, String password, String lang)
	{
		String body="";
		if (lang.equals("en"))
		{
			body=
					"You asked for your credentials on www.TheJobCoach.fr\n"+
					"Your user name is: '" + user + "'\n"+
					"Your password is: '" + password + "'\n\n"+
					"Hoping to see you soon on www.TheJobCoach.fr!";
		} 
		else
		{
			body=
					"Vous avez demandé vos identifiants de www.TheJobCoach.fr\n"+
					"Votre login est : '" + user + "'\n"+
					"Votre mot de passe est : '" + password + "'\n\n"+
					"En espérant vous revoir bientôt sur www.TheJobCoach.fr !";
		}
		return body;
	}
}
