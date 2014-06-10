package com.TheJobCoach.webapp.userpage.client.Coach;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
import com.google.gwt.user.client.ui.Panel;

public class MessagePipe implements ReturnValue
{
	Vector<String> pipedQueue = new Vector<String>();
	Vector<String> appendQueue = new Vector<String>();
	ClientUserValuesUtils userValues = null;

	boolean loaded = false;
	String coachIs = null;

	public static ICoachStrings strings = new CoachStrings();

	public Set<String> messageSet = new HashSet<String>();

	static public MessagePipe instance = null;

	public static MessagePipe getMessagePipe(UserId user, Panel root)
	{
		if (instance == null) instance = new MessagePipe(user, root);
		return instance;
	}
	
	private MessagePipe(UserId user, Panel root)
	{
		if (userValues == null)
		{
			userValues = new ClientUserValuesUtils(root, user);
			userValues.preloadValueList(UserValuesConstantsCoachMessages.COACH_ROOT, new ClientUserValuesUtils.ReturnValue() {
				@Override
				public void notifyValue(boolean set, String key, String value)
				{
					loaded = true;
					checkQueue();
				}
			});
			userValues.addListener(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, this);
			// normally loaded by panel, but can happen here with test.
			userValues.preloadValueList(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, this);
		}
	}
	
	static final UserValuesConstantsCoachMessages.messageMinMax defaultMinMax = new UserValuesConstantsCoachMessages.messageMinMax(0,0);

	private void checkQueue()
	{
		if ((loaded == true)&&(coachIs != null))
		{
			for (String key: pipedQueue)
			{
				String val = userValues.getValueFromCache(key);
				int v = 0;
				try {
				 v = Integer.parseInt(val);
				} catch (Exception e) {} // Ignore, should never happen
				UserValuesConstantsCoachMessages.messageMinMax minMax = UserValuesConstantsCoachMessages.minMaxKeyMap.get(key);
				if (minMax == null) minMax = defaultMinMax;
				System.out.println("Check message: " + key + " min: " + minMax.min + " max: " + minMax.max + " current: " + v);
				if ((v >= minMax.min)&&( (minMax.max == 0) || (v < minMax.max) ))
				{
					appendQueue.add(strings.getMessage(key, coachIs));
				}
				v++;
				String newValue = String.valueOf(v);
				userValues.setValue(key, newValue);
			}
			pipedQueue.clear();
		}
	}

	// Directly add a parameterized message to the queue list, without checking for min/max in user values.
	public void addDirectParameterizedMessage(String messageKey, Vector<String> params)
	{
		String msg = strings.getMessage(messageKey, coachIs);
		for (int i = 0; i != params.size(); i++)
		{
			msg = msg.replace("%" + String.valueOf(i + 1), params.get(i));
		}
		System.out.println("Append message key " + messageKey + " with params " + params);
		appendQueue.add(msg);
	}
	
	public void addMessage(String messageKey)
	{
		if (messageSet.contains(messageKey))
			return; // each message only once.
		messageSet.add(messageKey);
		pipedQueue.add(messageKey);
		checkQueue();
	}
	
	public String getMessage()
	{
		if (appendQueue.size() == 0) return null;
		String result = appendQueue.get(0);
		appendQueue.remove(0);
		return result;
		
	}

	@Override
	public void notifyValue(boolean set, String key, String value)
	{
		if (key.equals(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR))
		{
			String previousCoach = coachIs;
			coachIs = value;
			if ((previousCoach != null)&&(!coachIs.equals(previousCoach)))
				addMessage(UserValuesConstantsCoachMessages.COACH_PRESENTING);
			checkQueue();
		}
	}
}
