package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;
import java.util.Date;

public class NewsInformation implements Serializable {

	
	private static final long serialVersionUID = 1115255124601443731L;
		
	public String ID;
	public Date created;
	public String title;
	public String text;
	
	public NewsInformation(String _ID, Date _created, String _title, String _text)
	{
		ID = _ID;
		created = _created;
		title = _title;
		text = _text;
	}
	
	public NewsInformation()
	{
		created = new Date();
		title = "";
		ID = "";
		text = "";
	}
}
