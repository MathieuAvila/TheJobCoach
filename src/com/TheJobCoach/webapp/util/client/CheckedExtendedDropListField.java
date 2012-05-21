package com.TheJobCoach.webapp.util.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.ListBox;

public class CheckedExtendedDropListField implements IExtendedField {

	String defaultValue = null;

	ListBox tb = null;

	public ListBox getItem()
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
		if (getValue() == "") return false;
		return true;
	}

	IChanged changed;

	private void checkUserValue(boolean init)
	{
		if (changed != null) changed.changed(isValid(), getIsDefault(), init);
	}

	public void setValue(String value)
	{
		for (int i=0; i != tb.getItemCount(); i++)
		{
			//System.out.println("VALUE "+ value + "+"+tb.getValue(i));
			if (tb.getValue(i).equals(value))
				tb.setSelectedIndex(i);
		}
		checkUserValue(true);
	}

	private void init(
			ListBox tb, 
			List<String> values, 
			Map<String, String> texts, 
			String prefix,
			IChanged changed)
	{
		this.tb = tb;

		for (String v: values)
		{
			tb.insertItem(v, v, tb.getItemCount());
		}

		this.changed = changed;
		
		ChangeHandler changeH = new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) 
			{
				checkUserValue(false);
			}
		};

		KeyUpHandler changeKey = new KeyUpHandler()	{
			@Override
			public void onKeyUp(KeyUpEvent event) {
				checkUserValue(false);
			}
		};
		tb.addChangeHandler(changeH);
		tb.addKeyUpHandler(changeKey);
		checkUserValue(true);		
	}

	public CheckedExtendedDropListField(
			ListBox tb, 
			List<String> values, 
			Map<String, String> texts, 
			String prefix,
			IChanged changed)
	{
		init(tb, values, texts, prefix, changed); 
	}

	public CheckedExtendedDropListField(
			List<String> values, 
			Map<String, String> texts, 
			String prefix,
			IChanged changed)
	{
		init(new ListBox(), values, texts, prefix, changed); 
	}

	public void setDefault(String defaultValue)
	{
		this.defaultValue = defaultValue;
		checkUserValue(true);
	}

	public CheckedExtendedDropListField(
			ListBox tb, 
			List<String> values, 
			Map<String, String> texts, 
			String prefix, 
			IChanged changed, String regexp, String init)
	{
		setDefault(init);
		init(tb, values, texts, prefix, changed);
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
		//System.out.println("INDEX " + tb.getSelectedIndex() + " " + tb.getItemCount());
		if ((tb.getSelectedIndex() >= 0) &&(tb.getSelectedIndex() < tb.getItemCount()))
			return tb.getValue(tb.getSelectedIndex());
		return "";
	}
}
