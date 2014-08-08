package com.TheJobCoach.webapp.userpage.client.Connection;

import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.ExternalContact.LangExternalContact;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class DetailContact extends DetailPanel {

	// The list of opportunity data to display.
	private Vector<ExternalContact> contactList = new Vector<ExternalContact>();

	final ExtendedCellTable<ExternalContact> cellTable = new ExtendedCellTable<ExternalContact>(contactList);
	final static LangExternalContact langExternalContact = GWT.create(LangExternalContact.class);

	public DetailContact(UserId user, ContactInformation connectionUser)
	{
		super(user, connectionUser);
		add(cellTable);

		// Create first name column.
		cellTable.specialAddColumnSortableString(new GetValue<String, ExternalContact>() {
			@Override
			public String getValue(ExternalContact contact)
			{
				return contact.firstName;
			}			
		},  langExternalContact._TextFirstName());
		
		// Create last name column.
		cellTable.specialAddColumnSortableString(new GetValue<String, ExternalContact>() {
			@Override
			public String getValue(ExternalContact contact)
			{
				return contact.lastName;
			}			
		},  langExternalContact._TextLastName());
		
		// Create organization column.
		cellTable.specialAddColumnSortableString(new GetValue<String, ExternalContact>() {
			@Override
			public String getValue(ExternalContact contact)
			{
				return contact.organization;
			}			
		},  langExternalContact._Text_Organization());

		// Create phone column.
		cellTable.specialAddColumnSortableString(new GetValue<String, ExternalContact>() {
			@Override
			public String getValue(ExternalContact contact)
			{
				return contact.phone;
			}			
		},  langExternalContact._Text_Phone());

		 // email
		cellTable.addColumnEmail(new GetValue<String, ExternalContact>() {
			@Override
			public String getValue(ExternalContact contact)
			{				
				return contact.email;
			}			
		});

	}

	boolean loaded = false;

	@Override
	public void showPanelDetail()
	{
		if (loaded) return;
		loaded = true;
		AsyncCallback<Vector<ExternalContact>>callback = new ServerCallHelper<Vector<ExternalContact>>(RootPanel.get()) {
			@Override
			public void onSuccess(Vector<ExternalContact> result) {
				contactList.clear();
				contactList.addAll(result);
				cellTable.updateData();
				cellTable.redraw();
			}
		};
		userService.getExternalContactList(contactId, callback);
		setSize("100%", "100%");
	}
}
