package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.CheckedTextField;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
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
	DialogBlockOkCancel okCancel;
	
	private final LoginServiceAsync loginService = GWT.create(LoginService.class);

	public LostCredentials(Panel panel)
	{
		rootPanel = panel;
	}

	// Create a handler for the create account button.em
	class LostCredentialsHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			okCancel.setEnabled(false);
			try {
				loginService.lostCredentials(textBoxMail.getValue(), LocaleInfo.getCurrentLocale().getLocaleName(), 
						new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						MessageBox.messageBoxException(rootPanel, "An error occured while retrieving account information: " + caught);
						okCancel.setEnabled(true);						
					}

					public void onSuccess(Boolean b) 
					{	
						okCancel.setEnabled(true);				
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
	public void changed(boolean ok, boolean isDefault, boolean init) 
	{
		if (init) return;
		okCancel.getOk().setEnabled(false);
		boolean setOk = true;
		setOk = setOk && textBoxMail.isValid();
		okCancel.getOk().setEnabled(setOk);	
	}

	public void checkUserValues()
	{		
		changed(true, true, false); 
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

		Grid grid = new Grid(1, 2);
		grid.setBorderWidth(0);

		vp.setSpacing(20);

		vp.add(new Label(lang._TextLostCredentialsExplanation()));
		vp.add(grid);

		dBox.setWidget(vp);

		grid.setWidget(0, 0, labelMail);
		grid.setWidget(0, 1, textBoxMail);
		
		okCancel = new DialogBlockOkCancel(lang._TextLostCredentialsValidate(), dBox);
		okCancel.getOk().addClickHandler(new LostCredentialsHandler());
		
		vp.add(okCancel);

		checkUserValues();

		rootPanel.add(dBox);
		dBox.center();	
	}

}
