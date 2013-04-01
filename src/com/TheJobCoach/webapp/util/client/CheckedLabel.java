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
	}
	
	void init(boolean initial)
	{
		setStatus(initial);
	}
	
	public CheckedLabel(String text, boolean tip, IExtendedField from)
	{
		super(text + (tip ? " [*]" : ""));
		if (from != null) from.registerListener(this);
		init(false);
	}
	CheckedLabel(String text, boolean tip)
	{
		super(text + (tip ? " [*]" : ""));
		init(false);
	}

}
