package com.TheJobCoach.webapp.userpage.client.CoachSettings;

import java.util.ArrayList;
import java.util.HashMap;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.CheckedExtendedDropListField;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils.ReturnValue;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.DialogBlockApplyReset;
import com.TheJobCoach.webapp.util.client.DialogBlockApplyReset.IApply;
import com.TheJobCoach.webapp.util.client.DynamicImage;
import com.TheJobCoach.webapp.util.client.DynamicLabel;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.client.IExtendedField;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Grid;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentCoachSettings implements EntryPoint, IChanged, ReturnValue, IApply {

	UserId user;

	final static Lang lang = GWT.create(Lang.class);
	final static LangCoachSettings langAccount = GWT.create(LangCoachSettings.class);
	final static com.TheJobCoach.webapp.util.client.Lang langUtil = GWT.create(com.TheJobCoach.webapp.util.client.Lang.class);

	ClientUserValuesUtils values = null;

	DialogBlockApplyReset applyReset = null;

	public ContentCoachSettings(Panel panel, UserId _user)
	{
		rootPanel = panel;
		user = _user;
		values = new ClientUserValuesUtils(rootPanel, user);		
	}

	Panel rootPanel = null;

	CheckedExtendedDropListField tfVirtualCoach = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR_LIST, langAccount.coachNameMap(), "coachNameMap_");
	CheckedLabel clVirtualCoach = new CheckedLabel(langAccount.Text_MyVirtualCoach(), false, tfVirtualCoach);
	DynamicLabel dlVirtualCoach = new DynamicLabel(tfVirtualCoach, langAccount.coachDescriptionMap(), "coachDescriptionMap_");
	
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

		ContentHelper.insertTitlePanel(simplePanelCenter, lang._TextCoachSettings(), ClientImageBundle.INSTANCE.coachSettingsContent());
		
		ContentHelper.insertSubTitlePanel(simplePanelCenter, langAccount.Text_TitleCoach());

		Grid grid2 = new Grid(1, 4);
		simplePanelCenter.add(grid2);
		
		grid2.setWidget(0,0, clVirtualCoach);
		grid2.setWidget(0,1, tfVirtualCoach.getItem());
		grid2.setWidget(0,2, dlVirtualCoach);
		HashMap<String, ImageResource> images = new HashMap<String, ImageResource>();
		images.put("DEFAULT_MAN", ClientImageBundle.INSTANCE.coachIconSmall());
		images.put("DEFAULT_WOMAN", ClientImageBundle.INSTANCE.coachIconWomanSmall());
		images.put("COACH_MILITARY", ClientImageBundle.INSTANCE.coachIconMilitarySmall());
		images.put("COACH_SURFER", ClientImageBundle.INSTANCE.coachIconSurferSmall());
		DynamicImage diVirtualCoach = new DynamicImage(tfVirtualCoach, images);
			grid2.setWidget(0,3, diVirtualCoach);
		dlVirtualCoach.setWidth("300px");
		grid2.setCellSpacing(20);
		
		fields.put(UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR, tfVirtualCoach);

		applyReset = new DialogBlockApplyReset(new ArrayList<IExtendedField>(fields.values()), this);

		for (IExtendedField f: fields.values()) f.registerListener(this);
		
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
