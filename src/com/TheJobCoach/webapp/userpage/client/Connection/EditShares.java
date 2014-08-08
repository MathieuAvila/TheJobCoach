package com.TheJobCoach.webapp.userpage.client.Connection;

import com.TheJobCoach.webapp.userpage.client.ComponentUpdatePeriod;
import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.client.CheckedCheckBox;
import com.TheJobCoach.webapp.util.client.CheckedLabel;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.IChanged;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditShares extends DialogBox implements IChanged, IEditDialogModel<ContactInformation> {

	final static Lang lang = GWT.create(Lang.class);
	final static LangConnection langConnection = GWT.create(LangConnection.class);

	CheckedCheckBox checkBoxOpportunity = new CheckedCheckBox();
	CheckedLabel lblOpportunity = new CheckedLabel(langConnection.opportunities(), false, checkBoxOpportunity);
	CheckedCheckBox checkBoxLog = new CheckedCheckBox();
	CheckedLabel lblLog = new CheckedLabel(langConnection.logs(), false, checkBoxLog);
	CheckedCheckBox checkBoxContact = new CheckedCheckBox();
	CheckedLabel lblContact = new CheckedLabel(langConnection.contacts(), false, checkBoxContact);
	CheckedCheckBox checkBoxDocument = new CheckedCheckBox();
	CheckedLabel lblDocument = new CheckedLabel(langConnection.documents(), false, checkBoxDocument);
	Grid gridHisHerShares;
	
	IChooseResult<ContactInformation> result;
	ContactInformation currentContactInformation;
	
	DialogBlockOkCancel okCancel;

	ComponentUpdatePeriod updatePeriod;
	
	static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);
	
	static ImageResource documentIcon = wpImageBundle.userDocumentContent_menu();
	static ImageResource opportunityIcon = wpImageBundle.opportunityContent_menu();
	static ImageResource contactIcon = wpImageBundle.userExternalContactContent_menu();
	static ImageResource logIcon = wpImageBundle.userLogContent_menu();
	
	static ImageResource documentIconThawed = wpImageBundle.userDocumentContent_thawed();
	static ImageResource opportunityIconThawed = wpImageBundle.opportunityContent_thawed();
	static ImageResource contactIconThawed = wpImageBundle.userExternalContactContent_thawed();
	static ImageResource logIconThawed = wpImageBundle.userLogContent_thawed();

	public EditShares()
	{
		// to be cloned.
	}

	void insertFixedCheckedBoxAtRow(Grid grid, int row, String text, ImageResource image, boolean value)
	{
		CheckBox cb = new CheckBox();
		cb.setEnabled(false);
		cb.setValue(value);
		Label lbl = new Label(text);
		lbl.setWidth("100%");		
		grid.setWidget(row, 0, new Image(image));
		grid.setWidget(row, 1, lbl);
		grid.setWidget(row, 2, cb);
	}

	void insertCheckedBoxAtRow(Grid grid, int row, CheckedLabel text, ImageResource image, CheckedCheckBox cb, boolean value)
	{
		cb.setDefault(value);
		cb.setValue(value);
		text.setWidth("100%");		
		grid.setWidget(row, 0, new Image(image));
		grid.setWidget(row, 1, text);
		grid.setWidget(row, 2, cb.getItem());
	}
	
	Grid createGrid()
	{
		Grid result = new Grid(4, 3);
		result.setBorderWidth(0);
		result.setWidth("100%");
		return result;
	}
	
	public EditShares(final ContactInformation currentContactInformation, final IChooseResult<ContactInformation> result)
	{
		this.currentContactInformation = currentContactInformation;
		this.result = result;
	
		setText(langConnection.shares());

		setGlassEnabled(true);
		setAnimationEnabled(true);

		VerticalPanel vp = new VerticalPanel();
		setWidget(vp);		
		vp.setWidth("100%");
		
		ContentHelper.insertSubTitlePanel(vp, langConnection.myShares());
		Grid gridMyShares = createGrid();
		vp.add(gridMyShares);

		insertCheckedBoxAtRow(gridMyShares, 0, lblOpportunity, opportunityIcon, checkBoxOpportunity, currentContactInformation.myVisibility.opportunity);
		insertCheckedBoxAtRow(gridMyShares, 1, lblLog, logIcon, checkBoxLog, currentContactInformation.myVisibility.log);
		insertCheckedBoxAtRow(gridMyShares, 2, lblDocument, documentIcon, checkBoxDocument, currentContactInformation.myVisibility.document);
		insertCheckedBoxAtRow(gridMyShares, 3, lblContact, contactIcon, checkBoxContact, currentContactInformation.myVisibility.contact);
		
		ContentHelper.insertSubTitlePanel(vp, langConnection.hisHerShares());
		
		gridHisHerShares = createGrid();
		vp.add(gridHisHerShares);
		
		insertFixedCheckedBoxAtRow(gridHisHerShares, 0, langConnection.opportunities(), opportunityIconThawed, currentContactInformation.hisVisibility.opportunity);
		insertFixedCheckedBoxAtRow(gridHisHerShares, 1, langConnection.logs(), logIconThawed, currentContactInformation.hisVisibility.log);
		insertFixedCheckedBoxAtRow(gridHisHerShares, 2, langConnection.documents(), documentIconThawed, currentContactInformation.hisVisibility.document);
		insertFixedCheckedBoxAtRow(gridHisHerShares, 3, langConnection.contacts(), contactIconThawed, currentContactInformation.hisVisibility.contact);

		okCancel = new DialogBlockOkCancel(null, this);
		okCancel.getOk().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				okCancel.setEnabled(false);
				currentContactInformation.myVisibility.contact = checkBoxContact.getItem().getValue();
				currentContactInformation.myVisibility.document = checkBoxDocument.getItem().getValue();
				currentContactInformation.myVisibility.log = checkBoxLog.getItem().getValue();
				currentContactInformation.myVisibility.opportunity = checkBoxOpportunity.getItem().getValue();
				result.setResult(currentContactInformation);
				hide();
			}
		});

		vp.add(okCancel);
		center();

		checkBoxOpportunity.registerListener(this);
		checkBoxLog.registerListener(this);
		checkBoxContact.registerListener(this);
		checkBoxDocument.registerListener(this);

		changed(false, true, false);
	}

	@Override
	public void changed(boolean ok, boolean changed, boolean init)
	{
		if (init) return;
		okCancel.getOk().setEnabled(false);
		boolean setOk = false;
		setOk = setOk || !checkBoxOpportunity.getIsDefault();
		setOk = setOk || !checkBoxLog.getIsDefault();
		setOk = setOk || !checkBoxContact.getIsDefault();
		setOk = setOk || !checkBoxDocument.getIsDefault();
		okCancel.getOk().setEnabled(setOk);	
	}

	@Override
	public IEditDialogModel<ContactInformation> clone(Panel rootPanel,
			UserId userId, ContactInformation edition,
			IChooseResult<ContactInformation> result)
	{
		return new EditShares(edition, result);
	}

	@Override
	public void onModuleLoad()
	{
		// TODO Auto-generated method stub
		
	}

}
