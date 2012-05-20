package com.TheJobCoach.webapp.util.client;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;

public class CheckedTextField extends TextBox implements IExtendedField {

	String regexp;
	String defaultValue = null;
	
	@Override
	public boolean getIsDefault()
	{
		if (defaultValue == null) return true;
		return defaultValue.equals(getValue());
	}
	
	@Override
	public boolean isValid()
	{
		if (getValue() == null) return false;
		return getValue().matches(regexp);
	}

	IChanged changed;

	private void checkUserValue(boolean init)
	{
		if (changed != null) changed.changed(isValid(), getIsDefault(), init);
	}
	
	public void setValue(String value)
	{
		super.setValue(value);		
		checkUserValue(true);
	}
	
	private void init(IChanged changed, String regexp)
	{
		this.changed = changed;
		this.regexp = regexp;

		ValueChangeHandler<String> changeH = new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				checkUserValue(false);
			}
		};

		KeyUpHandler changeKey = new KeyUpHandler()	{
			@Override
			public void onKeyUp(KeyUpEvent event) {
				checkUserValue(false);
			}
		};
		addValueChangeHandler(changeH);
		addKeyUpHandler(changeKey);
		checkUserValue(true);		
	}

	public CheckedTextField(IChanged changed, String regexp)
	{
		init(changed, regexp); 
	}
	
	public void setDefault(String defaultValue)
	{
		this.defaultValue = defaultValue;
		checkUserValue(true);
	}
	
	public CheckedTextField(IChanged changed, String regexp, String init)
	{
		setDefault(init);
		init(changed, regexp);
		setValue(init);
	}

	@Override
	public void resetToDefault() 
	{
		setText(this.defaultValue);
	}
}
