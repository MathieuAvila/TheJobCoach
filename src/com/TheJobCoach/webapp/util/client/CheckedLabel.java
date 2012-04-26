package com.TheJobCoach.webapp.util.client;

import com.google.gwt.user.client.ui.Label;

public class CheckedLabel extends Label implements IChanged {

	public void setStatus(boolean ok)
	{
		setStyleName(ok ? "label-create-account-ok" : "label-create-account-error");
	}
	
	@Override
	public void changed(boolean ok, boolean init) 
	{
		//System.out.println("OK is: " + ok);
		setStatus(ok);
		if (up != null) up.changed(ok, init);
	}
	
	IChanged up;
	
	void init(IChanged up, boolean initial)
	{
		this.up = up;
		setStatus(initial);
	}
	
	public CheckedLabel(String text, boolean tip, IChanged up)
	{
		super(text + (tip ? " [*]" : ""));
		init(up, false);
	}
	
	public CheckedLabel(String text, boolean tip, IChanged up, boolean initial)
	{
		super(text + (tip ? " [*]" : ""));
		init(up, initial);
	}

}
