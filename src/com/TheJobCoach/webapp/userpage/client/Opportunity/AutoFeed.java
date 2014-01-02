package com.TheJobCoach.webapp.userpage.client.Opportunity;

import java.util.HashMap;

import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.JobBoardDefinition;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.DetectChangeTextBox;
import com.TheJobCoach.webapp.util.client.DetectChangeTextBox.CustomChangeHandler;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.VerticalSpacer;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AutoFeed implements EntryPoint, ChangeHandler, KeyUpHandler, ValueChangeHandler<String>, CustomChangeHandler {
	
	private final static UserServiceAsync userService = GWT.create(UserService.class);

	private static final LangLogEntry langLogEntry = GWT.create(LangLogEntry.class);
	
	final DialogBox dBox = new DialogBox();
	
	UserId user;

	DetectChangeTextBox txtbxId = new DetectChangeTextBox();
	ListBox comboBoxSite = new ListBox();

	DialogBlockOkCancel okCancel;

	Panel rootPanel;

	IChooseResult<UserOpportunity> chooseResult;
	
	public AutoFeed(Panel panel, UserId _user, IChooseResult<UserOpportunity> chooseResult)
	{
		user = _user;
		rootPanel = panel;
		this.chooseResult = chooseResult;
	}

	private void launchEdition(UserOpportunity tmp)
	{
		tmp.ID = SiteUUID.getDateUuid();
		EditOpportunity ele = new EditOpportunity(rootPanel, user, tmp, chooseResult);
		ele.onModuleLoad();
	}
	
	private void commit()
	{
		userService.fetchUserOpportunity(user, txtbxId.getText(), comboBoxSite.getValue(comboBoxSite.getSelectedIndex()), 
				new ServerCallHelper<UserOpportunity>(rootPanel) {
			public void onSuccess(UserOpportunity tmp)
			{
				System.out.println("commit " + tmp);
				if (tmp != null)
				{
					dBox.hide();
					launchEdition(tmp);
				}
				else
				{	
					System.out.println("commit error");
					MessageBox errorBox = MessageBox.messageBox(rootPanel, MessageBox.TYPE.ERROR, langLogEntry._Text_feed_result_error());
					errorBox.onModuleLoad();
					okCancel.setEnabled(true);
				}
			}
		});
	};

	HashMap<String, Integer> reverseKey = new HashMap<String, Integer>();
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{	
		dBox.setText(langLogEntry._Text_feed_from());
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		VerticalPanel hp = new VerticalPanel();		
		Grid grid = new Grid(2, 2);
		grid.setBorderWidth(0);
		dBox.setWidget(hp);		
		grid.setSize("100%", "100%");

		hp.add(grid);
		
		Label lblFromRef = new Label(langLogEntry._Text_feed_ref_id());
		grid.setWidget(0, 0, lblFromRef);		
		grid.setWidget(0, 1, txtbxId);
		grid.getCellFormatter().setWidth(0, 1, "50%");
		grid.getCellFormatter().setWidth(0, 0, "50%");
		txtbxId.setWidth("100%");
		lblFromRef.setWidth("100%");
		
		Label lblSite = new Label(langLogEntry._Text_feed_site());
		grid.setWidget(1, 0, lblSite);
		grid.setWidget(1, 1, comboBoxSite);
		
		comboBoxSite.addItem("", "");
		for (String key : JobBoardDefinition.JOBBOARD_MAP.keySet())
		{
			comboBoxSite.addItem(key, JobBoardDefinition.JOBBOARD_MAP.get(key));
			reverseKey.put(key, comboBoxSite.getItemCount() - 1);
		}

		okCancel = new DialogBlockOkCancel(null, dBox);
		okCancel.getOk().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				okCancel.setEnabled(false);
				commit();
			}
		});
		
		comboBoxSite.addChangeHandler(this);
		
		txtbxId.addKeyUpHandler(this);
		txtbxId.addValueChangeHandler(this);
		txtbxId.setCustomChangeHandler(this);
		
		hp.add(new VerticalSpacer("10px"));
		hp.add(okCancel);
		
		checkChange();
		
		dBox.center();
	}

	public void checkChange()
	{
		System.out.println("checkChange");
		
		// check it corresponds to a given site
		for (String key: JobBoardDefinition.JOBBOARD_REGEXP.keySet())
		{
			System.out.println("------------------");
			System.out.println(txtbxId.getValue());
			System.out.println(key);
			System.out.println(JobBoardDefinition.JOBBOARD_REGEXP.get(key));
			System.out.println(txtbxId.getValue().matches(JobBoardDefinition.JOBBOARD_REGEXP.get(key)));
			if (txtbxId.getValue().matches(JobBoardDefinition.JOBBOARD_REGEXP.get(key)))
			{
				comboBoxSite.setSelectedIndex(reverseKey.get(key));
			}
		}

		boolean setOk = true;
		setOk = setOk && !"".equals(txtbxId.getValue());
		setOk = setOk && (comboBoxSite.getSelectedIndex() > 0);
		okCancel.getOk().setEnabled(setOk);
	}
	
	@Override
	public void onChange(ChangeEvent event)
	{
		System.out.println("onChange");
		checkChange();
	}

	@Override
	public void onKeyUp(KeyUpEvent event)
	{
		System.out.println("onKeyUp");
		checkChange();
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event)
	{
		System.out.println("onValueChange");
		checkChange();
	}

	@Override
	public void call()
	{
		System.out.println("call");
		checkChange();
	}

}
