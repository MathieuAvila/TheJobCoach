package com.TheJobCoach.webapp.userpage.client;

import java.util.Map;

import com.google.gwt.i18n.client.Constants;

public interface LangMyReports extends Constants {

	@Key("periodMap")
	Map<String, String> periodMap();

	@Key("formatMap")
	Map<String, String> formatMap();

	@Key("format")
	String format();

	@Key("period")
	String period();
	
	@Key("includeOpportunityDetail")
	String includeOpportunityDetail();
	
	@Key("includeLogDetail")
	String includeLogDetail();
	
	@Key("onlyLogPeriod")
	String onlyLogPeriod();
	
	@Key("startDate")
	String startDate();
	
	@Key("endDate")
	String endDate();
	
	
}
