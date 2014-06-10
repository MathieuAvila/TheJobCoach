package com.TheJobCoach.webapp.userpage.client.Coach;

import java.util.HashMap;

import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;

public class CoachStrings implements ICoachStrings
{
	final static LangCoachDEFAULTMAN DEFAULT_MAN = GWT.create(LangCoachDEFAULTMAN.class);
	final static LangCoachDEFAULTWOMAN DEFAULT_WOMAN = GWT.create(LangCoachDEFAULTWOMAN.class);

	final static HashMap<String, HashMap<String, String[]> > messages = new HashMap<String, HashMap<String, String[]> >();
	
	static
	{
		HashMap<String, String[]> messages_DEFAULT_MAN = new HashMap<String, String[]>();
		HashMap<String, String[]> messages_DEFAULT_WOMAN = new HashMap<String, String[]>();

		messages.put(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__DEFAULT_MAN, messages_DEFAULT_MAN);
		messages.put(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR__DEFAULT_WOMAN, messages_DEFAULT_WOMAN);
		
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_WELCOME             , DEFAULT_MAN.COACH_WELCOME             ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_PRESENTING          , DEFAULT_MAN.COACH_PRESENTING          ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_HELLO               , DEFAULT_MAN.COACH_HELLO               ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_HELLO_AGAIN         , DEFAULT_MAN.COACH_HELLO_AGAIN         ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_LATE_ARRIVAL        , DEFAULT_MAN.COACH_LATE_ARRIVAL        ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_LATE_DEPARTURE      , DEFAULT_MAN.COACH_LATE_DEPARTURE      ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_VERY_LATE_ARRIVAL   , DEFAULT_MAN.COACH_VERY_LATE_ARRIVAL   ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_VERY_LATE_DEPARTURE , DEFAULT_MAN.COACH_VERY_LATE_DEPARTURE ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_DEPARTURE_WARNING   , DEFAULT_MAN.COACH_DEPARTURE_WARNING   ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_DEPARTURE_TIME      , DEFAULT_MAN.COACH_DEPARTURE_TIME      ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_GOAL_END_PERIOD     , DEFAULT_MAN.COACH_GOAL_END_PERIOD     ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_GOAL_SEND_EMAIL     , DEFAULT_MAN.COACH_GOAL_SEND_EMAIL     ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_OPP_NONE            , DEFAULT_MAN.COACH_OPP_NONE            ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_OPP_NO_LOG          , DEFAULT_MAN.COACH_OPP_NO_LOG          ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_OPP_NO_APPLICATION  , DEFAULT_MAN.COACH_OPP_NO_APPLICATION  ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_OPP_NO_CONTACT      , DEFAULT_MAN.COACH_OPP_NO_CONTACT      ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_LOG_RECALL          , DEFAULT_MAN.COACH_LOG_RECALL          ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_LOG_INTERVIEW       , DEFAULT_MAN.COACH_LOG_INTERVIEW       ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_LOG_FAILURE         , DEFAULT_MAN.COACH_LOG_FAILURE         ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_LOG_SUCCESS         , DEFAULT_MAN.COACH_LOG_SUCCESS         ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_PERSONAL_NOTE       , DEFAULT_MAN.COACH_PERSONAL_NOTE       ());
		messages_DEFAULT_MAN.put(UserValuesConstantsCoachMessages.COACH_PASSWORD_WARNING    , DEFAULT_MAN.COACH_PASSWORD_WARNING    ());

