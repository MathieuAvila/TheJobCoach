package com.TheJobCoach.webapp.util.client;

import java.util.ArrayList;
import java.util.List;

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

	private void checkUserValue(boolean init)
	{
		for (IChanged changed: listChanged)
			changed.changed(isValid(), getIsDefault(), init);
	}
	
	public void setValue(String value)
	{
		super.setValue(value);		
		checkUserValue(true);
	}
	
	private void init(String regexp)
	{
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

	public CheckedTextField(String regexp)
	{
		init(regexp); 
	}
	
	public void setDefault(String defaultValue)
	{
		this.defaultValue = defaultValue;
		checkUserValue(true);
	}
	
	public CheckedTextField(String regexp, String init)
	{
		setDefault(init);
		init(regexp);
		setValue(init);
	}

	@Override
	public void resetToDefault() 
	{
		setValue(this.defaultValue);
	}
	
	List<IChanged> listChanged = new ArrayList<IChanged>();

	@Override
	public void registerListener(IChanged listener) 
	{
		listChanged.add(listener);	
	}
}
