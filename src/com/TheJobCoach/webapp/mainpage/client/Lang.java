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
	
	@ Key("userlastname")
	String _TextUserLastName();
	
	@ Key("createaccount")
	String _TextCreateAccount();

	@ Key("lostcredentials")
	String _TextLostCredentials();
	
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
	
	@ Key("login_invalid_user_password")
	String _TextLoginNoSuchLoginPassword(); 
	
	@ Key("login_not_validated")
	String _TextLoginNotValidated();
	
	@ Key("create_login_already_exist")
	String _TextCreateLoginAlreadyExists();
	
	@ Key("create_email_already_exist")
	String 	_TextCreateEmailAlreadyExists();
	
	@ Key("create_login_unexpected_error")
	String _TextCreateLoginUnexpectedError();
	
	@ Key("create_login_success")
	String _TextCreateAccountSuccess();
	
	@ Key("validate_login_no_such_login")
	String _TextValidateLoginNoSuchLogin(); 
	
	@ Key("validate_login_unexpected_error")
	String _TextValidateLoginUnexpectedError(); 
	
	@ Key("validate_login_wait")
	String _TextValidateLoginWait(); 
	
	@ Key("validate_login_go_to_login")
	String _TextValidateGoToLogin(); 
	
	@ Key("validate_succesfully_validated")
	String _TextValidateSuccess(); 
	
	@Key("createaccounttitle")
	String _TextCreateAccountTitle(); 
	
	@Key("createaccountok")
	String _TextCreateAccountOk(); 
	
	@Key("createaccountcancel")
	String _TextCreateCancel();

	@Key("createaccountpasswordcheck")
	String _TextUserPasswordCheck();

	@Key("lostcredentialsexplanation")
	String _TextLostCredentialsExplanation();
	
	@Key("lostcredentialsvalidate")
	String _TextLostCredentialsValidate(); 
	
	@Key("lostcredentialsok")
	String _TextLostCredentialsOk();
	
	@Key("lostcredentialserror")
	String _TextLostCredentialsError();
}
