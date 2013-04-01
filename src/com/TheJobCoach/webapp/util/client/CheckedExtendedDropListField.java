package com.TheJobCoach.webapp.util.client;

import java.util.ArrayList;
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

	private void checkUserValue(boolean init)
	{
		for (IChanged changed: listChanged)
			changed.changed(isValid(), getIsDefault(), init);
	}

	public void setValue(String value)
	{
		for (int i=0; i != tb.getItemCount(); i++)
		{
			if (tb.getValue(i).equals(value))
			{
				tb.setSelectedIndex(i);
			}
		}
		checkUserValue(true);
	}

	private void init(
			ListBox tb, 
			List<String> values, 
			Map<String, String> texts, 
			String prefix)
	{
		this.tb = tb;

		if (texts != null)
		for (String v: values)
		{
			tb.insertItem(texts.get(prefix + v), v, tb.getItemCount());
		}

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
			String prefix)
	{
		init(tb, values, texts, prefix); 
	}

	public CheckedExtendedDropListField(
			List<String> values, 
			Map<String, String> texts, 
			String prefix)
	{
		init(new ListBox(), values, texts, prefix); 
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
			String regexp, String init)
	{
		setDefault(init);
		init(tb, values, texts, prefix);
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
		if ((tb.getSelectedIndex() >= 0) &&(tb.getSelectedIndex() < tb.getItemCount()))
			return tb.getValue(tb.getSelectedIndex());
		return "";
	}
	
	List<IChanged> listChanged = new ArrayList<IChanged>();

	@Override
	public void registerListener(IChanged listener) 
	{
		listChanged.add(listener);	
	}
}
