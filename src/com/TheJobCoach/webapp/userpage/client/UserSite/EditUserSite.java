package com.TheJobCoach.webapp.userpage.client.UserSite;

import java.util.Date;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.CheckedTextField;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.GridHelper;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EditUserSite implements EntryPoint, IChanged {

	static Lang lang = GWT.create(Lang.class);
	private final UserServiceAsync userService = GWT.create(UserService.class);

	UserId user;

	CheckedTextField textBoxName = new CheckedTextField(".+");
	CheckedLabel lblName = new CheckedLabel(lang._TextName(), true, textBoxName);
	RichTextArea textAreaDescription = new RichTextArea();
	CheckedTextField textBoxUrl = new CheckedTextField("http://.*", "http://");
	CheckedLabel lblUrl = new CheckedLabel(lang._TextURL(), true, textBoxUrl);
	TextBox textBoxLogin = new TextBox();
	TextBox textBoxPassword = new TextBox();
	DateBox datePickerLastVisit = new DateBox();
	
	Panel rootPanel;
	IChooseResult<UserJobSite> result;
	UserJobSite currentUserSite;
	
	DialogBlockOkCancel okCancel;
	
	public EditUserSite(Panel panel, UserJobSite _currentUserSite, UserId _user, IChooseResult<UserJobSite> _result)
	{
		rootPanel = panel;
		currentUserSite = _currentUserSite;
		result = _result;
		user = _user;
	}

	private void setUserJobSite(UserJobSite opp)
	{
		if (currentUserSite != null)
		{
			currentUserSite = opp;
			textBoxName.setValue(opp.name);		
			textAreaDescription.setHTML(opp.description);
			textBoxUrl.setValue(opp.URL);
			textBoxLogin.setValue(opp.login);
			textBoxPassword.setValue(opp.password);
			datePickerLastVisit.setValue(opp.lastVisit);
		}
	}

	private UserJobSite getUserJobSite()
	{
		String ID = new Date().toString();
		if (currentUserSite != null) ID = currentUserSite.ID;
		return new UserJobSite(ID,
				textBoxName.getText(), textBoxUrl.getText(), textAreaDescription.getHTML(), textBoxLogin.getValue(),
				textBoxPassword.getText(), datePickerLastVisit.getValue());
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{			
		final DialogBox dBox = new DialogBox();
		dBox.setText(currentUserSite == null ? lang._TextCreateUserSiteTitle() : lang._TextEditUserSiteTitle());
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		GridHelper grid = new GridHelper(rootPanel);

		VerticalPanel vp = new VerticalPanel();
		dBox.setWidget(vp);		

		vp.add(grid);
		
		grid.addLine(lblName, textBoxName);
		grid.addLine(new Label(lang._TextDescription()), textAreaDescription);
		grid.addLine(lblUrl, textBoxUrl);
		grid.addLine(new Label(lang._TextLogin()), textBoxLogin);
		grid.addLine(new Label(lang._TextPassword()), textBoxPassword);
		grid.addLine(new Label(lang._TextLastVisit()), datePickerLastVisit);

		setUserJobSite(currentUserSite);

		okCancel = new DialogBlockOkCancel(null, dBox);
		okCancel.getOk().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				okCancel.setEnabled(false);
				EasyAsync.serverCall(rootPanel, new EasyAsync.ServerCallRun() {
					public void Run() throws CassandraException
					{
						final UserJobSite resultSite = getUserJobSite();
						userService.setUserSite(user, resultSite, new ServerCallHelper<Integer>(rootPanel)
						{	
							@Override
							public void onSuccess(Integer r)
							{
								result.setResult(resultSite);
							}
						});
					}});
				dBox.hide();			
			}
		});		

		textBoxName.registerListener(this);
		textBoxUrl.registerListener(this);

		vp.add(okCancel);
		changed(false, true, false);
		dBox.center();
	}

	@Override
	public void changed(boolean ok, boolean isDefault, boolean init)
	{		
		if (init) return;
		okCancel.getOk().setEnabled(false);
		boolean setOk = true;
		setOk = setOk && textBoxName.isValid();
		setOk = setOk && textBoxUrl.isValid();
		okCancel.getOk().setEnabled(setOk);	
	}
}
