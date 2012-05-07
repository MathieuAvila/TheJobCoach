package com.TheJobCoach.webapp.util.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

public class DialogToolBox 
{
	public static void connectTextEnterToButton(final Button btn, TextBox wgt)
	{
		KeyUpHandler changeKey = new KeyUpHandler()	{
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) 
			{
				btn.fireEvent( new GwtEvent<ClickHandler>() {
			        @Override
			        public com.google.gwt.event.shared.GwtEvent.Type<ClickHandler> getAssociatedType() {
			        return ClickEvent.getType();
			        }
			        @Override
			        protected void dispatch(ClickHandler handler) {
			            handler.onClick(null);
			        }
			   });
			}			
		};
		};
		wgt.addKeyUpHandler(changeKey);
	}
	
	public static void addVerticalSpacer(CellPanel insidePanel, String size)
	{
		Panel result = new FlowPanel();
		insidePanel.add(result);
		insidePanel.setCellHeight(result, size);
		result.setHeight(size);
	}
	
	public static void addHorizontalSpacer(CellPanel insidePanel, String size)
	{
		Panel result = new FlowPanel();
		insidePanel.add(result);
		insidePanel.setCellWidth(result, size);
		result.setWidth(size);
	}
}
