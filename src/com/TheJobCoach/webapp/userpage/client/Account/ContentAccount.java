package com.TheJobCoach.webapp.userpage.client.Account;

import java.util.ArrayList;
import java.util.HashMap;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
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
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IExtendedField;
import com.TheJobCoach.webapp.util.client.LangUtil;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.MessageBox.ICallback;
import com.TheJobCoach.webapp.util.client.MessageBox.TYPE;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.UserImageHelper;
import com.TheJobCoach.webapp.util.client.VerticalSpacer;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstants;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContentAccount extends VerticalPanel implements IChanged, ReturnValue, IApply{

	UserId user;

	final static Lang lang = GWT.create(Lang.class);
	final static LangAccount langAccount = GWT.create(LangAccount.class);
	final static LangUtil langUtil = GWT.create(LangUtil.class);
	private static final UserServiceAsync userService = GWT.create(UserService.class);

	Label lblPassword = new Label(lang._TextPassword());
	ButtonImageText btnChangePassword = new ButtonImageText(ButtonImageText.Type.LOCK, langAccount.Text_changespassword());
	
	ClientUserValuesUtils values = null;

	DialogBlockApplyReset applyReset = null;

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

	ButtonImageText btnChangePhoto = new ButtonImageText(ButtonImageText.Type.USER_IMAGE, langAccount.changeMyPhoto());
	SimplePanel image = new SimplePanel();
	
	CheckedExtendedDropListField tfPublishSeeker = new CheckedExtendedDropListField(
			UserValuesConstants.YES_NO_LIST, langUtil.yesNoMap(), "yesNoMap_");
	CheckedLabel clPublishSeeker = new CheckedLabel(langAccount.Text_VisibleProfileSeeker(), false, tfPublishSeeker);

	CheckedExtendedDropListField tfPublishCoach = new CheckedExtendedDropListField(
			UserValuesConstants.YES_NO_LIST, langUtil.yesNoMap(), "yesNoMap_");
	CheckedLabel clPublishCoach = new CheckedLabel(langAccount.Text_VisibleProfileCoach(), false, tfPublishCoach);

	CheckedExtendedDropListField tfPublishRecruiter = new CheckedExtendedDropListField(
			UserValuesConstants.YES_NO_LIST, langUtil.yesNoMap(), "yesNoMap_");
	CheckedLabel clPublishRecruiter = new CheckedLabel(langAccount.Text_VisibleProfileRecruiter(), false, tfPublishRecruiter);
	
	CheckedTextField tfFirstName = new CheckedTextField("...*");
	CheckedLabel clFirstName = new CheckedLabel(langAccount.myFirstName(), false, tfFirstName);

	CheckedTextField tfLastName = new CheckedTextField("...*");
	CheckedLabel clLastName = new CheckedLabel(langAccount.myLastName(), false, tfLastName);

	VerticalPanel dangerousSettings = new VerticalPanel();
	ButtonImageText btnDeleteAccount = new ButtonImageText(ButtonImageText.Type.DESTROY, langAccount.deleteAccount());
	ButtonImageText btnCancelDeleteAccount = new ButtonImageText(ButtonImageText.Type.DESTROY, langAccount.cancelDeleteConfirm());
	Label deletionInformationDate = new Label();
	SimplePanel destroyPanel = new SimplePanel();
	SimplePanel restorePanel = new SimplePanel();
	
	ButtonImageText btnShowDangerousSettings = new ButtonImageText(ButtonImageText.Type.OK, langAccount.dangerousSettings());
	
	void getValues()
	{	
		values.preloadValueList("ACCOUNT", this);
	}

	HashMap<String, IExtendedField> fields = new HashMap<String, IExtendedField>();
	
	private void toggleDeletion(final boolean delete)
	{
		EasyAsync.serverCall(RootPanel.get(), new EasyAsync.ServerCallRun() {
		public void Run() throws CassandraException, CoachSecurityException, SystemException
		{
				userService.toggleDeleteAccount(delete, new ServerCallHelper<Boolean>(RootPanel.get()));
		}});
	}
	
	public void updateImage()
	{
		image.setWidget(UserImageHelper.getImage(user, 256));
	}
	
	public ContentAccount(UserId _user)
	{
		user = _user;
		values = ClientUserValuesUtils.getInstance(user);	
			
		setSize("100%", "100%");

		ContentHelper.insertTitlePanel(this, lang._TextMyAccount(), ClientImageBundle.INSTANCE.parametersContent());
		
		ContentHelper.insertSubTitlePanel(this, langAccount.Text_TitleAccount());

		Grid grid0 = new Grid(2, 2);
		this.add(grid0);

		grid0.setWidget(0,0, clModel);
		grid0.setWidget(0,1, tfModel.getItem());

		fields.put(UserValuesConstantsAccount.ACCOUNT_MODEL, tfModel);

		grid0.setWidget(1,0, lblPassword);
		grid0.setWidget(1,1, btnChangePassword);
		btnChangePassword.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event)
			{
				new EditPassword(user);
			}	
		});
		
		ContentHelper.insertSubTitlePanel(this, langAccount.Text_TitlePersonalInformation());

		Grid grid1 = new Grid(4, 2);
		this.add(grid1);

		grid1.setWidget(0,0, clTitle);
		grid1.setWidget(0,1, tfTitle);

		grid1.setWidget(1,0, clStatus);
		grid1.setWidget(1,1, tfStatus.getItem());

		grid1.setWidget(2,0, clKeywords);
		grid1.setWidget(2,1, tfKeywords.getItem());

		grid1.setWidget(3,0, btnChangePhoto);
		grid1.setWidget(3,1, image);
		updateImage();
		btnChangePhoto.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event)
			{
				new EditPhoto(user, new IChooseResult<String>()	{
					@Override
					public void setResult(String result)
					{
						updateImage();
					}});
			}			
		});
		
		fields.put(UserValuesConstantsAccount.ACCOUNT_TITLE, tfTitle);
		fields.put(UserValuesConstantsAccount.ACCOUNT_STATUS, tfStatus);
		fields.put(UserValuesConstantsAccount.ACCOUNT_KEYWORDS, tfKeywords);
		
		ContentHelper.insertSubTitlePanel(this, langAccount.Text_TitleVisibility());

		Grid grid3 = new Grid(3, 2);
		this.add(grid3);
		
		grid3.setWidget(0,0, clPublishSeeker);
		grid3.setWidget(0,1, tfPublishSeeker.getItem());

		grid3.setWidget(1,0, clPublishCoach);
		grid3.setWidget(1,1, tfPublishCoach.getItem());

		grid3.setWidget(2,0, clPublishRecruiter);
		grid3.setWidget(2,1, tfPublishRecruiter.getItem());

		fields.put(UserValuesConstantsAccount.ACCOUNT_PUBLISH_SEEKER, tfPublishSeeker);
		fields.put(UserValuesConstantsAccount.ACCOUNT_PUBLISH_COACH, tfPublishCoach);
		fields.put(UserValuesConstantsAccount.ACCOUNT_PUBLISH_RECRUITER, tfPublishRecruiter);
				
		ContentHelper.insertSubTitlePanel(this, langAccount.dangerousSettings());
		this.add(btnShowDangerousSettings);
		btnShowDangerousSettings.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event)
			{
				btnShowDangerousSettings.setVisible(false);
				dangerousSettings.setVisible(true);
			}			
		});
		
		ButtonImageText btnDeleteAccount = new ButtonImageText(ButtonImageText.Type.DESTROY, langAccount.deleteAccount());
		ButtonImageText btnCancelDeleteAccount = new ButtonImageText(ButtonImageText.Type.DESTROY, langAccount.cancelDeletion());

		btnDeleteAccount.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event)
			{
				MessageBox mb = new MessageBox(RootPanel.get(), true, true, TYPE.WARNING, 
						langAccount.deleteAccount(), 
						langAccount.deleteConfirm(), new ICallback() {

							@Override
							public void complete(boolean ok)
							{
								if (ok) toggleDeletion(true);
							}});
				mb.onModuleLoad();
				}		
			
		});
		
		btnCancelDeleteAccount.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event)
			{
				MessageBox mb = new MessageBox(RootPanel.get(), true, true, TYPE.WARNING, 
						langAccount.cancelDeletion(), 
						langAccount.cancelDeleteConfirm(), new ICallback() {

							@Override
							public void complete(boolean ok)
							{
								if (ok) toggleDeletion(false);
							}});
				mb.onModuleLoad();
				}		
			
		});
	
		VerticalPanel restorePanelContent = new VerticalPanel();

		dangerousSettings.setVisible(false);
		
		Grid grid4 = new Grid(2, 2);
		this.add(grid4);
		
		grid4.setWidget(0,0, clFirstName);
		grid4.setWidget(0,1, tfFirstName);

		grid4.setWidget(1,0, clLastName);
		grid4.setWidget(1,1, tfLastName);

		dangerousSettings.add(grid4);
		dangerousSettings.add(destroyPanel);
		dangerousSettings.add(restorePanel);
		this.add(new VerticalSpacer("1em"));
		this.add(dangerousSettings);
		
		restorePanel.add(restorePanelContent);
		destroyPanel.add(btnDeleteAccount);
		restorePanelContent.add(deletionInformationDate);
		restorePanelContent.add(new VerticalSpacer("1em"));
		restorePanelContent.add(btnCancelDeleteAccount);
		destroyPanel.setVisible(false);
		restorePanel.setVisible(false);

		fields.put(UserValuesConstantsAccount.ACCOUNT_FIRSTNAME, tfFirstName);
		fields.put(UserValuesConstantsAccount.ACCOUNT_LASTNAME, tfLastName);

		for (IExtendedField f: fields.values()) f.registerListener(this);
		
		applyReset = new DialogBlockApplyReset(new ArrayList<IExtendedField>(fields.values()), this);

		this.add(applyReset);

		getValues();
		values.addListener(UserValuesConstantsAccount.ACCOUNT_DELETION, this);	
		values.addListener(UserValuesConstantsAccount.ACCOUNT_DELETION_DATE, this);	
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
		if (key.equals(UserValuesConstantsAccount.ACCOUNT_DELETION))
		{
			boolean deletion = UserValuesConstants.YES.equals(value);
			destroyPanel.setVisible(!deletion);
			restorePanel.setVisible(deletion);
		}
		if (key.equals(UserValuesConstantsAccount.ACCOUNT_DELETION_DATE))
		{
			String deletionDate = FormatUtil.getFormattedDate(FormatUtil.getStringDate(value));
			deletionInformationDate.setText(langAccount.deletionPlanned() + deletionDate);
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
