package com.TheJobCoach.webapp.mainpage.client;

import com.google.gwt.i18n.client.Constants;

public interface Lang extends Constants {

	@Key("slogan")
	String _TextSlogan();
		
	@ Key("username")
	String _TextUserName();
	
	@ Key("userpassword")
	String _TextUserPassword();
	
	@ Key("useremail")
	String _TextUserEMail();
	
	@ Key("userfirstname")
	String _TextUserFirstName();
	
	@ Key("createaccount")
	String _TextCreateAccount();
	
	@ Key("login")
	String _TextLogin();
	
	@ Key("notyetregistered")
	String _TextNotYesRegistered();
	
	@ Key("whoweare")
	String _TextWhoWeAre();
	
	@ Key("termsofuse")
	String _TextTermsOfUse();
	
	@ Key("confidentiality")
	String _TextConfidentiality();
	
	String panelFooter_html();
}
