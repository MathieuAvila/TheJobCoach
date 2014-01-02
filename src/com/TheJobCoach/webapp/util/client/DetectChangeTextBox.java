package com.TheJobCoach.webapp.util.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

public class DetectChangeTextBox extends TextBox
{
	public interface CustomChangeHandler 
	{
		public void call();
	}
	
	CustomChangeHandler customChangeHandler;
	
	public DetectChangeTextBox() {
		super();
		sinkEvents(Event.ONPASTE);
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
		case Event.ONPASTE:
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {

				@Override
				public void execute() {
					if (customChangeHandler != null) 
						customChangeHandler.call();
				}

			});
			break;
		}
	}
	
	public void setCustomChangeHandler(CustomChangeHandler customChangeHandler)
	{
		this.customChangeHandler = customChangeHandler;
	}
}
