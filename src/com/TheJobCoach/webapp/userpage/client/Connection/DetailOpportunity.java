package com.TheJobCoach.webapp.userpage.client.Connection;

import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Opportunity.LangLogEntry;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.client.IconCellSingle.IconType;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.VerticalSpacer;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DetailOpportunity extends DetailPanel {

	// The list of opportunity data to display.
	private Vector<UserOpportunity> opportunityList = new Vector<UserOpportunity>();

	final ExtendedCellTable<UserOpportunity> cellTable = new ExtendedCellTable<UserOpportunity>(opportunityList);

	// The list of log data to display.
	private Vector<UserLogEntry> logList = new Vector<UserLogEntry>();
	final ExtendedCellTable<UserLogEntry> cellTableLog = new ExtendedCellTable<UserLogEntry>(logList);

	VerticalPanel logPanel = new VerticalPanel();
	VerticalPanel opportunityPanel = new VerticalPanel();

	SimplePanel oppDescription = new SimplePanel();

	final LangLogEntry langLogEntry = GWT.create(LangLogEntry.class);

	void selectUserOpportunity(UserOpportunity object)
	{
		AsyncCallback<Vector<UserLogEntry>>callback = new ServerCallHelper<Vector<UserLogEntry>>(RootPanel.get()) {
			@Override
			public void onSuccess(Vector<UserLogEntry> result) {
				logPanel.setVisible(true);
				logList.clear();
				logList.addAll(result);
				cellTableLog.updateData();
				cellTableLog.redraw();
			}
		};
		if (connectionUser.hisVisibility.log)
			userService.getUserLogEntryList(contactId, object.ID, callback);

		setSize("100%", "100%");

		opportunityPanel.setVisible(true);
		oppDescription.clear();
		oppDescription.setWidget(new HTMLPanel(object.description));
	}

	public DetailOpportunity(UserId user, ContactInformation connectionUser)
	{
		super(user, connectionUser);

		ContentHelper.insertTitlePanel(this, langConnection.opportunityList(), ClientImageBundle.INSTANCE.opportunityContent());

		// Create title column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity opp)
			{
				return opp.title;
			}			
		},  lang._TextName());

		// Create company column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity opp)
			{
				return opp.companyId;
			}			
		},  lang._TextCompany());

		// Create status column.
		cellTable.specialAddColumnSortableWithComparator(new GetValue<String, UserOpportunity>() {

			@Override
			public String getValue(UserOpportunity userOpportunity)
			{
				return lang.applicationStatusMap().get("ApplicationStatus_" + UserOpportunity.applicationStatusToString(userOpportunity.status));
			}}
		, new Comparator<UserOpportunity>(){
			@Override
			public int compare(UserOpportunity o1, UserOpportunity o2)
			{
				return o1.status.compareTo(o2.status);
			}}, lang._TextStatus());

		// Create location column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity opp)
			{
				return opp.location;
			}			
		},  lang._TextLocation());

		// Create salary column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity opp)
			{
				return opp.salary;
			}			
		},  lang._TextSalary());

		// Create contract type column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity opp)
			{
				return opp.contractType;
			}			
		},  lang._TextContractType());

		// Create creation date column.
		cellTable.specialAddColumnSortableDate(new GetValue<Date, UserOpportunity>() {
			@Override
			public Date getValue(UserOpportunity opp)
			{
				return opp.lastUpdate;
			}
		},  lang._TextCreationDate());

		// Create log column
		cellTable.addColumnWithIcon(IconType.RIGHT, new FieldUpdater<UserOpportunity, String>(){
			@Override
			public void update(int index, UserOpportunity object, String value)
			{
				selectUserOpportunity(object);
			}
		});

		add(cellTable);

		// now log information
		add(opportunityPanel);
		opportunityPanel.setVisible(false);
		ContentHelper.insertSubTitlePanel(opportunityPanel, langConnection.opportunityDetail());
		opportunityPanel.add(oppDescription);

		// now log information
		add(logPanel);
		logPanel.add(new VerticalSpacer("2em"));
		ContentHelper.insertTitlePanel(logPanel, langConnection.logList(), ClientImageBundle.INSTANCE.userLogContent());
		logPanel.setVisible(false);


		// Create title column.
		cellTableLog.specialAddColumnSortableString(new GetValue<String, UserLogEntry>() {
			@Override
			public String getValue(UserLogEntry userLog)
			{
				return userLog.title;
			}			
		},  lang._TextName());

		// Create status column.
		cellTableLog.specialAddColumnSortableWithComparator(new GetValue<String, UserLogEntry>() {

			@Override
			public String getValue(UserLogEntry userLog)
			{
				return  langLogEntry.logEntryStatusMap().get("logEntryStatus_" + UserLogEntry.entryTypeToString(userLog.type));
			}}
		, new Comparator<UserLogEntry>(){
			@Override
			public int compare(UserLogEntry o1, UserLogEntry o2)
			{
				return o1.type.compareTo(o2.type);
			}}, lang._TextStatus());

		// Create event column.
		cellTableLog.specialAddColumnSortableDate(new GetValue<Date, UserLogEntry>() {
			@Override
			public Date getValue(UserLogEntry log)
			{
				return log.eventDate;
			}			
		},  langLogEntry._TextCreated());

		cellTable.setStyleName("filecelltable");

		// documents, if allowed.
		if (connectionUser.hisVisibility.document)
			cellTableLog.addColumnHtml(new FieldUpdater<UserLogEntry, String>() {
				@Override
				public void update(int index, UserLogEntry object, String value) {				
				}}, 
				new GetValue<String, UserLogEntry>(){

					@Override
					public String getValue(UserLogEntry log)
					{
						String result = "";
						String brk = "";
						for (UserDocumentId doc : log.attachedDocumentId)
						{
							result = result + brk + doc.fileName;
							brk = "<br/>";
						}
						return result;
					}}, langLogEntry._TextFiles());

		// contacts, if allowed.
		if (connectionUser.hisVisibility.document)
			cellTableLog.addColumnHtml(new FieldUpdater<UserLogEntry, String>() {
				@Override
				public void update(int index, UserLogEntry object, String value) {				
				}}, 
				new GetValue<String, UserLogEntry>(){

					@Override
					public String getValue(UserLogEntry log)
					{
						String result = "";
						String brk = "";
						for (ExternalContact contact : log.linkedExternalContact)
						{
							result = result + brk + contact.firstName + " " + contact.lastName;
							brk = "<br/>";
						}
						return result;
					}}, langLogEntry._Text_Contacts());

		logPanel.add(cellTableLog);

		setSize("100%", "100%");
	}

	boolean loaded = false;

	@Override
	public void showPanelDetail()
	{
		if (loaded) return;
		loaded = true;
		ServerCallHelper<Vector<UserOpportunity>>callback = new ServerCallHelper<Vector<UserOpportunity>>(RootPanel.get()) {
			@Override
			public void onSuccess(Vector<UserOpportunity> result) {
				opportunityList.clear();
				opportunityList.addAll(result);
				cellTable.updateData();
				cellTable.redraw();
			}

		};
		userService.getUserOpportunityList(contactId, "managed", callback);
		setSize("100%", "100%");
	}

}
