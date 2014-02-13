package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.UtilService;
import com.TheJobCoach.webapp.util.client.UtilServiceAsync;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class PanelUpdate  extends SimplePanel implements EntryPoint {

	Label connectionTime;
	Panel rootPanel;
	UserId userId;

	int connectSec = 0;
	int previousTime = 0;
	boolean firstTime = true;
	Date today = new Date();

	static UtilServiceAsync utilService =  GWT.create(UtilService.class);

	Timer timer = new Timer() 
	{
		public void run() 
		{
			// Wait for next run.
			connectSec+=5;
			long total = connectSec + previousTime;
			long h = total / 60 / 60;
			long m = total / 60 - h * 60;
			long s = total % 60;
			if (!firstTime) connectionTime.setText(" " +
					((h != 0) ? (String.valueOf(h) + "h ") : new String()) + 
					((m != 0)  ? (String.valueOf(m) + "mn ") : new String()) + 
					String.valueOf(s) + "s");
			UpdateRequest request = new UpdateRequest(today, connectSec, firstTime);
			
			ServerCallHelper<UpdateResponse> callback =  new ServerCallHelper<UpdateResponse>(rootPanel){
				@Override
				public void onSuccess(UpdateResponse result)
				{
					if (firstTime) previousTime = result.totalDayTime;
					firstTime = false;
					// Store response. Send appropriate callbacks.				
				}
			};	
			utilService.sendUpdateList(userId, request, callback);
		};
	};

	public PanelUpdate(Panel rootPanel, UserId userId, Label connectionTime) 
	{
		this.connectionTime = connectionTime;
		this.rootPanel = rootPanel;
		this.userId = userId;
	}

	@Override
	public void onModuleLoad() {
		connectSec = 0;
		timer.scheduleRepeating(5000);
		setSize("0","0");
	}

}
