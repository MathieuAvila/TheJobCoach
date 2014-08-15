package com.TheJobCoach.webapp.userpage.client.MyGoals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.GoalReportInformation;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.CheckedExtendedDropListField;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.CheckedTextField;
import com.TheJobCoach.webapp.util.client.CheckedTime;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.DialogBlockApplyReset;
import com.TheJobCoach.webapp.util.client.DialogBlockApplyReset.IApply;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.client.IExtendedField;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Grid;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentMyGoals implements EntryPoint, IChanged, ReturnValue, IApply {

	UserId user;
	
	
	private final static UserServiceAsync userService = GWT.create(UserService.class);

	final Lang lang = GWT.create(Lang.class);

	ClientUserValuesUtils values = null;

	DialogBlockApplyReset applyReset = null;

	public ContentMyGoals(Panel panel, UserId _user)
	{
		rootPanel = panel;
		user = _user;
		values = ClientUserValuesUtils.getInstance(user);		
	}

	Panel rootPanel = null;

	final static LangGoals langGoals = GWT.create(LangGoals.class);
	
	CheckedExtendedDropListField tfGoalPeriod = new CheckedExtendedDropListField(
			UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD_LIST, langGoals.periodMap(), "periodMap_");
	CheckedLabel clGoalPeriod = new CheckedLabel(langGoals.evaluationPeriodSub(), false, tfGoalPeriod);

	CheckedTime tfConnectBefore = new CheckedTime(CheckedTime.DAY_MIDDAY);
	CheckedLabel clConnectBefore = new CheckedLabel(langGoals.imust_beforehour(), false, tfConnectBefore);
	ResultEvaluation reConnectBefore = new ResultEvaluation();
	
	CheckedTime tfConnectAfter = new CheckedTime( CheckedTime.DAY_END);
	CheckedLabel clConnectAfter = new CheckedLabel(langGoals.imustnot_afterhour(), false, tfConnectAfter);
	ResultEvaluation reConnectAfter = new ResultEvaluation();

	CheckedTextField tfConnectRatio = new CheckedTextField("[0-9]*");
	CheckedLabel clConnectRatio = new CheckedLabel(langGoals.imustnot_thisnumber(), false, tfConnectRatio);
	ResultEvaluation reConnectRatio = new ResultEvaluation();

	CheckedTextField tfCreateOpportunity = new CheckedTextField("[0-9]*");
	CheckedLabel clCreateOpportunity = new CheckedLabel(langGoals.imust_createopp(), false, tfCreateOpportunity);
	ResultEvaluation reCreateOpportunity = new ResultEvaluation();
	
	CheckedTextField tfCandidateOpportunity = new CheckedTextField("[0-9]*");
	CheckedLabel clCandidateOpportunity = new CheckedLabel(langGoals.imust_candidate(), false, tfCandidateOpportunity);
	ResultEvaluation reCandidateOpportunity = new ResultEvaluation();
	
	CheckedTextField tfInterviewOpportunity = new CheckedTextField("[0-9]*");
	CheckedLabel clInterviewOpportunity = new CheckedLabel(langGoals.imust_interviews(), false, tfInterviewOpportunity);
	ResultEvaluation reInterviewOpportunity = new ResultEvaluation();
	
	CheckedTextField tfProposal = new CheckedTextField("[0-9]*");
	CheckedLabel clProposal = new CheckedLabel(langGoals.imust_proposals(), false, tfProposal);
	ResultEvaluation reProposal = new ResultEvaluation();
	
	CheckedTextField tfPhoneCall = new CheckedTextField("[0-9]*");
	CheckedLabel clPhoneCall = new CheckedLabel(langGoals.imust_phonecalls(), false, tfPhoneCall);
	ResultEvaluation rePhoneCall = new ResultEvaluation();
	
	Map<String, Vector<ResultEvaluation>> mapEvalutionKey = new HashMap<String, Vector<ResultEvaluation>>();
	
	Label previousDate = new Label();
	Label nextDate = new Label();
	
	// Stores request start day and end day.
	long startTime;
	long endTime;
	
	int currentPeriod = 0;
	
	ButtonImageText nextButton = new ButtonImageText(ButtonImageText.Type.NEXT, langGoals.nextPeriod());
	ButtonImageText prevButton = new ButtonImageText(ButtonImageText.Type.BACK, langGoals.previousPeriod());
	
	void appendEvaluationKey(String key, ResultEvaluation re)
	{
		Vector<ResultEvaluation> v;
		if (mapEvalutionKey.containsKey(key)) v = mapEvalutionKey.get(key);
		else 
		{
			v = new Vector<ResultEvaluation>();
			mapEvalutionKey.put(key, v);
		}
		v.add(re);
	}
	
	void getValues()
	{	
		values.preloadValueList("PERFORMANCE", this);
	}
	
	static void helperInsertElementInGrid(Grid bigGrid, int line, String text)
	{
		helperInsertElementInGrid(bigGrid, line, 0, text);
	}

	static void helperInsertElementInGrid(Grid bigGrid, int line, int column, String text)
	{
		SimplePanel panelTitle = new SimplePanel();
		bigGrid.setWidget(line, column, panelTitle);
		ContentHelper.insertSubTitlePanel(panelTitle, text);
	}
	
	HashMap<String, IExtendedField> fields = new HashMap<String, IExtendedField>();
	
	int lastCurrent = -1; // This forces 1st refresh
	Date lastPrevious;
	Date lastNext;
	
	public void addPeriod(int count)
	{
		// Now update
		Date previousDateValue = new Date();
		Date nextDateValue = new Date();
		currentPeriod += count;
		if (currentPeriod > 0) currentPeriod = 0;
		FormatUtil.getPeriod(UserValuesConstantsMyGoals.mapStringPeriod.get(tfGoalPeriod.getValue()), currentPeriod, new Date(), previousDateValue, nextDateValue);
		
		// Set times in previousDate and nextDate with goal values.
		previousDateValue = new Date(FormatUtil.startOfTheDay(previousDateValue).getTime() + startTime);
		nextDateValue = new Date(FormatUtil.startOfTheDay(nextDateValue).getTime() + endTime);

		previousDate.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(previousDateValue));
		nextDate.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(nextDateValue));
		
		// No next on current period
		nextButton.setEnabled(currentPeriod < 0);
		
		reConnectBefore.setCurrent(currentPeriod == 0);
		reConnectAfter.setCurrent(currentPeriod == 0);
		reConnectRatio.setCurrent(currentPeriod == 0);
		reCreateOpportunity.setCurrent(currentPeriod == 0);
		reCandidateOpportunity.setCurrent(currentPeriod == 0);
		reInterviewOpportunity.setCurrent(currentPeriod == 0);
		reProposal.setCurrent(currentPeriod == 0);
		rePhoneCall.setCurrent(currentPeriod == 0);
		
		// now run request to get actual values
		ServerCallHelper<GoalReportInformation> callback = new ServerCallHelper<GoalReportInformation>(rootPanel) {
			@Override
			public void onSuccess(GoalReportInformation result)
			{
				reConnectBefore.setValue(result.succeedStartDay);
				reConnectAfter.setValue(result.succeedEndDay);
				reConnectRatio.setValue(result.connectedDays);
				reCreateOpportunity.setValue(result.newOpportunities);
				reCandidateOpportunity.setValue(result.log.get(UserLogEntry.LogEntryType.APPLICATION));
				reInterviewOpportunity.setValue(result.log.get(UserLogEntry.LogEntryType.INTERVIEW));
				reProposal.setValue(result.log.get(UserLogEntry.LogEntryType.PROPOSAL));
				rePhoneCall.setValue(result.log.get(UserLogEntry.LogEntryType.RECALL));
			}
		};
		if ((lastCurrent != currentPeriod)||(!previousDateValue.equals(lastPrevious))|(!nextDateValue.equals(lastNext)))
		{
			lastCurrent = currentPeriod;
			lastPrevious = previousDateValue;
			lastNext = nextDateValue;
			userService.getUserGoalReport(user, previousDateValue, nextDateValue, callback);
		}
	}
	
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{		
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "");
		
		ContentHelper.insertTitlePanel(simplePanelCenter, lang._TextMyGoals(), ClientImageBundle.INSTANCE.userVirtualCoachGoalsContent());
		rootPanel.add(simplePanelCenter);

		Grid bigGrid = new Grid(15, 3);
		bigGrid.setWidth("100%");
		
		simplePanelCenter.add(bigGrid);

		helperInsertElementInGrid(bigGrid, 0, langGoals.evaluationPeriod());
		
		bigGrid.setWidget(1, 0, clGoalPeriod);
		bigGrid.setWidget(1, 1, tfGoalPeriod.getItem());
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD, tfGoalPeriod);
		tfGoalPeriod.getItem().addChangeHandler(new ChangeHandler()
		{
			@Override
			public void onChange(ChangeEvent event)
			{
				currentPeriod = 0;
				addPeriod(0);
			}
		});

		helperInsertElementInGrid(bigGrid, 0, 2, langGoals.resultsOnPeriod());
		
		Grid gridPeriodEvaluation = new Grid(1, 3);
		//gridPeriodEvaluation.setWidth("100%");

		bigGrid.setWidget(1, 2, gridPeriodEvaluation);
		prevButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event)
			{
				addPeriod(-1);
			}
		});
		gridPeriodEvaluation.setWidget(0, 0, prevButton);
		
		VerticalPanel vpEval = new VerticalPanel();
		gridPeriodEvaluation.setWidget(0, 1, vpEval);
		
		vpEval.add(previousDate);
		vpEval.add(nextDate);

		previousDate.setWidth("10em");
		nextDate.setWidth("10em");
		nextButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event)
			{
				addPeriod(1);
			}
		});
		gridPeriodEvaluation.setWidget(0, 2, nextButton);
		
		helperInsertElementInGrid(bigGrid, 2, langGoals.connectionTimes());

		bigGrid.setWidget(3,0, clConnectBefore);
		bigGrid.setWidget(3,1, tfConnectBefore.getItem());
		bigGrid.setWidget(3,2, reConnectBefore);

		bigGrid.setWidget(4,0, clConnectAfter);
		bigGrid.setWidget(4,1, tfConnectAfter.getItem());
		bigGrid.setWidget(4,2, reConnectAfter);

		bigGrid.setWidget(5,0, clConnectRatio);
		bigGrid.setWidget(5,1, tfConnectRatio);
		bigGrid.setWidget(5,2, reConnectRatio);
		
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, tfConnectBefore);
		appendEvaluationKey(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO, reConnectBefore);
		
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR, tfConnectAfter);
		appendEvaluationKey(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO, reConnectAfter);
		
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO, tfConnectRatio);
		appendEvaluationKey(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO, reConnectRatio);
	
		helperInsertElementInGrid(bigGrid, 6, langGoals.opportunityGoals());

		bigGrid.setWidget(7,0, clCreateOpportunity);
		bigGrid.setWidget(7,1, tfCreateOpportunity);
		bigGrid.setWidget(7,2, reCreateOpportunity);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY, tfCreateOpportunity);
		appendEvaluationKey(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY, reCreateOpportunity);

		bigGrid.setWidget(8,0, clCandidateOpportunity);
		bigGrid.setWidget(8,1, tfCandidateOpportunity);
		bigGrid.setWidget(8,2, reCandidateOpportunity);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY, tfCandidateOpportunity);
		appendEvaluationKey(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY, reCandidateOpportunity);

		helperInsertElementInGrid(bigGrid, 9, langGoals.interviewGoals());

		bigGrid.setWidget(10,0, clInterviewOpportunity);
		bigGrid.setWidget(10,1, tfInterviewOpportunity);
		bigGrid.setWidget(10,2, reInterviewOpportunity);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW, tfInterviewOpportunity);
		appendEvaluationKey(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW, reInterviewOpportunity);

		helperInsertElementInGrid(bigGrid, 11, langGoals.phonecallGoals());

		bigGrid.setWidget(12,0, clPhoneCall);
		bigGrid.setWidget(12,1, tfPhoneCall);
		bigGrid.setWidget(12,2, rePhoneCall);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL, tfPhoneCall);
		appendEvaluationKey(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL, rePhoneCall);
			
		helperInsertElementInGrid(bigGrid, 13, langGoals.proposalGoals());

		bigGrid.setWidget(14,0, clProposal);
		bigGrid.setWidget(14,1, tfProposal);
		bigGrid.setWidget(14,2, reProposal);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL, tfProposal);
		appendEvaluationKey(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL, reProposal);
				
		for (IExtendedField f: fields.values()) f.registerListener(this);
		
		applyReset = new DialogBlockApplyReset(new ArrayList<IExtendedField>(fields.values()), this);

		simplePanelCenter.add(applyReset);
				
		getValues();
		addPeriod(0);		
	}

	HashMap<String, String> receivedValues = new HashMap<String, String>();
	
	@Override
	public void changed(boolean ok, boolean changed, boolean init) 
	{
		// Update evaluation values.
		for (String key: fields.keySet())
		{
			IExtendedField f = fields.get(key);
			String newV = f.getValue();
			String v = receivedValues.get(key);
			if (!newV.equals(v))
			{
				receivedValues.put(key, newV);
				notifyValueFromServer(key, newV, false);
			}
		}
		if (applyReset != null) applyReset.hasEvent();
	}
	
	
	public void notifyValueFromServer(String key, String value, boolean server) 
	{
		for (String testKey: fields.keySet())
		{
			if (server) 
			{
				if (testKey.equals(key))
				{
					fields.get(testKey).setDefault(value);
					fields.get(testKey).setValue(value);
				}
			}
			Vector<ResultEvaluation> rev = mapEvalutionKey.get(key);
			if (rev != null)
			{
				Integer v = null;
				try {
					if (!"".equals(value))
					v = Integer.valueOf(value);
				}
				catch (Exception e)
				{
					// Ok, this field may be anything.
				}
				for (ResultEvaluation re : rev)
				{
					if (v != null)
						re.setMinimum(v.intValue());
					else
						re.resetMinimum();
				}
			}
		}
		// Specifics for start/end day
		if (key.equals(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR) 
				|| key.equals(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR))
		{
			try {
			Long time = Long.valueOf(value);
			if (key.equals(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR))
				startTime = time;
			else 
				endTime = time;
			}
			catch (Exception e) {
				// valid: not set.
			}
		}
		addPeriod(0);
	}

	@Override
	public void notifyValue(boolean set, String key, String value) 
	{
		receivedValues.put(key, value);
		notifyValueFromServer(key, value, true);
	}

	@Override
	public void apply()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		for (String testKey: fields.keySet())
		{
			IExtendedField field = fields.get(testKey);
			if (!field.getIsDefault())
			{
				field.setDefault(field.getValue());
				map.put(testKey, field.getValue());
			}
		}
		values.setValues(map, null);
		addPeriod(0);
		getValues();
		applyReset.hasEvent();
	}
}
