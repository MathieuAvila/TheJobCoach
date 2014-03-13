package com.TheJobCoach.webapp.util.shared;

import java.util.HashMap;

public class UserValuesConstantsCoachMessages 
{	
	public final static String COACH_ROOT                     = "COACH_";
	public final static String COACH_WELCOME                  = "COACH_WELCOME";
	public final static String COACH_PRESENTING               = "COACH_PRESENTING";
	public final static String COACH_HELLO                    = "COACH_HELLO";
	public final static String COACH_HELLO_AGAIN              = "COACH_HELLO_AGAIN";
	public final static String COACH_LATE_ARRIVAL             = "COACH_LATE_ARRIVAL";
	public final static String COACH_LATE_DEPARTURE           = "COACH_LATE_DEPARTURE";
	public final static String COACH_VERY_LATE_ARRIVAL        = "COACH_VERY_LATE_ARRIVAL";
	public final static String COACH_VERY_LATE_DEPARTURE      = "COACH_VERY_LATE_DEPARTURE";
	public final static String COACH_DEPARTURE_WARNING        = "COACH_DEPARTURE_WARNING";
	public final static String COACH_DEPARTURE_TIME           = "COACH_DEPARTURE_TIME";
	public final static String COACH_GOAL_END_PERIOD          = "COACH_GOAL_END_PERIOD";
	public final static String COACH_GOAL_SEND_EMAIL          = "COACH_GOAL_SEND_EMAIL";
	public final static String COACH_OPP_NONE                 = "COACH_OPP_NONE";
	public final static String COACH_OPP_NO_LOG               = "COACH_OPP_NO_LOG";
	public final static String COACH_OPP_NO_APPLICATION       = "COACH_OPP_NO_APPLICATION";
	public final static String COACH_LOG_RECALL               = "COACH_LOG_RECALL";
	public final static String COACH_LOG_INTERVIEW            = "COACH_LOG_INTERVIEW";
	public final static String COACH_LOG_FAILURE              = "COACH_LOG_FAILURE";
	public final static String COACH_LOG_SUCCESS              = "COACH_LOG_SUCCESS";
	public final static String COACH_PERSONAL_NOTE            = "COACH_PERSONAL_NOTE";
	public final static String COACH_PASSWORD_WARNING         = "COACH_PASSWORD_WARNING";

	static public class messageMinMax 
	{
		public int min;
		public int max;
		public messageMinMax(int min, int max)
		{
			this.min = min;
			this.max = max;
		}
	}
	
	public static HashMap<String, messageMinMax> minMaxKeyMap = new HashMap<String, messageMinMax>();
	
	static {
		minMaxKeyMap.put(COACH_WELCOME,                 new messageMinMax(0, 1));
		minMaxKeyMap.put(COACH_HELLO,                   new messageMinMax(1, 0));
		//minMaxKeyMap.put(COACH_HELLO_AGAIN,             new messageMinMax(0, 0));
		minMaxKeyMap.put(COACH_GOAL_END_PERIOD,         new messageMinMax(1, 0));
		minMaxKeyMap.put(COACH_GOAL_SEND_EMAIL,         new messageMinMax(5, 10));
		minMaxKeyMap.put(COACH_OPP_NONE,                new messageMinMax(1, 0));
		minMaxKeyMap.put(COACH_OPP_NO_LOG,              new messageMinMax(3, 0));
		minMaxKeyMap.put(COACH_OPP_NO_APPLICATION,      new messageMinMax(3, 0));
		minMaxKeyMap.put(COACH_PERSONAL_NOTE,           new messageMinMax(0, 1));
		minMaxKeyMap.put(COACH_PASSWORD_WARNING,        new messageMinMax(0, 1));
		minMaxKeyMap.put(COACH_LOG_RECALL,              new messageMinMax(0, 3));
		//minMaxKeyMap.put(COACH_LOG_INTERVIEW,           new messageMinMax(0, 0));
		//minMaxKeyMap.put(COACH_LOG_FAILURE,             new messageMinMax(0, 0));
		//minMaxKeyMap.put(COACH_LOG_SUCCESS,             new messageMinMax(0, 0));
	}
	
	/*
	Arrivée sur le site
	Heure d'arrivée 20 minutes après l'heure paramétrée
	L'heure d'arrivée est avancée de -5 minutes : normal
	L'heure d'arrivée est avancée de -20 minutes : warning + Invitation à changer le réglage
	Heure de quitter le site
	Signaler l'heure de quitter le site 5 minutes avant.
	Signaler l'heure de quitter le site si heure dépassée de 20+ minutes
	Opportunités
	Pas d'opportunité créée : indiquer la rubrique des opportunités, demander d'aller chercher des sites web d'emploi (cf chomothèque). Indiquer ce qu'est une opportunité :offre, discussion avec un ami, un collègue, information dans les journaux, sur un site, prise de contact avec un cabinet.
	Objectifs
	Signaler lors de l'arrivée sur le site le premier jour de la fin d'une période que les résultats sont disponibles.
	Insister de faire envoyer les résultats par mail
	Journaux
	Pas de journal dans les opportunités (>50%) : signaler l'importance des journaux pour suivre les opportunités
	Pas de candidature dans les journaux (>50%, +2 opportunités) : rappeler de se porter candidat aux offres pour avoir des chances. Sinon ça ne sert à rien.
	Nouvelle entrée de journal sur un rappel : informer de la création d'un post-it 3 jours avant. (seulement 3x). Rappeler de préparer les éléments fondamentaux d'un appel.
	Nouvelle entrée de journal sur un entretien : informer de la création d'un post-it 3 jours avant. (seulement 3x). Informer de la nécessité de se préparer dès maintenant.
	Nouvelle entrée de journal sur un échec : inciter à continuer en cherchant d'autres opportunités (citer la plus avancée)
	Nouvelle entrée de journal sur une réussite: inciter à réfléchir sur la pertinence de l'emploi.
    */
	
}
