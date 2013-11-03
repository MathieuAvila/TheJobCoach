package com.TheJobCoach.webapp.userpage.client.MyGoals;

import java.util.ArrayList;
import java.util.HashMap;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.UserId;
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
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsMyGoals;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Panel;
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
	
	
	void getValues()
	{	
		values.preloadValueList("PERFORMANCE", this);
	}

	HashMap<String, IExtendedField> fields = new HashMap<String, IExtendedField>();
	
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
		rootPanel.add(simplePanelCenter);

		ContentHelper.insertTitlePanel(simplePanelCenter, lang._TextMyGoals(), ClientImageBundle.INSTANCE.userVirtualCoachGoalsContent());

		ContentHelper.insertSubTitlePanel(simplePanelCenter, langGoals.evaluationPeriod());
		Grid gridPeriod = new Grid(1, 2);
		simplePanelCenter.add(gridPeriod);
		gridPeriod.setWidget(0,0, clGoalPeriod);
		gridPeriod.setWidget(0,1, tfGoalPeriod.getItem());
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_EVALUATION_PERIOD, tfGoalPeriod);

		ContentHelper.insertSubTitlePanel(simplePanelCenter, langGoals.connectionTimes());

		Grid grid0 = new Grid(3, 2);
		simplePanelCenter.add(grid0);

		grid0.setWidget(0,0, clConnectBefore);
		grid0.setWidget(0,1, tfConnectBefore.getItem());

		grid0.setWidget(1,0, clConnectAfter);
		grid0.setWidget(1,1, tfConnectAfter.getItem());

		grid0.setWidget(2,0, clConnectRatio);
		grid0.setWidget(2,1, tfConnectRatio);
		
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, tfConnectBefore);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR, tfConnectAfter);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_RATIO, tfConnectRatio);
		
		ContentHelper.insertSubTitlePanel(simplePanelCenter, langGoals.opportunityGoals());

		Grid grid1 = new Grid(1, 2);
		simplePanelCenter.add(grid1);
		
		grid1.setWidget(0,0, clCreateOpportunity);
		grid1.setWidget(0,1, tfCreateOpportunity);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY, tfCreateOpportunity);

		Grid grid2 = new Grid(1, 2);
		simplePanelCenter.add(grid2);
		
		grid2.setWidget(0,0, clCandidateOpportunity);
		grid2.setWidget(0,1, tfCandidateOpportunity);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY, tfCandidateOpportunity);

		ContentHelper.insertSubTitlePanel(simplePanelCenter, langGoals.interviewGoals());

		Grid grid3 = new Grid(1, 2);
		simplePanelCenter.add(grid3);
		
		grid3.setWidget(0,0, clInterviewOpportunity);
		grid3.setWidget(0,1, tfInterviewOpportunity);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW, tfInterviewOpportunity);

		ContentHelper.insertSubTitlePanel(simplePanelCenter, langGoals.phonecallGoals());

		Grid grid4 = new Grid(1, 2);
		simplePanelCenter.add(grid4);
		
		grid4.setWidget(0,0, clPhoneCall);
		grid4.setWidget(0,1, tfPhoneCall);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL, tfPhoneCall);
			
		ContentHelper.insertSubTitlePanel(simplePanelCenter, langGoals.proposalGoals());

		Grid grid5 = new Grid(1, 2);
		simplePanelCenter.add(grid5);
		
		grid5.setWidget(0,0, clProposal);
		grid5.setWidget(0,1, tfProposal);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_PROPOSAL, tfProposal);
				
		for (IExtendedField f: fields.values()) f.registerListener(this);
		
		applyReset = new DialogBlockApplyReset(new ArrayList<IExtendedField>(fields.values()), this);

		simplePanelCenter.add(applyReset);

		getValues();		
	}

	@Override
	public void changed(boolean ok, boolean changed, boolean init) 
	{
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
