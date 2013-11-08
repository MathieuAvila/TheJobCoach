package com.TheJobCoach.webapp.userpage.client.MyGoals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.CheckedExtendedDropListField;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.CheckedTextField;
import com.TheJobCoach.webapp.util.client.CheckedTime;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
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

	final Lang lang = GWT.create(Lang.class);

	ClientUserValuesUtils values = null;

	DialogBlockApplyReset applyReset = null;

	public ContentMyGoals(Panel panel, UserId _user)
	{
		rootPanel = panel;
		user = _user;
		values = new ClientUserValuesUtils(rootPanel, user);		
	}

	Panel rootPanel = null;


	final static LangGoals langGoals = GWT.create(LangGoals.class);
	
	CheckedExtendedDropListField tfGoalPeriod = new CheckedExtendedDropListField(
			UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD_LIST, langGoals.periodMap(), "periodMap_");
	CheckedLabel clGoalPeriod = new CheckedLabel(langGoals.evaluationPeriodSub(), false, tfGoalPeriod);

	CheckedTime tfConnectBefore = new CheckedTime(CheckedTime.DAY_MIDDAY);
	CheckedLabel clConnectBefore = new CheckedLabel(langGoals.imust_beforehour(), false, tfConnectBefore);

	CheckedTime tfConnectAfter = new CheckedTime( CheckedTime.DAY_END);
	CheckedLabel clConnectAfter = new CheckedLabel(langGoals.imustnot_afterhour(), false, tfConnectAfter);

	CheckedTextField tfConnectRatio = new CheckedTextField("[0-9]*");
	CheckedLabel clConnectRatio = new CheckedLabel(langGoals.imustnot_thisnumber(), false, tfConnectRatio);

	CheckedTextField tfCreateOpportunity = new CheckedTextField("[0-9]*");
	CheckedLabel clCreateOpportunity = new CheckedLabel(langGoals.imust_createopp(), false, tfCreateOpportunity);
	
	CheckedTextField tfCandidateOpportunity = new CheckedTextField("[0-9]*");
	CheckedLabel clCandidateOpportunity = new CheckedLabel(langGoals.imust_candidate(), false, tfCandidateOpportunity);
	
	CheckedTextField tfInterviewOpportunity = new CheckedTextField("[0-9]*");
	CheckedLabel clInterviewOpportunity = new CheckedLabel(langGoals.imust_interviews(), false, tfInterviewOpportunity);
	
	CheckedTextField tfProposal = new CheckedTextField("[0-9]*");
	CheckedLabel clProposal = new CheckedLabel(langGoals.imust_proposals(), false, tfProposal);
	
	CheckedTextField tfPhoneCall = new CheckedTextField("[0-9]*");
	CheckedLabel clPhoneCall = new CheckedLabel(langGoals.imust_phonecalls(), false, tfPhoneCall);
	
	Label previousDate = new Label();
	Label nextDate = new Label();
	
	int currentPeriod = 0;
	
	ButtonImageText nextButton = new ButtonImageText(ButtonImageText.Type.BACK, "Suivant");
	ButtonImageText prevButton = new ButtonImageText(ButtonImageText.Type.BACK, "Précédent");
	
	void getValues()
	{	
		values.preloadValueList("PERFORMANCE", this);
	}
	
	static void helperInsertElementInGrid(Grid bigGrid, int line, String text)
	{
	SimplePanel panelTitle = new SimplePanel();
	bigGrid.setWidget(line, 0, panelTitle);
	ContentHelper.insertSubTitlePanel(panelTitle, text);
	}
	
	HashMap<String, IExtendedField> fields = new HashMap<String, IExtendedField>();
	
	public void addPeriod(int count)
	{
		// Now update
		Date previousDateValue = new Date();
		Date nextDateValue = new Date();
		currentPeriod += count;
		if (currentPeriod > 0) currentPeriod = 0;
		FormatUtil.getPeriod(UserValuesConstantsMyGoals.mapStringPeriod.get(tfGoalPeriod.getValue()), currentPeriod, new Date(), previousDateValue, nextDateValue);
		
		previousDate.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(previousDateValue));
		nextDate.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(nextDateValue));
		
		// No next on current period
		nextButton.setEnabled(currentPeriod < 0);
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
		
		Grid gridPeriodEvaluation = new Grid(1, 4);
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
		gridPeriodEvaluation.setWidget(0, 1, previousDate);
		gridPeriodEvaluation.setWidget(0, 2, nextDate);
		previousDate.setWidth("10em");
		nextDate.setWidth("10em");
		nextButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event)
			{
				addPeriod(1);
			}
		});
		gridPeriodEvaluation.setWidget(0, 3, nextButton);
		
		helperInsertElementInGrid(bigGrid, 2, langGoals.connectionTimes());

		bigGrid.setWidget(3,0, clConnectBefore);
		bigGrid.setWidget(3,1, tfConnectBefore.getItem());

		bigGrid.setWidget(4,0, clConnectAfter);
		bigGrid.setWidget(4,1, tfConnectAfter.getItem());

		bigGrid.setWidget(5,0, clConnectRatio);
		bigGrid.setWidget(5,1, tfConnectRatio);
		
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, tfConnectBefore);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR, tfConnectAfter);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO, tfConnectRatio);
		
		helperInsertElementInGrid(bigGrid, 6, langGoals.opportunityGoals());

		bigGrid.setWidget(7,0, clCreateOpportunity);
		bigGrid.setWidget(7,1, tfCreateOpportunity);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY, tfCreateOpportunity);

		bigGrid.setWidget(8,0, clCandidateOpportunity);
		bigGrid.setWidget(8,1, tfCandidateOpportunity);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY, tfCandidateOpportunity);

		helperInsertElementInGrid(bigGrid, 9, langGoals.interviewGoals());

		bigGrid.setWidget(10,0, clInterviewOpportunity);
		bigGrid.setWidget(10,1, tfInterviewOpportunity);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW, tfInterviewOpportunity);

		helperInsertElementInGrid(bigGrid, 11, langGoals.phonecallGoals());

		bigGrid.setWidget(12,0, clPhoneCall);
		bigGrid.setWidget(12,1, tfPhoneCall);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL, tfPhoneCall);
			
		helperInsertElementInGrid(bigGrid, 13, langGoals.proposalGoals());

		bigGrid.setWidget(14,0, clProposal);
		bigGrid.setWidget(14,1, tfProposal);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL, tfProposal);
				
		for (IExtendedField f: fields.values()) f.registerListener(this);
		
		applyReset = new DialogBlockApplyReset(new ArrayList<IExtendedField>(fields.values()), this);

		simplePanelCenter.add(applyReset);

		getValues();
		addPeriod(0);		
	}

	@Override
	public void changed(boolean ok, boolean changed, boolean init) 
	{
		System.out.println("changed ... " + applyReset);
		if (applyReset != null) applyReset.hasEvent();
	}

	@Override
	public void notifyValue(boolean set, String key, String value) 
	{
		for (String testKey: fields.keySet())
		{
			if (testKey.equals(key))
			{
				fields.get(testKey).setDefault(value);
				fields.get(testKey).setValue(value);
			}
		}
		addPeriod(0);
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
		applyReset.hasEvent();
	}
}
