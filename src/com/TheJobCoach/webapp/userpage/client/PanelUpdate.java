package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;

import com.TheJobCoach.webapp.userpage.client.Coach.MessagePipe;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.UtilService;
import com.TheJobCoach.webapp.util.client.UtilServiceAsync;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.UpdateRequest;
import com.TheJobCoach.webapp.util.shared.UpdateResponse;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class PanelUpdate  extends SimplePanel implements EntryPoint, ReturnValue {

	Label connectionTime;
	Panel rootPanel;
	UserId userId;

	int connectSec = 0;
	int previousTime = 0;
	boolean firstTime = true;
	Date today = new Date();

	MessagePipe message = null;
	ClientUserValuesUtils values = null;

	Date arrivalTime = null;
	Date arrivalTimeMore30minutes = null;
	
	Date departureTime = null;
	Date departureTimeLess15minutes = null;
	Date departureTimeMore30minutes = null;
	
	int warningDeparture = 0;
	
	static UtilServiceAsync utilService =  GWT.create(UtilService.class);

	void checkTime(int totalTime)
	{
		System.out.println("total time: " + totalTime);
		Date currentTime = new Date();
		if (firstTime) // send hello message, or reconnect message
		{
			if (totalTime < 10000) //== 5) // really first time of the day
			{
				message.addMessage(UserValuesConstantsCoachMessages.COACH_WELCOME);
				message.addMessage(UserValuesConstantsCoachMessages.COACH_HELLO);
				// Check arrival time.
				if ((arrivalTime != null && (currentTime.after(arrivalTime))))
				{
					message.addMessage(UserValuesConstantsCoachMessages.COACH_LATE_ARRIVAL);
				}
			}
			else
			{
				message.addMessage(UserValuesConstantsCoachMessages.COACH_HELLO_AGAIN);
			}
			previousTime = totalTime;
			firstTime = false;
		}
		else
		{
			// Check logout time.
			if ((departureTimeLess15minutes != null && (currentTime.after(departureTimeLess15minutes))) && (warningDeparture == 0))
			{
				warningDeparture++;
				message.addMessage(UserValuesConstantsCoachMessages.COACH_LATE_ARRIVAL);
			} 
			else if ((departureTimeMore30minutes != null && (currentTime.after(departureTimeMore30minutes))) && (warningDeparture == 1))
			{
				warningDeparture++;
				message.addMessage(UserValuesConstantsCoachMessages.COACH_DEPARTURE_WARNING);
			}
		}
		// Store response. Send appropriate callbacks.				
	}
	
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
					checkTime(result.totalDayTime);		
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
		message = new MessagePipe(userId, rootPanel);
		// Get time goals.
		values = new ClientUserValuesUtils(rootPanel, userId);
		values.preloadValueList(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, this);
		values.preloadValueList(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR, this);
	}

	@Override
	public void onModuleLoad() {
		connectSec = 0;
		timer.scheduleRepeating(5000);
		setSize("0","0");
	}

	@Override
	public void notifyValue(boolean set, String key, String value)
	{
		Date currentTime = new Date();
		if (key.equals(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR))
		{
			System.out.println("arrivalTime is: " + value);
			if (!"".equals(value))
			{
				arrivalTime = new Date(FormatUtil.startOfTheDay(currentTime).getTime() + Integer.parseInt(value));
				arrivalTimeMore30minutes = new Date(FormatUtil.startOfTheDay(currentTime).getTime() + Integer.parseInt(value) + 30*60*1000);
				System.out.println("arrivalTime is: " + arrivalTime + " late is " + arrivalTimeMore30minutes);
			}
		}
		else if (key.equals(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR))
		{
			System.out.println("departureTime time is: " + value);
			if (!"".equals(value))
			{
				departureTime = new Date(FormatUtil.startOfTheDay(currentTime).getTime() + Integer.parseInt(value));
				departureTimeLess15minutes = new Date(FormatUtil.startOfTheDay(currentTime).getTime() + Integer.parseInt(value) - 15*60*1000);
				departureTimeMore30minutes = new Date(FormatUtil.startOfTheDay(currentTime).getTime() + Integer.parseInt(value) + 30*60*1000);
			}
		}
	}

}
