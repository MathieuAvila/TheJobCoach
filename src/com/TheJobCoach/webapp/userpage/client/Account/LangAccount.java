package com.TheJobCoach.webapp.userpage.client.Account;

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


	@Key("accounttype")
	String Text_AccountType();
	
	@Key("jobtitle")
	String Text_JobTitle();
	
	@Key("actualstatus")
	String Text_ActualStatus();
	
	@Key("skills")
	String Text_Skills();
	
	@Key("myvirtualcoach")
	String Text_MyVirtualCoach();
	
	@Key("visible_seeker")
	String Text_VisibleProfileSeeker();

	@Key("visible_coach")
	String Text_VisibleProfileCoach();

	@Key("visible_recruiter")
	String Text_VisibleProfileRecruiter();

	
	@Key("title_account")
	String Text_TitleAccount();
	
	@Key("title_personalinformation")
	String Text_TitlePersonalInformation();
	
	@Key("title_coach")
	String Text_TitleCoach();
	
	@Key("title_visibility")
	String Text_TitleVisibility();

	
	@Key("changespassword")
	String Text_changespassword();
	
	@Key("typepassword")
	String _Text_typepassword();

	@Key("retypepassword")
	String _Text_retypepassword();

	// deletion procedure
	
	@Key("dangerousSettings")
	String dangerousSettings();
	
	@Key("deleteAccount")
	String deleteAccount();
	
	@Key("deletionPlanned")
	String deletionPlanned();
	
	@Key("cancelDeletion")	
	String cancelDeletion();
	
	@Key("deleteConfirm")	
	String deleteConfirm();
	
	@Key("cancelDeleteConfirm")
	String cancelDeleteConfirm();
	
	// nom, prénom

	@Key("myFirstName")	
	String myFirstName();
	
	@Key("myLastName")
	String myLastName();

	
	// photo change
	@Key("myPhoto")	
	String myPhoto();
	
	@Key("changeMyPhoto")	
	String changeMyPhoto();
	
	@Key("choosePhotoFile")	
	String choosePhotoFile();
	
}
