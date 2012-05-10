package com.TheJobCoach.webapp.util.client;

import com.google.gwt.i18n.client.Constants;

public interface Lang extends Constants {

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
}
