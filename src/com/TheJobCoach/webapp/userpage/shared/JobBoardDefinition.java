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

	static {
		JOBBOARD_MAP.put(APEC, APEC_ID);
		JOBBOARD_MAP.put(POLE_EMPLOI, POLE_EMPLOI_ID);
	}

}
