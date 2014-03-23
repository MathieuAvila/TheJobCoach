package com.TheJobCoach.webapp.userpage.client.Coach;

import java.util.Vector;

import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
import com.google.gwt.user.client.ui.Panel;

public class MessagePipe implements ReturnValue
{
	static Vector<String> pipedQueue = new Vector<String>();
	static Vector<String> appendQueue = new Vector<String>();
	static ClientUserValuesUtils userValues = null;
	
	static boolean loaded = false;
	static String coachIs = null;
	
	public static ICoachStrings strings = new CoachStrings();
	
	public MessagePipe(UserId user, Panel root)
	{
		if (userValues == null)
		{
			userValues = new ClientUserValuesUtils(root, user);
			userValues.preloadValueList(UserValuesConstantsCoachMessages.COACH_ROOT, new ClientUserValuesUtils.ReturnValue() {
				@Override
				public void notifyValue(boolean set, String key, String value)
				{
					System.out.println("loaded " + key);
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

	public void addMessage(String messageKey)
	{
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
