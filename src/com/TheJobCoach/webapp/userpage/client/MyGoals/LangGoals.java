package com.TheJobCoach.webapp.userpage.client.MyGoals;

import java.util.Map;

import com.google.gwt.i18n.client.Constants;

public interface LangGoals extends Constants {

	@Key("periodMap")
	Map<String, String> periodMap();

	@Key("evaluationperiod")
	String evaluationPeriod();

	@Key("connectiontimes")
	String connectionTimes();

	@Key("opportunitygoals")
	String opportunityGoals();

	@Key("interviewgoals")
	String interviewGoals();

	@Key("phonecallgoals")
	String phonecallGoals();

	@Key("proposalGoals")
	String proposalGoals();



	@Key("evaluationperiodsub")
	String evaluationPeriodSub();


	@Key("imust_beforehour")
	String imust_beforehour();
	
	@Key("imustnot_afterhour")
	String imustnot_afterhour();
	
	@Key("imustnot_thisnumber")
	String imustnot_thisnumber();
	
	@Key("imust_createopp")
	String imust_createopp();
	
	@Key("imust_candidate")
	String imust_candidate();
	
	@Key("imust_interviews")
	String imust_interviews();
	
	@Key("imust_proposals")
	String imust_proposals();
	
	@Key("imust_phonecalls")
	String imust_phonecalls();
}
