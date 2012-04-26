package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.CheckedTextField;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.CassandraException;
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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LostCredentials implements EntryPoint, IChanged {

	Panel rootPanel;
	private final DialogBox dBox = new DialogBox();
	final Lang lang = GWT.create(Lang.class);

	private CheckedLabel labelMail = new CheckedLabel(lang._TextUserEMail(), true, this);
	private final CheckedTextField textBoxMail = new CheckedTextField(labelMail, UserInformation.getMailRegexp());
	ButtonImageText btnLostCredentials = new ButtonImageText(ButtonImageText.Type.OK, lang._TextLostCredentialsValidate());
	ButtonImageText btnCancel = new ButtonImageText(ButtonImageText.Type.CANCEL, lang._TextCreateCancel());

	private final LoginServiceAsync loginService = GWT.create(LoginService.class);

	public LostCredentials(Panel panel)
	{
		rootPanel = panel;
	}

	// Create a handler for the create account button.em
	class LostCredentialsHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			btnLostCredentials.setEnabled(false);
			btnCancel.setEnabled(false);
			try {
				loginService.lostCredentials(textBoxMail.getValue(), LocaleInfo.getCurrentLocale().getLocaleName(), 
						new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						MessageBox.messageBoxException(rootPanel, "An error occured while retrieving account information: " + caught);
						btnLostCredentials.setEnabled(true);
						btnCancel.setEnabled(true);
					}

					public void onSuccess(Boolean b) 
					{	
						btnLostCredentials.setEnabled(true);
						btnCancel.setEnabled(true);					
						if (!b.booleanValue())
						{
							MessageBox.messageBox(rootPanel, MessageBox.TYPE.ERROR, "Unexpected Error", lang._TextLostCredentialsError());
						}
						else
						{
							MessageBox.messageBox(rootPanel, MessageBox.TYPE.INFO, "Success", lang._TextLostCredentialsOk());
							dBox.hide();
						}						
					}
				});
			} 
			catch (CassandraException e) 
			{
				MessageBox.messageBoxException(rootPanel, e);				
			}
		}
	}

	@Override
	public void changed(boolean ok, boolean init) 
	{
		if (init) return;
		btnLostCredentials.setEnabled(false);
		boolean setOk = true;
		setOk = setOk && textBoxMail.isValid();

		btnLostCredentials.setEnabled(setOk);	
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
		dBox.setText(lang._TextLostCredentials());
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		VerticalPanel vp = new VerticalPanel();

		Grid grid = new Grid(2, 2);
		grid.setBorderWidth(0);

		vp.setSpacing(20);

		vp.add(new Label(lang._TextLostCredentialsExplanation()));
		vp.add(grid);

		dBox.setWidget(vp);

		grid.setWidget(0, 0, labelMail);
		grid.setWidget(0, 1, textBoxMail);
		
		// Add a handler to the connect button clicker.
		LostCredentialsHandler createAccountHandler = new LostCredentialsHandler();
		btnLostCredentials.addClickHandler(createAccountHandler);

		// Add a handler to the connect button clicker.
		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dBox.hide();
			}});

		HorizontalPanel hpButton = new HorizontalPanel();
		hpButton.add(btnLostCredentials);
		hpButton.add(btnCancel);

		grid.setWidget(1, 1, hpButton);

		checkUserValues();

		rootPanel.add(dBox);
		dBox.center();	
	}

}
