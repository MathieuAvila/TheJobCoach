package com.TheJobCoach.webapp.util.client;

import java.util.ArrayList;
import java.util.List;

import com.TheJobCoach.webapp.util.client.timepicker.TimePicker;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class CheckedTime implements IExtendedField {

	TimePicker timePicker = new TimePicker();
	
	String defaultValue = "";
	
	final public static String DAY_START = "0";
	final public static String DAY_MIDDAY = String.valueOf( (12 * 60) * 60 * 1000 );
	final public static String DAY_END = String.valueOf( (23 * 60 + 59) * 60 * 1000 );
	
	@Override
	public String getValue()
	{
		return String.valueOf(timePicker.getValue());
	}
	
	@Override
	public boolean getIsDefault()
	{
		if (defaultValue == null) return true;
		System.out.println(getValue() + " " + defaultValue+ " " + defaultValue.equals(getValue()));
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
		timePicker.setValue(Long.parseLong(value));		
		checkUserValue(true);
	}
	
	private void init()
	{
		ValueChangeHandler<Long> changeH = new ValueChangeHandler<Long>() {
			@Override
			public void onValueChange(ValueChangeEvent<Long> event)
			{
				checkUserValue(false);				
			}
		};
		timePicker.addValueChangeHandler(changeH);
		checkUserValue(true);		
	}

	public CheckedTime()
	{
		super();
		init(); 
	}
	
	public CheckedTime(String init)
	{
		super();
		setDefault(init);
		init();
		setValue(init);
	}

	public TimePicker getItem()
	{
		return timePicker;
	}
	
	@Override
	public void resetToDefault() 
	{
		timePicker.setValue(Long.parseLong(this.defaultValue));
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
		defaultValue = value;
	}
}
