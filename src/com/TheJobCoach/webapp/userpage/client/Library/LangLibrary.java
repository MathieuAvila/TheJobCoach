package com.TheJobCoach.webapp.userpage.client.Library;

import java.util.Map;

import com.google.gwt.i18n.client.Constants;

public interface LangLibrary extends Constants {
	
	@Key("selectName")
	String selectName();
	
	@Key("selectLocation")
	String selectLocation();

	@Key("selectPeople")
	String selectPeople();
	
	@Key("selectSector")
	String selectSector();
	
	@Key("selectType")
	String selectType();
	
	@Key("selectNone")
	String selectNone();
	
	
	@Key("publicTarget")
	String publicTarget();
	
	@Key("sector")
	String sector();

	@Key("siteType")
	String siteType();
	
	
	@Key("publicTargetMapMap")
	Map<String, String> publicTargetMap();
	
	@Key("sectorMapMap")
	Map<String, String> sectorMap();

	@Key("siteTypeMapMap")
	Map<String, String> siteTypeMap();
	
	@Key("locationMapMap")
	Map<String, String> locationMap();

}
