package com.TheJobCoach.webapp.util.client;

import java.util.Map;

import com.google.gwt.user.client.ui.Label;

public class DynamicLabel extends Label implements IChanged {
	
	public void setValue()
	{
		if (from != null)
		{
			String value = from.getValue();
			setText(values.get(prefix + value));
		}
	}
	
	@Override
	public void changed(boolean ok, boolean isDefault, boolean init) 
	{
		setValue();
	}
	
	IExtendedField from = null;
	Map<String, String> values;
	String prefix;
	
	void init(boolean initial, IExtendedField from, Map<String, String> values, String prefix)
	{
		this.from = from;
		this.values = values;
		this.prefix = prefix;
	}
	
	public DynamicLabel(IExtendedField from, Map<String, String> values, String prefix)
	{
		super("");
		this.setStylePrimaryName("label-status-ok-nc");
		init(false, from, values, prefix);
		setSource(from);
	}

	public void setSource(IExtendedField from)
	{
		this.from = from;
		from.registerListener(this);
		setValue();
	}

	public DynamicLabel(Map<String, String> values, String prefix)
	{
		super("");
		init(false, null, values, prefix);
	}

	
}
