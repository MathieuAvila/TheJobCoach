package com.TheJobCoach.webapp.userpage.client.Account;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.CheckedTextField;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.GridHelper;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditPassword implements IChanged {

	private static final UserServiceAsync userService = GWT.create(UserService.class);
	final DialogBox dBox = new DialogBox();

	UserId user;

	final static Lang lang = GWT.create(Lang.class);
	final static LangAccount langPassword = GWT.create(LangAccount.class);
	
	final static String PWD_REGEXP = "......*";
	
	CheckedTextField textBoxPassword = new CheckedTextField(PWD_REGEXP, "");
	CheckedLabel lblTypePassword = new CheckedLabel(langPassword._Text_typepassword(), true, textBoxPassword);
	
	CheckedTextField textBoxRetypePassword = new CheckedTextField(PWD_REGEXP, "");
	CheckedLabel lblRetypePassword = new CheckedLabel(langPassword._Text_retypepassword(), true, textBoxRetypePassword);
	
	DialogBlockOkCancel okCancel;

	public void commit()
	{
		EasyAsync.serverCall(RootPanel.get(), new EasyAsync.ServerCallRun() {
			public void Run() throws CassandraException
			{
				userService.setPassword(user, textBoxPassword.getValue(), new ServerCallHelper<String>(RootPanel.get()) {
					@Override
					public void onSuccess(String result)
					{
						dBox.hide();
					}
					}
				);
			}});		
	}

	public EditPassword(UserId _user)
	{
		user = _user;
		dBox.setText(langPassword.Text_changespassword());
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		GridHelper grid = new GridHelper(RootPanel.get(), "70%", "30%");

		grid.addLine(1, lblTypePassword, textBoxPassword);		
		grid.addLine(2, lblRetypePassword, textBoxRetypePassword);
		
		okCancel = new DialogBlockOkCancel(null, dBox);
		okCancel.getOk().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				commit();
			}
		});
		
		textBoxPassword.registerListener(this);
		textBoxRetypePassword.registerListener(this);
		changed(false, true, true);
		
		VerticalPanel vp = new VerticalPanel();
		dBox.setWidget(vp);	
		vp.add(grid);
		vp.add(okCancel);
		
		dBox.center();
	}


	@Override
	public void changed(boolean ok, boolean changed, boolean init)
	{
		boolean valid = (textBoxPassword.isValid()) 
				&& (textBoxRetypePassword.isValid()) 
				&& (textBoxRetypePassword.getValue().equals(textBoxPassword.getValue()));
		okCancel.getOk().setEnabled(valid);
	}
}
