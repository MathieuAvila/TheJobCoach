package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.CheckedTextField;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CreateAccount implements EntryPoint, IChanged {

	Panel rootPanel;
	final Lang lang = GWT.create(Lang.class);

	private CheckedLabel labelMail = new CheckedLabel(lang._TextUserEMail(), true, this);
	private CheckedLabel lblUserName = new CheckedLabel(lang._TextUserName(), true, this);
	private final CheckedTextField textBoxUserName = new CheckedTextField(lblUserName, UserId.getRegexp());
	private final CheckedTextField textBoxMail = new CheckedTextField(labelMail, UserInformation.getMailRegexp());
	private final TextBox newUserPassword = new PasswordTextBox();
	private final TextBox newUserPasswordCheck = new PasswordTextBox();
	private final TextBox textBoxCreateName = new TextBox();
	private final TextBox textBoxFirstName = new TextBox();
	private final DialogBox dBox = new DialogBox();
	private CheckedLabel labelPasswordCheck = new CheckedLabel(lang._TextUserPasswordCheck(), true, null);
	private CheckedLabel labelPassword = new CheckedLabel(lang._TextUserPassword(), true, null);
	private Button btnCreateAccount = null;
	private final DialogBlockOkCancel okCancel = new DialogBlockOkCancel(lang._TextCreateAccountOk(), dBox);
	
	private final LoginServiceAsync loginService = GWT.create(LoginService.class);

	public CreateAccount(Panel panel)
	{
		rootPanel = panel;
	}

	// Create a handler for the create account button.em
	class CreateAccountHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			okCancel.setEnabled(false);
			loginService.createAccount(
					new UserId(textBoxUserName.getText(), "", UserId.UserType.USER_TYPE_SEEKER),
					new UserInformation(textBoxCreateName.getText(), textBoxMail.getText(), newUserPassword.getText(), textBoxFirstName.getText()),
					LocaleInfo.getCurrentLocale().getLocaleName(),
					new AsyncCallback<MainPageReturnCode.CreateAccountStatus>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							MessageBox.messageBoxException(rootPanel, "An error occured while creating account: " + caught);
						}

						public void onSuccess(MainPageReturnCode.CreateAccountStatus result) 
						{							
							switch (result)
							{
							case CREATE_STATUS_ALREADY_EXISTS:
								MessageBox.messageBox(rootPanel, MessageBox.TYPE.ERROR, "Error", lang._TextCreateEmailAlreadyExists());
								break;
							case CREATE_STATUS_ALREADY_EXISTS_EMAIL:
								MessageBox.messageBox(rootPanel, MessageBox.TYPE.ERROR, "Error", lang._TextCreateLoginAlreadyExists());
								break;
							case CREATE_STATUS_ERROR:
								MessageBox.messageBox(rootPanel, MessageBox.TYPE.ERROR, "Unexpected Error", lang._TextCreateLoginUnexpectedError());
								break;
							case CREATE_STATUS_INVALID:
								MessageBox.messageBox(rootPanel, MessageBox.TYPE.INFO, "Unexpected Error", lang._TextCreateLoginUnexpectedError());
								break;
							case CREATE_STATUS_OK:
								MessageBox.messageBox(rootPanel, MessageBox.TYPE.INFO, "Success", lang._TextCreateAccountSuccess());
								dBox.hide();
								break;
							}
							okCancel.setEnabled(true);							
						}
					});
		}
	}


	@Override
	public void changed(boolean ok, boolean init) 
	{
		if (init) return;
		btnCreateAccount.setEnabled(false);
		boolean setOk = true;
		setOk = setOk && textBoxUserName.isValid();
		setOk = setOk && textBoxMail.isValid();

		if (!UserId.checkUserName(newUserPasswordCheck.getValue()) || !newUserPasswordCheck.getValue().equals(newUserPassword.getValue())) 
		{
			labelPasswordCheck.setStatus(false);
			labelPassword.setStatus(false);
			setOk = false;
		}
		else
		{
			labelPasswordCheck.setStatus(true);
			labelPassword.setStatus(true);
		}
		btnCreateAccount.setEnabled(setOk);	
	}

	public void checkUserValues()
	{		
		changed(true, false); 
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{			
		dBox.setStylePrimaryName("common-form-dialog");
		dBox.setText(lang._TextCreateAccountTitle());
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		Grid grid = new Grid(7, 2);
		grid.setBorderWidth(0);
		VerticalPanel vp = new VerticalPanel();
		vp.add(grid);
		dBox.setWidget(vp);

		grid.setWidget(0, 0, lblUserName);
		grid.setWidget(0, 1, textBoxUserName);
		grid.setWidget(1, 0, labelMail);
		grid.setWidget(1, 1, textBoxMail);

		Label label_2 = new Label(lang._TextUserLastName());
		grid.setWidget(2, 0, label_2);

		grid.setWidget(2, 1, textBoxCreateName);

		Label label_1 = new Label(lang._TextUserFirstName());
		grid.setWidget(3, 0, label_1);

		grid.setWidget(3, 1, textBoxFirstName);
		grid.setWidget(4, 0, labelPassword);
		grid.setWidget(4, 1, newUserPassword);
		grid.setWidget(5, 0, labelPasswordCheck);
		grid.setWidget(5, 1, newUserPasswordCheck);	

		ValueChangeHandler<String> changeH = new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				checkUserValues();
			}
		};

		KeyUpHandler changeKey = new KeyUpHandler()	{
			@Override
			public void onKeyUp(KeyUpEvent event) 
			{
				checkUserValues();
			}
		};

		newUserPasswordCheck.addValueChangeHandler(changeH);
		newUserPassword.addValueChangeHandler(changeH);

		newUserPasswordCheck.addKeyUpHandler(changeKey);
		newUserPassword.addKeyUpHandler(changeKey);

		btnCreateAccount = okCancel.getOk();
		btnCreateAccount.addClickHandler(new CreateAccountHandler());
		vp.add(okCancel);

		checkUserValues();

		rootPanel.add(dBox);
		dBox.center();	
	}

}
