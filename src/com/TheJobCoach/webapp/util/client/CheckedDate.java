package com.TheJobCoach.webapp.util.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.datepicker.client.DateBox;

public class CheckedDate implements IExtendedField {

	DateBox dateBox = new DateBox();
	
	Date defaultValue = new Date();
	
	@Override
	public String getValue()
	{
		return FormatUtil.getDateString(dateBox.getValue());
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
		dateBox.setValue(FormatUtil.getStringDate(value));		
		checkUserValue(true);
	}
	
	private void init()
	{
		ValueChangeHandler<Date> changeH = new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event)
			{
				checkUserValue(false);				
			}
		};
		dateBox.addValueChangeHandler(changeH);
		checkUserValue(true);		
	}

	public CheckedDate()
	{
		super();
		init(); 
	}
	
	public CheckedDate(String init)
	{
		super();
		setDefault(init);
		init();
		setValue(init);
	}

	public DateBox getItem()
	{
		return dateBox;
	}
	
	@Override
	public void resetToDefault() 
	{
		dateBox.setValue(this.defaultValue);
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
		defaultValue = FormatUtil.getStringDate(value);
	}
}
