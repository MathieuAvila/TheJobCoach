package com.TheJobCoach.webapp.util.client;

import com.google.gwt.user.client.ui.Label;

public class CheckedLabel extends Label implements IChanged {

	boolean isChanged = false;
	boolean isError = false;
	
	private void setStyle()
	{
		String style="label-status-" 
				+ (isError ? "error" : "ok")
				+ (isChanged ? "-c" : "-nc");
		//System.out.println("STYLE:" + style);
		setStyleName(style);			
	} 
	
	public void setStatus(boolean ok)
	{
		isError = !ok;
		setStyle();
	}
		
	@Override
	public void changed(boolean ok, boolean isDefault, boolean init) 
	{
		this.isChanged = !isDefault;
		setStatus(ok);
		if (up != null) up.changed(ok, isDefault, init);
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
