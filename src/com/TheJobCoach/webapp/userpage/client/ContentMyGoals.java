package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.CheckedTextField;
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

	CheckedLabel clConnectBefore = new CheckedLabel("Je dois me connecter avant cette heure ", false, this);
	CheckedTextField tfConnectBefore = new CheckedTextField(clConnectBefore, "[0-9]*");

	CheckedLabel clConnectAfter = new CheckedLabel("Je dois être déconnecté après cette heure", false, this);
	CheckedTextField tfConnectAfter = new CheckedTextField(clConnectAfter, "[0-9]*");

	CheckedLabel clConnectRatio = new CheckedLabel("Je dois me connecter au moins ce nombre de jours par semaine", false, this);
	CheckedTextField tfConnectRatio = new CheckedTextField(clConnectRatio, "[0-9]*");

	CheckedLabel clConnectRatioMonthly = new CheckedLabel("Je dois me connecter au moins ce nombre de jours par mois", false, this);
	CheckedTextField tfConnectRatioMonthly = new CheckedTextField(clConnectRatioMonthly, "[0-9]*");
	
	
	CheckedLabel clCreateOpportunityW = new CheckedLabel("Je dois créer au moins ce nombre d'opportunités par semaine", false, this);
	CheckedTextField tfCreateOpportunityW = new CheckedTextField(clCreateOpportunityW, "[0-9]*");
	
	CheckedLabel clCreateOpportunityBW = new CheckedLabel("Je dois créer au moins ce nombre d'opportunités toutes les deux semaines", false, this);
	CheckedTextField tfCreateOpportunityBW = new CheckedTextField(clCreateOpportunityBW, "[0-9]*");
	
	CheckedLabel clCreateOpportunityM = new CheckedLabel("Je dois créer au moins ce nombre d'opportunités par mois", false, this);
	CheckedTextField tfCreateOpportunityM = new CheckedTextField(clCreateOpportunityM, "[0-9]*");

	
	
	CheckedLabel clCandidateOpportunityW = new CheckedLabel("Je dois candidater à au moins ce nombre d'opportunités par semaine", false, this);
	CheckedTextField tfCandidateOpportunityW = new CheckedTextField(clCandidateOpportunityW, "[0-9]*");
	
	CheckedLabel clCandidateOpportunityBW = new CheckedLabel("Je dois candidater au moins ce nombre d'opportunités toutes les deux semaines", false, this);
	CheckedTextField tfCandidateOpportunityBW = new CheckedTextField(clCandidateOpportunityBW, "[0-9]*");
	
	CheckedLabel clCandidateOpportunityM = new CheckedLabel("Je dois candidater au moins ce nombre d'opportunités par mois", false, this);
	CheckedTextField tfCandidateOpportunityM = new CheckedTextField(clCandidateOpportunityM, "[0-9]*");

	
	
	CheckedLabel clInterviewOpportunityW = new CheckedLabel("Je dois passer au moins ce nombre d'entretiens par semaine", false, this);
	CheckedTextField tfInterviewOpportunityW = new CheckedTextField(clInterviewOpportunityW, "[0-9]*");
	
	CheckedLabel clInterviewOpportunityBW = new CheckedLabel("Je dois passer au moins ce nombre d'entretiens toutes les deux semaines", false, this);
	CheckedTextField tfInterviewOpportunityBW = new CheckedTextField(clInterviewOpportunityBW, "[0-9]*");
	
	CheckedLabel clInterviewOpportunityM = new CheckedLabel("Je dois passer au moins ce nombre d'entretiens par mois", false, this);
	CheckedTextField tfInterviewOpportunityM = new CheckedTextField(clInterviewOpportunityM, "[0-9]*");

	
	
	CheckedLabel clProposalW = new CheckedLabel("Je dois obtenir au moins ce nombre de propositions par semaine", false, this);
	CheckedTextField tfProposalW = new CheckedTextField(clProposalW, "[0-9]*");
	
	CheckedLabel clProposalBW = new CheckedLabel("Je dois obtenir au moins ce nombre de propositions toutes les deux semaines", false, this);
	CheckedTextField tfProposalBW = new CheckedTextField(clProposalBW, "[0-9]*");
	
	CheckedLabel clProposalM = new CheckedLabel("Je dois obtenir au moins ce nombre de propositions par mois", false, this);
	CheckedTextField tfProposalM = new CheckedTextField(clProposalM, "[0-9]*");

	
	
	CheckedLabel clPhoneCallW = new CheckedLabel("Je dois passer ce nombre d'appels téléphoniques par semaine", false, this);
	CheckedTextField tfPhoneCallW = new CheckedTextField(clPhoneCallW, "[0-9]*");
	
	CheckedLabel clPhoneCallBW = new CheckedLabel("Je dois passer ce nombre d'appels téléphoniques toutes les deux semaines", false, this);
	CheckedTextField tfPhoneCallBW = new CheckedTextField(clPhoneCallBW, "[0-9]*");
	
	CheckedLabel clPhoneCallM = new CheckedLabel("Je dois passer ce nombre d'appels téléphoniques par mois", false, this);
	CheckedTextField tfPhoneCallM = new CheckedTextField(clPhoneCallM, "[0-9]*");
	
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

		ContentHelper.insertTitlePanel(simplePanelCenter, lang._TextMyGoals(), ClientImageBundle.INSTANCE.userVirtualCoachGoals());
		
		ContentHelper.insertSubTitlePanel(simplePanelCenter, "Mes horaires de connection");

		Grid grid0 = new Grid(4, 2);
		simplePanelCenter.add(grid0);

		grid0.setWidget(0,0, clConnectBefore);
		grid0.setWidget(0,1, tfConnectBefore);

		grid0.setWidget(1,0, clConnectAfter);
		grid0.setWidget(1,1, tfConnectAfter);

		grid0.setWidget(2,0, clConnectRatio);
		grid0.setWidget(2,1, tfConnectRatio);
		
		grid0.setWidget(3,0, clConnectRatioMonthly);
		grid0.setWidget(3,1, tfConnectRatioMonthly);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_BEFORE_HOUR, tfConnectBefore);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_NOT_AFTER_HOUR, tfConnectAfter);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_WEEKLY_RATIO, tfConnectRatio);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CONNECT_MONTHLY_RATIO, tfConnectRatio);
		
		ContentHelper.insertSubTitlePanel(simplePanelCenter, "Mes objectifs de créations d'opportunités");

		Grid grid1 = new Grid(3, 2);
		simplePanelCenter.add(grid1);
		
		grid1.setWidget(0,0, clCreateOpportunityW);
		grid1.setWidget(0,1, tfCreateOpportunityW);

		grid1.setWidget(1,0, clCreateOpportunityBW);
		grid1.setWidget(1,1, tfCreateOpportunityBW);

		grid1.setWidget(2,0, clCreateOpportunityM);
		grid1.setWidget(2,1, tfCreateOpportunityM);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY_WEEKLY, tfCreateOpportunityW);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY_BIWEEKLY, tfCreateOpportunityBW);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CREATEOPPORTUNITY_MONTHLY, tfCreateOpportunityM);

		ContentHelper.insertSubTitlePanel(simplePanelCenter, "Mes objectifs de candidatures");

		Grid grid2 = new Grid(3, 2);
		simplePanelCenter.add(grid2);
		
		grid2.setWidget(0,0, clCandidateOpportunityW);
		grid2.setWidget(0,1, tfCandidateOpportunityW);

		grid2.setWidget(1,0, clCandidateOpportunityBW);
		grid2.setWidget(1,1, tfCandidateOpportunityBW);

		grid2.setWidget(2,0, clCandidateOpportunityM);
		grid2.setWidget(2,1, tfCandidateOpportunityM);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY_WEEKLY, tfCandidateOpportunityW);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY_BIWEEKLY, tfCandidateOpportunityBW);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_CANDIDATEOPPORTUNITY_MONTHLY, tfCandidateOpportunityM);

		ContentHelper.insertSubTitlePanel(simplePanelCenter, "Mes objectifs d'entretiens");

		Grid grid3 = new Grid(3, 2);
		simplePanelCenter.add(grid3);
		
		grid3.setWidget(0,0, clInterviewOpportunityW);
		grid3.setWidget(0,1, tfInterviewOpportunityW);

		grid3.setWidget(1,0, clInterviewOpportunityBW);
		grid3.setWidget(1,1, tfInterviewOpportunityBW);

		grid3.setWidget(2,0, clInterviewOpportunityM);
		grid3.setWidget(2,1, tfInterviewOpportunityM);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW_WEEKLY, tfInterviewOpportunityW);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW_BIWEEKLY, tfInterviewOpportunityBW);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_INTERVIEW_MONTHLY, tfInterviewOpportunityM);

		ContentHelper.insertSubTitlePanel(simplePanelCenter, "Mes objectifs d'appels téléphoniques");

		Grid grid4 = new Grid(3, 2);
		simplePanelCenter.add(grid4);
		
		grid4.setWidget(0,0, clPhoneCallW);
		grid4.setWidget(0,1, tfPhoneCallW);

		grid4.setWidget(1,0, clPhoneCallBW);
		grid4.setWidget(1,1, tfPhoneCallBW);

		grid4.setWidget(2,0, clPhoneCallM);
		grid4.setWidget(2,1, tfPhoneCallM);

		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL_WEEKLY, tfPhoneCallW);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL_BIWEEKLY, tfPhoneCallBW);
		fields.put(UserValuesConstantsMyGoals.PERFORMANCE_PHONECALL_MONTHLY, tfPhoneCallM);

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
		System.out.println("found key: " + key + " has value " + value);
		for (String testKey: fields.keySet())
		{
			if (testKey.equals(key))
			{
				//System.out.println("found key: " + key + " is set");
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
				//System.out.println("KEY IS:" + testKey + " VALUE: " + field.getValue());
			}
		}
		values.setValues(map, null);
		applyReset.hasEvent();
	}
}
