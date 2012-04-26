package com.TheJobCoach.webapp.util.client;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;

public class CheckedTextField extends TextBox {

	String regexp;

	public boolean isValid()
	{
		if (getValue() == null) return false;
		return getValue().matches(regexp);
	}

	IChanged changed;

	private void checkUserValue(boolean init)
	{
		//System.out.println("VALID " + isValid() + " VAL: " + getValue() + " INIT " + init);
		if (changed != null) changed.changed(isValid(), init);
	}

	public void setText(String value)
	{
		super.setText(value);
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
	
	public CheckedTextField(IChanged changed, String regexp, String init)
	{
		init(changed, regexp);
		setValue(init);
	}
}
