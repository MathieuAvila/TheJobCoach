package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EditLogEntry implements EntryPoint {
	
	private final static UserServiceAsync userService = GWT.create(UserService.class);

	public interface EditLogEntryResult
	{
		public void setResult(UserLogEntry result);
	}

	UserId user;

	RichTextArea richTextAreaDescription = new RichTextArea();
	TextBox txtbxTitle = new TextBox();
	ListBox comboBoxStatus = new ListBox();
	DateBox dateBoxCreation = new DateBox();
	DateBox dateBoxEvent = new DateBox();
	
	String id;

	Panel rootPanel;
	EditLogEntryResult result;
	UserLogEntry currentLogEntry;
	String oppId;

	public EditLogEntry(Panel panel, UserLogEntry _currentLogEntry, String _oppId, UserId _user, EditLogEntryResult _result)
	{
		user = _user;
		rootPanel = panel;
		currentLogEntry = _currentLogEntry;
		result = _result;
		oppId = _oppId;
	}

	public UserLogEntry getLogEntry()
	{
		String id = SiteUUID.getDateUuid();
		if (currentLogEntry != null)
		{
			id = currentLogEntry.ID;
			oppId = currentLogEntry.opportunityId;
		}
		return new UserLogEntry(
				oppId,
				id, 
				txtbxTitle.getText(), richTextAreaDescription.getHTML(), 
				dateBoxCreation.getValue(), dateBoxEvent.getValue(),
				UserLogEntry.entryTypeToString(comboBoxStatus.getValue(comboBoxStatus.getSelectedIndex())),
				null, null);
	}

	private void commit()
	{
		UserLogEntry log = getLogEntry();
		try {
		userService.setUserLogEntry(user, log, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				MessageBox.messageBoxException(rootPanel, caught.toString());
			}
			public void onSuccess(String result)
			{
				System.out.println("Created user log entry: " + result);			
			}
		});
		}
		catch (Exception e) { MessageBox.messageBoxException(rootPanel, e);}
		result.setResult(log);
	};

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{	
		Lang lang = GWT.create(Lang.class);
		final DialogBox dBox = new DialogBox();
		dBox.setText(currentLogEntry == null ? lang._TextNewLogEntry(): lang._TextUpdateLogEntry());
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		Grid grid = new Grid(7, 2);
		grid.setBorderWidth(0);
		dBox.setWidget(grid);		
		grid.setSize("386px", "472px");

		Label lblTitle = new Label(lang._TextUserLogTitle());
		grid.setWidget(0, 0, lblTitle);		
		grid.setWidget(0, 1, txtbxTitle);
		grid.getCellFormatter().setWidth(0, 1, "100%");
		txtbxTitle.setWidth("100%");

		Label lblStatus = new Label("Status");
		grid.setWidget(1, 0, lblStatus);

		grid.setWidget(1, 1, comboBoxStatus);
		for (LogEntryType e : UserLogEntry.getLogTypeTable())
		{
			comboBoxStatus.addItem(lang.logEntryStatusMap().get("logEntryStatus_" + UserLogEntry.entryTypeToString(e)), UserLogEntry.entryTypeToString(e));
		}

		Label lblDescription = new Label("Description");
		grid.setWidget(2, 0, lblDescription);		
		grid.setWidget(2, 1, richTextAreaDescription);
		grid.getCellFormatter().setWidth(2, 1, "100%");
		richTextAreaDescription.setWidth("100%");

		Label lblEventDate = new Label(lang._TextCreated());
		grid.setWidget(3, 0, lblEventDate);

		grid.setWidget(3, 1, dateBoxCreation);

		Label lblEndDate = new Label(lang._TextExpectedFollowUp());
		grid.setWidget(4, 0, lblEndDate);

		grid.setWidget(4, 1, dateBoxEvent);

		grid.getCellFormatter().setHorizontalAlignment(6, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid.setWidget(6, 1, horizontalPanel);

		final Button btnOk = new ButtonImageText(ButtonImageText.Type.OK, lang._TextOk());
		horizontalPanel.add(btnOk);

		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				btnOk.setEnabled(false);
				commit();
				dBox.hide();
			}
		});

		Button btnCancel = new ButtonImageText(ButtonImageText.Type.CANCEL, lang._TextCancel());
		horizontalPanel.add(btnCancel);

		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				result.setResult(null);
				dBox.hide();
			}
		});
		if (currentLogEntry != null)
		{
			txtbxTitle.setText(currentLogEntry.title);
			richTextAreaDescription.setHTML(currentLogEntry.description);
			dateBoxCreation.setValue(currentLogEntry.creation);
			dateBoxEvent.setValue(currentLogEntry.expectedFollowUp);
		}
		dBox.center();
	}
}
