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
	
	@Key("titletodo")
	String Text_titletodo();
	
	@Key("todointerview")
	String Text_todointerview();
	
	@Key("todosite")
	String Text_todosite();
	
	@Key("todocontact")
	String Text_todocontact();
	
	@Key("todoopportunityrecall")
	String Text_todoopportunityrecall();

	@Key("todoevent")
	String Text_todoevent();
}
