package com.TheJobCoach.webapp.util.client;

import java.util.ArrayList;
import java.util.List;

import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;

public class CheckedCheckBox implements IExtendedField {

	CheckBox checkBox = new CheckBox();
	
	Boolean defaultValue = new Boolean(false);
	
	final static Boolean trueBool = new Boolean(true);
	final static Boolean falseBool = new Boolean(false);
	
	@Override
	public String getValue()
	{
		if (checkBox.getValue().equals(trueBool)) 
			return FormatUtil.trueString; 
		else 
			return FormatUtil.falseString;
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
		return true;
	}

	private void checkUserValue(boolean init)
	{
		for (IChanged changed: listChanged)
			changed.changed(isValid(), getIsDefault(), init);
	}
	
	public void setValue(String value)
	{
		Boolean sValue = new Boolean("1".equals(value));
		checkBox.setValue(sValue);		
		checkUserValue(true);
	}
	
	private void init()
	{
		ValueChangeHandler<Boolean> changeH = new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkUserValue(false);
			}
		};

		KeyUpHandler changeKey = new KeyUpHandler()	{
			@Override
			public void onKeyUp(KeyUpEvent event) {
				checkUserValue(false);
			}
		};
		checkBox.addValueChangeHandler(changeH);
		checkBox.addKeyUpHandler(changeKey);
		checkUserValue(true);		
	}

	public CheckedCheckBox()
	{
		super();
		init(); 
	}
	
	public CheckedCheckBox(String init)
	{
		super();
		setDefault(init);
		init();
		setValue(init);
	}

	public CheckBox getItem()
	{
		return checkBox;
	}
	
	@Override
	public void resetToDefault() 
	{
		checkBox.setValue(this.defaultValue);
	}
	
	List<IChanged> listChanged = new ArrayList<IChanged>();

	@Override
	public void registerListener(IChanged listener) 
	{
		listChanged.add(listener);	
	}

	@Override
	public void setDefault(String value)
	{
		if (value == null) defaultValue = falseBool;
		if (value.equals("1")) defaultValue = trueBool;
		else defaultValue = falseBool;
	}
}