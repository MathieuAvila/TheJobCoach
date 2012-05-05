package com.TheJobCoach.webapp.util.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class DialogBlockOkCancel extends HorizontalPanel {
	
	final Lang lang = GWT.create(Lang.class);
	ButtonImageText btnOk = new ButtonImageText(ButtonImageText.Type.OK, lang._TextOk());
	ButtonImageText btnCancel = new ButtonImageText(ButtonImageText.Type.CANCEL, lang._TextCancel());
	
	public DialogBlockOkCancel(String okMsg, final DialogBox upper)
	{
		super.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		//super.setWidth("100%");
		if (okMsg != null) btnOk = new ButtonImageText(ButtonImageText.Type.OK, okMsg);
		HorizontalPanel spacer = new HorizontalPanel();
		spacer.setWidth("20px");		
		HorizontalPanel innerHp = new HorizontalPanel();
		super.add(innerHp);		
		innerHp.add(btnOk);
		innerHp.add(spacer);
		innerHp.add(btnCancel);		
		if (upper != null)
		{
			btnCancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					upper.hide();
				}});
		}
	}
	
	public Button getOk()
	{
		return btnOk;
	}
	
	public void setEnabled(boolean value)
	{		
		btnOk.setEnabled(value);
		btnCancel.setEnabled(value);
	}
	
}