		// those are not stored on the server. They are used when user has progressed toward the goal.
		messages_DEFAULT_MAN.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY    , DEFAULT_MAN.PERFORMANCE_CREATEOPPORTUNITY    ());
		messages_DEFAULT_MAN.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY , DEFAULT_MAN.PERFORMANCE_CANDIDATEOPPORTUNITY ());
		messages_DEFAULT_MAN.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW            , DEFAULT_MAN.PERFORMANCE_INTERVIEW            ());
		messages_DEFAULT_MAN.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL            , DEFAULT_MAN.PERFORMANCE_PHONECALL            ());
		messages_DEFAULT_MAN.put(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL             , DEFAULT_MAN.PERFORMANCE_PROPOSAL             ());
		// Those are used when the goal is reached.
		messages_DEFAULT_MAN.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY + "_REACHED"    , DEFAULT_MAN.PERFORMANCE_CREATEOPPORTUNITY_REACHED    ());
		messages_DEFAULT_MAN.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY + "_REACHED" , DEFAULT_MAN.PERFORMANCE_CANDIDATEOPPORTUNITY_REACHED ());
		messages_DEFAULT_MAN.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW + "_REACHED"            , DEFAULT_MAN.PERFORMANCE_INTERVIEW_REACHED            ());
		messages_DEFAULT_MAN.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL + "_REACHED"            , DEFAULT_MAN.PERFORMANCE_PHONECALL_REACHED            ());
		messages_DEFAULT_MAN.put(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL + "_REACHED"             , DEFAULT_MAN.PERFORMANCE_PROPOSAL_REACHED             ());


		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_WELCOME             , DEFAULT_WOMAN.COACH_WELCOME             ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_PRESENTING          , DEFAULT_WOMAN.COACH_PRESENTING          ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_HELLO               , DEFAULT_WOMAN.COACH_HELLO               ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_HELLO_AGAIN         , DEFAULT_WOMAN.COACH_HELLO_AGAIN         ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_LATE_ARRIVAL        , DEFAULT_WOMAN.COACH_LATE_ARRIVAL        ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_LATE_DEPARTURE      , DEFAULT_WOMAN.COACH_LATE_DEPARTURE      ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_VERY_LATE_ARRIVAL   , DEFAULT_WOMAN.COACH_VERY_LATE_ARRIVAL   ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_VERY_LATE_DEPARTURE , DEFAULT_WOMAN.COACH_VERY_LATE_DEPARTURE ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_DEPARTURE_WARNING   , DEFAULT_WOMAN.COACH_DEPARTURE_WARNING   ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_DEPARTURE_TIME      , DEFAULT_WOMAN.COACH_DEPARTURE_TIME      ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_GOAL_END_PERIOD     , DEFAULT_WOMAN.COACH_GOAL_END_PERIOD     ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_GOAL_SEND_EMAIL     , DEFAULT_WOMAN.COACH_GOAL_SEND_EMAIL     ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_OPP_NONE            , DEFAULT_WOMAN.COACH_OPP_NONE            ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_OPP_NO_LOG          , DEFAULT_WOMAN.COACH_OPP_NO_LOG          ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_OPP_NO_APPLICATION  , DEFAULT_WOMAN.COACH_OPP_NO_APPLICATION  ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_OPP_NO_CONTACT      , DEFAULT_WOMAN.COACH_OPP_NO_CONTACT      ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_LOG_RECALL          , DEFAULT_WOMAN.COACH_LOG_RECALL          ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_LOG_INTERVIEW       , DEFAULT_WOMAN.COACH_LOG_INTERVIEW       ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_LOG_FAILURE         , DEFAULT_WOMAN.COACH_LOG_FAILURE         ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_LOG_SUCCESS         , DEFAULT_WOMAN.COACH_LOG_SUCCESS         ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_PERSONAL_NOTE       , DEFAULT_WOMAN.COACH_PERSONAL_NOTE       ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsCoachMessages.COACH_PASSWORD_WARNING    , DEFAULT_WOMAN.COACH_PASSWORD_WARNING    ());

		// those are not stored on the server. They are used when user has progressed toward the goal.
		messages_DEFAULT_WOMAN.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY    , DEFAULT_WOMAN.PERFORMANCE_CREATEOPPORTUNITY    ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY , DEFAULT_WOMAN.PERFORMANCE_CANDIDATEOPPORTUNITY ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW            , DEFAULT_WOMAN.PERFORMANCE_INTERVIEW            ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL            , DEFAULT_WOMAN.PERFORMANCE_PHONECALL            ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL             , DEFAULT_WOMAN.PERFORMANCE_PROPOSAL             ());
		// Those are used when the goal is reached.
		messages_DEFAULT_WOMAN.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY + "_REACHED"    , DEFAULT_WOMAN.PERFORMANCE_CREATEOPPORTUNITY_REACHED    ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY + "_REACHED" , DEFAULT_WOMAN.PERFORMANCE_CANDIDATEOPPORTUNITY_REACHED ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW + "_REACHED"            , DEFAULT_WOMAN.PERFORMANCE_INTERVIEW_REACHED            ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL + "_REACHED"            , DEFAULT_WOMAN.PERFORMANCE_PHONECALL_REACHED            ());
		messages_DEFAULT_WOMAN.put(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL + "_REACHED"             , DEFAULT_WOMAN.PERFORMANCE_PROPOSAL_REACHED             ());
		}
	
	public String getMessage(String key, String coach)
	{
		HashMap<String, String[]> avatarMessages = messages.get(coach);
		if (avatarMessages == null) return "No such avatar";
		String[] messagesTable = avatarMessages.get(key);
		if (messagesTable == null) return "No such message: " + key;
		int i = Random.nextInt(messagesTable.length);
		return messagesTable[i];
	}
	
}
