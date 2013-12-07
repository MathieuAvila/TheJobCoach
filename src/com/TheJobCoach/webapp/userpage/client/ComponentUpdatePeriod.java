package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.shared.UpdatePeriod;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.datepicker.client.DateBox;

public class ComponentUpdatePeriod extends CaptionPanel 
{	
	final Lang lang = GWT.create(Lang.class);
	final ListBox periodType = new ListBox();
	final IntegerBox intBox = new IntegerBox();
	final DateBox dateBox = new DateBox();
	final CheckBox needRecallCheck = new CheckBox();		
	
	UpdatePeriod obj;
	
	public enum RecallType { RECONTACT, UPDATE };
	
	RecallType recontactOrUpdate;
	
	public ComponentUpdatePeriod(UpdatePeriod obj, RecallType recontactOrUpdate)
	{
		this.obj = obj;
		this.recontactOrUpdate = recontactOrUpdate;
	}

	private void checkNeedRecall()
	{
		periodType.setEnabled(obj.needRecall);
		intBox.setEnabled(obj.needRecall);
	}
	
	void setUpdatePeriod(UpdatePeriod obj)
	{
		this.obj = obj;
		dateBox.setValue(obj.last);
		periodType.setEnabled(obj.needRecall);
		intBox.setEnabled(obj.needRecall);
		intBox.setValue(obj.length);
		needRecallCheck.setValue(obj.needRecall);
		int index = 0;
		periodType.clear();
		int sIndex = 0;
		for (UpdatePeriod.PeriodType t: UpdatePeriod.PeriodType.values() )
		{
			periodType.addItem(lang.frequencyTypeMap().get("frequencytypeMap_" + UpdatePeriod.periodType2String(t)), t.toString());
			if (obj.periodType.equals(t))
			{
				sIndex = index;
			}			
			index++;
		}
		periodType.setItemSelected(sIndex, true);
		checkNeedRecall();
	}
	
	/** for UT purposes only */
	public void setTimeCount(int c)
	{
		intBox.setText(String.valueOf(c));
		obj.length = c;
	}
	
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{		
		setCaptionText(recontactOrUpdate ==  RecallType.RECONTACT ? lang._TextPeriodTitleRecall() : lang._TextPeriodTitleUpdate());
		
		Grid grid = new Grid(3, 2);
		grid.setBorderWidth(0);
		grid.setSize("100%", "100%");

		Label lblNeedRecall = new Label(lang._TextPeriodUpdateNeedRecall());
		grid.setWidget(0, 0, lblNeedRecall);		
		grid.setWidget(0, 1, needRecallCheck);
		needRecallCheck.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				obj.needRecall = needRecallCheck.getValue();
				checkNeedRecall();
			}});

		Label lblType = new Label(lang._TextPeriodUpdatePeriodFrequencyType());
		grid.setWidget(1, 0, lblType);		
		HorizontalPanel hpType = new HorizontalPanel();		
		grid.setWidget(1, 1, hpType);
		periodType.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event)
			{
				String text = periodType.getValue(periodType.getSelectedIndex());
				obj.periodType = UpdatePeriod.string2PeriodType(text);
			}});
		
		hpType.add(intBox);
		hpType.add(periodType);
		
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
		dateBox.setWidth("100%");
		
		setUpdatePeriod(obj);
			
		setContentWidget(grid);					
	}
}
