package com.TheJobCoach.webapp.userpage.client.Account;

import java.util.ArrayList;
import java.util.HashMap;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.CheckedExtendedDropListField;
import com.TheJobCoach.webapp.util.client.CheckedExtendedTextField;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.CheckedTextField;
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Grid;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentAccount implements EntryPoint, IChanged, ReturnValue, IApply {

	UserId user;

	final static Lang lang = GWT.create(Lang.class);
	final static LangAccount langAccount = GWT.create(LangAccount.class);
	final static com.TheJobCoach.webapp.util.client.Lang langUtil = GWT.create(com.TheJobCoach.webapp.util.client.Lang.class);
	
	Label lblPassword = new Label(lang._TextPassword());
	ButtonImageText btnChangePassword = new ButtonImageText(ButtonImageText.Type.NEW, langAccount.Text_changespassword());
	
	ClientUserValuesUtils values = null;

	DialogBlockApplyReset applyReset = null;

	public ContentAccount(Panel panel, UserId _user)
	{
		rootPanel = panel;
		user = _user;
		values = new ClientUserValuesUtils(rootPanel, user);		
	}

	Panel rootPanel = null;
	
	CheckedExtendedDropListField tfModel = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.ACCOUNT_MODEL_LIST, langAccount.accountTypeMap(), "accountTypeMap_");
	CheckedLabel clModel = new CheckedLabel(langAccount.Text_AccountType(), false, tfModel);
	
	CheckedTextField tfTitle = new CheckedTextField(".*");
	CheckedLabel clTitle = new CheckedLabel(langAccount.Text_JobTitle(), false, tfTitle);
	
	CheckedExtendedDropListField tfStatus = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.ACCOUNT_STATUS_LIST, langAccount.accountStatusMap(), "accountStatusMap_");
	CheckedLabel clStatus = new CheckedLabel(langAccount.Text_ActualStatus(), false, tfStatus);
	
	CheckedExtendedTextField tfKeywords = new CheckedExtendedTextField(new TextArea(), "[a-zA-Z0-9-\\.\\n ]*");
	CheckedLabel clKeywords = new CheckedLabel(langAccount.Text_Skills(), false, tfKeywords);
	
	CheckedExtendedDropListField tfVirtualCoach = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.ACCOUNT_COACH_AVATAR_LIST, langAccount.coachNameMap(), "coachNameMap_");
	CheckedLabel clVirtualCoach = new CheckedLabel(langAccount.Text_MyVirtualCoach(), false, tfVirtualCoach);
	DynamicLabel dlVirtualCoach = new DynamicLabel(tfVirtualCoach, langAccount.coachDescriptionMap(), "coachDescriptionMap_");
	
	CheckedExtendedDropListField tfPublishSeeker = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.YES_NO_LIST, langUtil.yesNoMap(), "yesNoMap_");
	CheckedLabel clPublishSeeker = new CheckedLabel(langAccount.Text_VisibleProfileSeeker(), false, tfPublishSeeker);

	CheckedExtendedDropListField tfPublishCoach = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.YES_NO_LIST, langUtil.yesNoMap(), "yesNoMap_");
	CheckedLabel clPublishCoach = new CheckedLabel(langAccount.Text_VisibleProfileCoach(), false, tfPublishCoach);

	CheckedExtendedDropListField tfPublishRecruiter = new CheckedExtendedDropListField(
			UserValuesConstantsAccount.YES_NO_LIST, langUtil.yesNoMap(), "yesNoMap_");
	CheckedLabel clPublishRecruiter = new CheckedLabel(langAccount.Text_VisibleProfileRecruiter(), false, tfPublishRecruiter);
	
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

		ContentHelper.insertTitlePanel(simplePanelCenter, lang._TextMyAccount(), ClientImageBundle.INSTANCE.parametersContent());
		
		ContentHelper.insertSubTitlePanel(simplePanelCenter, langAccount.Text_TitleAccount());

		Grid grid0 = new Grid(2, 2);
		simplePanelCenter.add(grid0);

		grid0.setWidget(0,0, clModel);
		grid0.setWidget(0,1, tfModel.getItem());

		fields.put(UserValuesConstantsAccount.ACCOUNT_MODEL, tfModel);

		grid0.setWidget(1,0, lblPassword);
		grid0.setWidget(1,1, btnChangePassword);
		btnChangePassword.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event)
			{
				EditPassword ep = new EditPassword(rootPanel, user);
				ep.onModuleLoad();
			}	
		});
		
		ContentHelper.insertSubTitlePanel(simplePanelCenter, langAccount.Text_TitlePersonalInformation());

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

		ContentHelper.insertSubTitlePanel(simplePanelCenter, langAccount.Text_TitleVisibility());

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
