package com.TheJobCoach.webapp.userpage.client;

import java.util.Map;

import com.google.gwt.i18n.client.Constants;

public interface LangAccount extends Constants {

	@Key("accountTypeMap")
	Map<String, String> accountTypeMap();

	@Key("coachNameMap")	
	Map<String, String> coachNameMap();

	@Key("coachDescriptionMap")	
	Map<String, String> coachDescriptionMap();

	@Key("accountStatusMap")	
	Map<String, String> accountStatusMap();


}
