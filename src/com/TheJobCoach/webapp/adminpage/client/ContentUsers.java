package com.TheJobCoach.webapp.adminpage.client;

import java.util.ArrayList;
import java.util.List;

import com.TheJobCoach.webapp.adminpage.shared.UserReport;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUsers implements EntryPoint {

	UserId user;

	private final AdminServiceAsync adminService = GWT.create(AdminService.class);

	Panel rootPanel;

	public ContentUsers(Panel panel, UserId _user)
	{
		rootPanel = panel;
		user = _user;
	}

	// The list of data to display.
	private List<UserReport> userList = new ArrayList<UserReport>();

	final ExtendedCellTable<UserReport> cellTable = new ExtendedCellTable<UserReport>(userList);

	void getAllContent()
	{		
		ServerCallHelper<List<UserReport>> callback = new ServerCallHelper<List<UserReport>>(rootPanel) {
			@Override
			public void onSuccess(List<UserReport> result) {
				System.out.println("received : " + result.size());
				userList.clear();
				userList.addAll(result);
				cellTable.updateData();
			}
		};
		adminService.getUserReportList(user, callback);
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel rootPanel = RootPanel.get("centercontent");
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "100%");
		rootPanel.add(simplePanelCenter);

		// Create name column.

		cellTable.specialAddColumnSortableString(new GetValue<String, UserReport>() {
			@Override
			public String getValue(UserReport report)
			{
				return report.userName;
			}			
		},  "Name");

		// Create password column.
		TextColumn<UserReport> passwordColumn = new TextColumn<UserReport>() 	{
			@Override
			public String getValue(UserReport report) 
			{
				return report.password;
			}
		};
		passwordColumn.setSortable(true);
		cellTable.addColumn(passwordColumn, "Password");

		// Create mail column.
		TextColumn<UserReport> mailColumn = new TextColumn<UserReport>() 	{
			@Override
			public String getValue(UserReport report) 
			{
				return report.mail;
			}
		};
		mailColumn.setSortable(true);
		cellTable.addColumn(mailColumn, "email");

		// Create type column.
		TextColumn<UserReport> typeColumn = new TextColumn<UserReport>() 	{
			@Override
			public String getValue(UserReport report) 
			{
				return report.type.toString();
			}
		};
		typeColumn.setSortable(true);
		cellTable.addColumn(typeColumn, "Type");

		// Create token column.
		TextColumn<UserReport> tokenColumn = new TextColumn<UserReport>() 	{
			@Override
			public String getValue(UserReport report) 
			{
				return report.token;
			}
		};
		tokenColumn.setSortable(true);
		cellTable.addColumn(tokenColumn, "Token");

		// Create validated column.
		TextColumn<UserReport> validatedColumn = new TextColumn<UserReport>() 	{
			@Override
			public String getValue(UserReport report) 
			{
				return (report.validated == true) ? "ok" : "nok";
			}
		};
		validatedColumn.setSortable(true);
		cellTable.addColumn(validatedColumn, "Validated");

		// Create created date column.
		TextColumn<UserReport> createdColumn = new TextColumn<UserReport>() 	{
			@SuppressWarnings("deprecation")
			@Override
			public String getValue(UserReport report) 
			{
				return report.creationDate.toGMTString();
			}
		};
		createdColumn.setSortable(true);
		cellTable.addColumn(createdColumn, "Created on");

		cellTable.specialAddColumnSortableString(new GetValue<String, UserReport>() {
			@Override
			public String getValue(UserReport report)
			{
				return report.dead ? "dead":"";
			}			
		},  "Dead");

		cellTable.specialAddColumnSortableString(new GetValue<String, UserReport>() {
			@Override
			public String getValue(UserReport report)
			{
				return report.toggleDelete ? "to delete":"";
			}			
		},  "Deletion ?");

		cellTable.specialAddColumnSortableString(new GetValue<String, UserReport>() {
			@Override
			public String getValue(UserReport report)
			{
				return report.deletionDate.toString();
			}			
		},  "Deletion date");

		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<UserReport, String>() {
			@Override
			public void update(int index, UserReport object, String value) {
				deleteUser(object);
			}}	
				);

		getAllContent();
		
		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");
	}


	protected void deleteUser(final UserReport object) {
		MessageBox mb = new MessageBox(
				rootPanel, true, true, MessageBox.TYPE.QUESTION, "delete", 
				"Confirm delete user: " + object.userName, new MessageBox.ICallback() {

					@Override
					public void complete(boolean ok) {
						if (ok == true)
						{
							ServerCallHelper<String> callback = new ServerCallHelper<String>(rootPanel) {
								@Override
								public void onSuccess(String result) {
									getAllContent();
								}
							};
							adminService.deleteUser(user,  object.userName, callback);		
							cellTable.redraw();							
						}
					}
				});
		mb.onModuleLoad();
	}
}
