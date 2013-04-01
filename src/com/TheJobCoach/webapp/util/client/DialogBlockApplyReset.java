package com.TheJobCoach.webapp.util.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class DialogBlockApplyReset extends HorizontalPanel 
{
	
	final Lang lang = GWT.create(Lang.class);
	ButtonImageText btnApply = new ButtonImageText(ButtonImageText.Type.OK, lang._TextApply());
	ButtonImageText btnReset = new ButtonImageText(ButtonImageText.Type.CANCEL, lang._TextReset());
	
	List<IExtendedField> listValues = null;
	
	IApply apply;
	
	public interface IApply
	{
		public void apply();
	}
	
	public DialogBlockApplyReset(final List<IExtendedField> listValues, final IApply apply)
	{
		this.apply = apply;
		this.listValues = listValues;
		super.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		super.setWidth("100%");
		HorizontalPanel spacer = new HorizontalPanel();
		spacer.setWidth("20px");		
		HorizontalPanel innerHp = new HorizontalPanel();
		super.add(innerHp);		
		innerHp.add(btnApply);
		innerHp.add(spacer);
		innerHp.add(btnReset);		
		btnReset.addClickHandler(new ClickHandler() 
		{
			@Override
			public void onClick(ClickEvent event) 
			{
				for (IExtendedField elem : listValues)
				{
					elem.resetToDefault();
				}
				hasEvent(); 
			}});
		btnApply.addClickHandler(new ClickHandler() 
		{
			@Override
			public void onClick(ClickEvent event) 
			{
				apply.apply();
			}});
		
	}

	public void hasEvent() 
	{
		boolean ok = true;
		boolean oneIsNotDefault = false;
		for (IExtendedField elem : listValues)
		{
			ok = ok && (elem.isValid());
			oneIsNotDefault = oneIsNotDefault || !elem.getIsDefault();
		}
		btnApply.setEnabled(oneIsNotDefault && ok);
		btnReset.setEnabled(oneIsNotDefault && ok);
	}
}
