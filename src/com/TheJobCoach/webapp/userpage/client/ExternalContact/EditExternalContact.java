package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import com.TheJobCoach.webapp.userpage.client.ComponentUpdatePeriod;
import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.Coach.MessagePipe;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.CheckedTextField;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EditExternalContact implements EntryPoint, IChanged, IEditDialogModel<ExternalContact>  {

	final static Lang lang = GWT.create(Lang.class);
	final static LangExternalContact langExternalContact = GWT.create(LangExternalContact.class);


	UserId user;

	CheckedTextField textBoxFirstName = new CheckedTextField(".+");
	CheckedLabel lblFirstName = new CheckedLabel(langExternalContact._TextFirstName(), true, textBoxFirstName);
	CheckedTextField textBoxLastName = new CheckedTextField(".+");
	CheckedLabel lblLastName = new CheckedLabel(langExternalContact._TextLastName(), true, textBoxLastName);
	CheckedTextField textBoxEmail = new CheckedTextField(".+@.+|");
	CheckedLabel lblEmail = new CheckedLabel(langExternalContact._Text_Email(), false, textBoxEmail);
	CheckedTextField textBoxPhone = new CheckedTextField("[0-9 \\.]*");
	CheckedLabel lblPhone = new CheckedLabel(langExternalContact._Text_Phone(), false, textBoxPhone);
	TextBox textBoxOrganization = new TextBox();
	Label lblOrganization = new Label(langExternalContact._Text_Organization());
	RichTextArea textAreaPersonalNote = new RichTextArea();
	Label lblPersonalNote = new Label(langExternalContact._Text_PersonalNote());

	Panel rootPanel;
	IChooseResult<ExternalContact> result;
	ExternalContact currentExternalContact;
	
	DialogBlockOkCancel okCancel;

	ComponentUpdatePeriod updatePeriod;
	
	public EditExternalContact(Panel panel, ExternalContact _currentExternalContact, UserId _user, IChooseResult<ExternalContact> editExternalContactResult)
	{
		rootPanel = panel;
		currentExternalContact = _currentExternalContact;
		result = editExternalContactResult;
		user = _user;
	}

	public EditExternalContact()
	{
	}
	
	private void setExternalContact()
	{
		if (currentExternalContact != null)
		{
			textBoxFirstName.setValue(currentExternalContact.firstName);		
			textBoxLastName.setValue(currentExternalContact.lastName);
			textBoxEmail.setValue(currentExternalContact.email);
			textBoxPhone.setValue(currentExternalContact.phone);
			textBoxOrganization.setValue(currentExternalContact.organization);
			textAreaPersonalNote.setHTML(currentExternalContact.personalNote);
		}
		else
		{
			textBoxFirstName.setValue("");		
			textBoxLastName.setValue("");
			textBoxEmail.setValue("");
			textBoxPhone.setValue("");
			textBoxOrganization.setValue("");
			textAreaPersonalNote.setHTML("");
		}
	}

	private ExternalContact getExternalContact()
	{
		String ID = null;
		if (currentExternalContact != null) ID = currentExternalContact.ID;
		return new ExternalContact(ID,
				textBoxFirstName.getText(), textBoxLastName.getText(), 
				textBoxEmail.getText(), 
				textBoxPhone.getText(), 
				textAreaPersonalNote.getHTML(),
				textBoxOrganization.getText(),
				currentExternalContact.update);
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{			
		final DialogBox dBox = new DialogBox();
		dBox.setText(currentExternalContact == null ? langExternalContact._TextNewExternalContact() : langExternalContact._TextUpdateExternalContact());

		// Spawn one external contact if necessary
		currentExternalContact = (ExternalContact) (currentExternalContact == null ? new ExternalContact() : new ExternalContact(currentExternalContact));
		
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		Grid grid = new Grid(6, 2);
		grid.setBorderWidth(0);
		
		VerticalPanel vp = new VerticalPanel();
		dBox.setWidget(vp);		

		vp.add(grid);
		
		grid.setWidget(0, 0, lblFirstName);
		grid.setWidget(0, 1, textBoxFirstName);
		textBoxFirstName.setWidth("100%");
		
		grid.setWidget(1, 0, lblLastName);
		grid.setWidget(1, 1, textBoxLastName);
		textBoxFirstName.setWidth("100%");
		
		grid.setWidget(2, 0, lblEmail);
		grid.setWidget(2, 1, textBoxEmail);
		textBoxEmail.setWidth("100%");

		grid.setWidget(3, 0, lblPhone);
		grid.setWidget(3, 1, textBoxPhone);
		textBoxPhone.setWidth("100%");

		grid.setWidget(4, 0, lblOrganization);
		grid.setWidget(4, 1, textBoxOrganization);
		textBoxOrganization.setWidth("100%");
		
		grid.setWidget(5, 0, lblPersonalNote);
		grid.setWidget(5, 1, textAreaPersonalNote);
		textAreaPersonalNote.setWidth("100%");

		setExternalContact();

		okCancel = new DialogBlockOkCancel(null, dBox);
		okCancel.getOk().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				okCancel.setEnabled(false);
				dBox.hide();
				result.setResult(getExternalContact());				
			}
		});

		updatePeriod = new ComponentUpdatePeriod(currentExternalContact.update, ComponentUpdatePeriod.RecallType.RECONTACT);
		updatePeriod.onModuleLoad();

		vp.add(updatePeriod);
		
		vp.add(okCancel);
		dBox.center();
		
		textBoxFirstName.registerListener(this);
		textBoxLastName.registerListener(this);
		textBoxEmail.registerListener(this);
		textBoxPhone.registerListener(this);
		
		changed(false, true, false);
		
		// Inform user about personal note
		new MessagePipe(user,  rootPanel).addMessage(UserValuesConstantsCoachMessages.COACH_PERSONAL_NOTE);
	}

	@Override
	public void changed(boolean ok, boolean changed, boolean init)
	{
		if (init) return;
		okCancel.getOk().setEnabled(false);
		boolean setOk = true;
		setOk = setOk && textBoxFirstName.isValid();
		setOk = setOk && textBoxLastName.isValid();
		setOk = setOk && textBoxEmail.isValid();
		setOk = setOk && textBoxPhone.isValid();
		okCancel.getOk().setEnabled(setOk);	
	}

	@Override
	public IEditDialogModel<ExternalContact> clone(Panel rootPanel,
			UserId userId, ExternalContact edition,
			IChooseResult<ExternalContact> result)
	{
		return new EditExternalContact(rootPanel, edition, userId, result);
	}

}
