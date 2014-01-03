package com.TheJobCoach.webapp.util.client;

import java.util.Map;

import com.google.gwt.i18n.client.Constants;

public interface LangUtil extends Constants {

	@Key("ok")
	String _TextOk();

	@Key("cancel")
	String _TextCancel();

	@Key("INFO")
	String _TextINFO();

	@Key("WARNING")
	String _TextWARNING();

	@Key("QUESTION")
	String _TextQUESTION();

	@Key("ERROR")
	String _TextERROR();

	@Key("WAIT")
	String _TextWAIT();

	@Key("NONE")
	String _TextNONE();

	@Key("Apply")
	String _TextApply();

	@Key("Reset")
	String _TextReset();
	
	@Key("yesNoMap")	
	Map<String, String> yesNoMap();
}
