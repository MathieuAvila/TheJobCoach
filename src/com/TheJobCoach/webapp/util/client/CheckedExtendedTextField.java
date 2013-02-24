package com.TheJobCoach.webapp.util.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBoxBase;

public class CheckedExtendedTextField implements IExtendedField {

	String regexp;
	String defaultValue = null;
	
	TextBoxBase tb = null;
	
	public TextBoxBase getItem()
	{
		return tb;
	}
	
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
		System.out.println("A" + getValue() + "A");
		return getValue().matches(regexp);
	}

	List<IChanged> listChanged = new ArrayList<IChanged>();

	private void checkUserValue(boolean init)
	{
		for (IChanged changed: listChanged)
			changed.changed(isValid(), getIsDefault(), init);
	}
	
	public void setValue(String value)
	{
		tb.setValue(value);		
		checkUserValue(true);
	}
	
	private void init(TextBoxBase tb, String regexp)
	{
		this.tb = tb;
		
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
		tb.addValueChangeHandler(changeH);
		tb.addKeyUpHandler(changeKey);
		checkUserValue(true);		
	}

	public CheckedExtendedTextField(TextBoxBase tb, String regexp)
	{
		init(tb, regexp); 
	}
	
	public void setDefault(String defaultValue)
	{
		this.defaultValue = defaultValue;
		checkUserValue(true);
	}
	
	public CheckedExtendedTextField(TextBoxBase tb, String regexp, String init)
	{
		setDefault(init);
		init(tb, regexp);
		setValue(init);
	}

	@Override
	public void resetToDefault() 
	{
		setValue(this.defaultValue);
	}

	@Override
	public String getValue() 
	{		
		return tb.getValue();
	}

	@Override
	public void registerListener(IChanged listener) 
	{
		listChanged.add(listener);	
	}
}
