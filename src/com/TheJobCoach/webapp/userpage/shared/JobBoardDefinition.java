package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.HashMap;

public class JobBoardDefinition implements Serializable {

	private static final long serialVersionUID = 1115255124501443736L;

	final static String APEC = "Apec";
	final static String POLE_EMPLOI = "Pôle Emploi";

	final public static String APEC_ID = "apec";
	final public static String POLE_EMPLOI_ID = "poleemploi";
	
	public static HashMap<String, String> JOBBOARD_MAP =  new HashMap<String, String>(); 
	public static HashMap<String, String> JOBBOARD_REGEXP =  new HashMap<String, String>(); 

	static {
		JOBBOARD_MAP.put(APEC_ID, APEC_ID);
		JOBBOARD_MAP.put(POLE_EMPLOI_ID, POLE_EMPLOI_ID);
		
		JOBBOARD_REGEXP.put(APEC_ID, "http://.*apec.fr.*");
		JOBBOARD_REGEXP.put(POLE_EMPLOI_ID, "http://candidat.pole-emploi.fr/.*");
	}

}
