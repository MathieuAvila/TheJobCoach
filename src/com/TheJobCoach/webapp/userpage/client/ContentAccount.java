package com.TheJobCoach.webapp.userpage.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.util.client.CheckedExtendedDropListField;
import com.TheJobCoach.webapp.util.client.CheckedExtendedTextField;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.CheckedTextField;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.DialogBlockApplyReset;
import com.TheJobCoach.webapp.util.client.DialogBlockApplyReset.IApply;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.client.IExtendedField;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Grid;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentAccount implements EntryPoint, IChanged, ReturnValue, IApply {

	UserId user;

	final Lang lang = GWT.create(Lang.class);

	ClientUserValuesUtils values = null;

	DialogBlockApplyReset applyReset = null;

	public ContentAccount(Panel panel, UserId _user)
	{
		rootPanel = panel;
		user = _user;
		values = new ClientUserValuesUtils(rootPanel, user);		
	}

	Panel rootPanel = null;
	
	CheckedLabel clModel = new CheckedLabel("Type de compte", false, this);
	CheckedExtendedDropListField tfModel = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.ACCOUNT_MODEL_LIST, null, "",
			clModel);

	CheckedLabel clTitle = new CheckedLabel("Mon métier (ou nom de poste) visé", false, this);
	CheckedTextField tfTitle = new CheckedTextField(clTitle, ".*");

	CheckedLabel clStatus = new CheckedLabel("Statut actuel", false, this);
	CheckedExtendedDropListField tfStatus = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.ACCOUNT_STATUS_LIST, null, "",
			clStatus);

	CheckedLabel clKeywords = new CheckedLabel("Mes compétences clés", false, this);
	CheckedExtendedTextField tfKeywords = new CheckedExtendedTextField(new TextArea(), clKeywords, ".*");

	
	
	CheckedLabel clVirtualCoach = new CheckedLabel("Mon coach virtuel", false, this);
	CheckedExtendedDropListField tfVirtualCoach = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR_LIST, null, "",
			clVirtualCoach 
			);
	

	CheckedLabel clPublishSeeker = new CheckedLabel("Rendre mon profil visible aux autres chercheurs d'emploi", false, this);
	CheckedExtendedDropListField tfPublishSeeker = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.YES_NO_LIST, null, "",
			clPublishSeeker 
			);

	CheckedLabel clPublishCoach = new CheckedLabel("Rendre mon profil visible aux coachs emploi", false, this);
	CheckedExtendedDropListField tfPublishCoach = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.YES_NO_LIST, null, "",
			clPublishCoach 
			);

	CheckedLabel clPublishRecruiter = new CheckedLabel("Rendre mon profil visible aux recruteurs", false, this);
	CheckedExtendedDropListField tfPublishRecruiter = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.YES_NO_LIST, null, "",
			clPublishRecruiter 
			);
	
	void getValues()
	{	
		values.preloadValueList("ACCOUNT", this);
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

		ContentHelper.insertTitlePanel(simplePanelCenter, lang._TextMyAccount(), ClientImageBundle.INSTANCE.userVirtualCoachGoals());
		
		ContentHelper.insertSubTitlePanel(simplePanelCenter, "Mon mode d'inscription");

		Grid grid0 = new Grid(1, 2);
		simplePanelCenter.add(grid0);

		grid0.setWidget(0,0, clModel);
		grid0.setWidget(0,1, tfModel.getItem());

		fields.put(UserValuesConstantsAccount.ACCOUNT_MODEL, tfModel);
		
		ContentHelper.insertSubTitlePanel(simplePanelCenter, "Mes informations personnelles");

		Grid grid1 = new Grid(3, 2);
		simplePanelCenter.add(grid1);

		grid1.setWidget(0,0, clTitle);
		grid1.setWidget(0,1, tfTitle);

		grid1.setWidget(1,0, clStatus);
		grid1.setWidget(1,1, tfStatus.getItem());

		grid1.setWidget(2,0, clKeywords);
		grid1.setWidget(2,1, tfKeywords.getItem());

		fields.put(UserValuesConstantsAccount.ACCOUNT_TITLE, tfTitle);
		fields.put(UserValuesConstantsAccount.ACCOUNT_STATUS, tfStatus);
		fields.put(UserValuesConstantsAccount.ACCOUNT_KEYWORDS, tfKeywords);
		
		ContentHelper.insertSubTitlePanel(simplePanelCenter, "Mon coach virtuel personnel");

		Grid grid2 = new Grid(1, 2);
		simplePanelCenter.add(grid2);
		
		grid2.setWidget(0,0, clVirtualCoach);
		grid2.setWidget(0,1, tfVirtualCoach.getItem());

		fields.put(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, tfVirtualCoach);

		ContentHelper.insertSubTitlePanel(simplePanelCenter, "Visibilité de mon profil");

		Grid grid3 = new Grid(3, 2);
		simplePanelCenter.add(grid3);
		
		grid3.setWidget(0,0, clPublishSeeker);
		grid3.setWidget(0,1, tfPublishSeeker.getItem());

		grid3.setWidget(1,0, clPublishCoach);
		grid3.setWidget(1,1, tfPublishCoach.getItem());

		grid3.setWidget(2,0, clPublishRecruiter);
		grid3.setWidget(2,1, tfPublishRecruiter.getItem());

		fields.put(UserValuesConstantsAccount.ACCOUNT_PUBLISH_SEEKER, tfPublishSeeker);
		fields.put(UserValuesConstantsAccount.ACCOUNT_PUBLISH_COACH, tfPublishCoach);
		fields.put(UserValuesConstantsAccount.ACCOUNT_PUBLISH_RECRUITER, tfPublishRecruiter);
		
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
				//System.out.println("REcevied key " + testKey + " with value " + value);
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
