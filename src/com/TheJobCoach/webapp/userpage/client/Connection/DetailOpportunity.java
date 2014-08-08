package com.TheJobCoach.webapp.userpage.client.Connection;

import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.ui.RootPanel;

public class DetailOpportunity extends DetailPanel {

	// The list of opportunity data to display.
	private Vector<UserOpportunity> opportunityList = new Vector<UserOpportunity>();

	final ExtendedCellTable<UserOpportunity> cellTable = new ExtendedCellTable<UserOpportunity>(opportunityList);

	public DetailOpportunity(UserId user, ContactInformation connectionUser)
	{
		super(user, connectionUser);
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

		add(cellTable);
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
