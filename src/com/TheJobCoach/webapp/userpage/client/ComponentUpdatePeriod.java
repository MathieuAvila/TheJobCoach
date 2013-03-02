package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class ComponentUpdatePeriod extends VerticalPanel 
{	
	final Lang lang = GWT.create(Lang.class);
	final ListBox periodType = new ListBox();
	final IntegerBox intBox = new IntegerBox();
	final DateBox dateBox = new DateBox();
	
	UpdatePeriod obj;
	
	public ComponentUpdatePeriod(Vector<UserDocumentId> docList, UpdatePeriod obj)
	{
		this.obj = obj;
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{		
		final CaptionPanel panel = new CaptionPanel();
		add(panel);
		final VerticalPanel vp = new VerticalPanel();
		panel.setContentWidget(vp);
		panel.setCaptionText(lang._TextPeriodTitle());
		
		Grid grid = new Grid(3, 2);
		grid.setBorderWidth(0);
		grid.setSize("100%", "100%");
		
		Label lblType = new Label(lang._TextPeriodUpdatePeriodFrequencyType());
		grid.setWidget(0, 0, lblType);		
		grid.setWidget(0, 1, periodType);
		int index = 0;
		for (UpdatePeriod.PeriodType t: UpdatePeriod.PeriodType.values() )
		{
			periodType.addItem(lang.frequencyTypeMap().get("frequencyTypeMap_" + UpdatePeriod.periodType2String(t)), t.toString());
			if (obj.periodType.equals(t))
			{
				periodType.setItemSelected(index, true);
			}			
			index++;
		}
		periodType.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event)
			{
				String text = periodType.getItemText(periodType.getSelectedIndex());
				obj.periodType = UpdatePeriod.string2PeriodType(text);				
			}});
		
		Label lblCount = new Label(lang._TextPeriodUpdatePeriodFrequencyCount());
		grid.setWidget(1, 0, lblCount);		
		grid.setWidget(1, 1, intBox);		
		intBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event)
			{				
				obj.length = intBox.getValue();				
			}});
		
		Label lblLast = new Label(lang._TextPeriodLastUpdate());
		grid.setWidget(2, 0, lblLast);
		grid.setWidget(2, 1, dateBox);		
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event)
			{
				obj.last = event.getValue();
			}});		
	}
}
