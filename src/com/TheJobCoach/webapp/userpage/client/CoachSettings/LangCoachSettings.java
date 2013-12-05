package com.TheJobCoach.webapp.userpage.client.CoachSettings;

import java.util.Map;

import com.google.gwt.i18n.client.Constants;

public interface LangCoachSettings extends Constants {

	@Key("coachNameMap")	
	Map<String, String> coachNameMap();

	@Key("coachDescriptionMap")	
	Map<String, String> coachDescriptionMap();

	@Key("myvirtualcoach")
	String Text_MyVirtualCoach();

	@Key("title_coach")
	String Text_TitleCoach();
	
}
